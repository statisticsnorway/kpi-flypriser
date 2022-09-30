package no.ssb.kpi.flypriser.converter;

import no.ssb.kpi.flypriser.model.domene.FlightOfferSearch;
import no.ssb.kpi.flypriser.model.Search;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class FlightOfferResultConverter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String delim = ";";
    private final String notAvailable = "Na";
    private LocalDate rundate;

    public List<String> convertToCharDelimited(FlightOfferSearch[] flightOffers, Search search, boolean extended, LocalDate date) {
        List<String> flightOfferList = new ArrayList<>();
        rundate = date;
        if (flightOffers != null) {
            Arrays.asList(flightOffers).forEach(flightOffer -> {
                final StringBuilder resultat = new StringBuilder();
                FlightOfferSearch.Itinerary[] offerItem = flightOffer.getItineraries();
                FlightOfferSearch.Itinerary utreise = offerItem[0];
                FlightOfferSearch.Itinerary innreise = offerItem.length > 1 ? offerItem[1] : null;

                if (extended) {
                    resultat.append(resultatStringExtended(search, flightOffer, utreise.getSegments(), innreise != null ? innreise.getSegments() : null));
                } else {
                    resultat.append(resultatStringNormal(search, flightOffer, utreise.getSegments()));
                }
//                    log.info("resultat: {}", resultat);
                flightOfferList.add(resultat.toString());
            });
        } else {
            log.info("ikke noen flygninger funnet for search {}", search.toString());
            flightOfferList.add("ingen flygninger funnet for " + search.toString());
        }
        return flightOfferList;
    }






    private String metadata(Search search) {
        return search.getDepartureDay() + delim + search.getDuration() + delim + rundate + delim;
    }

    private StringBuilder resultatStringExtended(Search search, FlightOfferSearch offerItem,
                                                 FlightOfferSearch.SearchSegment[] utreise,
                                                 FlightOfferSearch.SearchSegment[] innreise) {//}, boolean nonstop) {


        StringBuilder resultat = new StringBuilder();
        resultat.append(search.getOrigin()).append("-")
                .append(search.getDestination()).append(delim)
//                .append(search.getCurrency())
                .append(offerItem.getPrice().getCurrency())
                .append(String.format("%.2f", offerItem.getPrice().getTotal()).replace(",",".")).append(delim);
//                .append(offerItem.getPrice().getTotal()).append("0").append(delim);


        FlightOfferSearch.TravelerPricing pricing = getAdultPricing(offerItem);
        List<String> utSegmentIds = sortedFlightSegments(utreise).stream().map(FlightOfferSearch.SearchSegment::getId).collect(Collectors.toList());
        List<FlightOfferSearch.FareDetailsBySegment> utTravelPricings = Arrays.stream(pricing.getFareDetailsBySegment())
                .filter(p -> utSegmentIds.contains(p.getSegmentId())).collect(Collectors.toList());

        List<FlightOfferSearch.FareDetailsBySegment> innTravelPricings = new ArrayList<>();
        if (innreise != null) {
            List<String> innSegmentIds = sortedFlightSegments(innreise).stream().map(FlightOfferSearch.SearchSegment::getId).collect(Collectors.toList());
            innTravelPricings = Arrays.stream(pricing.getFareDetailsBySegment())
                    .filter(p -> innSegmentIds.contains(p.getSegmentId())).collect(Collectors.toList());
        }

        resultat.append(String.format("%.0f", pricing.getPrice().getTotal())).append(delim)
                .append(envei(utreise, utTravelPricings, offerItem.getNumberOfBookableSeats()))
                .append(envei(innreise, innTravelPricings, offerItem.getNumberOfBookableSeats()))
                .append(ekstraBagString(offerItem.getPrice()).intValue()).append(delim)
                .append(metadata(search)) // denne passer best til slutt
                .append(String.format("%.0f", pricing.getPrice().getBase())).append(delim)
                .append(fareBasisString(utTravelPricings)).append(delim)
                .append(fareBasisString(innTravelPricings)).append(delim)
                .append(brandedFareString(utTravelPricings)).append(delim)
                .append(brandedFareString(innTravelPricings)).append(delim);
    return resultat;
    }

    private FlightOfferSearch.TravelerPricing getAdultPricing(FlightOfferSearch offerItem) {
        return Arrays.stream(offerItem.getTravelerPricings())
                .filter(t -> "ADULT".equals(t.getTravelerType())).findFirst().get();
    }

    private StringBuilder resultatStringNormal(Search search, FlightOfferSearch offerItem,
                                               FlightOfferSearch.SearchSegment[] reise) {//}, boolean nonstop) {
        List<FlightOfferSearch.SearchSegment> sortertRute = sortedFlightSegments(reise);
        FlightOfferSearch.SearchSegment firstFlight = sortertRute.get(0);
        FlightOfferSearch.SearchSegment lastFlight = sortertRute.get(sortertRute.size()-1);

        StringBuilder resultat = new StringBuilder();
        resultat.append(operating(firstFlight)).append(delim)
                .append(search.getOrigin()).append("-").append(firstFlight.getDeparture().getIataCode()).append(delim)
                .append(search.getDestination()).append("-").append(lastFlight.getArrival().getIataCode()).append(delim)
                .append(firstFlight.getDeparture().getAt(), 0, 16).append(delim)
//                .append(search.getCurrency())
                .append(offerItem.getPrice().getCurrency())
                .append(String.format("%.2f", offerItem.getPrice().getTotal()).replace(",",".")).append(delim)
//                .append(offerItem.getPrice().getTotal()).append("0").append(delim)
                .append(offerItem.getNumberOfBookableSeats()).append(delim);

        FlightOfferSearch.TravelerPricing pricing = getAdultPricing(offerItem);
        resultat.append(String.format("%.0f", pricing.getPrice().getTotal())).append(delim)
                .append(pricing.getFareDetailsBySegment()[0].getCabin()).append(delim)
                .append(pricing.getFareDetailsBySegment()[0].getSegmentClass()).append(delim)
                .append(metadata(search));

        return resultat;
    }




    private StringBuilder envei(FlightOfferSearch.SearchSegment[] enveiInfo, List<FlightOfferSearch.FareDetailsBySegment> travelerPricings, int bookableSeats) {
        StringBuilder reiseplan = new StringBuilder();
        if (enveiInfo != null && enveiInfo.length > 0) {
            List<FlightOfferSearch.SearchSegment> segments = sortedFlightSegments(enveiInfo);

            String departure = segments.get(0).getDeparture().getAt().substring(0, 16);
            String arrival = segments.get(segments.size() - 1).getArrival().getAt().substring(0, 16);
            StringBuilder reiserute = reiseruteString(segments);

            String travelClasses = travelClassesString(travelerPricings);
            String bookingCodes = bookingCodesString(travelerPricings);
            String bags = bagsString(travelerPricings);

            return reiseplan.append(reiserute).append(delim)
                    .append(departure).append(delim)
                    .append(arrival).append(delim)
                    .append(bookableSeats).append(delim)
                    .append(travelClasses).append(delim)
                    .append(bookingCodes).append(delim)
                    .append(bags).append(delim);
        } else {
            return reiseplan.append(delim).append(delim).append(delim).append(delim).append(delim).append(delim).append(delim);
        }
    }


