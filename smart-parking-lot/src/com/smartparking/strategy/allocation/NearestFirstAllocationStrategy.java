package com.smartparking.strategy.allocation;

import com.smartparking.model.ParkingFloor;
import com.smartparking.model.ParkingLot;
import com.smartparking.model.ParkingSpot;
import com.smartparking.model.Vehicle;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy that prioritizes parking spots closest to the entry gate (lowest floor first).
 */
public class NearestFirstAllocationStrategy implements SpotAllocationStrategy {

    @Override
    public ParkingSpot allocateSpot(ParkingLot parkingLot, Vehicle vehicle) {
        if (parkingLot == null || vehicle == null) {
            return null;
        }

        List<ParkingFloor> sortedFloors = parkingLot.getFloors().stream()
                .sorted(Comparator.comparingInt(ParkingFloor::getFloorNumber))
                .collect(Collectors.toList());

        for (ParkingFloor floor : sortedFloors) {
            List<ParkingSpot> availableSpots = floor.getAvailableSpotsForVehicle(vehicle.getVehicleType());
            for (ParkingSpot spot : availableSpots) {
                // Attempt atomic spot reservation
                if (spot.assignVehicle(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }
}
