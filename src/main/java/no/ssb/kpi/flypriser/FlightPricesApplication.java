package no.ssb.kpi.flypriser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by lrb on 06.02.2017.
 */
@SpringBootApplication
@EnableScheduling
public class FlightPricesApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FlightPricesApplication.class);
        app.run();
    }
}
