package com.library.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a reservation placed by a patron for a book currently checked out.
 */
public class Reservation {
    private final String reservationId;
    private final String isbn;
    private final String patronId;
    private final LocalDateTime reservationDate;
    private ReservationStatus status;

    public Reservation(String isbn, String patronId) {
        this.reservationId = UUID.randomUUID().toString().substring(0, 8);
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.patronId = Objects.requireNonNull(patronId, "Patron ID cannot be null");
        this.reservationDate = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPatronId() {
        return patronId;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    @Override
    public String toString() {
        return String.format("Reservation[ID='%s', ISBN='%s', Patron='%s', Date=%s, Status=%s]",
                reservationId, isbn, patronId, reservationDate, status);
    }
}
