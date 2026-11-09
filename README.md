# Event Booking System

An Object-Oriented Java application that simulates a robust event reservation and ticketing system. It manages customers, events, and bookings while enforcing dynamic group policies and financial constraints.

## Key Features
* **Transactional Integrity:** Safely handles financial transactions, ensuring customer balances are validated before deductions and properly refunded upon cancellations.
* **Dynamic Policy Enforcement:** Implements an algorithmic policy engine that applies dynamic rules (e.g., `BOOKING_LIMIT`, `GROUP_DISCOUNT`, `CATEGORY_RESTRICTION`) based on configurable group identifiers.
* **State Management:** Uses strict `Enum` definitions to track the lifecycle of bookings (`ACTIVE`, `CANCELLED`) and categorize events.
* **Result Objects:** Utilizes a `BookingResult` pattern instead of raw exceptions to return detailed success/failure states and transaction messages, ensuring clean control flow in the main application.

## Technologies
* Java (OOP, Encapsulation, State Management, Policy Engines)
