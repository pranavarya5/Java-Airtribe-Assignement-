package com.smartparking.model;

import java.util.Objects;

/**
 * Abstract base class representing a Vehicle.
 */
public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType vehicleType;

    public Vehicle(String licensePlate, VehicleType vehicleType) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty.");
        }
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null.");
        }
        this.licensePlate = licensePlate.trim().toUpperCase();
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(licensePlate, vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate);
    }

    @Override
    public String toString() {
        return vehicleType + " [" + licensePlate + "]";
    }
}
