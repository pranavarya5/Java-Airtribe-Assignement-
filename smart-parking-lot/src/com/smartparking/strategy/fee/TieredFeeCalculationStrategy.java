package com.smartparking.strategy.fee;

import com.smartparking.model.ParkingTicket;
import com.smartparking.model.VehicleType;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Tiered fee strategy offering discounted rates for longer parking stays.
 */
public class TieredFeeCalculationStrategy implements FeeCalculationStrategy {

    @Override
    public double calculateFee(ParkingTicket ticket, LocalDateTime exitTime) {
        if (ticket == null || ticket.getEntryTime() == null) {
            return 0.0;
        }
        LocalDateTime exit = exitTime != null ? exitTime : LocalDateTime.now();
        if (exit.isBefore(ticket.getEntryTime())) {
            exit = ticket.getEntryTime();
        }

        long minutes = Duration.between(ticket.getEntryTime(), exit).toMinutes();
        long hours = (long) Math.ceil(minutes / 60.0);
        if (hours <= 0) {
            hours = 1;
        }

        VehicleType type = ticket.getVehicleType();
        double baseFirstHour = getBaseRate(type);
        double midRate = getMidRate(type);
        double longStayRate = getLongStayRate(type);

        if (hours == 1) {
            return baseFirstHour;
        } else if (hours <= 5) {
            return baseFirstHour + (hours - 1) * midRate;
        } else {
            return baseFirstHour + (4 * midRate) + (hours - 5) * longStayRate;
        }
    }

    private double getBaseRate(VehicleType type) {
        switch (type) {
            case MOTORCYCLE: return 10.0;
            case CAR: return 20.0;
            case BUS: return 50.0;
            default: return 20.0;
        }
    }

    private double getMidRate(VehicleType type) {
        switch (type) {
            case MOTORCYCLE: return 8.0;
            case CAR: return 15.0;
            case BUS: return 40.0;
            default: return 15.0;
        }
    }

    private double getLongStayRate(VehicleType type) {
        switch (type) {
            case MOTORCYCLE: return 5.0;
            case CAR: return 10.0;
            case BUS: return 25.0;
            default: return 10.0;
        }
    }
}
