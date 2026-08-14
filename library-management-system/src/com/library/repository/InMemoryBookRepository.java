package com.library.repository;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe In-Memory implementation of BookRepository.
 */
public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> bookStorage = new ConcurrentHashMap<>();

    @Override
    public void save(Book book) {
        bookStorage.put(book.getIsbn(), book);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(bookStorage.get(isbn));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(bookStorage.values());
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookStorage.remove(isbn);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookStorage.containsKey(isbn);
    }
}
