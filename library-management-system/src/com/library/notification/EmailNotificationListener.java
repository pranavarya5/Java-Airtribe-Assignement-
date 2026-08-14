package com.library.notification;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.util.LoggerUtil;

import java.util.logging.Logger;

/**
 * Concrete Observer sending email notifications to patrons.
 */
public class EmailNotificationListener implements Observer {

    private static final Logger LOGGER = LoggerUtil.getLogger(EmailNotificationListener.class);

    @Override
    public void onBookAvailable(Patron patron, Book book) {
        String msg = String.format("EMAIL SENT to %s <%s>: The reserved book '%s' (ISBN: %s) is now available for checkout!",
                patron.getName(), patron.getEmail(), book.getTitle(), book.getIsbn());
        LOGGER.info(msg);
    }
}
