package com.library.search;

import com.library.model.Book;

/**
 * Strategy Pattern Interface for searching books based on varying algorithms/criteria.
 */
@FunctionalInterface
public interface SearchStrategy {
    /**
     * Determines whether a given book matches the search criteria.
     * @param book The book to test
     * @return true if the book satisfies criteria, false otherwise
     */
    boolean matches(Book book);
}
