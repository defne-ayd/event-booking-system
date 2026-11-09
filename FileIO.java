import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

// Reads event and customer records from CSV files.
public class FileIO {

    // Reads events from the given CSV file path and returns a list of Event objects.
    public static ArrayList<Event> loadEvents(String path) {
        ArrayList<Event> events = new ArrayList<Event>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (trimmedLine.length() == 0) {
                    continue;
                }

                StringTokenizer tokenizer = new StringTokenizer(trimmedLine, ",");
                if (!tokenizer.hasMoreTokens()) {
                    continue;
                }

                int eventId = Integer.parseInt(tokenizer.nextToken().trim());
                String eventName = tokenizer.nextToken().trim();
                String categoryStr = tokenizer.nextToken().trim().toUpperCase();
                int capacity = Integer.parseInt(tokenizer.nextToken().trim());
                double ticketPrice = Double.parseDouble(tokenizer.nextToken().trim());

                EventCategory category = null;

                if (categoryStr.equals("CONCERT")) {
                    category = EventCategory.CONCERT;
                } 
                
                else if (categoryStr.equals("CONFERENCE")) {
                    category = EventCategory.CONFERENCE;
                } 
                
                else if (categoryStr.equals("SPORTS")) {
                    category = EventCategory.SPORTS;
                }

                // Add event if all fields are valid and ID is unique
                if (category != null && capacity > 0 && ticketPrice >= 0 && !containsEventId(events, eventId)) {
                    events.add(new Event(eventId, eventName, category, capacity, ticketPrice));
                }
            }

            reader.close();
        } 
        
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
        } 
        
        catch (IOException e) {
            System.out.println("Error reading file: " + path);
        }

        return events;
    }

    // Reads customers from the given CSV file path and returns a list of Customer objects.
    public static ArrayList<Customer> loadCustomers(String path) {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (trimmedLine.length() == 0) {
                    continue;
                }

                StringTokenizer tokenizer = new StringTokenizer(trimmedLine, ",");
                if (!tokenizer.hasMoreTokens()) {
                    continue;
                }

                int customerId = Integer.parseInt(tokenizer.nextToken().trim());
                String customerName = tokenizer.nextToken().trim();
                double balance = Double.parseDouble(tokenizer.nextToken().trim());
                String membershipType = tokenizer.nextToken().trim().toUpperCase();

                // Create Customer or PremiumCustomer based on membership type
                if (balance >= 0 && !containsCustomerId(customers, customerId)) {
                    if (membershipType.equals("REGULAR")) {
                        customers.add(new Customer(customerId, customerName, balance));
                    } 
                    
                    else if (membershipType.equals("PREMIUM")) {
                        customers.add(new PremiumCustomer(customerId, customerName, balance));
                    }
                }
            }

            reader.close();
        } 
        
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
        } 
        
        catch (IOException e) {
            System.out.println("Error reading file: " + path);
        }

        return customers;
    }

    // Checks if an event with the given ID already exists in the list.
    private static boolean containsEventId(ArrayList<Event> events, int eventId) {
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                return true;
            }
        }
        return false;
    }

    // Checks if a customer with the given ID already exists in the list.
    private static boolean containsCustomerId(ArrayList<Customer> customers, int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return true;
            }
        }
        return false;
    }
}
