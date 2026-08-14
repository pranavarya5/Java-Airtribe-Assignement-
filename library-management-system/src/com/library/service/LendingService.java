package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.BorrowRecord;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import com.library.util.LoggerUtil;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service managing book lending operations: Checkout and Return workflows.
 */
public class LendingService {

    private static final Logger LOGGER = LoggerUtil.getLogger(LendingService.class);
    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final ReservationService reservationService;

    public LendingService(BookRepository bookRepository,
                          PatronRepository patronRepository,
                          ReservationService reservationService) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.reservationService = reservationService;
    }

    /**
     * Checks out a book to a patron.
     */
    public BorrowRecord checkoutBook(String isbn, String patronId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ISBN: " + isbn));

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found with ID: " + patronId));

        if (book.getStatus() != BookStatus.AVAILABLE && book.getStatus() != BookStatus.RESERVED) {
            throw new IllegalStateException("Book '" + book.getTitle() + "' is not available for checkout. Current status: " + book.getStatus());
        }

        // Create borrow record
        BorrowRecord record = new BorrowRecord(isbn, patronId, LocalDate.now(), DEFAULT_LOAN_PERIOD_DAYS);
        patron.addBorrowRecord(record);

        // Update book status
        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);
        patronRepository.save(patron);

        LOGGER.info(String.format("Checked out Book '%s' (ISBN: %s) to Patron '%s' (ID: %s). Due date: %s",
                book.getTitle(), isbn, patron.getName(), patronId, record.getDueDate()));

        return record;
    }

    /**
     * Returns a borrowed book, updates status, and processes any pending reservations for the book.
     */
    public BorrowRecord returnBook(String isbn, String patronId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ISBN: " + isbn));

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found with ID: " + patronId));

        Optional<BorrowRecord> activeRecordOpt = patron.getBorrowingHistory().stream()
                .filter(r -> r.getIsbn().equals(isbn) && !r.isReturned())
                .findFirst();

        if (activeRecordOpt.isEmpty()) {
            throw new IllegalStateException("No active borrow record found for Patron '" + patronId + "' and Book ISBN '" + isbn + "'");
        }

        BorrowRecord record = activeRecordOpt.get();
        record.setReturnDate(LocalDate.now());

        LOGGER.info(String.format("Book '%s' returned by Patron '%s'.", book.getTitle(), patron.getName()));

        // Process reservation queue if any patron reserved this book
        if (reservationService != null) {
            boolean fulfilled = reservationService.processNextReservation(isbn).isPresent();
            if (!fulfilled) {
                book.setStatus(BookStatus.AVAILABLE);
                bookRepository.save(book);
            }
        } else {
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.save(book);
        }

        return record;
    }
}
