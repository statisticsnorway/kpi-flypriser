package no.ssb.kpi.flypriser.model.domene;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;

import com.amadeus.resources.Resource;

public class FlightOfferSearch extends Resource {
    protected FlightOfferSearch() {}

    private String type;
    private String id;
    private String source;
    private boolean instantTicketingRequired;
    private boolean nonHomogeneous;
    private boolean oneWay;
    private Date lastTicketingDate;
    private int numberOfBookableSeats;
    private Itinerary[] itineraries;
    private SearchPrice price;
    private PricingOptions pricingOptions;
    private String[] validatingAirlineCodes;
    private TravelerPricing[] travelerPricings;

    public String getType() { return type; }
    public String getId() { return id; }
    public String getSource() { return source; }
    public boolean isInstantTicketingRequired() { return instantTicketingRequired; }
    public boolean isNonHomogeneous() { return nonHomogeneous; }
    public boolean isOneWay() { return oneWay; }
    public Date getLastTicketingDate() { return lastTicketingDate; }
    public int getNumberOfBookableSeats() { return numberOfBookableSeats; }
    public Itinerary[] getItineraries() { return itineraries; }
    public SearchPrice getPrice() { return price; }
    public PricingOptions getPricingOptions() { return pricingOptions; }
    public String[] getValidatingAirlineCodes() { return validatingAirlineCodes; }
    public TravelerPricing[] getTravelerPricings() { return travelerPricings; }

    @Override
    public String toString() {
        return "FlightOfferSearch{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", instantTicketingRequired=" + instantTicketingRequired +
                ", nonHomogeneous=" + nonHomogeneous +
                ", oneWay=" + oneWay +
                ", lastTicketingDate=" + lastTicketingDate +
                ", numberOfBookableSeats=" + numberOfBookableSeats +
                ", itineraries=" + Arrays.toString(itineraries) +
                ", price=" + price +
                ", pricingOptions=" + pricingOptions +
                ", validatingAirlineCodes=" + Arrays.toString(validatingAirlineCodes) +
                ", travelerPricings=" + Arrays.toString(travelerPricings) +
                '}';
    }

    public class Itinerary {
        protected Itinerary() {
        }

        private String duration;
        private SearchSegment[] segments;

        public String getDuration() { return duration; }
        public SearchSegment[] getSegments() { return segments; }

        @Override
        public String toString() {
            return "Itinerary{" +
                    "duration='" + duration + '\'' +
                    ", segments=" + Arrays.toString(segments) +
                    '}';
        }
    }


    public static class SearchSegment {
        protected SearchSegment() {
        }

        private AirportInfo departure;
        private AirportInfo arrival;
        private String carrierCode;
        private String number;
        private Aircraft aircraft;
        private String duration;
        private String id;
        private int numberOfStops;
        private boolean blacklistedInEU;

        public AirportInfo getDeparture() { return departure; }
        public AirportInfo getArrival() { return arrival; }
        public String getCarrierCode() { return carrierCode; }
        public String getNumber() { return number; }
        public Aircraft getAircraft() { return aircraft; }
        public String getDuration() { return duration; }
        public String getId() { return id; }
        public int getNumberOfStops() { return numberOfStops; }
        public boolean isBlacklistedInEU() { return blacklistedInEU; }

        @Override
        public String toString() {
            return "SearchSegment{" +
                    "departure=" + departure +
                    ", arrival=" + arrival +
                    ", carrierCode='" + carrierCode + '\'' +
                    ", number='" + number + '\'' +
                    ", aircraft=" + aircraft +
                    ", duration='" + duration + '\'' +
                    ", id='" + id + '\'' +
                    ", numberOfStops=" + numberOfStops +
                    ", blacklistedInEU=" + blacklistedInEU +
                    '}';
        }
    }

    public static class AirportInfo {
        protected AirportInfo() {
        }

        private String iataCode;
        private String terminal;
        private String at;

        public String getIataCode() { return iataCode; }
        public String getTerminal() { return terminal; }
        public String getAt() { return at; }

        @Override
        public String toString() {
            return "AirportInfo{" +
                    "iataCode='" + iataCode + '\'' +
                    ", terminal='" + terminal + '\'' +
                    ", at='" + at + '\'' +
                    '}';
        }
    }


