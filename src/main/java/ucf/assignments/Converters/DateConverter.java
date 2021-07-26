/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.Converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateConverter{
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateConverter() {}

    public static LocalDate toDate(String date) {
        return !date.isEmpty() ? LocalDate.parse(date, dtf) : null;
    }

    public static String toString(LocalDate date) {
        return date != null ? dtf.format(date) : "";
    }
}
