package com.smartparking.strategy.fee;

import com.smartparking.model.ParkingTicket;

import java.time.LocalDateTime;

/**
 * Strategy interface for calculating parking fees.
 */
public interface FeeCalculationStrategy {
    double calculateFee(ParkingTicket ticket, LocalDateTime exitTime);
}
