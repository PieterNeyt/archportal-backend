package be.kdg.ip3.archportal.shops.infrastructure.mollie;

import be.woutschoovaerts.mollie.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MollieConfig {

    @Value("${mollie.api.key}")
    private String apiKey;

    @Bean
    public Client mollieClient() {
        return new Client(apiKey);
    }
}