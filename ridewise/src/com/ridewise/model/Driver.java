package com.ridewise.model;

import java.util.Objects;

public class Driver {
    private final String id;
    private String name;
    private Location currentLocation;
    private boolean available;
    private VehicleType vehicleType;
    private int completedRidesCount;

    public Driver(String id, String name, Location currentLocation, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.available = true;
        this.vehicleType = vehicleType;
        this.completedRidesCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getCompletedRidesCount() {
        return completedRidesCount;
    }

    public void incrementCompletedRides() {
        this.completedRidesCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Driver{id='%s', name='%s', location=%s, available=%b, vehicle=%s, rides=%d}",
                id, name, currentLocation, available, vehicleType, completedRidesCount);
    }
}
