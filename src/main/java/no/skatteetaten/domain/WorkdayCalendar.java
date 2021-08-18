package no.skatteetaten.domain;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkdayCalendar {

    private final Set<LocalDate> holidays = new HashSet<>();

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays) {
        LocalDate startDato = LocalDate.of(2012, 3, 7);
        LocalDate endDate = LocalDate.of(2012, 6, 7);

// I've hardcoded the holidays as LocalDates
// and put them in a Set
// For the sake of efficiency, I also put the business days into a Set.
// In general, a Set has a better lookup speed than a List.
        final Set<DayOfWeek> businessDays = Set.of(
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
        );

        List<LocalDate> allDates =

            // Java 9 provides a method to return a stream with dates from the
            // startdate to the given end date. Note that the end date itself is
            // NOT included.
            startDato.datesUntil(endDate)

                // Retain all business days. Use static imports from
                // java.time.DayOfWeek.*
                .filter(t -> businessDays.contains(t.getDayOfWeek()))

                // Retain only dates not present in our holidays list
                .filter(t -> !holidays.contains(t))

                // Collect them into a List. If you only need to know the number of
                // dates, you can also use .count()
                .collect(Collectors.toList());

        return new Date();
    }

    public void setWorkdayStartAndStop(Calendar Start, Calendar stop) {

    }

    public void setRecurringHoliday(GregorianCalendar gregorianCalendar) {
        holidays.add(gregorianCalendar.toZonedDateTime().toLocalDate());
    }

    public void setHoliday(GregorianCalendar gregorianCalendar) {
        holidays.add(gregorianCalendar.toZonedDateTime().toLocalDate());

    }

    public Set<LocalDate> getHolidays() {
        return holidays;
    }
}
