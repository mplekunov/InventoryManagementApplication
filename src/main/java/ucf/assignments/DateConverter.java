package ucf.assignments;

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
