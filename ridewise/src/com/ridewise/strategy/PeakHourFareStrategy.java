package com.ridewise.strategy;

import com.ridewise.model.Ride;

public class PeakHourFareStrategy implements FareStrategy {
    private final double surgeMultiplier;

    public PeakHourFareStrategy() {
        this(1.5);
    }

    public PeakHourFareStrategy(double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public double calculateFare(Ride ride) {
        DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        double baseFare = defaultFareStrategy.calculateFare(ride);
        return baseFare * surgeMultiplier;
    }
}
