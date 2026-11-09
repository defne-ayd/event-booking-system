/**
 * Represents a premium tier customer. 
 * Inherits standard customer attributes and behaviors.
 */
public class PremiumCustomer extends Customer {
    
    // Defines the fixed premium discount rate (5%).
    private static final double PREMIUM_DISCOUNT_RATE = 0.05; 

    public PremiumCustomer(int customerId, String customerName, double balance) {
        super(customerId, customerName, balance);
    }

    //Returns the discount rate applicable to this premium customer as a decimal.
    public double getPremiumDiscountRate() {
        return PREMIUM_DISCOUNT_RATE;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}