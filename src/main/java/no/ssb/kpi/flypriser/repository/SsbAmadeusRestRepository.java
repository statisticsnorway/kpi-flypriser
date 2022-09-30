package no.ssb.kpi.flypriser.repository;

import no.ssb.kpi.flypriser.model.domene.FlightOfferSearch;
import com.google.gson.Gson;
import no.ssb.kpi.flypriser.model.Search;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrb on 06.02.2017.
 */
@Component
public class SsbAmadeusRestRepository {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    RestTemplate restTemplate;

    @Value("${flightprices.host}")
    private String host;
    @Value("${flightprices.path}")
    private String path;


    public FlightOfferSearch[] searchFlightOffersFlightPrice(Search search, String[] dates) throws URISyntaxException {
        restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = createHttpEntity();

//        log.info("kall ssb-amadeus: host:{}, path:{}", host, path);
        URI uri = buildUri(search, dates);
        log.info("Search-uri: {}", uri.toString());
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(uri,
                    HttpMethod.GET,
                    httpEntity,
                    String.class);
            log.info("Hit OK");

            if (HttpStatus.OK.equals(response.getStatusCode())) {
                String flightDataJson = response.getBody();
//                log.info("respons: {}", response);
//                log.info("respons: {}, ({})", response.getBody(), response.getStatusCodeValue());

                FlightOfferSearch[] flightOffers = getFlightOffersFromJson(flightDataJson);
                log.info("ant result: {}", flightOffers != null ? flightOffers.length : 0);
                return flightOffers;
            } else {
                log.error("Ikke OK resultat fra ssb-amadeus: {}, ({})", response.getBody(), response.getStatusCodeValue());
            }

        } catch (HttpClientErrorException e) {
            log.error("feil i kall til FlightOffers i ssb-amadeus: {}, {}", e.getStatusCode().value(),  e.getResponseBodyAsString());
            return null;
        }
        return null;
    }

    /**
     *
     * @param flightDataJson Jsonstring of FlightOfferSearch-data
     * @return array of FlightOfferSearch
     */
    private FlightOfferSearch[] getFlightOffersFromJson(String flightDataJson) {
        try {
            String flightOfferString = (new JSONObject(flightDataJson)).getString("data");
            Gson gson = new Gson();
            return gson.fromJson(flightOfferString, FlightOfferSearch[].class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private HttpEntity<String> createHttpEntity() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/xml; charset=utf-8");
        return new HttpEntity<>(requestHeaders);
    }

    private URI buildUri(Search search, String[] dates) throws URISyntaxException {
        log.info("Search: {}, dates: {}", search.toString(), dates);


        return new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath(path)
                .setParameters(createParameters(search,dates))
                .build();
    }

    private List<NameValuePair> createParameters(Search search, String[] dates) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add( new BasicNameValuePair("origin", search.getOrigin()));
        parameters.add( new BasicNameValuePair("destination", search.getDestination()));
        parameters.add( new BasicNameValuePair("departureDate", dates[0]));
        parameters.add( new BasicNameValuePair("returnDate", dates[1]));
        parameters.add( new BasicNameValuePair("travelClass", search.getTravelClass()));
        parameters.add( new BasicNameValuePair("adults", search.getAdults()));
        parameters.add( new BasicNameValuePair("children", search.getChildren()));
        parameters.add( new BasicNameValuePair("infants", search.getInfants()));
//        parameters.add( new BasicNameValuePair("seniors", search.getSeniors()));
        parameters.add( new BasicNameValuePair("nonstop", search.getNonstop()));
        parameters.add( new BasicNameValuePair("currency", search.getCurrency()));
        if (search.getIncludeAirlineCodes() != null) {
            parameters.add(new BasicNameValuePair("includeAirlineCodes", search.getIncludeAirlineCodes()));
        }
        if (search.getIncludeAirlineCodes() != null) {
            parameters.add( new BasicNameValuePair("excludeAirlineCodes", search.getExcludeAirlineCodes()));
        }
        if (search.getIncludeAirlineCodes() != null) {
            parameters.add( new BasicNameValuePair("numResult", search.getNumResult()));
        }
        return parameters;

    }

}
