package de.seifi.rechnung_common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneralUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    public static String formatDate(LocalDate ldt){
        String date = dateFormatter.format(ldt);
        return date;
    }

    public static String formatDate(LocalDateTime ldt){
        String date = dateFormatter.format(ldt);
        return date;
    }
}
