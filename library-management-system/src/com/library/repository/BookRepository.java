package com.library.repository;

import com.library.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining contract for Book data access (Dependency Inversion Principle).
 */
public interface BookRepository {
    void save(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    void deleteByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}
