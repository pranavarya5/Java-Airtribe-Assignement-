package com.library.search;

import com.library.model.Book;

/**
 * Concrete strategy for matching books by Author (case-insensitive substring match).
 */
public class AuthorSearchStrategy implements SearchStrategy {
    private final String authorQuery;

    public AuthorSearchStrategy(String authorQuery) {
        this.authorQuery = authorQuery != null ? authorQuery.trim().toLowerCase() : "";
    }

    @Override
    public boolean matches(Book book) {
        if (authorQuery.isEmpty() || book == null || book.getAuthor() == null) {
            return false;
        }
        return book.getAuthor().toLowerCase().contains(authorQuery);
    }
}
