package be.xplore.conference.parsing.converter.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class MillisConverter {
    public static LocalDate toDate(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
