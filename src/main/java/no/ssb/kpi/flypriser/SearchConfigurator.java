package no.ssb.kpi.flypriser;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import no.ssb.kpi.flypriser.model.Search;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrb on 06.02.2017.
 */
public class SearchConfigurator {

    private final List<Search> searchList = new ArrayList<>();
    public SearchConfigurator(File configFile, LocalDate rundate) throws FileNotFoundException{
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(Search.class);
        String[] columns = new String[] {"id","konfigtype","origin","destination","departureDay","duration","weeks","travelClass", "adults", "children", "infants", "nonstop", "includeAirlineCodes", "excludeAirlineCodes", "numResult"}; // the fields to bind do in your JavaBean
        strat.setColumnMapping(columns);

        CsvToBean csv = new CsvToBean();
        CSVReader csvReader = new CSVReader(new FileReader(configFile));

        CSVReader csvReaderTest = new CSVReader(new FileReader(configFile));
        Logger log = LoggerFactory.getLogger(this.getClass());
        for (String[] konfigArray : csvReaderTest) {
            log.info("fillengde: " + konfigArray.length);
            for (String s : konfigArray) {
                log.info("   konfig: " + s);
            }
        }

        csv.setCsvReader(csvReader);
        csv.setMappingStrategy(strat);
        List list = csv.parse();
        log.info(list.size() + " søkekriterier: " + " på konfigfil " + configFile.getAbsolutePath());
        for (Object object : list) {
            if (object.toString().length() > 0 && !object.toString().startsWith("Search{id='#")) {
                Search search = (Search) object;
                search.configureDates(rundate);
                searchList.add(search);
            }
        }
    }
    public List<Search> getSearchList(){
        return searchList;
    }
}
