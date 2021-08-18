package no.skatteetaten.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class WorkdayCalendar {

    private final Set<LocalDate> holidays = new HashSet<>();
    private Calendar workingDayStart;
    private Calendar workingDayStop;

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays) {
        LocalDateTime startDateAndTime = startTimeOfWorkingDayBasertOneDate(startDate);

        int days = (int) incrementInWorkdays;
        float remaining = incrementInWorkdays - days;
        float fhours = remaining * 8f;
        int hours = (int) fhours;
        remaining = fhours - hours;
        float fminutes = remaining * 60f;
        int minutes = (int) fminutes;

        LocalDateTime lastWorkingDay =
            startDateAndTime.plusDays(numberOfBusinessDaysFromDate(startDate, days)).plusHours(hours).plusMinutes(minutes);
        Instant instant = lastWorkingDay.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    LocalDateTime startTimeOfWorkingDayBasertOneDate(Date startDate) {
        LocalDateTime startTimeOfWorkingDay = Instant.ofEpochMilli(startDate.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay();

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        if (startCal.get(Calendar.HOUR_OF_DAY) >= workingDayStart.get(Calendar.HOUR_OF_DAY)
            && startCal.get(Calendar.HOUR_OF_DAY) < workingDayStop.get(Calendar.HOUR_OF_DAY)) {
            return startTimeOfWorkingDay.plusHours(startCal.get(Calendar.HOUR_OF_DAY)).plusMinutes(startCal.get(Calendar.MINUTE));
        } else {
            return startTimeOfWorkingDay.plus(workingDayStart.get(Calendar.HOUR), ChronoUnit.HOURS);
        }
    }

    Integer numberOfBusinessDaysFromDate(Date startDate, Integer numberOfDays) {
        Calendar startCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startCal.add(Calendar.DAY_OF_WEEK, 1);

        int workingdays = 0;
        while (numberOfDays > 0) {
            if (isDayAWorkingDay(startCal)) {
                numberOfDays--;
            }

            startCal.add(Calendar.DAY_OF_WEEK, 1);
            workingdays++;
        }

        if (startCal.get(Calendar.HOUR_OF_DAY) > workingDayStop.get(Calendar.HOUR_OF_DAY)) {
            workingdays++;
        }

        return workingdays;
    }

    boolean isDayAWorkingDay(Calendar startCal) {
        return startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
            && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !holidays.contains(
            LocalDate.ofInstant(startCal.toInstant(), ZoneId.systemDefault()));
    }

    public void setWorkdayStartAndStop(Calendar start, Calendar stop) {
        this.workingDayStart = start;
        this.workingDayStop = stop;
    }

    public void setRecurringHoliday(GregorianCalendar gregorianCalendar) {
        holidays.add(gregorianCalendar.toZonedDateTime().toLocalDate());
    }

    public void setHoliday(GregorianCalendar gregorianCalendar) {
        holidays.add(gregorianCalendar.toZonedDateTime().toLocalDate());
    }

    Set<LocalDate> getHolidays() {
        return holidays;
    }
}
