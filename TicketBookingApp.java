import java.util.ArrayList;
import java.util.Locale;

// Main application class that executes Phase 1 and Phase 2 operations in order.
public class TicketBookingApp {

    private static final int GROUP_NUMBER = 16;
    private static final String EVENTS_CSV_PATH = "events.csv";
    private static final String CUSTOMERS_CSV_PATH = "customers.csv";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        // Phase 1: Basic System with group policy disabled
        BookingSystem phase1System = createInitializedSystem(GROUP_NUMBER);
        phase1System.disableGroupPolicy();

        // 1. System Information
        printSystemInformation(phase1System);
        System.out.println();

        // 2. Common Operations (B1-B6, C1-C2)
        runCommonOperations(phase1System);
        System.out.println();

        // 3. Queries (Q1-Q4)
        Query.printQueries(phase1System);
        System.out.println();

        // Phase 2: Fresh system instance with group policy enabled
        BookingSystem phase2System = createInitializedSystem(GROUP_NUMBER);
        phase2System.enableGroupPolicy();

        // 4. Group-Specific Rule Demonstration (R1-R4)
        runGroupSpecificRule(phase2System);
    }

    // Creates a new BookingSystem instance and loads fresh records from the CSV files.
    private static BookingSystem createInitializedSystem(int groupNumber) {
        ArrayList<Event> events = FileIO.loadEvents(EVENTS_CSV_PATH);
        ArrayList<Customer> customers = FileIO.loadCustomers(CUSTOMERS_CSV_PATH);

        BookingSystem system = new BookingSystem(groupNumber);
        for (Event event : events) {
            system.addEvent(event);
        }
        for (Customer customer : customers) {
            system.addCustomer(customer);
        }
        return system;
    }

    // Prints the total counts of loaded events, customers, and premium customers.
    private static void printSystemInformation(BookingSystem system) {
        int eventCount = system.getEvents().size();
        int customerCount = system.getCustomers().size();
        int premiumCustomerCount = 0;

        for (Customer customer : system.getCustomers()) {
            if (customer.isPremium()) {
                premiumCustomerCount++;
            }
        }

        System.out.println("========== SYSTEM INFORMATION ==========");
        System.out.println("Events: " + eventCount);
        System.out.println("Customers: " + customerCount);
        System.out.println("Premium Customers: " + premiumCustomerCount);
    }

    // Executes common booking (B1-B6) and cancellation (C1-C2) operations.
    private static void runCommonOperations(BookingSystem system) {
        System.out.println("========== COMMON OPERATIONS ==========");

        BookingResult b1Result = system.bookTickets(201, 101, 1);
        printBookingOperation("B1", b1Result);

        BookingResult b2Result = system.bookTickets(202, 103, 1);
        printBookingOperation("B2", b2Result);

        BookingResult b3Result = system.bookTickets(205, 104, 6);
        printBookingOperation("B3", b3Result);

        BookingResult b4Result = system.bookTickets(203, 101, 1);
        printBookingOperation("B4", b4Result);

        BookingResult b5Result = system.bookTickets(201, 102, 0);
        printBookingOperation("B5", b5Result);

        BookingResult b6Result = system.bookTickets(205, 102, 2);
        printBookingOperation("B6", b6Result);

        int b1BookingId = -1;
        if (b1Result.getBooking() != null) {
            b1BookingId = b1Result.getBooking().getBookingId();
        }

        BookingResult c1Result = system.cancelBooking(b1BookingId);
        printCancellationOperation("C1", c1Result);

        BookingResult c2Result = system.cancelBooking(b1BookingId);
        printCancellationOperation("C2", c2Result);
    }

    // Executes the BOOKING_LIMIT rule scenario (R1-R4).
    private static void runGroupSpecificRule(BookingSystem system) {
        int maxAllowedActiveTickets = 2 + (system.getGroupNumber() % 3);

        System.out.println("========== GROUP-SPECIFIC RULE ==========");
        System.out.println("Group: " + system.getGroupNumber());
        System.out.println("Rule: " + system.getActivePolicy());
        System.out.println("Parameter: " + maxAllowedActiveTickets);

        // R1: Book maximum allowed active tickets
        BookingResult r1Result = system.bookTickets(201, 105, maxAllowedActiveTickets);
        printBookingOperation("R1", r1Result);

        // R2: Attempt to book one additional ticket beyond the limit
        BookingResult r2Result = system.bookTickets(201, 105, 1);
        printBookingOperation("R2", r2Result);

        // R3: Cancel the booking created by R1
        int r1BookingId = -1;
        if (r1Result.getBooking() != null) {
            r1BookingId = r1Result.getBooking().getBookingId();
        }
        BookingResult r3Result = system.cancelBooking(r1BookingId);
        printCancellationOperation("R3", r3Result);

        // R4: Book one ticket again after cancellation
        BookingResult r4Result = system.bookTickets(201, 105, 1);
        printBookingOperation("R4", r4Result);
    }

    // Prints the formatted result of a booking operation.
    private static void printBookingOperation(String operationName, BookingResult result) {
        if (result.isSuccessful()) {
            System.out.println(operationName + ": SUCCESS | Paid: " + Query.formatMoney(result.getAmount()));
        } 
        
        else {
            System.out.println(operationName + ": FAILED | " + result.getMessage());
        }
    }

    // Prints the formatted result of a cancellation operation.
    private static void printCancellationOperation(String operationName, BookingResult result) {
        if (result.isSuccessful()) {
            System.out.println(operationName + ": SUCCESS | Refund: " + Query.formatMoney(result.getAmount()));
        } 
        
        else {
            System.out.println(operationName + ": FAILED | " + result.getMessage());
        }
    }
}
