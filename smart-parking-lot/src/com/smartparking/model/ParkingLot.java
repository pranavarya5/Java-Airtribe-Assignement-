package com.smartparking.model;

import com.smartparking.display.DisplayBoard;
import com.smartparking.display.DisplayBoardObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main ParkingLot entity managing floors, capacity, and display observers.
 */
public class ParkingLot {
    private static volatile ParkingLot instance;

    private final String name;
    private final List<ParkingFloor> floors;
    private final List<DisplayBoardObserver> observers;
    private DisplayBoard displayBoard;

    public ParkingLot(String name) {
        this.name = name;
        this.floors = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
    }

    /**
     * Singleton instance provider.
     */
    public static ParkingLot getInstance(String name) {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot(name);
                }
            }
        }
        return instance;
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            return getInstance("Smart City Parking Lot");
        }
        return instance;
    }

    public static void resetInstance() {
        synchronized (ParkingLot.class) {
            instance = null;
        }
    }

    public String getName() {
        return name;
    }

    public void addFloor(ParkingFloor floor) {
        if (floor != null) {
            floors.add(floor);
        }
    }

    public List<ParkingFloor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public ParkingFloor getFloor(int floorNumber) {
        for (ParkingFloor floor : floors) {
            if (floor.getFloorNumber() == floorNumber) {
                return floor;
            }
        }
        return null;
    }

    public DisplayBoard getDisplayBoard() {
        return displayBoard;
    }

    public void setDisplayBoard(DisplayBoard displayBoard) {
        this.displayBoard = displayBoard;
        if (displayBoard != null && !observers.contains(displayBoard)) {
            observers.add(displayBoard);
        }
    }

    public void registerObserver(DisplayBoardObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregisterObserver(DisplayBoardObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (DisplayBoardObserver observer : observers) {
            observer.onSpotStatusChanged();
        }
    }
}
