package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Patron;
import com.library.model.Reservation;
import com.library.model.ReservationStatus;
import com.library.notification.NotificationService;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import com.library.util.LoggerUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Service managing book reservation queues and Observer notifications when books become available.
 */
public class ReservationService {

    private static final Logger LOGGER = LoggerUtil.getLogger(ReservationService.class);
    private final Map<String, Queue<Reservation>> reservationQueues = new ConcurrentHashMap<>();
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final NotificationService notificationService;

    public ReservationService(BookRepository bookRepository,
                              PatronRepository patronRepository,
                              NotificationService notificationService) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.notificationService = notificationService;
    }

    /**
     * Reserves a book for a patron if the book is currently BORROWED or RESERVED.
     */
    public Reservation reserveBook(String isbn, String patronId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ISBN: " + isbn));

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found with ID: " + patronId));

        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book '" + book.getTitle() + "' is currently AVAILABLE for checkout, reservation not needed.");
        }

        Reservation reservation = new Reservation(isbn, patronId);
        reservationQueues.computeIfAbsent(isbn, k -> new LinkedList<>()).add(reservation);
        patron.addReservation(reservation);

        LOGGER.info(String.format("Reservation created for Patron [%s] on Book '%s' [Queue Position: %d]",
                patron.getName(), book.getTitle(), reservationQueues.get(isbn).size()));

        return reservation;
    }

    /**
     * Called when a book is returned to allocate it to the next pending reservation.
     */
    public Optional<Reservation> processNextReservation(String isbn) {
        Queue<Reservation> queue = reservationQueues.get(isbn);
        if (queue == null || queue.isEmpty()) {
            return Optional.empty();
        }

        Reservation nextReservation = queue.poll();
        nextReservation.setStatus(ReservationStatus.FULFILLED);

        Book book = bookRepository.findByIsbn(isbn).orElse(null);
        Patron patron = patronRepository.findById(nextReservation.getPatronId()).orElse(null);

        if (book != null && patron != null) {
            book.setStatus(BookStatus.RESERVED);
            bookRepository.save(book);
            patron.removeReservation(nextReservation);

            LOGGER.info(String.format("Fulfilled reservation for Patron [%s] for Book '%s'", patron.getName(), book.getTitle()));

            // Notify Observers (Email/In-App alert listeners)
            notificationService.notifyObservers(patron, book);
        }

        return Optional.of(nextReservation);
    }

    public List<Reservation> getReservationsForBook(String isbn) {
        Queue<Reservation> queue = reservationQueues.get(isbn);
        return queue != null ? new ArrayList<>(queue) : new ArrayList<>();
    }
}
