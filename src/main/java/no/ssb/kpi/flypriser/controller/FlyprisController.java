package no.ssb.kpi.flypriser.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.ssb.kpi.flypriser.service.FlightPricesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lrb on 06.02.2017.
 */
@Api("ssb-amadeus-api")
@RestController
@RequestMapping("")
public class FlyprisController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String HEADER_APIKEY = "X-SSB-APIKEY";

    @Value("${api-keys}")
    private List<String> acceptedApiKeys;

    @Value("${flightprices.path}")
    private String ssbAmadeusUrl;

    @Value("${flightprices.configfile.kpi}")
    private String kpiConfig;
    @Value("${flightprices.filenameprefix.kpi}")
    private String kpiPrefix;
    @Value("${flightprices.resultType.kpi}")
    private String kpiResultType;

    @Value("${flightprices.configfile.ppp}")
    private String pppConfig;
    @Value("${flightprices.filenameprefix.ppp}")
    private String pppPrefix;
    @Value("${flightprices.resultType.ppp}")
    private String pppResultType;

    @Value("${flightprices.configfile.tppi}")
    private String tppiConfig;
    @Value("${flightprices.filenameprefix.tppi}")
    private String tppiPrefix;
    @Value("${flightprices.resultType.tppi}")
    private String tppiResultType;

    @Autowired
    FlightPricesService flightPricesService;

    @ApiOperation(value = "Søke etter billige flypriser")
    @RequestMapping(value ="/flight-offers", method = RequestMethod.GET)
    public ResponseEntity<String> flightOffers (@RequestHeader HttpHeaders header
            , @RequestParam String configfile
            , @RequestParam String filenamePrefix
            , @RequestParam String resultType
    ) {
        if (!authorizeRequest(header)) {
            return new ResponseEntity<>("Unauthorized. Api-key er ugyldig eller mangler", HttpStatus.UNAUTHORIZED);
        }
        log.info("kjør flightoffer-søk");
        try {
            flightPricesService.retrieveFlightPrices(configfile, filenamePrefix, resultType);
            return new ResponseEntity<>("flyprissøk ferdig", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Søke etter billige flypriser KPI")
    @RequestMapping(value ="/flypris-kpi", method = RequestMethod.GET)
    public ResponseEntity<String> flightOffersKpi (@RequestHeader HttpHeaders header) {
        return flightOffers(header, kpiConfig, kpiPrefix, kpiResultType);
    }

    @ApiOperation(value = "Søke etter billige flypriser PPP")
    @RequestMapping(value ="/flypris-ppp", method = RequestMethod.GET)
    public ResponseEntity<String> flightOffersPPP (@RequestHeader HttpHeaders header) {
        return flightOffers(header, pppConfig, pppPrefix, pppResultType);
    }


    @ApiOperation(value = "Søke etter billige flypriser TPPI")
    @RequestMapping(value ="/flypris-tppi", method = RequestMethod.GET)
    public ResponseEntity<String> flightOffersTPPI (@RequestHeader HttpHeaders header) {
        return flightOffers(header, tppiConfig, tppiPrefix, tppiResultType);
    }

    @ApiOperation(value = "Ping metode for å kunne sjekke at applikasjonen er oppe")
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }



    private boolean authorizeRequest(HttpHeaders header) {
        return header.get(HEADER_APIKEY) != null &&acceptedApiKeys.contains(header.get(HEADER_APIKEY).get(0));
    }

//    @PostConstruct
//    public void printValues() {
//        log.info("acceptedApiKeys: " + acceptedApiKeys + ", ssbAmadeusUrl: " + ssbAmadeusUrl);
//    }
}
