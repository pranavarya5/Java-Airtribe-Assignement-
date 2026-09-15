package com.smartparking.display;

import com.smartparking.model.ParkingFloor;
import com.smartparking.model.ParkingSpotType;

import java.util.ArrayList;
import java.util.List;

/**
 * DisplayBoard displays real-time available spot counts per floor and spot size.
 * Implements Observer pattern to react to check-in / check-out events.
 */
public class DisplayBoard implements DisplayBoardObserver {
    private final String displayId;
    private final List<ParkingFloor> floors;

    public DisplayBoard(String displayId, List<ParkingFloor> floors) {
        this.displayId = displayId;
        this.floors = floors != null ? floors : new ArrayList<>();
    }

    @Override
    public void onSpotStatusChanged() {
        showDisplay();
    }

    /**
     * Prints current real-time parking availability dashboard.
     */
    public synchronized void showDisplay() {
        System.out.println("=================================================");
        System.out.println("   REAL-TIME PARKING AVAILABILITY BOARD [" + displayId + "]");
        System.out.println("=================================================");
        int totalAvailable = 0;

        for (ParkingFloor floor : floors) {
            int motoCount = floor.getAvailableSpotCount(ParkingSpotType.MOTORCYCLE);
            int compactCount = floor.getAvailableSpotCount(ParkingSpotType.COMPACT);
            int largeCount = floor.getAvailableSpotCount(ParkingSpotType.LARGE);
            int floorTotal = floor.getTotalAvailableSpotCount();
            totalAvailable += floorTotal;

            System.out.printf("Floor %d -> Motorcycle: %d | Compact: %d | Large: %d | Total Free: %d%n",
                    floor.getFloorNumber(), motoCount, compactCount, largeCount, floorTotal);
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("TOTAL PARKING LOT FREE SPOTS: %d%n", totalAvailable);
        System.out.println("=================================================\n");
    }
}
