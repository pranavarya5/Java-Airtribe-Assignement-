package com.ridewise.strategy;

import com.ridewise.model.Driver;
import com.ridewise.model.Rider;

import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (drivers == null || drivers.isEmpty()) {
            return null;
        }

        Driver leastActive = null;
        int minRides = Integer.MAX_VALUE;

        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                if (driver.getCompletedRidesCount() < minRides) {
                    minRides = driver.getCompletedRidesCount();
                    leastActive = driver;
                }
            }
        }

        return leastActive;
    }
}
