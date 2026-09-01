package com.ridewise.model;

public enum VehicleType {
    BIKE(20.0, 5.0),
    AUTO(30.0, 8.0),
    CAR(50.0, 12.0);

    private final double baseFare;
    private final double perKmRate;

    VehicleType(double baseFare, double perKmRate) {
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public double getPerKmRate() {
        return perKmRate;
    }
}
