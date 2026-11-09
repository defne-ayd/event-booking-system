import java.util.ArrayList;

public class BookingSystem {
    private ArrayList<Event> events;
    private ArrayList<Customer> customers;
    private ArrayList<Booking> bookings;

    private int groupNumber;
    private BookingPolicy activePolicy;
    private boolean policyEnabled;

    private int nextBookingId;

    public BookingSystem(int groupNumber) {
        this.events = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.groupNumber = groupNumber;
        this.policyEnabled = false;
        this.nextBookingId = 1;

        // Group 16 -> Rule 1: BOOKING_LIMIT
        int ruleNumber = ((groupNumber - 1) % 5) + 1;
        if (ruleNumber == 1) {
            this.activePolicy = BookingPolicy.BOOKING_LIMIT;
        } else if (ruleNumber == 2) {
            this.activePolicy = BookingPolicy.GROUP_DISCOUNT;
        } else if (ruleNumber == 3) {
            this.activePolicy = BookingPolicy.CATEGORY_RESTRICTION;
        } else if (ruleNumber == 4) {
            this.activePolicy = BookingPolicy.CANCELLATION_FEE;
        } else if (ruleNumber == 5) {
            this.activePolicy = BookingPolicy.RESERVED_CAPACITY;
        }
    }

    public void addCustomer(Customer customer) {
        if (customer != null) {
            this.customers.add(customer);
        }
    }

    public void addEvent(Event event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public Customer findCustomerById(int customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId() == customerId) {
                return c;
            }
        }
        return null;
    }

    public Event findEventById(int eventId) {
        for (Event e : events) {
            if (e.getEventId() == eventId) {
                return e;
            }
        }
        return null;
    }

    public Booking findBookingById(int bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) {
                return b;
            }
        }
        return null;
    }

    public int getActiveTickets(Customer customer, Event event) {
        if (customer == null || event == null) return 0;
        int count = 0;
        for (Booking b : bookings) {
            if (b.isActive() && 
                b.getCustomer().getCustomerId() == customer.getCustomerId() && 
                b.getEvent().getEventId() == event.getEventId()) {
                count += b.getTicketQuantity();
            }
        }
        return count;
    }

    public int getAvailableSeats(Event event) {
        if (event == null) return 0;
        int activeTickets = 0;
        for (Booking b : bookings) {
            if (b.isActive() && b.getEvent().getEventId() == event.getEventId()) {
                activeTickets += b.getTicketQuantity();
            }
        }
        return event.getCapacity() - activeTickets;
    }

    public BookingResult bookTickets(int customerId, int eventId, int quantity) {
        if (quantity <= 0) {
            return BookingResult.failure("Invalid Quantity");
        }

        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            return BookingResult.failure("Customer Not Found");
        }

        Event event = findEventById(eventId);
        if (event == null) {
            return BookingResult.failure("Event Not Found");
        }

        if (getAvailableSeats(event) < quantity) {
            return BookingResult.failure("Insufficient Capacity");
        }

        if (policyEnabled && activePolicy == BookingPolicy.BOOKING_LIMIT) {
            int maxTicketsAllowed = 2 + (groupNumber % 3);
            int currentActiveTickets = getActiveTickets(customer, event);
            if (currentActiveTickets + quantity > maxTicketsAllowed) {
                return BookingResult.failure("Booking Limit Exceeded");
            }
        }

        double originalTotalPrice = event.getTicketPrice() * quantity;
        double finalPrice = originalTotalPrice;

        if (customer.isPremium()) {
            finalPrice = finalPrice * 0.95;
        }

        if (customer.getBalance() < finalPrice) {
            return BookingResult.failure("Insufficient Balance");
        }

        customer.deductBalance(finalPrice);

        Booking booking = new Booking(
            nextBookingId++,
            customer,
            event,
            quantity,
            originalTotalPrice,
            finalPrice
        );

        bookings.add(booking);

        return BookingResult.success("SUCCESS", booking, finalPrice);
    }

    public BookingResult cancelBooking(int bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            return BookingResult.failure("Booking Not Found");
        }

        if (!booking.isActive()) {
            return BookingResult.failure("Already Cancelled");
        }

        double refundAmount = booking.getFinalPaidAmount();

        booking.cancelBooking();
        booking.getCustomer().addBalance(refundAmount);

        return BookingResult.success("SUCCESS", refundAmount);
    }

    public void enableGroupPolicy() {
        this.policyEnabled = true;
    }

    public void disableGroupPolicy() {
        this.policyEnabled = false;
    }

    public ArrayList<Event> getEvents() { return events; }
    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<Booking> getBookings() { return bookings; }
    public BookingPolicy getActivePolicy() { return activePolicy; }
    public int getGroupNumber() { return groupNumber; }
}