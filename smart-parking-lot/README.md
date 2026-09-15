# Smart Parking Lot System - Low Level Design (Backend Java)

## Overview
This repository contains the production-grade, object-oriented Low-Level Design (LLD) implementation for a **Smart Parking Lot System** written in Java. The system manages multi-floor vehicle entry/exit, automated spot allocation based on vehicle size and proximity, real-time parking space availability tracking via the Observer pattern, extensible fee calculation strategies, and thread-safe concurrent operations.

---

## Key Features & Requirements Addressed

1. **Multi-Floor & Multi-Spot Architecture**:
   - Manages multiple floors with varying spot sizes (`MOTORCYCLE`, `COMPACT`, `LARGE`).
   - Supports vehicle size compatibility matching (e.g., Motorcycle fits in Motorcycle/Compact/Large spots; Cars fit in Compact/Large spots; Buses/Trucks fit in Large spots).

2. **Automated Parking Spot Allocation Strategies (Strategy Pattern)**:
   - `NearestFirstAllocationStrategy`: Assigns available spots closest to entry (lowest floor & lowest spot ID).
   - `FirstAvailableAllocationStrategy`: Assigns the first available spot found across floors.

3. **Check-In & Check-Out Management**:
   - `EntryGate`: Validates vehicle, allocates spot atomically, issues `ParkingTicket`, and triggers real-time board updates.
   - `ExitGate`: Validates ticket, computes parking fee, processes payment, frees the spot, and updates availability.

4. **Extensible Parking Fee Calculation (Strategy Pattern)**:
   - `HourlyFeeCalculationStrategy`: Calculates fee based on duration and vehicle-type hourly rates.
   - `TieredFeeCalculationStrategy`: Tiered pricing with base rate for the first hour and discounted rates for longer stays.

5. **Real-Time Display Board (Observer Pattern)**:
   - Live updates of available spots per floor and vehicle type upon vehicle check-in and check-out.

6. **Concurrency & Thread Safety**:
   - Synchronized spot reservation methods and concurrent data structures (`ConcurrentHashMap`, `CopyOnWriteArrayList`) prevent double-booking race conditions during simultaneous entry/exit requests across multiple threads.

---

## Project Structure

```text
smart-parking-lot/
├── src/
│   └── com/
│       └── smartparking/
│           ├── Main.java                        # System demo & multi-threaded concurrency simulation
│           ├── display/
│           │   ├── DisplayBoard.java            # Real-time parking availability board
│           │   └── DisplayBoardObserver.java    # Observer pattern interface for display updates
│           ├── exception/
│           │   ├── InvalidTicketException.java
│           │   ├── NoAvailableSpotException.java
│           │   ├── PaymentFailedException.java
│           │   └── SpotAlreadyOccupiedException.java
│           ├── gate/
│           │   ├── EntryGate.java               # Vehicle check-in & ticket issuing gate
│           │   └── ExitGate.java                # Vehicle check-out & payment gate
│           ├── model/
│           │   ├── Bus.java
│           │   ├── Car.java
│           │   ├── Motorcycle.java
│           │   ├── ParkingFloor.java            # Floor holding parking spots
│           │   ├── ParkingLot.java              # Central Singleton entity
│           │   ├── ParkingSpot.java             # Thread-safe individual parking spot
│           │   ├── ParkingSpotType.java         # Spot size enums & compatibility logic
│           │   ├── ParkingTicket.java           # Ticket details & stay tracking
│           │   ├── Payment.java                 # Payment record
│           │   ├── PaymentMode.java             # Payment options (UPI, CASH, CARD, FASTAG)
│           │   ├── PaymentStatus.java
│           │   ├── TicketStatus.java            # Ticket status lifecycle (ACTIVE, PAID, COMPLETED)
│           │   ├── Vehicle.java                 # Abstract vehicle model
│           │   └── VehicleType.java             # Vehicle size enums (MOTORCYCLE, CAR, BUS)
│           ├── service/
│           │   └── ParkingLotService.java       # Facade orchestration service
│           └── strategy/
│               ├── allocation/
│               │   ├── FirstAvailableAllocationStrategy.java
│               │   ├── NearestFirstAllocationStrategy.java
│               │   └── SpotAllocationStrategy.java
│               └── fee/
│                   ├── FeeCalculationStrategy.java
│                   ├── HourlyFeeCalculationStrategy.java
│                   └── TieredFeeCalculationStrategy.java
└── README.md
```

---

## How to Compile & Run

### Prerequisites
- JDK 8 or higher
- Terminal / PowerShell

### Compilation Command
From inside the `smart-parking-lot` directory:

```bash
# Windows PowerShell
powershell -Command "if (-not (Test-Path bin)) { New-Item -ItemType Directory -Path bin }; javac -d bin -sourcepath src (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)"
```

### Execution Command
```bash
java -cp bin com.smartparking.Main
```

---

## How to Commit & Submit Project to Airtribe

Follow these steps to submit your project to Airtribe:

### Step 1: Check Git Status & Add Files
Navigate to the root repository folder (`Java Track Assignment`) in terminal:

```bash
git status
```

Stage the newly created `smart-parking-lot` project files:

```bash
git add smart-parking-lot/
```

### Step 2: Commit Your Changes
Commit your project with a clean message:

```bash
git commit -m "feat: Implement Smart Parking Lot System LLD Java assignment"
```

### Step 3: Push to GitHub
Push your branch `feature/smart-parking-lot-system` to your remote repository on GitHub:

```bash
git push -u origin feature/smart-parking-lot-system
```

*(Alternatively, if you wish to merge into `main` branch before submitting)*:
```bash
git checkout main
git merge feature/smart-parking-lot-system
git push origin main
```

### Step 4: Submit on Airtribe
1. Open the [Airtribe Assignment Submission Page](https://www.airtribe.live/dashboard/backend-java/courses/R81P30VAIW0L/assignments/C6Z64YI56OCD?src=projects#).
2. Copy and paste your GitHub repository link or specific branch link:
   `https://github.com/pranavarya5/Java-Airtribe-Assignement-/tree/feature/smart-parking-lot-system` (or `main`).
3. Submit the assignment!
