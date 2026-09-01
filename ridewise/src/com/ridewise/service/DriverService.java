package com.ridewise.service;

import com.ridewise.model.Driver;
import com.ridewise.model.Location;
import com.ridewise.model.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DriverService {
    private final Map<String, Driver> drivers = new HashMap<>();

    public Driver registerDriver(String id, String name, Location location, VehicleType vehicleType) {
        if (drivers.containsKey(id)) {
            throw new IllegalArgumentException("Driver with ID '" + id + "' already exists.");
        }
        Driver driver = new Driver(id, name, location, vehicleType);
        drivers.put(id, driver);
        return driver;
    }

    public Driver getDriverById(String id) {
        return drivers.get(id);
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        if (driver == null) {
            throw new IllegalArgumentException("Driver with ID '" + driverId + "' not found.");
        }
        driver.setAvailable(available);
    }

    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers.values());
    }
}
