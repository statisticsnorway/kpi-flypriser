package no.ssb.kpi.flypriser;

import no.ssb.kpi.flypriser.model.Search;
import no.ssb.kpi.flypriser.model.domene.FlightOfferSearch;
import no.ssb.kpi.flypriser.repository.SsbAmadeusRestRepository;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * Created by lrb on 06.02.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class SsbAmadeusRestRepositoryTest {
    @Autowired
    SsbAmadeusRestRepository restRepository;
    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = LocalDate.now().minusDays(1);

    @Test
    public void testRepo_rundayToday_notNullResult() throws Exception {
        SearchConfigurator configurator = new SearchConfigurator(new File("C:\\Api\\flypriser\\src\\test\\resources\\testconfig.csv"), today);
        String[] dates = {today.toString(), today.toString()};
        for (Search search : configurator.getSearchList()) {
            FlightOfferSearch[] flightOffers = restRepository.searchFlightOffersFlightPrice(search, dates);
//            System.out.println("flightOffers: \n");
//            Arrays.asList(flightOffers).forEach(f -> System.out.println(f.toString()));
            assert flightOffers != null && flightOffers.length > 0;
        }
    }

    @Test
    public void testRepo_rundayYesterday_nullResult() throws Exception {
        SearchConfigurator configurator = new SearchConfigurator(new File("C:\\Api\\flypriser\\src\\test\\resources\\testconfig.csv"), yesterday);
        String[] dates = {yesterday.toString(), yesterday.toString()};
        for (Search search : configurator.getSearchList()) {
            FlightOfferSearch[] flightOffers = restRepository.searchFlightOffersFlightPrice(search, dates);
            System.out.println("flightOffers: \n");
//            Arrays.asList(flightOffers).forEach(f -> System.out.println(f.toString()));
            assert flightOffers == null;
        }
    }

}
