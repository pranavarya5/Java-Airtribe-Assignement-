package com.library.notification;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.util.LoggerUtil;

import java.util.logging.Logger;

/**
 * Concrete Observer recording in-app notification alerts for patrons.
 */
public class InAppNotificationListener implements Observer {

    private static final Logger LOGGER = LoggerUtil.getLogger(InAppNotificationListener.class);

    @Override
    public void onBookAvailable(Patron patron, Book book) {
        String msg = String.format("IN-APP ALERT for Patron [%s]: Book '%s' has been allocated from reservation queue.",
                patron.getPatronId(), book.getTitle());
        LOGGER.info(msg);
    }
}
