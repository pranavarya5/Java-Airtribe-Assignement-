package com.ridewise;

import com.ridewise.model.*;
import com.ridewise.service.DriverService;
import com.ridewise.service.RideService;
import com.ridewise.service.RiderService;
import com.ridewise.strategy.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final RiderService riderService = new RiderService();
    private static final DriverService driverService = new DriverService();
    private static RideMatchingStrategy currentMatchingStrategy = new NearestDriverStrategy();
    private static FareStrategy currentFareStrategy = new DefaultFareStrategy();
    private static final RideService rideService = new RideService(currentMatchingStrategy, currentFareStrategy);

    public static void main(String[] args) {
        seedInitialData();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("====================================================");
        System.out.println("   🚗 Welcome to RideWise — Ride-Sharing System 🚗   ");
        System.out.println("====================================================");

        while (running) {
            printMenu();
            System.out.print("Select an option (1-8): ");
            String input = scanner.nextLine().trim();

            try {
                switch (input) {
                    case "1":
                        handleAddRider(scanner);
                        break;
                    case "2":
                        handleAddDriver(scanner);
                        break;
                    case "3":
                        handleViewAvailableDrivers();
                        break;
                    case "4":
                        handleRequestRide(scanner);
                        break;
                    case "5":
                        handleCompleteRide(scanner);
                        break;
                    case "6":
                        handleViewRides();
                        break;
                    case "7":
                        handleConfigureStrategies(scanner);
                        break;
                    case "8":
                        running = false;
                        System.out.println("\nThank you for using RideWise! Goodbye!");
                        break;
                    default:
                        System.out.println("\n❌ Invalid choice. Please enter a number from 1 to 8.");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n----------------------------------------------------");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Configure Strategies (Matching & Pricing)");
        System.out.println("8. Exit");
        System.out.println("----------------------------------------------------");
    }

    private static void handleAddRider(Scanner scanner) {
        System.out.println("\n--- Add Rider ---");
        System.out.print("Enter Rider ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Rider Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Location X coordinate: ");
        double x = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter Location Y coordinate: ");
        double y = Double.parseDouble(scanner.nextLine().trim());

        Rider rider = riderService.registerRider(id, name, new Location(x, y));
        System.out.println("✅ Registered successfully: " + rider);
    }

    private static void handleAddDriver(Scanner scanner) {
        System.out.println("\n--- Add Driver ---");
        System.out.print("Enter Driver ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Driver Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Location X coordinate: ");
        double x = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter Location Y coordinate: ");
        double y = Double.parseDouble(scanner.nextLine().trim());

        System.out.println("Select Vehicle Type:");
        System.out.println("1. BIKE");
        System.out.println("2. AUTO");
        System.out.println("3. CAR");
        System.out.print("Choice: ");
        String vChoice = scanner.nextLine().trim();

        VehicleType vehicleType;
        switch (vChoice) {
            case "1": vehicleType = VehicleType.BIKE; break;
            case "2": vehicleType = VehicleType.AUTO; break;
            case "3": vehicleType = VehicleType.CAR; break;
            default:
                System.out.println("⚠️ Unknown choice, defaulting to CAR.");
                vehicleType = VehicleType.CAR;
        }

        Driver driver = driverService.registerDriver(id, name, new Location(x, y), vehicleType);
        System.out.println("✅ Driver registered successfully: " + driver);
    }

    private static void handleViewAvailableDrivers() {
        System.out.println("\n--- Available Drivers ---");
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            System.out.println("No drivers currently available.");
        } else {
            for (Driver driver : availableDrivers) {
                System.out.println(" • " + driver);
            }
        }
    }

    private static void handleRequestRide(Scanner scanner) {
        System.out.println("\n--- Request Ride ---");
        System.out.print("Enter Rider ID: ");
        String riderId = scanner.nextLine().trim();
        Rider rider = riderService.getRiderById(riderId);
        if (rider == null) {
            System.out.println("❌ Rider with ID '" + riderId + "' not found. Register rider first.");
            return;
        }

        System.out.print("Enter Distance (in km): ");
        double distance = Double.parseDouble(scanner.nextLine().trim());

        System.out.println("Select Desired Vehicle Type:");
        System.out.println("1. BIKE");
        System.out.println("2. AUTO");
        System.out.println("3. CAR");
        System.out.print("Choice: ");
        String vChoice = scanner.nextLine().trim();

        VehicleType vehicleType;
        switch (vChoice) {
            case "1": vehicleType = VehicleType.BIKE; break;
            case "2": vehicleType = VehicleType.AUTO; break;
            case "3": vehicleType = VehicleType.CAR; break;
            default: vehicleType = VehicleType.CAR;
        }

        List<Driver> candidateDrivers = driverService.getAvailableDrivers();
        Ride ride = rideService.requestRide(rider, distance, vehicleType, candidateDrivers);

        if (ride.getStatus() == RideStatus.ASSIGNED) {
            System.out.println("🎉 Ride Created & Assigned!");
            System.out.println("   " + ride);
        } else {
            System.out.println("⚠️ Ride requested but no driver was available to match right now.");
            System.out.println("   " + ride);
        }
    }

    private static void handleCompleteRide(Scanner scanner) {
        System.out.println("\n--- Complete Ride ---");
        System.out.print("Enter Ride ID (e.g. RIDE-1): ");
        String rideId = scanner.nextLine().trim();

        FareReceipt receipt = rideService.completeRide(rideId);
        System.out.println("✅ Ride Completed Successfully!");
        System.out.println("🧾 Receipt: " + receipt);
    }

    private static void handleViewRides() {
        System.out.println("\n--- All Rides ---");
        List<Ride> rides = rideService.getAllRides();
        if (rides.isEmpty()) {
            System.out.println("No rides have been requested yet.");
        } else {
            for (Ride ride : rides) {
                System.out.println(" • " + ride);
            }
        }
    }

    private static void handleConfigureStrategies(Scanner scanner) {
        System.out.println("\n--- Configure System Strategies ---");
        System.out.println("Current Matching Strategy: " + currentMatchingStrategy.getClass().getSimpleName());
        System.out.println("Current Pricing Strategy:  " + currentFareStrategy.getClass().getSimpleName());
        System.out.println("\nSelect Strategy to Change:");
        System.out.println("1. Change Matching Strategy (Nearest Driver vs. Least Active Driver)");
        System.out.println("2. Change Pricing Strategy (Default Fare vs. Peak Hour Surge Fare)");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        if ("1".equals(choice)) {
            System.out.println("1. NearestDriverStrategy");
            System.out.println("2. LeastActiveDriverStrategy");
            System.out.print("Selection: ");
            String sChoice = scanner.nextLine().trim();
            if ("2".equals(sChoice)) {
                currentMatchingStrategy = new LeastActiveDriverStrategy();
            } else {
                currentMatchingStrategy = new NearestDriverStrategy();
            }
            rideService.setMatchingStrategy(currentMatchingStrategy);
            System.out.println("✅ Matching strategy set to: " + currentMatchingStrategy.getClass().getSimpleName());
        } else if ("2".equals(choice)) {
            System.out.println("1. DefaultFareStrategy");
            System.out.println("2. PeakHourFareStrategy (1.5x Surge)");
            System.out.print("Selection: ");
            String sChoice = scanner.nextLine().trim();
            if ("2".equals(sChoice)) {
                currentFareStrategy = new PeakHourFareStrategy(1.5);
            } else {
                currentFareStrategy = new DefaultFareStrategy();
            }
            rideService.setFareStrategy(currentFareStrategy);
            System.out.println("✅ Pricing strategy set to: " + currentFareStrategy.getClass().getSimpleName());
        }
    }

    private static void seedInitialData() {
        // Sample Riders
        riderService.registerRider("R1", "Alice", new Location(0, 0));
        riderService.registerRider("R2", "Bob", new Location(10, 10));

        // Sample Drivers
        driverService.registerDriver("D1", "Charlie (Car)", new Location(2, 3), VehicleType.CAR);
        driverService.registerDriver("D2", "David (Bike)", new Location(1, 1), VehicleType.BIKE);
        driverService.registerDriver("D3", "Emma (Auto)", new Location(12, 11), VehicleType.AUTO);
    }
}
