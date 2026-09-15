package com.smartparking.gate;

import com.smartparking.exception.NoAvailableSpotException;
import com.smartparking.model.*;
import com.smartparking.strategy.allocation.SpotAllocationStrategy;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * EntryGate handles vehicle entry check-in, spot allocation, ticket issuance, and display notifications.
 */
public class EntryGate {
    private final String gateId;
    private final ParkingLot parkingLot;

    public EntryGate(String gateId, ParkingLot parkingLot) {
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
     * Thread-safe vehicle check-in procedure.
     */
    public synchronized ParkingTicket processEntry(Vehicle vehicle, SpotAllocationStrategy strategy, LocalDateTime entryTime)
            throws NoAvailableSpotException {

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("SpotAllocationStrategy cannot be null.");
        }

        ParkingSpot spot = strategy.allocateSpot(parkingLot, vehicle);
        if (spot == null) {
            throw new NoAvailableSpotException("No available spot matching vehicle: " + vehicle);
        }

        String ticketId = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot, entryTime != null ? entryTime : LocalDateTime.now());

        // Update real-time display observers
        parkingLot.notifyObservers();

        return ticket;
    }

    public ParkingTicket processEntry(Vehicle vehicle, SpotAllocationStrategy strategy) throws NoAvailableSpotException {
        return processEntry(vehicle, strategy, LocalDateTime.now());
    }
}
