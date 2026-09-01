# RideWise — Modular Ride-Sharing System

A clean, extensible, modular ride-sharing system built in Java adhering to Low-Level Design (LLD) standards, SOLID principles, and Strategy Design Pattern.

---

## 🎯 Features
- **Rider Management**: Register riders with coordinate-based locations.
- **Driver Management**: Register drivers with locations, vehicle types (BIKE, AUTO, CAR), availability, and tracking of completed rides.
- **Pluggable Ride Matching Strategies**:
  - `NearestDriverStrategy`: Uses Euclidean distance to assign the closest available driver to the rider.
  - `LeastActiveDriverStrategy`: Selects the available driver with the fewest completed rides to balance workload.
- **Pluggable Pricing Strategies**:
  - `DefaultFareStrategy`: Base fare + (distance × per-km rate) based on `VehicleType`.
  - `PeakHourFareStrategy`: Incorporates surge pricing multipliers (e.g. 1.5x multiplier).
- **Interactive CLI Menu**: Full console menu with validation, error handling, and runtime strategy switching.

---

## 🏛️ SOLID Principles & Design Patterns Implementation

1. **Single Responsibility Principle (SRP)**:
   - `RiderService`: Manages registration and query of riders only.
   - `DriverService`: Manages driver state and availability only.
   - `RideService`: Coordinates ride lifecycle, delegating matching and pricing to strategy components.
2. **Open-Closed Principle (OCP)**:
   - New driver matching or fare calculation strategies can be added by implementing `RideMatchingStrategy` or `FareStrategy` without altering existing service logic.
3. **Liskov Substitution Principle (LSP)**:
   - Strategy implementations (`NearestDriverStrategy`, `LeastActiveDriverStrategy`, `DefaultFareStrategy`, `PeakHourFareStrategy`) are fully interchangeable.
4. **Interface Segregation Principle (ISP)**:
   - Small, highly focused interfaces: `RideMatchingStrategy` and `FareStrategy`.
5. **Dependency Inversion Principle (DIP)**:
   - `RideService` depends on high-level abstractions (`RideMatchingStrategy`, `FareStrategy`), not concrete strategy classes.
6. **Strategy Pattern & Composition over Inheritance**:
   - Algorithms are encapsulated into separate strategy classes and injected into `RideService`.

---

## 🚀 How to Compile & Run

### Prerequisites
- JDK 8 or higher installed on your path.

### Build & Compilation
From the `ridewise` directory:

```bash
# Create build bin folder and compile
mkdir -p bin
javac -d bin src/com/ridewise/model/*.java src/com/ridewise/strategy/*.java src/com/ridewise/service/*.java src/com/ridewise/Main.java
```

### Execution
```bash
# Run the interactive console application
java -cp bin com.ridewise.Main
```
