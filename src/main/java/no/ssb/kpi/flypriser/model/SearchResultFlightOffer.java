package no.ssb.kpi.flypriser.model;

import com.amadeus.resources.FlightOffer;

/**
 * Created by lrb on 08.02.2017.
 */
public class SearchResultFlightOffer {
    private final Search search;
    private final String[] departureDate;
    private final FlightOffer[] flightOffers;

    public SearchResultFlightOffer(Search search, String[] departureDate, FlightOffer[] flightOffers) {
        this.search = search;
        this.departureDate = departureDate;
        this.flightOffers = flightOffers;
    }
    public Search getSearch() {
        return search;
    }
    public String[] getDepartureDate() {
        return departureDate;
    }
    public FlightOffer[] getFlightOffers(){return flightOffers;}
}
