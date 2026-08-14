package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import com.library.search.SearchStrategy;
import com.library.util.LoggerUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service managing book inventory operations.
 */
public class BookService {

    private static final Logger LOGGER = LoggerUtil.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists.");
        }
        bookRepository.save(book);
        LOGGER.info("Added new book to inventory: " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
    }

    public void updateBook(Book book) {
        if (!bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Cannot update. Book with ISBN " + book.getIsbn() + " does not exist.");
        }
        bookRepository.save(book);
        LOGGER.info("Updated book details: " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
    }

    public void removeBook(String isbn) {
        if (!bookRepository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException("Cannot remove. Book with ISBN " + isbn + " does not exist.");
        }
        bookRepository.deleteByIsbn(isbn);
        LOGGER.info("Removed book with ISBN: " + isbn + " from inventory.");
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(SearchStrategy strategy) {
        return bookRepository.findAll().stream()
                .filter(strategy::matches)
                .collect(Collectors.toList());
    }

    public List<Book> getBooksByStatus(BookStatus status) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    public long getAvailableBookCount() {
        return bookRepository.findAll().stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .count();
    }

    public long getBorrowedBookCount() {
        return bookRepository.findAll().stream()
                .filter(b -> b.getStatus() == BookStatus.BORROWED)
                .count();
    }
}
