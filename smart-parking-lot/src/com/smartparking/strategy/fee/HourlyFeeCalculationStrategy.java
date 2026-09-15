package com.smartparking.strategy.fee;

import com.smartparking.model.ParkingTicket;
import com.smartparking.model.VehicleType;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Standard hourly fee calculation strategy with vehicle-type based rates.
 */
public class HourlyFeeCalculationStrategy implements FeeCalculationStrategy {
    private final double motorcycleHourlyRate;
    private final double carHourlyRate;
    private final double busHourlyRate;

    public HourlyFeeCalculationStrategy() {
        this(10.0, 20.0, 50.0);
    }

    public HourlyFeeCalculationStrategy(double motorcycleHourlyRate, double carHourlyRate, double busHourlyRate) {
        this.motorcycleHourlyRate = motorcycleHourlyRate;
        this.carHourlyRate = carHourlyRate;
        this.busHourlyRate = busHourlyRate;
    }

    @Override
    public double calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
        if (ticket == null || ticket.getEntryTime() == null) {
            return 0.0;
        }
        LocalDateTime exit = exitTime != null ? exitTime : LocalDateTime.now();
        if (exit.isBefore(ticket.getEntryTime())) {
            exit = ticket.getEntryTime();
        }

        Duration duration = Duration.between(ticket.getEntryTime(), exit);
        long minutes = duration.toMinutes();
        
        // Minimum 1 hour charge, rounded up for partial hours
        long hours = (long) Math.ceil(minutes / 60.0);
        if (hours <= 0) {
            hours = 1;
        }

        double hourlyRate = getRateForVehicleType(ticket.getVehicleType());
        return hours * hourlyRate;
    }

    private double getRateForVehicleType(VehicleType vehicleType) {
        switch (vehicleType) {
            case MOTORCYCLE:
                return motorcycleHourlyRate;
            case CAR:
                return carHourlyRate;
            case BUS:
                return busHourlyRate;
            default:
                return carHourlyRate;
        }
    }
}
