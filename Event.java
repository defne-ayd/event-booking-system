//Represents an event within the booking system.
public class Event {
    private final int eventId;
    private final String eventName;
    private final EventCategory category;
    private final int capacity;
    private final double ticketPrice;

    //Initializes a new Event.
    public Event(int eventId, String eventName, EventCategory category, int capacity, double ticketPrice) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.category = category;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public EventCategory getCategory() {
        return category;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", category=" + category +
                ", capacity=" + capacity +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}