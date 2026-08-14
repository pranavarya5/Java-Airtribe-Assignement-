package com.library.search;

import com.library.model.Book;

/**
 * Concrete strategy for matching books by exact ISBN.
 */
public class IsbnSearchStrategy implements SearchStrategy {
    private final String isbnQuery;

    public IsbnSearchStrategy(String isbnQuery) {
        this.isbnQuery = isbnQuery != null ? isbnQuery.trim() : "";
    }

    @Override
    public boolean matches(Book book) {
        if (isbnQuery.isEmpty() || book == null || book.getIsbn() == null) {
            return false;
        }
        return book.getIsbn().equalsIgnoreCase(isbnQuery);
    }
}
