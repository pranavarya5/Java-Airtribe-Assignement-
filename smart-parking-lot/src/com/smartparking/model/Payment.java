package com.smartparking.model;

import java.time.LocalDateTime;

/**
 * Represents a payment transaction for a parking ticket.
 */
public class Payment {
    private final String paymentId;
    private final String ticketId;
    private final double amount;
    private final LocalDateTime paymentTime;
    private final PaymentMode mode;
    private PaymentStatus status;

    public Payment(String paymentId, String ticketId, double amount, PaymentMode mode) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment ID cannot be null or empty.");
        }
        if (ticketId == null || ticketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket ID cannot be null or empty.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.paymentId = paymentId.trim();
        this.ticketId = ticketId.trim();
        this.amount = amount;
        this.mode = mode != null ? mode : PaymentMode.CASH;
        this.paymentTime = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public PaymentMode getMode() {
        return mode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment[" +
                "id='" + paymentId + '\'' +
                ", ticket='" + ticketId + '\'' +
                ", amount=$" + String.format("%.2f", amount) +
                ", mode=" + mode +
                ", status=" + status +
                ']';
    }
}
