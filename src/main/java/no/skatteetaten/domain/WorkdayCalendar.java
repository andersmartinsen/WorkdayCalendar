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
    private Calendar workingDayStart;
    private Calendar workingDayStop;

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays) {
        LocalDateTime startDateAndTime = startTimeOfWorkingDayBasedOnDate(startDate, incrementInWorkdays);

        int days = (int) incrementInWorkdays;
        float remaining = incrementInWorkdays - days;
        float floatHours = remaining * 8f;
        int hours = (int) floatHours;
        remaining = floatHours - hours;
        float floatMinutes = remaining * 60f;
        int minutes = (int) floatMinutes;

        Integer businessDayFromDate = numberOfBusinessDaysFromDate(startDate, days);
        Instant instant;
        if (incrementInWorkdays > 0) {
            LocalDateTime lastWorkingDay =
                startDateAndTime.plusDays(businessDayFromDate).plusHours(hours).plusMinutes(minutes);
            instant = lastWorkingDay.atZone(ZoneId.systemDefault()).toInstant();
        } else {
            LocalDateTime lastWorkingDay =
                startDateAndTime.minusDays(businessDayFromDate).plusHours(hours);
            instant = lastWorkingDay.atZone(ZoneId.systemDefault()).toInstant();
        }

        return Date.from(instant);
    }

    LocalDateTime startTimeOfWorkingDayBasedOnDate(Date startDate, float incrementInWorkdays) {
        LocalDateTime startTimeOfWorkingDay = Instant.ofEpochMilli(startDate.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay();

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        if (incrementInWorkdays >= 0 && startCal.get(Calendar.HOUR_OF_DAY) >= workingDayStart.get(Calendar.HOUR_OF_DAY)
            && startCal.get(Calendar.HOUR_OF_DAY) < workingDayStop.get(Calendar.HOUR_OF_DAY)) {
            return startTimeOfWorkingDay.plusHours(startCal.get(Calendar.HOUR_OF_DAY))
                .plusMinutes(startCal.get(Calendar.MINUTE));
        } else if (incrementInWorkdays < 0 && startCal.get(Calendar.HOUR_OF_DAY) >= workingDayStop.get(
            Calendar.HOUR_OF_DAY)) {
            return startTimeOfWorkingDay.plus(workingDayStop.get(Calendar.HOUR_OF_DAY), ChronoUnit.HOURS);

        } else {
            return startTimeOfWorkingDay.plus(workingDayStart.get(Calendar.HOUR_OF_DAY), ChronoUnit.HOURS);
        }
    }

    Integer numberOfBusinessDaysFromDate(Date startDate, Integer numberOfDays) {
        Calendar startCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        int workingdays = 0;
        if (numberOfDays > 0) {
            startCal.add(Calendar.DAY_OF_WEEK, 1);
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
        } else {
            startCal.add(Calendar.DAY_OF_WEEK, -1);
            int abs = Math.abs(numberOfDays);
            while (abs > 0) {
                if (isDayAWorkingDay(startCal)) {
                    abs--;
                }

                startCal.add(Calendar.DAY_OF_WEEK, -1);
                workingdays++;

            }
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
