package com.ridewise.model;

import java.util.Objects;

public class Ride {
    private final String id;
    private final Rider rider;
    private Driver driver;
    private final double distance;
    private final VehicleType vehicleType;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(String id, Rider rider, double distance, VehicleType vehicleType) {
        this.id = id;
        this.rider = rider;
        this.distance = distance;
        this.vehicleType = vehicleType;
        this.status = RideStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getDistance() {
        return distance;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void setFareReceipt(FareReceipt fareReceipt) {
        this.fareReceipt = fareReceipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return Objects.equals(id, ride.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String driverName = (driver != null) ? driver.getName() : "Unassigned";
        String fareStr = (fareReceipt != null) ? String.format("₹%.2f", fareReceipt.getAmount()) : "Pending";
        return String.format("Ride{id='%s', rider='%s', driver='%s', vehicle=%s, distance=%.2f km, status=%s, fare=%s}",
                id, rider.getName(), driverName, vehicleType, distance, status, fareStr);
    }
}
