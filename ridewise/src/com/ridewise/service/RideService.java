package com.ridewise.service;

import com.ridewise.model.*;
import com.ridewise.strategy.FareStrategy;
import com.ridewise.strategy.RideMatchingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideService {
    private final Map<String, Ride> rides = new HashMap<>();
    private RideMatchingStrategy matchingStrategy;
    private FareStrategy fareStrategy;
    private int rideCounter = 1;

    public RideService(RideMatchingStrategy matchingStrategy, FareStrategy fareStrategy) {
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public void setMatchingStrategy(RideMatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public void setFareStrategy(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(Rider rider, double distance, VehicleType vehicleType, List<Driver> candidateDrivers) {
        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null.");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0.");
        }

        String rideId = "RIDE-" + (rideCounter++);
        Ride ride = new Ride(rideId, rider, distance, vehicleType);

        Driver matchedDriver = matchingStrategy.findDriver(rider, candidateDrivers);
        if (matchedDriver != null) {
            ride.setDriver(matchedDriver);
            ride.setStatus(RideStatus.ASSIGNED);
            matchedDriver.setAvailable(false);
        } else {
            ride.setStatus(RideStatus.REQUESTED);
        }

        rides.put(rideId, ride);
        return ride;
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride with ID '" + rideId + "' not found.");
        }

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new IllegalStateException("Ride is already completed.");
        }

        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete a cancelled ride.");
        }

        if (ride.getDriver() == null) {
            throw new IllegalStateException("Cannot complete a ride without an assigned driver.");
        }

        double fareAmount = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(rideId, fareAmount);

        ride.setFareReceipt(receipt);
        ride.setStatus(RideStatus.COMPLETED);

        Driver driver = ride.getDriver();
        driver.setAvailable(true);
        driver.incrementCompletedRides();

        return receipt;
    }

    public void cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride with ID '" + rideId + "' not found.");
        }

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed ride.");
        }

        ride.setStatus(RideStatus.CANCELLED);
        if (ride.getDriver() != null) {
            ride.getDriver().setAvailable(true);
        }
    }

    public Ride getRideById(String rideId) {
        return rides.get(rideId);
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides.values());
    }
}
