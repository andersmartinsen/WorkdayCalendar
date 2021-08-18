package no.skatteetaten.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkdayCalendarTest {
    private WorkdayCalendar workdayCalendar;

    @BeforeEach
    public void oppsett() {
        workdayCalendar = new WorkdayCalendar();
    }

    @Test
    void test_that_holidays_returns_correct_number() {
        GregorianCalendar cal = new GregorianCalendar(2021, 11, 31);
        workdayCalendar.setRecurringHoliday(cal);
        assertTrue(workdayCalendar.getHolidays().contains(LocalDate.of(2021, 12, 31)));
    }

    @Test
    public void test_that_calculator_shows_correct_() {
        workdayCalendar.setWorkdayStartAndStop(
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0),
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0));

        workdayCalendar.setRecurringHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

        workdayCalendar.setHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0));
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime();
        float increment = -5.5f;
        System.out.println(
            f.format(start) + " med tillegg av " +
                increment + " arbeidsdager er " + f.format(workdayCalendar.getWorkdayIncrement(start, increment)));

        assertEquals(workdayCalendar.getWorkdayIncrement(start, increment),
            formatOutput(f, start, increment));
    }

    private String formatOutput(SimpleDateFormat f, Date start, Float increment) {
        return f.format(start) + " med tillegg av " +
            increment + " arbeidsdager er " + f.format(workdayCalendar.getWorkdayIncrement(start, increment));

    }

}