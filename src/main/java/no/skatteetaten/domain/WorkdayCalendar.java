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

        Integer businessDayFromDate = numberOfBusinessDaysFromDate(startDate, (int) incrementInWorkdays);

        LocalDateTime workingDateAfterIncrementingWorkdays =
            incrementInWorkdays > 0 ? calculate(incrementInWorkdays, startDateAndTime.plusDays(businessDayFromDate))
                : calculate(incrementInWorkdays, startDateAndTime.minusDays(businessDayFromDate));

        return Date.from(workingDateAfterIncrementingWorkdays.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime calculate(float incrementInWorkdays, LocalDateTime startDateAndTime) {
        int days = (int) incrementInWorkdays;
        float remaining = incrementInWorkdays - days;
        float floatHours = remaining * 8f;
        int hours = (int) floatHours;
        remaining = floatHours - hours;
        float floatMinutes = remaining * 60f;
        int minutes = (int) floatMinutes;

        return startDateAndTime.plusHours(hours).plusMinutes(minutes);
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

    Integer numberOfBusinessDaysFromDate(Date startDate, Integer numberOfDays) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        int numberOfBusinessDays = 0;
        int positiveNumberOfDays = Math.abs(numberOfDays);

        startCal.add(Calendar.DAY_OF_WEEK, numberOfDays > 0 ? 1 : -1);
        while (positiveNumberOfDays > 0) {
            if (isDayAWorkingDay(startCal)) {
                positiveNumberOfDays--;
            }

            startCal.add(Calendar.DAY_OF_WEEK, numberOfDays > 0 ? 1 : -1);
            numberOfBusinessDays++;
        }

        if (numberOfDays > 0 && startCal.get(Calendar.HOUR_OF_DAY) > workingDayStop.get(Calendar.HOUR_OF_DAY)) {
            numberOfBusinessDays++;
        }

        return numberOfBusinessDays;
    }

    boolean isDayAWorkingDay(Calendar startCal) {
        return startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
            && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !holidays.contains(
            LocalDate.ofInstant(startCal.toInstant(), ZoneId.systemDefault()));
    }

    Set<LocalDate> getHolidays() {
        return holidays;
    }

    LocalDateTime startTimeOfWorkingDayBasedOnDate(Date startDate, float incrementInWorkdays) {
        LocalDateTime startTimeOfWorkingDay = Instant.ofEpochMilli(startDate.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay();

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        if (isWorkingDaysAPositiveNumberAndTimeBetweenWorkingHours(incrementInWorkdays, startCal)) {
            return startTimeOfWorkingDay.plusHours(startCal.get(Calendar.HOUR_OF_DAY))
                .plusMinutes(startCal.get(Calendar.MINUTE));
        } else if (isWorkingDaysANegativeNumberAndWorkingHoursAfterWorkingDayStop(incrementInWorkdays, startCal)) {
            return startTimeOfWorkingDay.plus(workingDayStop.get(Calendar.HOUR_OF_DAY), ChronoUnit.HOURS);
        } else {
            return startTimeOfWorkingDay.plus(workingDayStart.get(Calendar.HOUR_OF_DAY), ChronoUnit.HOURS);
        }
    }

    private boolean isWorkingDaysANegativeNumberAndWorkingHoursAfterWorkingDayStop(float incrementInWorkdays,
        Calendar startCal) {
        return incrementInWorkdays < 0 && startCal.get(Calendar.HOUR_OF_DAY) >= workingDayStop.get(
            Calendar.HOUR_OF_DAY);
    }

    private boolean isWorkingDaysAPositiveNumberAndTimeBetweenWorkingHours(float incrementInWorkdays,
        Calendar startCal) {
        return incrementInWorkdays >= 0 && startCal.get(Calendar.HOUR_OF_DAY) >= workingDayStart.get(
            Calendar.HOUR_OF_DAY)
            && startCal.get(Calendar.HOUR_OF_DAY) < workingDayStop.get(Calendar.HOUR_OF_DAY);
    }
}