package com.ridewise.strategy;

import com.ridewise.model.Driver;
import com.ridewise.model.Rider;

import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (rider == null || drivers == null || drivers.isEmpty()) {
            return null;
        }

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : drivers) {
            if (driver.isAvailable() && driver.getCurrentLocation() != null) {
                double dist = rider.getLocation().distanceTo(driver.getCurrentLocation());
                if (dist < minDistance) {
                    minDistance = dist;
                    nearestDriver = driver;
                }
            }
        }

        return nearestDriver;
    }
}