//    private String bookingCodesString(FlightOfferSearch.TravelerPricing adultPricing, List<String> segmentIds) {
//        return Arrays.stream(adultPricing.getFareDetailsBySegment())
//                .filter(p -> segmentIds.contains(p.getSegmentId()))
//                .map(p -> p.getCabin())
//                .collect(Collectors.joining("/"));
//    }


    private String bookingCodesString(List<FlightOfferSearch.FareDetailsBySegment> fareDetails) {
        return fareDetails.stream()
                .map(FlightOfferSearch.FareDetailsBySegment::getCabin)
                .collect(Collectors.joining("/"));
    }

    private String travelClassesString(List<FlightOfferSearch.FareDetailsBySegment> fareDetails) {
        return fareDetails.stream()
                .map(FlightOfferSearch.FareDetailsBySegment::getSegmentClass)
                .collect(Collectors.joining("/"));
    }

    private String fareBasisString(List<FlightOfferSearch.FareDetailsBySegment> fareDetails) {
        return fareDetails.stream()
                .map(fd -> fd.getFareBasis() != null ? fd.getFareBasis() : notAvailable)
                .collect(Collectors.joining("/"));
    }

    private String brandedFareString(List<FlightOfferSearch.FareDetailsBySegment> fareDetails) {
        return fareDetails.stream()
                .map(fd -> fd.getBrandedFare() != null ? fd.getBrandedFare() : notAvailable)
                .collect(Collectors.joining("/"));
    }

    private String bagsString(List<FlightOfferSearch.FareDetailsBySegment> fareDetails) {
        return fareDetails.stream()
                .map(p -> String.valueOf(p.getIncludedCheckedBags().getQuantity()))
                .collect(Collectors.joining("/"));
    }

    private Double ekstraBagString(FlightOfferSearch.SearchPrice price) {
        return price.getAdditionalServices() != null ?
                Arrays.stream(price.getAdditionalServices()).filter(s -> "CHECKED_BAGS".equals(s.getType()))
                .map(FlightOfferSearch.Fee::getAmount)
                .findFirst().orElse(-1.0) : -1.0;
    }


    private StringBuilder reiseruteString(List<FlightOfferSearch.SearchSegment> segments) {
        final StringBuilder reiserute = new StringBuilder(segments.get(0).getDeparture().getIataCode());
        segments.forEach(s ->
            reiserute.append(" (").append(s.getCarrierCode()).append("/").append(operating(s)).append(") ")
                    .append(s.getArrival().getIataCode())
        );
        return reiserute;
    }

    private String operating(FlightOfferSearch.SearchSegment flytur) {
        return flytur.getCarrierCode() + flytur.getNumber();
    }

    private List<FlightOfferSearch.SearchSegment> sortedFlightSegments(FlightOfferSearch.SearchSegment[] segments) {
        List<FlightOfferSearch.SearchSegment> segmentList = new ArrayList<>(Arrays.asList(segments));
        segmentList.sort(
                Comparator.comparing((FlightOfferSearch.SearchSegment s) -> s.getDeparture().getAt()));
        return segmentList;
    }



}
