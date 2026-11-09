public class BookingResult {
    private final boolean successful;
    private final String message;
    private final Booking booking;
    private final double amount;

    public BookingResult(boolean successful, String message, Booking booking, double amount) {
        this.successful = successful;
        this.message = message;
        this.booking = booking;
        this.amount = amount;
    }

    public static BookingResult failure(String message) {
        return new BookingResult(false, message, null, 0.0);
    }

    public static BookingResult success(String message, Booking booking, double amount) {
        return new BookingResult(true, message, booking, amount);
    }

    public static BookingResult success(String message, double amount) {
        return new BookingResult(true, message, null, amount);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public Booking getBooking() {
        return booking;
    }

    public double getAmount() {
        return amount;
    }
}