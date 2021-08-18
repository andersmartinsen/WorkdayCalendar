# WorkdayCalendar

Prosjekt for Vivende

Arbeidsdager-kalkulator Vi trenger en kalkulator for arbeidsdager:

```java
WorkdayCalendar workdayCalendar = new WorkdayCalendar();
```

Kalenderen skal kunne beregne avstand i arbeidsdager fra en gitt dato (en java.util.Date). En arbeidsdag er en dag fra
mandag til fredag som ikke er en helgedag. WorkdayCalendar har 2 metoder for å fortelle om hvilke dager skal betraktes
som helgedager:
workdayCalendar.setHoliday(Calendar date)
sier at den gitte dato er en helgedag og skal ikke betraktes som arbeidsdag. workdayCalendar.setRecurringHoliday(
Calendar date)
sier at den gitte datoen er å betrakte som helgedag hvert år (mao se bort fra årskomponenten).
workdayCalendar.setWorkdayStartAndStop(Calendar start, Calendar stop)
stiller start og stopp klokkeslett for arbeidsdagen, f.eks. 8-16:
workdayCalendar.setWorkdayStartAndStop(
new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0), new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0))
Her må vi også se bort fra selve datoen. Kjernen i løsningen blir følgende metode:
public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays)
Metoden må alltid returnere et klokkeslett mellom de 2 punktene definert i kallet setWorkdayStartAndStop, selv om
startDate ikke behøver å følge regelen selv. På denne måten blir kl 15:07 + 0,25 arbeidsdager kl 9:07, og kl 4:00 pluss
0,5 arbeidsdager lik kl 12:00. Klassen kan testes med f.eks. kode som denne:
WorkdayCalendar workdayCalendar = new WorkdayCalendar(); workdayCalendar.setWorkdayStartAndStop(
new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0), new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0));
workdayCalendar.setRecurringHoliday(
new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));

workdayCalendar.setHoliday(
new GregorianCalendar(2004, Calendar.MAY, 27, 0, 0)); SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5). getTime(); float increment = -5.5f;
System.out.println(
f.format(start) + " med tillegg av " + increment + " arbeidsdager er " + f.format(workdayCalendar.getWorkdayIncrement(
start, increment))); Noe som burde gi følgende resultat:
24-05-2004 18:05 med tillegg av -5.5 arbeidsdager er 14-05-2004 12:00 Andre korrekte resultater:
24-05-2004 19:03 med tillegg av 44.723656 arbeidsdager er 27-07-2004 13:47 24-05-2004 18:03 med tillegg av -6.7470217
arbeidsdager er 13-05-2004 10:02 24-05-2004 08:03 med tillegg av 12.782709 arbeidsdager er 10-06-2004 14:18 24-05-2004
07:03 med tillegg av 8.276628 arbeidsdager er 04-06-2004 10:12 Hele saken bør løses i én klassefil, evt. med indre
klasser hvis du trenger dem. De beste løsninger er lett å forstå, relativt korte (under 250 java-linjer), og de bør
selvsagt komme frem til riktig svar på en rekke forskjellige tilfeller. Lykke til!
