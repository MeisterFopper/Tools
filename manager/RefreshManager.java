package manager;

/**
 * RefreshManager is a utility class to manage refresh actions.
 */
public class RefreshManager {

    /**
     * The time in milliseconds when the next refresh action will be required.
     */
    private long expirationTimeMillis;

    /**
     * Constructor to initialize the RefreshManager with a specified refresh interval.
     *
     * @param refreshTimeMillis The time in milliseconds until the next refresh action is required.
     */
    public RefreshManager(long refreshTimeMillis) {
        setExpirationTime(refreshTimeMillis);
    }

    /**
     * Sets the expiration time based on the current time and the provided refresh interval.
     *
     * @param refreshTimeMillis The time in milliseconds until the next refresh action is required.
     */
    public synchronized void setExpirationTime(long refreshTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        this.expirationTimeMillis = currentTimeMillis + refreshTimeMillis;
    }

    /**
     * Calculates the remaining time until the next refresh action is required.
     *
     * @return The remaining time in milliseconds.
     */
    public synchronized long remainingTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return (this.expirationTimeMillis - currentTimeMillis);
    }

    /**
     * Checks if a refresh action is currently required.
     * A refresh action is considered required if the remaining time is less than or equal to zero.
     *
     * @return true if a refresh action is required, false otherwise.
     */
    public synchronized boolean isRefreshRequired() {
        return (remainingTime() <= 0);
    }
}
