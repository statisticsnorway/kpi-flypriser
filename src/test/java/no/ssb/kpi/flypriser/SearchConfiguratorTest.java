package no.ssb.kpi.flypriser;

import no.ssb.kpi.flypriser.model.Search;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by lrb on 06.02.2017.
 */
public class SearchConfiguratorTest {
    private final LocalDate rundate = new LocalDate(2020,10,20);//2017, FEBRUARY, MONDAY

    @Test
    public void testConfigFile() throws Exception{
        SearchConfigurator configurator = new SearchConfigurator(new File("src/test/resources/testconfig.csv"),rundate);
        for (Search search : configurator.getSearchList()){
            if (search.getId().equals("1")){
                testSearchId1(search);
            }
            if (search.getId().equals("2")){
                testSearchId2(search);
            }
            if (search.getId().equals("4")){
                testSearchId4(search);
            }
            if (search.getId().equals("5")){
                testSearchId5(search);
            }
            if (search.getId().equals("6")){
                testSearchId6(search);
            }
            assert !search.getId().equals("3") : "id 3 er ikke utkommentert";
        }
    }

    private void testSearchId1(Search search){
          /* Config: 1,DAGUKEIMND,NYC,LAX,FRIDAY,2,0%0#3#12#28
           Avgangsdag: fredag
           Returndag: 2 (søndag)
           Datoer:
                1: 2017-09-08 - 2017-09-10
                2: 2017-09-29 - 2017-10-01
                3: 2017-12-01 - 2017-12-03
                4: 2018-03-23 - 2018-03-25
         */
        assert search.getKonfigtype().equals("DAGUKEIMND") : "Wrong konfigtype for searchid 1";
        assert search.getOrigin().equals("NYC") : "Wrong origin city for search id 1";
        assert search.getDestination().equals("LAX") : "Wrong destination city for search id 1";
        assert search.getDepartureDates().size() == 4 : "Wrong number of searchdates for search id 1";
        assert search.getWeeks().equals("0%0#3#12#28") : "Wrong weeknumber(s) for search id 1";
        String departureDate = search.getDepartureDates().get(0)[0];
        assert departureDate.equals("2017-09-08") : "Wrong departuredate for searchdate 0 for search id 1";
        departureDate = search.getDepartureDates().get(1)[0];
        assert departureDate.equals("2017-09-29") : "Wrong departuredate for searchdate 1 for search id 1";
        departureDate = search.getDepartureDates().get(2)[0];
        assert departureDate.equals("2017-12-01") : "Wrong departuredate for searchdate 1 for search id 1";
        departureDate = search.getDepartureDates().get(3)[1];
        assert departureDate.equals("2018-03-25") : "Wrong returndate for searchdate 1 for search id 1";

    }

    private void testSearchId2(Search search){
        /* Config: 2,DAGUKE,OSL,RYG,MONDAY,0,0
           Avgangsdag: mandag
           Returdag: 0 (mandag)
           Datoer: som rundate
         */
        assert search.getKonfigtype().equals("DAGUKE") : "Wrong konfigtype for search id 2";
        assert search.getOrigin().equals("OSL") : "Wrong origin city for search id 2";
        assert search.getDestination().equals("RYG") : "Wrong destination city for search id 2";
        assert search.getDepartureDates().size() == 1 : "Wrong number of searchdates for search id 2";
        assert search.getWeeks().equals("0") : "Wrong weeknumber(s) for search id 2";
        String departureDate = search.getDepartureDates().get(0)[0];
        assert departureDate.equals("2017-09-04") : "Wrong departuredate for searchdate 0 for search id 2";
    }

    private void testSearchId4(Search search){
          /* Config: 4,DAGUKE,OSL,NYC,FRIDAY,2,4#5#6#12#13#14#20#21#22#28#29#30
           Avgangsdag: fredag
           Returndag: 2 (søndag)
           Datoer:
                1: 2017-10-06
                2: 2017-10-13
                3: 2017-10-20
                4: 2017-12-01
                5: 2017-12-08
                6: 2017-12-15
                7: 2018-01-26
                8: 2018-02-02
                9: 2018-02-09
               10: 2018-03-23
               11: 2018-03-30
               12: 2018-04-06 - 2018-04-08
         */
        assert search.getKonfigtype().equals("DAGUKE") : "Wrong konfigtype for searchid 4";
        assert search.getOrigin().equals("OSL") : "Wrong origin city for search id 4";
        assert search.getDestination().equals("NYC") : "Wrong destination city for search id 4";
        assert search.getDepartureDates().size() == 12 : "Wrong number of searchdates for search id 4";
        assert search.getWeeks().equals("4#5#6#12#13#14#20#21#22#28#29#30") : "Wrong weeknumber(s) for search id 4";
        String departureDate = search.getDepartureDates().get(0)[0];
        assert departureDate.equals("2017-10-06") : "Wrong departuredate for searchdate 0 for search id 4";
        departureDate = search.getDepartureDates().get(1)[0];
        assert departureDate.equals("2017-10-13") : "Wrong departuredate for searchdate 1 for search id 4";
        departureDate = search.getDepartureDates().get(4)[1];
        assert departureDate.equals("2017-12-10") : "Wrong returndate for searchdate 4 for search id 4";
        departureDate = search.getDepartureDates().get(6)[0];
        assert departureDate.equals("2018-01-26") : "Wrong departuredate for searchdate 6 for search id 4";
        departureDate = search.getDepartureDates().get(11)[1];
        assert departureDate.equals("2018-04-08") : "Wrong returndate for searchdate 11 for search id 4";

    }

