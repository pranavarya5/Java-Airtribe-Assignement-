package com.smartparking.strategy.allocation;

import com.smartparking.model.ParkingLot;
import com.smartparking.model.ParkingSpot;
import com.smartparking.model.Vehicle;

/**
 * Strategy interface for assigning parking spots to incoming vehicles.
 */
public interface SpotAllocationStrategy {
    ParkingSpot allocateSpot(ParkingLot parkingLot, Vehicle vehicle);
}
