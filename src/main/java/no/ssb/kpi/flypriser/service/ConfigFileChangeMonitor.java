package no.ssb.kpi.flypriser.service;

import org.apache.commons.jci.monitor.FilesystemAlterationListener;
import org.apache.commons.jci.monitor.FilesystemAlterationMonitor;
import org.apache.commons.jci.monitor.FilesystemAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by lrb on 08.02.2017.
 */
@Service
public class ConfigFileChangeMonitor implements FilesystemAlterationListener {
    private final FilesystemAlterationMonitor fileAlterMonitorObj
            = new FilesystemAlterationMonitor();
    @Value("${flightprices.configfile.kpi}")
    private String configfileName;
    @Value("${flightprices.filenameprefix.kpi}")
    String resultFilenamePrefix;
    @Value("${resultat.type}")
    String resultType;

    private File file;
    @Autowired
    FlightPricesService flightPricesService;



    public void attachListenerToFile() {
        fileAlterMonitorObj.addListener(file, this);
    }

    public void startListening() {
        fileAlterMonitorObj.start();
        fileAlterMonitorObj.setInterval(60000);
    }

    @Override
    public void onDirectoryChange(File arg0) {
    }

    @Override
    public void onDirectoryCreate(File arg0) {
    }

    @Override
    public void onDirectoryDelete(File arg0) {
    }

    @Override
    public void onFileChange(File arg0) {
        flightPricesService.retrieveFlightPrices(configfileName, resultFilenamePrefix, resultType);
    }

    @Override
    public void onFileCreate(File arg0) {
    }

    @Override
    public void onFileDelete(File arg0) {
    }

    @Override
    public void onStart(FilesystemAlterationObserver arg0) {
    }

    @Override
    public void onStop(FilesystemAlterationObserver arg0) {
    }
    @PostConstruct
    public void setup(){
        this.file = new File(configfileName);
        attachListenerToFile();
        startListening();
    }
}
