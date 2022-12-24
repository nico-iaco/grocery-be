package it.iacovelli.grocerybe;

import com.google.auth.oauth2.GoogleCredentials;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

@SpringBootApplication(proxyBeanMethods = false)
@OpenAPIDefinition(info = @Info(title = "Grocery API", version = "1.0.0",
        description = "Api of an app to avoid food waste"),
        servers = {@Server(url = "/", description = "Default URL")})
public class GroceryBeApplication {

    private static final Log LOGGER = LogFactory.getLog(GroceryBeApplication.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        String accessToken = null;
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            credentials.refreshIfExpired();
            accessToken = credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            LOGGER.error("Error while getting access token", e);
        }
        return builder
                .setConnectTimeout(Duration.ofSeconds(7))
                .setReadTimeout(Duration.ofSeconds(7))
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GroceryBeApplication.class, args);
    }

}
