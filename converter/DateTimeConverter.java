package converter;

public class DateTimeConverter {

    /**
     * Converts milliseconds to seconds.
     *
     * @param millis the time in milliseconds
     * @return the time in seconds
     */
    public static long convertMillisToSeconds(long millis) {
        return millis / 1000;
    }
}
