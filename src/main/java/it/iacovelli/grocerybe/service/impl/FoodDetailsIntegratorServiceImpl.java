package it.iacovelli.grocerybe.service.impl;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.service.FoodDetailsIntegratorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FoodDetailsIntegratorServiceImpl implements FoodDetailsIntegratorService {

    private final RestTemplate restTemplate;

    private final CircuitBreakerFactory circuitBreakerFactory;

    private static final Log LOGGER = LogFactory.getLog(FoodDetailsIntegratorServiceImpl.class);

    @Value("${grocery-be.external.food-details-integrator-be.details-path}")
    private String foodDetailsEndpoint;

    @Value("${grocery-be.external.food-details-integrator-be.kcal-consumed-path}")
    private String kcalConsumedEndpoint;

    @Value("${grocery-be.external.food-details-integrator-be.base-url}")
    private String foodDetailsIntegratorBaseUrl;

    @Override
    public FoodDetailDto getFoodDetails(String barcode) {
        CircuitBreaker foodDetails = circuitBreakerFactory.create("foodDetails");
        LOGGER.info("Calling food details integrator BE with endpoint: " + foodDetailsEndpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<FoodDetailDto> response = foodDetails.run(
                () -> restTemplate.exchange(foodDetailsEndpoint, HttpMethod.GET, request, FoodDetailDto.class, barcode),
                throwable -> {
                    throw new FoodDetailsNotAvailableException("Food details not available");
                }
        );

        return response.getBody();
    }

    @Override
    public Float getKcalConsumed(String barcode, float quantity) {
        CircuitBreaker kcalConsumed = circuitBreakerFactory.create("kcalConsumed");
        LOGGER.info("Calling food details integrator BE with endpoint: " + kcalConsumedEndpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Float> response = kcalConsumed.run(
                () -> restTemplate.exchange(kcalConsumedEndpoint, HttpMethod.GET, request, Float.class, barcode, quantity),
                throwable -> {
                    throw new FoodDetailsNotAvailableException("Food details not available");
                }
        );

        return response.getBody();
    }

    private String getAccessToken() {
        String accessToken = "";
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            if (credentials != null && credentials.hasRequestMetadata() && credentials.hasRequestMetadataOnly()) {
                //credentials.refreshIfExpired();
                AccessToken token = credentials.getAccessToken();
                if (token != null) {
                    accessToken = token.getTokenValue();
                } else {
                    LOGGER.error("Token is null trying with metadata server");
                    accessToken = getAccessTokenFromMetadataServer();
                }
            } else {
                LOGGER.error("Credentials not found trying with metadata server");
                accessToken = getAccessTokenFromMetadataServer();
            }
        } catch (IOException e) {
            LOGGER.error("Error while getting access token", e);
            accessToken = getAccessTokenFromMetadataServer();
        }
        return accessToken;
    }

    private String getAccessTokenFromMetadataServer() {
        String accessToken = "";
        String url = "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/identity?audience=" + foodDetailsIntegratorBaseUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Metadata-Flavor", "Google");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            accessToken = response.getBody();
            LOGGER.info("Access token retrieved from metadata server");
        } else {
            LOGGER.error("Error while getting access token from metadata server");
        }

        return accessToken;
    }



}