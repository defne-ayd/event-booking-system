/**
 * Represents a standard customer in the system. 
 * The balance can only be changed through deductBalance and addBalance.
 */
public class Customer {
    private final int customerId;
    private final String customerName;
    private double balance;

    //Constructs a Customer object.
    public Customer(int customerId, String customerName, double balance) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.balance = balance;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Returns false and leaves the balance unchanged
     * if the amount is negative or exceeds the balance.
     */
    public boolean deductBalance(double amount) {
        if (amount < 0 || this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    //Returns false and leaves the balance unchanged if the amount is negative.
    public boolean addBalance(double amount) {
        if (amount < 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    public boolean isPremium() {
        return false;
    }
}