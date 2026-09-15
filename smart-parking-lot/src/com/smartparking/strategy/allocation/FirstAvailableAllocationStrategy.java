package com.smartparking.strategy.allocation;

import com.smartparking.model.ParkingFloor;
import com.smartparking.model.ParkingLot;
import com.smartparking.model.ParkingSpot;
import com.smartparking.model.Vehicle;

import java.util.List;

/**
 * Strategy that assigns the first available matching spot found across floors.
 */
public class FirstAvailableAllocationStrategy implements SpotAllocationStrategy {

    @Override
    public ParkingSpot allocateSpot(ParkingLot parkingLot, Vehicle vehicle) {
        if (parkingLot == null || vehicle == null) {
            return null;
        }

        for (ParkingFloor floor : parkingLot.getFloors()) {
            List<ParkingSpot> availableSpots = floor.getAvailableSpotsForVehicle(vehicle.getVehicleType());
            for (ParkingSpot spot : availableSpots) {
                if (spot.assignVehicle(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }
}
