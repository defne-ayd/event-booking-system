/**
 * Establishes a relationship between a Customer and an Event,
 * while preserving the record of the financial transaction.
 */
public class Booking {
    private final int bookingId;
    private final Customer customer;
    private final Event event;
    private final int ticketQuantity;
    private final double originalTotalPrice;
    private final double finalPaidAmount;
    private BookingStatus status;

    //Initializes a Booking entity.
    public Booking(int bookingId, Customer customer, Event event, int ticketQuantity, double originalTotalPrice, double finalPaidAmount) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.event = event;
        this.ticketQuantity = ticketQuantity;
        this.originalTotalPrice = originalTotalPrice;
        this.finalPaidAmount = finalPaidAmount;
        
        // A newly created booking must inherently start in an ACTIVE state.
        this.status = BookingStatus.ACTIVE; 
    }

    public int getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Event getEvent() {
        return event;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public double getOriginalTotalPrice() {
        return originalTotalPrice;
    }

    public double getFinalPaidAmount() {
        return finalPaidAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    //Transitions the booking state from ACTIVE to CANCELLED.
    public boolean cancelBooking() {
        if (this.status == BookingStatus.ACTIVE) {
            this.status = BookingStatus.CANCELLED;
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return status == BookingStatus.ACTIVE;
    }
}