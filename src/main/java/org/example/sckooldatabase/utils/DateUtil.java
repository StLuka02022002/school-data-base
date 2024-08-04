package org.example.sckooldatabase.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateUtil {
    private static final List<DateTimeFormatter> dateFormats = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("ddMMyyyy")
    );

    private static final List<DateTimeFormatter> timeFormats = List.of(
            DateTimeFormatter.ISO_LOCAL_TIME,
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("HH.mm")
    );

    public static LocalDate parseDate(String dateString) {
        for (DateTimeFormatter formatter : dateFormats) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Игнорируем и пробуем следующий формат
            }
        }
        throw new DateTimeParseException("Неверный формат даты: " + dateString, dateString, 0);
    }

    public static LocalTime parseTime(String timeString) {
        for (DateTimeFormatter formatter : timeFormats) {
            try {
                return LocalTime.parse(timeString, formatter);
            } catch (DateTimeParseException e) {
                // Игнорируем и пробуем следующий формат
            }
        }
        throw new DateTimeParseException("Неверный формат времени: " + timeString, timeString, 0);
    }

}
