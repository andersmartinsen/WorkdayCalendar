package no.skatteetaten.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class WorkdayCalendar {

    private final Set<LocalDate> holidays = new HashSet<>();

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays) {
        LocalDateTime startDato = Instant.ofEpochMilli(startDate.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();

        System.out.println("Startdato er: " + startDate);

        int days = (int) incrementInWorkdays;
        float remaining = incrementInWorkdays - days;
        float fhours = remaining * 8f;
        int hours = (int) fhours;
        remaining = fhours - hours;
        float fminutes = remaining * 60f;
        int minutes = (int) fminutes;

        LocalDateTime endDate = startDato.plusDays(getAntallBusinessdagerFraDato(startDate, days)).plusHours(hours).plusMinutes(minutes);
        Instant instant = endDate.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    public Integer getAntallBusinessdagerFraDato(Date startdato, Integer antallDager) {
        Calendar startCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startdato);
        startCal.add(Calendar.DAY_OF_WEEK, 1);

        int arbeidsdager = 0;
        while (antallDager > 0) {
            if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || holidays.contains(LocalDate.ofInstant(startCal.toInstant(), ZoneId.systemDefault()))) {
            } else {
                antallDager--;
            }

            startCal.add(Calendar.DAY_OF_WEEK, 1);
            arbeidsdager++;
        }
        return arbeidsdager;
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
