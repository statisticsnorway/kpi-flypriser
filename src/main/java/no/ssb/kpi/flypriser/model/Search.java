package no.ssb.kpi.flypriser.model;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import static java.time.temporal.TemporalAdjusters.firstInMonth;

/**
 * Created by lrb on 06.02.2017.
 */
public class Search {
   private final Logger log = LoggerFactory.getLogger(this.getClass());

    public final String KONFIGTYPE_DAGUKE = "DAGUKE";
    public final String KONFIGTYPE_DAGUKEIMND = "DAGUKEIMND";
    public final String KONFIGTYPE_DAGNUMIMND = "DAGNUMIMND";
    public final String KONFIGTYPE_DATO = "DATO";


    private String id;
    private String konfigtype;
    private String origin;
    private String destination;
    private String departureDay;
    private String duration = "0";
    private String weeks;
    private String travelClass;
    private String adults = "1";
    private String children = "0";
    private String infants = "0";
    private String seniors = "0";
    private String nonstop = "true";
    private String currency = "NOK";
    private String includeAirlineCodes;
    private String excludeAirlineCodes;
    private String numResult;

    private final List<String[]> departureDates = new ArrayList<>();




    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getKonfigtype() { return konfigtype; }

    public void setKonfigtype(String konfigtype) { this.konfigtype = konfigtype; }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(String departureDay) {
        this.departureDay = departureDay;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getTravelClass() { return travelClass; }

    public void setTravelClass(String travelClass) { this.travelClass = travelClass; }

    public String getAdults() { return adults; }

    public void setAdults(String adults) { this.adults = adults; }

    public String getChildren() { return children; }

    public void setChildren(String children) { this.children = children; }

    public String getInfants() { return infants;}

    public void setInfants(String infants) { this.infants = infants; }

    public String getSeniors() { return seniors; }

    public void setSeniors(String seniors) { this.seniors = seniors; }

    public String getNonstop() { return nonstop; }

    public void setNonstop(String nonstop) { this.nonstop = nonstop; }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) { this.currency = currency; }

    public String getIncludeAirlineCodes() { return includeAirlineCodes; }

    public void setIncludeAirlineCodes(String includeAirlineCodes) { this.includeAirlineCodes = includeAirlineCodes; }

    public String getExcludeAirlineCodes() { return excludeAirlineCodes; }

    public void setExcludeAirlineCodes(String excludeAirlineCodes) { this.excludeAirlineCodes = excludeAirlineCodes; }

    public String getNumResult() { return numResult; }

    public void setNumResult(String numResult) { this.numResult = numResult; }

    public List<String[]> getDepartureDates(){
        return departureDates;
    }

    public void configureDates(LocalDate rundate) {
        switch (konfigtype) {
            case KONFIGTYPE_DAGUKE:
                this.configureDatesWeek(rundate);
                break;
            case KONFIGTYPE_DAGUKEIMND:
                this.configureDatesUkeIMnd(rundate);
                break;
            case KONFIGTYPE_DAGNUMIMND:
                this.configureDatesDagNumIMnd(rundate);
                break;
            case KONFIGTYPE_DATO:
                this.configureDateDato();
        }
    }


    /** Denne finner dato for en ukedag i antalluker fra kjøredato **/
    public void configureDatesWeek(LocalDate rundate) {
        StringTokenizer ukeTokenizer = new StringTokenizer(weeks, "#");
        while(ukeTokenizer.hasMoreElements()) {
            String ukeFraRundate = ukeTokenizer.nextToken();
//            log.info("ukeFraRundate: " + ukeFraRundate);
            LocalDate depDate = setDayAndAddWeek(rundate, this.departureDay, "0", ukeFraRundate);
            LocalDate retDate = duration == null || "-".equalsIgnoreCase(duration) ? null : depDate.plusDays(new Integer(duration));
//            log.info("departure: " + departureDay + ", depDate: " + depDate + ", return: " + retDate);
            String[] dates = {depDate.toString(), retDate == null ? null : retDate.toString()};
            this.departureDates.add(dates);
        }
    }

