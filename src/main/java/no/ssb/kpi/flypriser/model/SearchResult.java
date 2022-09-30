package no.ssb.kpi.flypriser.model;
/**
 * Created by lrb on 08.02.2017.
 */
public class SearchResult {
    private final Search search;
    private final String[] departureDate;
    private final String searchResponse;

    public SearchResult(Search search, String[] departureDate, String searchResponse) {
        this.search = search;
        this.departureDate = departureDate;
        this.searchResponse = searchResponse;
    }
    public Search getSearch() {
        return search;
    }
    public String[] getDepartureDate() {
        return departureDate;
    }
    public String getSearchResponse(){return searchResponse;}
}
