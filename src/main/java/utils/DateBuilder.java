package utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateBuilder {
    public static String currentTime() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTime.format(formatter);
    }
}
