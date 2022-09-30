package no.ssb.kpi.flypriser.scheduling;

import no.ssb.kpi.flypriser.service.FlightPricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by lrb on 07.02.2017.
 */
@Service
public class ScheduledTasks {
    @Autowired
    FlightPricesService flightPricesService;

    @Value("${flightprices.configfile.kpi}")
    String kpiConfigFile;
    @Value("${flightprices.filenameprefix.kpi}")
    String kpiResultFilenamePrefix;
    @Value("${flightprices.resultType.kpi}")
    String kpiResultType;

    @Value("${flightprices.configfile.ppp}")
    String pppConfigFile;
    @Value("${flightprices.filenameprefix.ppp}")
    String pppResultFilenamePrefix;
    @Value("${flightprices.resultType.ppp}")
    String pppResultType;

    @Value("${flightprices.configfile.tppi}")
    private String tppiConfigFile;
    @Value("${flightprices.filenameprefix.tppi}")
    private String tppiResultFilenamePrefix;
    @Value("${flightprices.resultType.tppi}")
    private String tppiResultType;

    @Scheduled(cron = "${scheduled.cron.kpi}")
    public void runSearchesKpi(){
        flightPricesService.retrieveFlightPrices(kpiConfigFile, kpiResultFilenamePrefix, kpiResultType);
    }

    @Scheduled(cron = "${scheduled.cron.ppp}")
    public void runSearchesPpp(){
        flightPricesService.retrieveFlightPrices(pppConfigFile, pppResultFilenamePrefix, pppResultType);
    }

    @Scheduled(cron = "${scheduled.cron.tppi}")
    public void runSearchesTppi(){
        flightPricesService.retrieveFlightPrices(tppiConfigFile, tppiResultFilenamePrefix, tppiResultType);
    }

}
