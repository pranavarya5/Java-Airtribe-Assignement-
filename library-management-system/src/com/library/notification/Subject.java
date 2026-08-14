package com.library.notification;

import com.library.model.Book;
import com.library.model.Patron;

/**
 * Subject interface in the Observer Pattern for registering, unregistering, and notifying observers.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Patron patron, Book book);
}
