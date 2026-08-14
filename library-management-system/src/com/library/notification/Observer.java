package com.library.notification;

import com.library.model.Book;
import com.library.model.Patron;

/**
 * Observer interface in the Observer Pattern.
 */
public interface Observer {
    /**
     * Triggered when a reserved book becomes available for a patron.
     */
    void onBookAvailable(Patron patron, Book book);
}
