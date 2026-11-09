import java.text.DecimalFormat;
import java.util.ArrayList;

// Evaluates and prints the required queries over active bookings.
public class Query {

    // Formats monetary values to two decimal places.
    public static String formatMoney(double amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(amount);
    }

    // Q1: Finds the event with the highest number of active tickets (smallest event ID on tie).
    public static String getMostPopularEventQuery(BookingSystem system) {
        ArrayList<Event> events = system.getEvents();
        ArrayList<Booking> bookings = system.getBookings();

        if (events == null || events.size() == 0 || !hasAnyActiveBooking(bookings)) {
            return "Q1: No active bookings | 0 tickets";
        }

        Event bestEvent = events.get(0);
        int maxTickets = getActiveTicketsForEvent(bestEvent, bookings);

        for (Event event : events) {
            int activeTickets = getActiveTicketsForEvent(event, bookings);
            if (activeTickets > maxTickets) {
                bestEvent = event;
                maxTickets = activeTickets;
            } 
            
            else if (activeTickets == maxTickets && event.getEventId() < bestEvent.getEventId()) {
                bestEvent = event;
            }
        }

        return "Q1: " + bestEvent.getEventName() + " | " + maxTickets + " tickets";
    }

    // Q2: Finds the event with the highest total revenue from active bookings (smallest event ID on tie).
    public static String getHighestRevenueEventQuery(BookingSystem system) {
        ArrayList<Event> events = system.getEvents();
        ArrayList<Booking> bookings = system.getBookings();

        if (events == null || events.size() == 0 || !hasAnyActiveBooking(bookings)) {
            return "Q2: No active bookings | 0.00";
        }

        Event bestEvent = events.get(0);
        double maxRevenue = getActiveRevenueForEvent(bestEvent, bookings);

        for (Event event : events) {
            double revenue = getActiveRevenueForEvent(event, bookings);
            if (revenue - maxRevenue >= 0.001) {
                bestEvent = event;
                maxRevenue = revenue;
            }
            
            else if (Math.abs(revenue - maxRevenue) < 0.001 && event.getEventId() < bestEvent.getEventId()) {
                bestEvent = event;
            }
        }

        return "Q2: " + bestEvent.getEventName() + " | " + formatMoney(maxRevenue);
    }

    // Q3: Finds the customer holding the most active tickets across all events (smallest customer ID on tie).
    public static String getTopCustomerQuery(BookingSystem system) {
        ArrayList<Customer> customers = system.getCustomers();
        ArrayList<Booking> bookings = system.getBookings();

        if (customers == null || customers.size() == 0 || !hasAnyActiveBooking(bookings)) {
            return "Q3: No active bookings | NONE | 0 tickets";
        }

        Customer bestCustomer = customers.get(0);
        int maxTickets = getActiveTicketsForCustomer(bestCustomer, bookings);

        for (Customer customer : customers) {
            int activeTickets = getActiveTicketsForCustomer(customer, bookings);
            if (activeTickets > maxTickets) {
                bestCustomer = customer;
                maxTickets = activeTickets;
            } 
            
            else if (activeTickets == maxTickets && customer.getCustomerId() < bestCustomer.getCustomerId()) {
                bestCustomer = customer;
            }
        }

        String membershipType = "REGULAR";
        if (bestCustomer.isPremium()) {
            membershipType = "PREMIUM";
        }

        return "Q3: " + bestCustomer.getCustomerName() + " | " + membershipType + " | " + maxTickets + " tickets";
    }

    // Q4: Calculates active ticket counts for each event category in order.
    public static String getTicketsByCategoryQuery(BookingSystem system) {
        ArrayList<Booking> bookings = system.getBookings();

        int concertCount = getActiveTicketsForCategory(EventCategory.CONCERT, bookings);
        int conferenceCount = getActiveTicketsForCategory(EventCategory.CONFERENCE, bookings);
        int sportsCount = getActiveTicketsForCategory(EventCategory.SPORTS, bookings);

        return "Q4: CONCERT: " + concertCount
                + " | CONFERENCE: " + conferenceCount
                + " | SPORTS: " + sportsCount;
    }

    // Prints the results of all four queries.
    public static void printQueries(BookingSystem system) {
        System.out.println("========== QUERIES ==========");
        System.out.println(getMostPopularEventQuery(system));
        System.out.println(getHighestRevenueEventQuery(system));
        System.out.println(getTopCustomerQuery(system));
        System.out.println(getTicketsByCategoryQuery(system));
    }

    // Returns true if there is at least one active booking in the list.
    private static boolean hasAnyActiveBooking(ArrayList<Booking> bookings) {
        if (bookings == null) {
            return false;
        }

        for (Booking booking : bookings) {
            if (booking.isActive()) {
                return true;
            }
        }
        return false;
    }

    // Sums active ticket quantities for a specific event.
    private static int getActiveTicketsForEvent(Event event, ArrayList<Booking> bookings) {
        int total = 0;
        for (Booking booking : bookings) {
            if (booking.isActive() && booking.getEvent().getEventId() == event.getEventId()) {
                total += booking.getTicketQuantity();
            }
        }
        return total;
    }

    // Sums final paid amounts of active bookings for a specific event.
    private static double getActiveRevenueForEvent(Event event, ArrayList<Booking> bookings) {
        double revenue = 0.0;
        for (Booking booking : bookings) {
            if (booking.isActive() && booking.getEvent().getEventId() == event.getEventId()) {
                revenue += booking.getFinalPaidAmount();
            }
        }
        return revenue;
    }

    // Sums active ticket quantities held by a specific customer across all events.
    private static int getActiveTicketsForCustomer(Customer customer, ArrayList<Booking> bookings) {
        int total = 0;
        for (Booking booking : bookings) {
            if (booking.isActive() && booking.getCustomer().getCustomerId() == customer.getCustomerId()) {
                total += booking.getTicketQuantity();
            }
        }
        return total;
    }

    // Sums active ticket quantities for a given event category.
    private static int getActiveTicketsForCategory(EventCategory category, ArrayList<Booking> bookings) {
        int total = 0;
        if (bookings == null) {
            return 0;
        }

        for (Booking booking : bookings) {
            if (booking.isActive() && booking.getEvent().getCategory() == category) {
                total += booking.getTicketQuantity();
            }
        }
        return total;
    }
}
