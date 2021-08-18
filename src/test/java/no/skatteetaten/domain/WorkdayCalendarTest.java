package no.skatteetaten.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        workdayCalendar.setWorkdayStartAndStop(
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0),
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0));

    }

    @Test
    void test_that_holidays_returns_correct_date_after_adding_recurring_holiday() {
        GregorianCalendar cal = new GregorianCalendar(2021, 11, 31);
        workdayCalendar.setRecurringHoliday(cal);
        assertTrue(workdayCalendar.getHolidays().contains(LocalDate.of(2021, 12, 31)));
    }

    @Test
    void test_that_holidays_returns_correct_date_after_adding_recurring_holiday_and_holiday() {
        GregorianCalendar newYearsEve = new GregorianCalendar(2021, 11, 31);
        workdayCalendar.setRecurringHoliday(newYearsEve);
        GregorianCalendar nationalDay = new GregorianCalendar(2021, 4, 17);
        workdayCalendar.setHoliday(nationalDay);
        assertTrue(workdayCalendar.getHolidays().contains(LocalDate.of(2021, 12, 31)));
        assertTrue(workdayCalendar.getHolidays().contains(LocalDate.of(2021, 5, 17)));
    }

    @Test
    void test_that_calculator_shows_correct_for_positive_number() {
        workdayCalendar.setRecurringHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

        workdayCalendar.setHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0));
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 19, 3).getTime();
        float increment = 44.723656f;

        assertEquals("24-05-2004 19:03 med tillegg av 44.723656 arbeidsdager er 27-07-2004 13:47",
            formatOutput(f, start, increment));
    }

    @Test
    void test_that_calculator_shows_correct_for_positive_number_2() {
        workdayCalendar.setRecurringHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

        workdayCalendar.setHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0));
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 8, 3).getTime();
        float increment = 12.782709f;

        assertEquals(
            "24-05-2004 08:03 med tillegg av 12.782709 arbeidsdager er 10-06-2004 14:18",
            formatOutput(f, start, increment));
    }

    @Test
    void test_that_calculator_shows_correct_for_positive_number_3() {
        workdayCalendar.setRecurringHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

        workdayCalendar.setHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0));
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 7, 3).getTime();
        float increment = 8.276628f;

        assertEquals("24-05-2004 07:03 med tillegg av 8.276628 arbeidsdager er 04-06-2004 10:12",
            formatOutput(f, start, increment));
    }

    @Test
    void test_that_calculator_shows_correct_for_negative_number_1() {
        workdayCalendar.setRecurringHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

        workdayCalendar.setHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0));
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime();
        float increment = -5.5f;

        assertEquals("24-05-2004 18:05 med tillegg av -5.5 arbeidsdager er 14-05-2004 12:00",
            formatOutput(f, start, increment));
    }

    @Test
    void test_that_calculator_shows_correct_for_negative_number_2() {
        workdayCalendar.setRecurringHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

        workdayCalendar.setHoliday(
            new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0));
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 3).getTime();
        float increment = -6.7470217f;

        assertEquals("24-05-2004 18:03 med tillegg av -6.7470217 arbeidsdager er 13-05-2004 10:02",
            formatOutput(f, start, increment));
    }

    @Test
    void test_get_number_of_business_days_from_date() {
        Integer antallBusinessdagerFraDato = workdayCalendar.numberOfBusinessDaysFromDate(
            new GregorianCalendar(2004, Calendar.MAY, 24, 19, 3).getTime(), 44);
        assertEquals(61, antallBusinessdagerFraDato);
    }

    @Test
    void test_that_wednesday_23_august_is_a_working_day() {
        assertTrue(workdayCalendar.isDayAWorkingDay(new GregorianCalendar(2021, Calendar.AUGUST, 23, 8, 0)));
    }

    @Test
    void test_that_saturday_21_august_is_not_a_working_day() {
        assertFalse(workdayCalendar.isDayAWorkingDay(new GregorianCalendar(2021, Calendar.AUGUST, 21, 8, 0)));
    }

    private String formatOutput(SimpleDateFormat f, Date start, Float increment) {
        return f.format(start) + " med tillegg av " +
            increment + " arbeidsdager er " + f.format(workdayCalendar.getWorkdayIncrement(start, increment));

    }
}