package com.smartparking.model;

import com.smartparking.exception.SpotAlreadyOccupiedException;

/**
 * Represents an individual parking spot on a floor.
 * Uses synchronized methods for thread-safe vehicle allocation and release.
 */
public class ParkingSpot {
    private final String spotId;
    private final int floorNumber;
    private final ParkingSpotType spotType;
    private boolean isOccupied;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, int floorNumber, ParkingSpotType spotType) {
        if (spotId == null || spotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Spot ID cannot be null or empty.");
        }
        if (spotType == null) {
            throw new IllegalArgumentException("Spot type cannot be null.");
        }
        this.spotId = spotId.trim();
        this.floorNumber = floorNumber;
        this.spotType = spotType;
        this.isOccupied = false;
        this.parkedVehicle = null;
    }

    public String getSpotId() {
        return spotId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public ParkingSpotType getSpotType() {
        return spotType;
    }

    public synchronized boolean isAvailable() {
        return !isOccupied;
    }

    public synchronized Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public boolean canFit(VehicleType vehicleType) {
        return spotType.canFitVehicle(vehicleType);
    }

    /**
     * Atomically assigns a vehicle to this parking spot if available and compatible.
     */
    public synchronized boolean assignVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        if (isOccupied) {
            return false;
        }
        if (!canFit(vehicle.getVehicleType())) {
            return false;
        }
        this.parkedVehicle = vehicle;
        this.isOccupied = true;
        return true;
    }

    /**
     * Atomically releases this parking spot and returns the parked vehicle.
     */
    public synchronized Vehicle removeVehicle() {
        if (!isOccupied) {
            return null;
        }
        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        this.isOccupied = false;
        return vehicle;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "id='" + spotId + '\'' +
                ", floor=" + floorNumber +
                ", type=" + spotType +
                ", occupied=" + isOccupied +
                '}';
    }
}
