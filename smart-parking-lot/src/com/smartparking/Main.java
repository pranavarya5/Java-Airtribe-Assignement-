package com.smartparking;

import com.smartparking.display.DisplayBoard;
import com.smartparking.exception.InvalidTicketException;
import com.smartparking.exception.NoAvailableSpotException;
import com.smartparking.exception.PaymentFailedException;
import com.smartparking.gate.EntryGate;
import com.smartparking.gate.ExitGate;
import com.smartparking.model.*;
import com.smartparking.service.ParkingLotService;
import com.smartparking.strategy.allocation.FirstAvailableAllocationStrategy;
import com.smartparking.strategy.allocation.NearestFirstAllocationStrategy;
import com.smartparking.strategy.fee.HourlyFeeCalculationStrategy;
import com.smartparking.strategy.fee.TieredFeeCalculationStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("        SMART PARKING LOT SYSTEM - LOW LEVEL DESIGN DEMO               ");
        System.out.println("=======================================================================\n");

        // 1. Initialize Parking Lot & Floors
        ParkingLot.resetInstance();
        ParkingLot parkingLot = ParkingLot.getInstance("Airtribe Central Parking Plaza");

        // Create Floor 1: 2 Motorcycle, 2 Compact, 1 Large
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(new ParkingSpot("F1-M1", 1, ParkingSpotType.MOTORCYCLE));
        floor1.addSpot(new ParkingSpot("F1-M2", 1, ParkingSpotType.MOTORCYCLE));
        floor1.addSpot(new ParkingSpot("F1-C1", 1, ParkingSpotType.COMPACT));
        floor1.addSpot(new ParkingSpot("F1-C2", 1, ParkingSpotType.COMPACT));
        floor1.addSpot(new ParkingSpot("F1-L1", 1, ParkingSpotType.LARGE));
        parkingLot.addFloor(floor1);

        // Create Floor 2: 1 Motorcycle, 2 Compact, 1 Large
        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(new ParkingSpot("F2-M1", 2, ParkingSpotType.MOTORCYCLE));
        floor2.addSpot(new ParkingSpot("F2-C1", 2, ParkingSpotType.COMPACT));
        floor2.addSpot(new ParkingSpot("F2-C2", 2, ParkingSpotType.COMPACT));
        floor2.addSpot(new ParkingSpot("F2-L1", 2, ParkingSpotType.LARGE));
        parkingLot.addFloor(floor2);

        // Setup Display Board & Observers
        DisplayBoard displayBoard = new DisplayBoard("GATE-BOARD-1", parkingLot.getFloors());
        parkingLot.setDisplayBoard(displayBoard);

        // Initialize Service & Gates
        ParkingLotService service = new ParkingLotService(parkingLot);
        EntryGate entryGate1 = new EntryGate("ENTRY-GATE-A", parkingLot);
        EntryGate entryGate2 = new EntryGate("ENTRY-GATE-B", parkingLot);
        ExitGate exitGate1 = new ExitGate("EXIT-GATE-A", parkingLot);
        ExitGate exitGate2 = new ExitGate("EXIT-GATE-B", parkingLot);

        service.registerEntryGate(entryGate1);
        service.registerEntryGate(entryGate2);
        service.registerExitGate(exitGate1);
        service.registerExitGate(exitGate2);

        System.out.println("--- INITIAL STATE ---");
        displayBoard.showDisplay();

        // 2. DEMO: Sequential Check-in Operations
        System.out.println("\n>>> [DEMO 1] SEQUENTIAL VEHICLE CHECK-INS <<<");
        List<ParkingTicket> tickets = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        try {
            // Check-in Vehicle 1: Motorcycle (Nearest First strategy)
            Vehicle v1 = new Motorcycle("KA-01-M-1234");
            ParkingTicket t1 = service.checkIn("ENTRY-GATE-A", v1, new NearestFirstAllocationStrategy(), now.minusHours(3));
            tickets.add(t1);
            System.out.println("Check-in Success: " + v1 + " -> Assigned Spot: " + t1.getAssignedSpot().getSpotId());

            // Check-in Vehicle 2: Car (First Available strategy)
            Vehicle v2 = new Car("KA-05-C-5678");
            ParkingTicket t2 = service.checkIn("ENTRY-GATE-B", v2, new FirstAvailableAllocationStrategy(), now.minusHours(2));
            tickets.add(t2);
            System.out.println("Check-in Success: " + v2 + " -> Assigned Spot: " + t2.getAssignedSpot().getSpotId());

            // Check-in Vehicle 3: Bus (Large spot)
            Vehicle v3 = new Bus("KA-09-B-9999");
            ParkingTicket t3 = service.checkIn("ENTRY-GATE-A", v3, new NearestFirstAllocationStrategy(), now.minusHours(5));
            tickets.add(t3);
            System.out.println("Check-in Success: " + v3 + " -> Assigned Spot: " + t3.getAssignedSpot().getSpotId());

            // Check-in Vehicle 4: Car (Compact spot on Floor 1)
            Vehicle v4 = new Car("MH-12-AB-3344");
            ParkingTicket t4 = service.checkIn("ENTRY-GATE-B", v4, new NearestFirstAllocationStrategy(), now.minusHours(1));
            tickets.add(t4);
            System.out.println("Check-in Success: " + v4 + " -> Assigned Spot: " + t4.getAssignedSpot().getSpotId());

        } catch (NoAvailableSpotException e) {
            System.err.println("Check-in Failed: " + e.getMessage());
        }

        // 3. DEMO: Check-out and Fee Calculation
        System.out.println("\n>>> [DEMO 2] VEHICLE CHECK-OUT & FEE CALCULATION <<<");
        try {
            // Check-out Ticket 1 (Motorcycle stayed 3 hours, Hourly Fee Strategy)
            ParkingTicket ticketToExit1 = tickets.get(0);
            Payment pay1 = service.checkOut("EXIT-GATE-A", ticketToExit1.getTicketId(), new HourlyFeeCalculationStrategy(), PaymentMode.UPI, now);
            System.out.println("Check-out Complete for " + ticketToExit1.getLicensePlate() + ": " + pay1);

            // Check-out Ticket 3 (Bus stayed 5 hours, Tiered Fee Strategy)
            ParkingTicket ticketToExit3 = tickets.get(2);
            Payment pay3 = service.checkOut("EXIT-GATE-B", ticketToExit3.getTicketId(), new TieredFeeCalculationStrategy(), PaymentMode.CREDIT_CARD, now);
            System.out.println("Check-out Complete for " + ticketToExit3.getLicensePlate() + ": " + pay3);

        } catch (InvalidTicketException | PaymentFailedException e) {
            System.err.println("Check-out Failed: " + e.getMessage());
        }

        // 4. DEMO: Error Handling (Spot allocation failure & invalid ticket check-out)
        System.out.println("\n>>> [DEMO 3] EDGE CASES & EXCEPTION HANDLING <<<");

        // Edge Case A: Try checking out an already processed ticket
        try {
            System.out.println("Attempting double checkout of processed ticket...");
            service.checkOut("EXIT-GATE-A", tickets.get(0).getTicketId(), PaymentMode.CASH);
        } catch (InvalidTicketException | PaymentFailedException e) {
            System.out.println("Expected Exception Caught: " + e.getMessage());
        }

        // Edge Case B: Fill up remaining Bus spots and test Lot Full for Bus
        try {
            System.out.println("Attempting to park buses when large spots are limited...");
            Vehicle bus2 = new Bus("KA-51-BUS-222");
            ParkingTicket tBus2 = service.checkIn("ENTRY-GATE-A", bus2);
            System.out.println("Check-in Success: " + bus2 + " -> Spot: " + tBus2.getAssignedSpot().getSpotId());

            Vehicle bus3 = new Bus("KA-51-BUS-333");
            System.out.println("Attempting to park 3rd bus...");
            service.checkIn("ENTRY-GATE-A", bus3);
        } catch (NoAvailableSpotException e) {
            System.out.println("Expected Exception Caught: " + e.getMessage());
        }

        // 5. DEMO: Multi-threaded Concurrency Simulation
        System.out.println("\n>>> [DEMO 4] CONCURRENCY & THREAD-SAFETY TEST <<<");
        System.out.println("Simulating 8 simultaneous entry requests across multiple threads...");

        ExecutorService executor = Executors.newFixedThreadPool(4);
        NearestFirstAllocationStrategy concurrentStrategy = new NearestFirstAllocationStrategy();

        for (int i = 1; i <= 8; i++) {
            final int vehicleId = i;
            executor.submit(() -> {
                try {
                    Vehicle vehicle;
                    if (vehicleId % 2 == 0) {
                        vehicle = new Car("CONC-CAR-" + vehicleId);
                    } else {
                        vehicle = new Motorcycle("CONC-MOTO-" + vehicleId);
                    }
                    ParkingTicket ticket = service.checkIn("ENTRY-GATE-A", vehicle, concurrentStrategy, LocalDateTime.now());
                    System.out.println("[Thread " + Thread.currentThread().getId() + "] SUCCESS: Parked " + vehicle.getLicensePlate()
                            + " at " + ticket.getAssignedSpot().getSpotId());
                } catch (NoAvailableSpotException e) {
                    System.out.println("[Thread " + Thread.currentThread().getId() + "] REJECTED: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("[Thread " + Thread.currentThread().getId() + "] ERROR: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- FINAL PARKING LOT STATE ---");
        displayBoard.showDisplay();

        System.out.println("=======================================================================");
        System.out.println("      SMART PARKING LOT SYSTEM DEMO COMPLETED SUCCESSFULLY!            ");
        System.out.println("=======================================================================");
    }
}
