package com.smartparking.gate;

import com.smartparking.exception.InvalidTicketException;
import com.smartparking.exception.PaymentFailedException;
import com.smartparking.model.*;
import com.smartparking.strategy.fee.FeeCalculationStrategy;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ExitGate handles vehicle exit check-out, fee calculation, payment processing, spot release, and display updates.
 */
public class ExitGate {
    private final String gateId;
    private final ParkingLot parkingLot;

    public ExitGate(String gateId, ParkingLot parkingLot) {
        if (gateId == null || gateId.trim().isEmpty()) {
            throw new IllegalArgumentException("Gate ID cannot be null or empty.");
        }
        if (parkingLot == null) {
            throw new IllegalArgumentException("ParkingLot cannot be null.");
        }
        this.gateId = gateId.trim();
        this.parkingLot = parkingLot;
    }

    public String getGateId() {
        return gateId;
    }

    /**
     * Thread-safe vehicle check-out procedure.
     */
    public synchronized Payment processExit(ParkingTicket ticket, FeeCalculationStrategy feeStrategy, PaymentMode paymentMode, LocalDateTime exitTime)
            throws InvalidTicketException, PaymentFailedException {

        if (ticket == null) {
            throw new InvalidTicketException("Ticket cannot be null.");
        }
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new InvalidTicketException("Ticket is already processed or invalid: " + ticket.getTicketId());
        }
        if (feeStrategy == null) {
            throw new IllegalArgumentException("FeeCalculationStrategy cannot be null.");
        }

        LocalDateTime exit = exitTime != null ? exitTime : LocalDateTime.now();
        double fee = feeStrategy.calculateFee(ticket, exit);

        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = new Payment(paymentId, ticket.getTicketId(), fee, paymentMode);
        payment.setStatus(PaymentStatus.COMPLETED);

        // Update ticket
        ticket.setExitTime(exit);
        ticket.setTotalFee(fee);
        ticket.setStatus(TicketStatus.COMPLETED);

        // Free up assigned spot
        ParkingSpot spot = ticket.getAssignedSpot();
        if (spot != null) {
            spot.removeVehicle();
        }

        // Update observers
        parkingLot.notifyObservers();

        return payment;
    }

    public Payment processExit(ParkingTicket ticket, FeeCalculationStrategy feeStrategy, PaymentMode paymentMode)
            throws InvalidTicketException, PaymentFailedException {
        return processExit(ticket, feeStrategy, paymentMode, LocalDateTime.now());
    }
}
