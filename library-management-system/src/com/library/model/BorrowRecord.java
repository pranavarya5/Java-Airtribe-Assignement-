package com.library.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a transaction record of a book borrowed by a patron.
 */
public class BorrowRecord {
    private final String recordId;
    private final String isbn;
    private final String patronId;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;

    public BorrowRecord(String isbn, String patronId, LocalDate checkoutDate, int loanPeriodDays) {
        this.recordId = UUID.randomUUID().toString().substring(0, 8);
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.patronId = Objects.requireNonNull(patronId, "Patron ID cannot be null");
        this.checkoutDate = Objects.requireNonNull(checkoutDate, "Checkout date cannot be null");
        this.dueDate = checkoutDate.plusDays(loanPeriodDays);
        this.returned = false;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPatronId() {
        return patronId;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.returned = true;
    }

    public boolean isReturned() {
        return returned;
    }

    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("BorrowRecord[ID='%s', ISBN='%s', Patron='%s', Borrowed=%s, Due=%s, Returned=%s]",
                recordId, isbn, patronId, checkoutDate, dueDate, returned ? returnDate : "NO");
    }
}
