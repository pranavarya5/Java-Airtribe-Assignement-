package com.smartparking.model;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a single floor in the Smart Parking Lot containing multiple spots.
 */
public class ParkingFloor {
    private final int floorNumber;
    private final Map<String, ParkingSpot> spotMap;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spotMap = new ConcurrentHashMap<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void addSpot(ParkingSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Parking spot cannot be null.");
        }
        spotMap.put(spot.getSpotId(), spot);
    }

    public ParkingSpot getSpot(String spotId) {
        return spotMap.get(spotId);
    }

    public List<ParkingSpot> getSpots() {
        return new ArrayList<>(spotMap.values());
    }

    /**
     * Returns all currently available spots matching the vehicle size requirement.
     */
    public List<ParkingSpot> getAvailableSpotsForVehicle(VehicleType vehicleType) {
        List<ParkingSpot> available = new ArrayList<>();
        for (ParkingSpot spot : spotMap.values()) {
            if (spot.isAvailable() && spot.canFit(vehicleType)) {
                available.add(spot);
            }
        }
        return available;
    }

    /**
     * Counts available spots for a specific spot type.
     */
    public int getAvailableSpotCount(ParkingSpotType spotType) {
        int count = 0;
        for (ParkingSpot spot : spotMap.values()) {
            if (spot.isAvailable() && spot.getSpotType() == spotType) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts total available spots on this floor.
     */
    public int getTotalAvailableSpotCount() {
        int count = 0;
        for (ParkingSpot spot : spotMap.values()) {
            if (spot.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "Floor " + floorNumber + " [Spots: " + spotMap.size() + ", Available: " + getTotalAvailableSpotCount() + "]";
    }
}