    public static class Aircraft {
        protected Aircraft() {
        }

        private String code;

        public String getCode() { return code; }

        @Override
        public String toString() {
            return "Aircraft{" +
                    "code='" + code + '\'' +
                    '}';
        }
    }


    public static class SearchPrice {
        protected SearchPrice() {
        }

        private String currency;
        private double total;
        private double base;
        private Fee[] fees;
        private double grandTotal;
        private Fee[] additionalServices;

        public String getCurrency() { return currency; }
        public double getTotal() { return total; }
        public double getBase() { return base; }
        public Fee[] getFees() { return fees; }
        public double getGrandTotal() { return grandTotal; }
        public Fee[] getAdditionalServices() { return additionalServices; }

        @Override
        public String toString() {
            return "SearchPrice{" +
                    "currency='" + currency + '\'' +
                    ", total=" + total +
                    ", base=" + base +
                    ", fees=" + Arrays.toString(fees) +
                    ", grandTotal=" + grandTotal +
                    ", additionalServices=" + Arrays.toString(additionalServices) +
                    '}';
        }
    }

    public static class Fee {
        protected Fee() {
        }

        private double amount;
        private String type;

        public double getAmount() { return amount; }
        public String getType() { return type; }

        @Override
        public String toString() {
            return "Fee{" +
                    "amount=" + amount +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public static class PricingOptions {
        private boolean includedCheckedBagsOnly;

        public boolean isIncludedCheckedBagsOnly() { return includedCheckedBagsOnly; }

        @Override
        public String toString() {
            return "PricingOptions{" +
                    "includedCheckedBagsOnly=" + includedCheckedBagsOnly +
                    '}';
        }
    }

    public class TravelerPricing {
        protected TravelerPricing() {
        }

        private String travelerId;
        private String fareOption;
        private String travelerType;
        private SearchPrice price;
        private FareDetailsBySegment[] fareDetailsBySegment;

        public String getTravelerId() { return travelerId; }
        public String getFareOption() { return fareOption; }
        public String getTravelerType() { return travelerType; }
        public SearchPrice getPrice() { return price; }
        public FareDetailsBySegment[] getFareDetailsBySegment() { return fareDetailsBySegment; }

        @Override
        public String toString() {
            return "TravelerPricing{" +
                    "travelerId='" + travelerId + '\'' +
                    ", fareOption='" + fareOption + '\'' +
                    ", travelerType='" + travelerType + '\'' +
                    ", price=" + price +
                    ", fareDetailsBySegment=" + Arrays.toString(fareDetailsBySegment) +
                    '}';
        }
    }

    public static class FareDetailsBySegment {
        protected FareDetailsBySegment() {
        }

        private String segmentId;
        private String cabin;
        private String fareBasis;
        private String brandedFare;
        @SerializedName("class")
        private String segmentClass;
        private IncludedCheckedBags includedCheckedBags;

        public String getSegmentId() { return segmentId; }
        public String getCabin() { return cabin; }
        public String getFareBasis() { return fareBasis; }
        public String getBrandedFare() { return brandedFare; }
        public String getSegmentClass() { return segmentClass; }
        public IncludedCheckedBags getIncludedCheckedBags() { return includedCheckedBags; }

        @Override
        public String toString() {
            return "FareDetailsBySegment{" +
                    "segmentId='" + segmentId + '\'' +
                    ", cabin='" + cabin + '\'' +
                    ", fareBasis='" + fareBasis + '\'' +
                    ", brandedFare='" + brandedFare + '\'' +
                    ", segmentClass='" + segmentClass + '\'' +
                    ", includedCheckedBags=" + includedCheckedBags +
                    '}';
        }
    }


    public static class IncludedCheckedBags {
        protected IncludedCheckedBags() {
        }

        private double weight;
        private String weightUnit;
        private int quantity;

        public double getWeight() { return weight; }
        public String getWeightUnit() { return weightUnit; }
        public int getQuantity() { return quantity; }

        @Override
        public String toString() {
            return "IncludedCheckedBags{" +
                    "quantity=" + quantity +
                    "weight=" + weight +
                    ", weightUnit=" + weightUnit +
                    '}';
        }
    }

}
