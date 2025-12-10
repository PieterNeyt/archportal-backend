package be.kdg.ip3.archportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ArchPortalApplication {

    static void main(String[] args) {
        SpringApplication.run(ArchPortalApplication.class, args);
    }

}
