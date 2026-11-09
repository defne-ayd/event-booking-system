/**
 * Represents the lifecycle state of a booking transaction.
 * Restricted strictly to ACTIVE and CANCELLED to maintain system integrity 
 * and prevent unsupported states.
 */
public enum BookingStatus {
    ACTIVE, CANCELLED
}