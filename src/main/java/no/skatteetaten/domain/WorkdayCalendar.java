package no.skatteetaten.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class WorkdayCalendar {

    private final Set<LocalDate> holidays = new HashSet<>();
    private Calendar start;
    private Calendar stop;

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays) {
        LocalDateTime startDato = Instant.ofEpochMilli(startDate.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay().plus(start.get(Calendar.HOUR), ChronoUnit.HOURS);

        int days = (int) incrementInWorkdays;
        float remaining = incrementInWorkdays - days;
        float fhours = remaining * 8f;
        int hours = (int) fhours;
        remaining = fhours - hours;
        float fminutes = remaining * 60f;
        int minutes = (int) fminutes;

        LocalDateTime localDateTime = startDato.plusDays(numberOfBusinessDaysFromDate(startDate, days));
        LocalDateTime endDate =
            startDato.plusDays(numberOfBusinessDaysFromDate(startDate, days)).plusHours(hours).plusMinutes(minutes);
        Instant instant = endDate.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    public Integer numberOfBusinessDaysFromDate(Date startDate, Integer numberOfDays) {
        Calendar startCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startCal.add(Calendar.DAY_OF_WEEK, 1);

        int workingdays = 0;
        while (numberOfDays > 0) {
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !holidays.contains(
                LocalDate.ofInstant(startCal.toInstant(), ZoneId.systemDefault()))) {
                numberOfDays--;
            }

            startCal.add(Calendar.DAY_OF_WEEK, 1);
            workingdays++;
        }

        if (startCal.get(Calendar.HOUR) > stop.get(Calendar.HOUR)) {
            workingdays++;
        }

        return workingdays;
    }

    public void setWorkdayStartAndStop(Calendar start, Calendar stop) {
        this.start = start;
        this.stop = stop;
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
