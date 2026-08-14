package com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a library patron (member).
 */
public class Patron {
    private final String patronId;
    private String name;
    private String email;
    private String phone;
    private final List<BorrowRecord> borrowingHistory;
    private final List<Reservation> activeReservations;

    public Patron(String patronId, String name, String email, String phone) {
        this.patronId = Objects.requireNonNull(patronId, "Patron ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.phone = phone != null ? phone : "";
        this.borrowingHistory = new ArrayList<>();
        this.activeReservations = new ArrayList<>();
    }

    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<BorrowRecord> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public void addBorrowRecord(BorrowRecord record) {
        borrowingHistory.add(Objects.requireNonNull(record, "Record cannot be null"));
    }

    public List<Reservation> getActiveReservations() {
        return Collections.unmodifiableList(activeReservations);
    }

    public void addReservation(Reservation reservation) {
        activeReservations.add(Objects.requireNonNull(reservation, "Reservation cannot be null"));
    }

    public void removeReservation(Reservation reservation) {
        activeReservations.remove(reservation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronId, patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

    @Override
    public String toString() {
        return String.format("Patron[ID='%s', Name='%s', Email='%s', BorrowedCount=%d, ReservationsCount=%d]",
                patronId, name, email, borrowingHistory.size(), activeReservations.size());
    }
}
