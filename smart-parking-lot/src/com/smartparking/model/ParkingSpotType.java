package com.smartparking.model;

/**
 * Enum representing parking spot sizes.
 */
public enum ParkingSpotType {
    MOTORCYCLE,
    COMPACT,
    LARGE;

    /**
     * Determines whether a spot type can fit a given vehicle type.
     */
    public boolean canFitVehicle(VehicleType vehicleType) {
        switch (this) {
            case MOTORCYCLE:
                return vehicleType == VehicleType.MOTORCYCLE;
            case COMPACT:
                return vehicleType == VehicleType.MOTORCYCLE || vehicleType == VehicleType.CAR;
            case LARGE:
                return true; // Large spots can fit Motorcycle, Car, or Bus
            default:
                return false;
        }
    }
}
