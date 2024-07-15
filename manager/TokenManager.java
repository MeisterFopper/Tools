package manager;

/**
 * TokenManager is a utility class to manage access and refresh tokens with expiration logic.
 * It ensures the tokens are refreshed before they expire.
 */
public class TokenManager {

    /** 
     * The access token to be managed.
     */
    private String accessToken;

    /**
     * The refresh token to be managed.
     */
    private String refreshToken;

    /** 
     * The lead time in milliseconds before the token expiration, after which the token is considered inactive.
     */
    private long activeLeadTime;

    /**
     * Manager to handle refresh actions.
     */
    private RefreshManager refreshManager;

    /**
     * Constructor to initialize the TokenManager with tokens and validity period.
     *
     * @param accessToken The access token to be managed.
     * @param refreshToken The refresh token to be managed.
     * @param accessTokenValidityMillis The validity period of the access token in milliseconds.
     */
    public TokenManager(String accessToken, String refreshToken, long accessTokenValidityMillis) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.activeLeadTime = 60000; // 1 minute (60000 ms) before expiration
        this.refreshManager = new RefreshManager(accessTokenValidityMillis);
    }

    /**
     * Sets the access token.
     *
     * @param accessToken The new access token.
     */
    public synchronized void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sets the refresh token.
     *
     * @param refreshToken The new refresh token.
     */
    public synchronized void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Sets the expiration time based on the current time and validity period.
     *
     * @param accessTokenValidityMillis The validity period of the access token in milliseconds.
     */
    public synchronized void setExpirationTime(long accessTokenValidityMillis) {
        this.refreshManager.setExpirationTime(accessTokenValidityMillis);
    }

    /**
     * Sets the active lead time, the time before expiration to mark the token as inactive.
     *
     * @param activeLeadTime The lead time in milliseconds.
     */
    public synchronized void setActiveLeadTime(long activeLeadTime) {
        this.activeLeadTime = activeLeadTime;
    }

    /**
     * Gets the current access token.
     *
     * @return The current access token.
     */
    public synchronized String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets the current refresh token.
     *
     * @return The current refresh token.
     */
    public synchronized String getRefreshToken() {
        return this.refreshToken;
    }

    /**
     * Gets the active lead time.
     *
     * @return The active lead time in milliseconds.
     */
    public synchronized long getActiveLeadTime() {
        return this.activeLeadTime;
    }

    /**
     * Calculates the remaining time until the access token expires.
     *
     * @return The remaining time in milliseconds.
     */
    public synchronized long remainingTime() {
        return this.refreshManager.remainingTime();
    }

    /**
     * Checks if the token is active.
     * A token is considered active if the remaining time is greater than the active lead time.
     *
     * @return true if the token is active, false otherwise.
     */
    public synchronized boolean isRefreshRequired() {
        return (remainingTime() <= getActiveLeadTime());
    }
}
