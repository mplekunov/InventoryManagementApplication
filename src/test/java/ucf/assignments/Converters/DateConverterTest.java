/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.Converters;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateConverterTest {

    @Test
    void testToDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateAsString = dtf.format(date);
        LocalDate actual = DateConverter.toDate(dateAsString);
        assertEquals(date, actual);
    }

    @Test
    void testToString() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String expected = dtf.format(date);
        String actual = DateConverter.toString(date);

        assertEquals(expected, actual);
    }
}