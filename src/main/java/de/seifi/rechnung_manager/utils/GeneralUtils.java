package de.seifi.rechnung_manager.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneralUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String formatDate(LocalDate ldt){
        String date = dateFormatter.format(ldt);
        return date;
    }

    public static String formatDate(LocalDateTime ldt){
        String date = dateFormatter.format(ldt);
        return date;
    }

    public static String formatTime(LocalDateTime ldt){
        String time = timeFormatter.format(ldt);
        return time;
    }
}
