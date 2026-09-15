package com.smartparking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a parking ticket issued upon vehicle entry.
 */
public class ParkingTicket {
    private final String ticketId;
    private final String licensePlate;
    private final VehicleType vehicleType;
    private final ParkingSpot assignedSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double totalFee;
    private TicketStatus status;

    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSpot assignedSpot, LocalDateTime entryTime) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket ID cannot be null or empty.");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        if (assignedSpot == null) {
            throw new IllegalArgumentException("Assigned spot cannot be null.");
        }
        this.ticketId = ticketId.trim();
        this.licensePlate = vehicle.getLicensePlate();
        this.vehicleType = vehicle.getVehicleType();
        this.assignedSpot = assignedSpot;
        this.entryTime = entryTime != null ? entryTime : LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
        this.totalFee = 0.0;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public ParkingSpot getAssignedSpot() {
        return assignedSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Ticket[" +
                "id='" + ticketId + '\'' +
                ", vehicle='" + licensePlate + '\'' +
                ", type=" + vehicleType +
                ", spot=" + assignedSpot.getSpotId() +
                ", entry=" + entryTime.format(formatter) +
                ", exit=" + (exitTime != null ? exitTime.format(formatter) : "N/A") +
                ", fee=$" + String.format("%.2f", totalFee) +
                ", status=" + status +
                ']';
    }
}
