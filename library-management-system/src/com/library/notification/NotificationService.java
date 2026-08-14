package com.library.notification;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Concrete Subject managing observers and dispatching notifications.
 */
public class NotificationService implements Subject {

    private static final Logger LOGGER = LoggerUtil.getLogger(NotificationService.class);
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            LOGGER.fine("Registered observer: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        LOGGER.fine("Unregistered observer: " + observer.getClass().getSimpleName());
    }

    @Override
    public void notifyObservers(Patron patron, Book book) {
        LOGGER.info(String.format("Dispatching notifications for Patron '%s' on Book '%s'", patron.getName(), book.getTitle()));
        for (Observer observer : observers) {
            try {
                observer.onBookAvailable(patron, book);
            } catch (Exception e) {
                LOGGER.severe("Error notifying observer: " + e.getMessage());
            }
        }
    }
}