    private void testSearchId5(Search search){
          /* Config: 5,DAGNUMIMND,OSL,NYC,FRIDAY,2,1%0#1#2?3%0#1#2?5%0#1#2?7%0#1#2
           Avgangsdag: fredag
           Returndag: 2 (søndag)
           Datoer:
                1: 2017-10-06
                2: 2017-10-13
                3: 2017-10-20
                4: 2017-12-01
                5: 2017-12-08
                6: 2017-12-15
                7: 2018-02-02
                8: 2018-02-09
                9: 2018-02-16
               10: 2018-04-06
               11: 2018-04-13
               12: 2018-04-20 - 2018-04-22
         */
        assert search.getKonfigtype().equals("DAGNUMIMND") : "Wrong konfigtype for searchid 5";
        assert search.getOrigin().equals("OSL") : "Wrong origin city for search id 5";
        assert search.getDestination().equals("NYC") : "Wrong destination city for search id 5";
        assert search.getDepartureDates().size() == 12 : "Wrong number of searchdates for search id 5";
        assert search.getWeeks().equals("1%0#1#2?3%0#1#2?5%0#1#2?7%0#1#2") : "Wrong weeknumber(s) for search id 5";
        String departureDate = search.getDepartureDates().get(0)[0];
        assert departureDate.equals("2017-10-06") : "Wrong departuredate for searchdate 0 for search id 5";
        departureDate = search.getDepartureDates().get(1)[0];
        assert departureDate.equals("2017-10-13") : "Wrong departuredate for searchdate 1 for search id 5";
        departureDate = search.getDepartureDates().get(4)[1];
        assert departureDate.equals("2017-12-10") : "Wrong returndate for searchdate 4 for search id 5";
        departureDate = search.getDepartureDates().get(6)[0];
        assert departureDate.equals("2018-02-02") : "Wrong departuredate for searchdate 6 for search id 5";
        departureDate = search.getDepartureDates().get(11)[1];
        assert departureDate.equals("2018-04-22") : "Wrong returndate for searchdate 11 for search id 5";

    }

    private void testSearchId6(Search search){
          /* Config: 6,DAGUKEIMND,OSL,NYC,FRIDAY,2,1%0#1#2?3%0#1#2?5%0#1#2?7%0#1#2
           Avgangsdag: fredag
           Returndag: 2 (søndag)
           Datoer:
                1: 2017-10-06
                2: 2017-10-13
                3: 2017-10-20
                4: 2017-12-08
                5: 2017-12-15
                6: 2017-12-22
                7: 2018-02-09
                8: 2018-02-16
                9: 2018-02-23
               10: 2018-04-06
               11: 2018-04-13
               12: 2018-04-20 - 2018-04-22
         */
        assert search.getKonfigtype().equals("DAGUKEIMND") : "Wrong konfigtype for searchid 6";
        assert search.getOrigin().equals("OSL") : "Wrong origin city for search id 6";
        assert search.getDestination().equals("NYC") : "Wrong destination city for search id 6";
        assert search.getDepartureDates().size() == 12 : "Wrong number of searchdates for search id 6";
        assert search.getWeeks().equals("1%0#1#2?3%0#1#2?5%0#1#2?7%0#1#2") : "Wrong weeknumber(s) for search id 6";
        String departureDate = search.getDepartureDates().get(0)[0];
        assert departureDate.equals("2017-10-06") : "Wrong departuredate for searchdate 0 for search id 6";
        departureDate = search.getDepartureDates().get(1)[0];
        assert departureDate.equals("2017-10-13") : "Wrong departuredate for searchdate 1 for search id 6";
        departureDate = search.getDepartureDates().get(4)[1];
        assert departureDate.equals("2017-12-17") : "Wrong returndate for searchdate 4 for search id 6";
        departureDate = search.getDepartureDates().get(6)[0];
        assert departureDate.equals("2018-02-09") : "Wrong departuredate for searchdate 6 for search id 6";
        departureDate = search.getDepartureDates().get(11)[1];
        assert departureDate.equals("2018-04-22") : "Wrong returndate for searchdate 11 for search id 6";

    }

    @Test
    public void test(){
        LocalDateTime fileDate = LocalDateTime.now();
        System.out.println(fileDate.toString("YYYY-MM-dd-HH24-mm")+".cvs");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
        System.out.println(fileDate.toString(fmt));
    }
}
