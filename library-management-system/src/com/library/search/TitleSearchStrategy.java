package com.library.search;

import com.library.model.Book;

/**
 * Concrete strategy for matching books by Title (case-insensitive substring match).
 */
public class TitleSearchStrategy implements SearchStrategy {
    private final String titleQuery;

    public TitleSearchStrategy(String titleQuery) {
        this.titleQuery = titleQuery != null ? titleQuery.trim().toLowerCase() : "";
    }

    @Override
    public boolean matches(Book book) {
        if (titleQuery.isEmpty() || book == null || book.getTitle() == null) {
            return false;
        }
        return book.getTitle().toLowerCase().contains(titleQuery);
    }
}
