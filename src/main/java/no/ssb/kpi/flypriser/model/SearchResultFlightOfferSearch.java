package no.ssb.kpi.flypriser.model;

import no.ssb.kpi.flypriser.model.domene.FlightOfferSearch;

/**
 * Created by lrb on 08.02.2017.
 */
public class SearchResultFlightOfferSearch {
    private final Search search;
    private final String[] departureDate;
    private final FlightOfferSearch[] flightOffers;

    public SearchResultFlightOfferSearch(Search search, String[] departureDate, FlightOfferSearch[] flightOffers) {
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
    public FlightOfferSearch[] getFlightOffers() { return flightOffers; }
}
