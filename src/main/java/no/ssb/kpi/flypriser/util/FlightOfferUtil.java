package no.ssb.kpi.flypriser.util;

import no.ssb.kpi.flypriser.model.domene.FlightOfferSearch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class FlightOfferUtil {
    public static FlightOfferSearch[] getFlightOffers(String searchresultJson) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FlightOfferSearch>>(){}.getType();
        List<FlightOfferSearch> flightOfferList = gson.fromJson(searchresultJson, listType);
        return flightOfferList.toArray(new FlightOfferSearch[0]);
    }

}