    /** Denne finner dato for en ukedag i antallmåneder pluss antalluker fra kjøredato **/
    public void configureDatesUkeIMnd(LocalDate rundate) {
        StringTokenizer mndTokenizer = new StringTokenizer(weeks, "?");
        while(mndTokenizer.hasMoreElements()) {
            String mndUke = mndTokenizer.nextToken();
//            log.info("mndUke: " + mndUke);
            int idx = mndUke.indexOf("%");
            if (idx > 0 && mndUke.length() > idx) {
                String mndFraNaa = mndUke.substring(0, idx);
                String ukerIMnd = mndUke.substring(idx + 1);
//                log.info("mndFraNaa: " + mndFraNaa + ", ukerIMnd: " + ukerIMnd);


                StringTokenizer ukeIMndTokenizer = new StringTokenizer(ukerIMnd, "#");
                while (ukeIMndTokenizer.hasMoreElements()) {
                    String ukeIMnd = ukeIMndTokenizer.nextToken();
//                    log.info("ukeIMnd: " + ukeIMnd);
                    LocalDate depDate = setDayAndAddWeek(rundate, this.departureDay, mndFraNaa, ukeIMnd);
                    LocalDate retDate = duration == null || "-".equalsIgnoreCase(duration) ? null : depDate.plusDays(new Integer(duration));
//                    log.info("departure: " + departureDay + ", depDate: " + depDate + ", return: " + retDate);
                    String[] dates = {depDate.toString(), retDate == null ? null : retDate.toString()};
                    this.departureDates.add(dates);
                }
            }
        }
    }

    /** Denne finner dato for en ukedag i antallmåneder fra kjøredato pluss antalluker fra første forekomst av ukedagen i måneden **/
    public void configureDatesDagNumIMnd(LocalDate rundate) {
        StringTokenizer mndTokenizer = new StringTokenizer(weeks, "?");
        while(mndTokenizer.hasMoreElements()) {
            String mndUke = mndTokenizer.nextToken();
//            log.info("mndUke: " + mndUke);
            int idx = mndUke.indexOf("%");
            if (idx > 0 && mndUke.length() > idx) {
                String mndFraNaa = mndUke.substring(0, idx);
                String forekomsterAvDagIMnd = mndUke.substring(idx + 1);
//                log.info("mndFraNaa: " + mndFraNaa + ", forekomsterAvDagIMnd: " + forekomsterAvDagIMnd);


                StringTokenizer dagForekomstTokenizer = new StringTokenizer(forekomsterAvDagIMnd, "#");
                while (dagForekomstTokenizer.hasMoreElements()) {
                    String dagForekomst = dagForekomstTokenizer.nextToken();
//                    log.info(dagForekomst + ". forekomst av dag i mnd: ");
                    LocalDate depDate = setFirstDayAndAddWeek(rundate, this.departureDay, mndFraNaa, dagForekomst);
                    LocalDate retDate = duration == null || "-".equalsIgnoreCase(duration) ? null : depDate.plusDays(new Integer(duration));
//                    log.info("departure: " + departureDay + ", depDate: " + depDate + ", return: " + retDate);
                    String[] dates = {depDate.toString(), retDate == null ? null : retDate.toString()};
                    this.departureDates.add(dates);
                }
            }
        }
    }

    /** Denne finner dato for en ukedag i antalluker fra kjøredato **/
    public void configureDateDato() {
        LocalDate depDate = setDateFromDato(this.departureDay);
        LocalDate retDate = duration == null || "-".equalsIgnoreCase(duration) ? null : depDate.plusDays(new Integer(duration));
//            log.info("departure: " + departureDay + ", depDate: " + depDate + ", return: " + retDate);
        String[] dates = {depDate.toString(), retDate == null ? null : retDate.toString()};
        this.departureDates.add(dates);
    }

