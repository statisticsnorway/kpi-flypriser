package no.ssb.kpi.flypriser;


import no.ssb.kpi.flypriser.model.domene.FlightOfferSearch;
import no.ssb.kpi.flypriser.model.Search;
import no.ssb.kpi.flypriser.model.SearchResultFlightOfferSearch;
import no.ssb.kpi.flypriser.repository.SsbAmadeusRestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrb on 06.02.2017.
 */
@Service
public class SearchExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SsbAmadeusRestRepository restRepository;


    public synchronized List<SearchResultFlightOfferSearch> execute(List<Search> searchList){
        List<SearchResultFlightOfferSearch> searchResults = new ArrayList<>();
        for (Search s : searchList) {
            log.info("search: " + s.toString());
            for (String[] departureDate: s.getDepartureDates()) {
                FlightOfferSearch[] response;
                try {
                    response = restRepository.searchFlightOffersFlightPrice(s, departureDate);
                    log.info("resultat: {}", response.toString());
                    searchResults.add(new SearchResultFlightOfferSearch(s, departureDate, response));
                } catch (Exception e){
                    log.error("Error during search with id " + s.getId() + " and departuredate " + departureDate[0] + "-" + departureDate[1]);
                }
            }
        }
        return searchResults;
    }



}
