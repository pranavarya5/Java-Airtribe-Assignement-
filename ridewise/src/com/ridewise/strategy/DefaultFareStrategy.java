package com.ridewise.strategy;

import com.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {

    @Override
    public double calculateFare(Ride ride) {
        if (ride == null || ride.getVehicleType() == null) {
            return 0.0;
        }

        double baseFare = ride.getVehicleType().getBaseFare();
        double perKmRate = ride.getVehicleType().getPerKmRate();
        double distance = ride.getDistance();

        return baseFare + (distance * perKmRate);
    }
}