    private LocalDate setDateFromDato(String departureDay) {
        return LocalDate.parse(departureDay); //Dato må være på format yyyy-mm-dd
    }

    private LocalDate setDayAndAddWeek(LocalDate rundate, String day, String mndFraNaa, String week) {
//        log.info("rundate: " + rundate + ", day: " + day + ", mndFraaNaa: " + mndFraNaa + ", week: " + week);
        LocalDate date = rundate.plusMonths(Integer.parseInt(mndFraNaa));
//        log.info("  lagt til " + mndFraNaa + " måneder: " + date);
        date = date.withDayOfWeek(dayToInteger(day));
//        log.info("  date.withDayOfWeek: " + date);
        date = date.plusWeeks(Integer.parseInt(week));
//        log.info("  lagt til " + week + " uker: " + date);
        return date;
    }

    private LocalDate setFirstDayAndAddWeek(LocalDate rundate, String day, String mndFraNaa, String week) {
//        log.info("rundate: " + rundate + ", day: " + day + ", mndFraaNaa: " + mndFraNaa + ", week: " + week);
        LocalDate date = rundate.plusMonths(Integer.parseInt(mndFraNaa));
//        log.info("  lagt til " + mndFraNaa + " måneder: " + date);
        LocalDate firstDayAppearInMonth = getFirstInMonth(date, dayToInteger(day));
        return firstDayAppearInMonth.plusWeeks(Integer.parseInt(week));
    }


    private int dayToInteger(String day) {
        switch (day) {
            case "MONDAY":
                return 1;
            case "TUESDAY":
                return 2;
            case "WEDNESDAY":
                return 3;
            case "THURSDAY":
                return 4;
            case "FRIDAY":
                return 5;
            case "SATURDAY":
                return 6;
            case "SUNDAY":
                return 7;
            default:
                return -1;
        }

    }

    private LocalDate getFirstInMonth(LocalDate dateWithMonth, int intDayOfWeek) {
        java.time.LocalDate javaLocalDateWithMonth = java.time.LocalDate.of(dateWithMonth.getYear(), dateWithMonth.getMonthOfYear(), dateWithMonth.getDayOfMonth());

        DayOfWeek dayOfWeek = DayOfWeek.of(intDayOfWeek);
//        log.info("dayOfWeek: " + dayOfWeek.name());
        java.time.LocalDate firstInMonthJava = javaLocalDateWithMonth.with(firstInMonth(dayOfWeek));
        return new LocalDate(firstInMonthJava.getYear(), firstInMonthJava.getMonthValue(), firstInMonthJava.getDayOfMonth());
    }

    private static LocalDate getNthOfMonth(int day_of_week, int month, int year) {
        LocalDate date = new LocalDate(year, month, 1).dayOfMonth()
                .withMaximumValue()
                .dayOfWeek()
                .setCopy(day_of_week);
        if (date.getMonthOfYear() != month) {
            return date.dayOfWeek().addToCopy(-7);
        }
        return date;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id='" + id + '\'' +
                ", konfigtype='" + konfigtype + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureDay='" + departureDay + '\'' +
                ", duration='" + duration + '\'' +
                ", weeks='" + weeks + '\'' +
                ", travelClass='" + travelClass + '\'' +
                ", adults='" + adults + '\'' +
                ", children='" + children + '\'' +
                ", infants='" + infants + '\'' +
                ", seniors='" + seniors + '\'' +
                ", nonstop='" + nonstop + '\'' +
                ", departureDates=" + getDepartureDates().size() +
                ", currency='" + currency + '\'' +
                ", includeAirlineCodes='" + includeAirlineCodes + '\'' +
                ", excludeAirlineCodes='" + excludeAirlineCodes + '\'' +
                ", numResult=" + numResult +
                '}';
    }
}
