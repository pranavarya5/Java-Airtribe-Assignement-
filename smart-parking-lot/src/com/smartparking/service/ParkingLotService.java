package com.smartparking.service;

import com.smartparking.exception.InvalidTicketException;
import com.smartparking.exception.NoAvailableSpotException;
import com.smartparking.exception.PaymentFailedException;
import com.smartparking.gate.EntryGate;
import com.smartparking.gate.ExitGate;
import com.smartparking.model.*;
import com.smartparking.strategy.allocation.NearestFirstAllocationStrategy;
import com.smartparking.strategy.allocation.SpotAllocationStrategy;
import com.smartparking.strategy.fee.FeeCalculationStrategy;
import com.smartparking.strategy.fee.HourlyFeeCalculationStrategy;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade service for high-level Smart Parking Lot management.
 */
public class ParkingLotService {
    private final ParkingLot parkingLot;
    private final Map<String, EntryGate> entryGates;
    private final Map<String, ExitGate> exitGates;
    private final Map<String, ParkingTicket> activeTickets;

    private SpotAllocationStrategy defaultAllocationStrategy;
    private FeeCalculationStrategy defaultFeeStrategy;

    public ParkingLotService(ParkingLot parkingLot) {
        if (parkingLot == null) {
            throw new IllegalArgumentException("ParkingLot cannot be null.");
        }
        this.parkingLot = parkingLot;
        this.entryGates = new ConcurrentHashMap<>();
        this.exitGates = new ConcurrentHashMap<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.defaultAllocationStrategy = new NearestFirstAllocationStrategy();
        this.defaultFeeStrategy = new HourlyFeeCalculationStrategy();
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void registerEntryGate(EntryGate gate) {
        if (gate != null) {
            entryGates.put(gate.getGateId(), gate);
        }
    }

    public void registerExitGate(ExitGate gate) {
        if (gate != null) {
            exitGates.put(gate.getGateId(), gate);
        }
    }

    public void setDefaultAllocationStrategy(SpotAllocationStrategy defaultAllocationStrategy) {
        this.defaultAllocationStrategy = defaultAllocationStrategy;
    }

    public void setDefaultFeeStrategy(FeeCalculationStrategy defaultFeeStrategy) {
        this.defaultFeeStrategy = defaultFeeStrategy;
    }

    /**
     * Vehicle Entry operation.
     */
    public ParkingTicket checkIn(String gateId, Vehicle vehicle, SpotAllocationStrategy strategy, LocalDateTime entryTime)
            throws NoAvailableSpotException {
        EntryGate gate = entryGates.get(gateId);
        if (gate == null) {
            throw new IllegalArgumentException("Entry gate not registered: " + gateId);
        }
        SpotAllocationStrategy allocStrategy = strategy != null ? strategy : defaultAllocationStrategy;
        ParkingTicket ticket = gate.processEntry(vehicle, allocStrategy, entryTime);
        activeTickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    public ParkingTicket checkIn(String gateId, Vehicle vehicle) throws NoAvailableSpotException {
        return checkIn(gateId, vehicle, defaultAllocationStrategy, LocalDateTime.now());
    }

    /**
     * Vehicle Exit operation.
     */
    public Payment checkOut(String gateId, String ticketId, FeeCalculationStrategy feeStrategy, PaymentMode mode, LocalDateTime exitTime)
            throws InvalidTicketException, PaymentFailedException {
        ExitGate gate = exitGates.get(gateId);
        if (gate == null) {
            throw new IllegalArgumentException("Exit gate not registered: " + gateId);
        }
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new InvalidTicketException("Ticket not found in active records: " + ticketId);
        }

        FeeCalculationStrategy feeCalcStrategy = feeStrategy != null ? feeStrategy : defaultFeeStrategy;
        Payment payment = gate.processExit(ticket, feeCalcStrategy, mode, exitTime);
        activeTickets.remove(ticketId);
        return payment;
    }

    public Payment checkOut(String gateId, String ticketId, PaymentMode mode)
            throws InvalidTicketException, PaymentFailedException {
        return checkOut(gateId, ticketId, defaultFeeStrategy, mode, LocalDateTime.now());
    }

    public ParkingTicket getActiveTicket(String ticketId) {
        return activeTickets.get(ticketId);
    }

    public int getActiveTicketCount() {
        return activeTickets.size();
    }
}
