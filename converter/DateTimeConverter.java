package converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

     /**
     * Format date and time.
     *
     * @param dateTime The date time in yyyyMMddHHmmss
     * @return Formatted date and time yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX
     */   
    public static String abasTimestampToTaktiq(String dateTime) {
        String formattedDate = null;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        java.time.ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX");
        formattedDate = zonedDateTime.format(outputFormatter);
        
        return formattedDate;
    }
}
