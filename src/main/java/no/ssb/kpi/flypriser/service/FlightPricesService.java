package no.ssb.kpi.flypriser.service;

import no.ssb.kpi.flypriser.SearchConfigurator;
import no.ssb.kpi.flypriser.SearchExecutor;
import no.ssb.kpi.flypriser.converter.FlightOfferResultConverter;
import no.ssb.kpi.flypriser.model.Search;
import no.ssb.kpi.flypriser.model.SearchResultFlightOfferSearch;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrb on 08.02.2017.
 */
@Service
public class FlightPricesService {

    @Autowired
    SearchExecutor searchExecutor;

    @Value("${flightprices.configfile}")
    String defaultConfigfileName;
    @Value("${flightprices.filenameprefix}")
    String defaultResultFilenamePrefix;
    @Value("${flightprices.outputdir}")
    String outputDir;

    @Value("${resultat.type}")
    String defaultResultatType;

    final String EXTENDED = "extended";

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public void retrieveFlightPrices(String configFileName, String filenamePrefix, String resultType) {
        log.info("******* Start retrieving flightoffers");
        SearchConfigurator searchConfigurator = getSearchConfigurator(configFileName != null ? configFileName : defaultConfigfileName);
        if (searchConfigurator == null) return;

        log.info("kall søketjenesten med liste av søkekriterier");

        List<SearchResultFlightOfferSearch> resultList = searchExecutor.execute(searchConfigurator.getSearchList());
        log.info("antall resultat-rader: {}", resultList.size());
        List<String> filInnhold = new ArrayList<>();
        resultType = resultType != null ? resultType : defaultResultatType;
        boolean extended = EXTENDED.equals(resultType);
//        log.info("extended: " + extended + ", resultatType: " + resultatType);

        FlightOfferResultConverter flightOfferResultConverter = new FlightOfferResultConverter();
        resultList.forEach(result -> {
            log.info("konverter resultat {}", result.getSearch().getId());
//            Arrays.asList(result.getFlightOffers()).forEach(fo -> log.info(fo.toString()));
            filInnhold.addAll(flightOfferResultConverter.convertToCharDelimited(
                    result.getFlightOffers(), result.getSearch(), extended, LocalDate.now() ));
            log.info("ferdig konvertert resultat {}", result.getSearch().getId());
        });

        log.info("lag filinnhold av {} resultatrader.", filInnhold.size());
        createFile(filenamePrefix != null ? filenamePrefix : defaultResultFilenamePrefix, filInnhold);
        log.info("******* End retrieving flight offers");
    }


    private void createFile(String filenamePrefix, List<String> filInnhold) {
        try {
            LocalDateTime fileDate = LocalDateTime.now();
            String filename = outputDir + filenamePrefix + fileDate.toString("YYYY-MM-dd-HH-mm-ss") + ".csv";
            log.info("skriv {} rader til fil {}", filInnhold.size(), filename);
            Files.write(Paths.get(filename), filInnhold);
        } catch (IOException ioe) {
            log.error("Error writing resultfile", ioe);
        }
    }

    private SearchConfigurator getSearchConfigurator(String configfileName) {
        SearchConfigurator searchConfigurator;
        LocalDate rundate = LocalDate.now();
        try {
            log.info("lager ny søkekonfigurasjon");
            searchConfigurator = new SearchConfigurator(new File(configfileName), rundate);
            for (Search s :searchConfigurator.getSearchList()) {
                log.info(s.toString());
            }
        } catch (FileNotFoundException f) {
            log.warn("Could not find configfile, aborting...", f);
            return null;
        }
        return searchConfigurator;
    }

}
