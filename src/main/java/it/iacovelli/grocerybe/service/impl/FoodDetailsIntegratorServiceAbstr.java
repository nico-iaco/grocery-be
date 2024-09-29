package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.service.FoodDetailsIntegratorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class FoodDetailsIntegratorServiceAbstr implements FoodDetailsIntegratorService {

    private final RestTemplate restTemplate;

    private final CircuitBreakerFactory circuitBreakerFactory;

    private static final Log LOGGER = LogFactory.getLog(FoodDetailsIntegratorServiceAbstr.class);

    @Value("${grocery-be.external.food-details-integrator-be.details-path}")
    private String foodDetailsEndpoint;

    @Value("${grocery-be.external.food-details-integrator-be.kcal-consumed-path}")
    private String kcalConsumedEndpoint;

    protected FoodDetailsIntegratorServiceAbstr(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    protected FoodDetailDto getFoodDetailsApi(String barcode, HttpEntity<Void> request) {
        CircuitBreaker foodDetails = circuitBreakerFactory.create("foodDetails");
        LOGGER.info("Calling food details integrator BE with endpoint: " + foodDetailsEndpoint);

        ResponseEntity<FoodDetailDto> response = foodDetails.run(
        () -> restTemplate.exchange(foodDetailsEndpoint, HttpMethod.GET, request, FoodDetailDto.class, barcode),
                throwable -> {
                    throw new FoodDetailsNotAvailableException("Food details not available");
                }
        );
        return response.getBody();
    }

    protected Float getKcalConsumedApi(String barcode, float quantity, HttpEntity<Void> request) {
        CircuitBreaker kcalConsumed = circuitBreakerFactory.create("kcalConsumed");
        LOGGER.info("Calling food details integrator BE with endpoint: " + kcalConsumedEndpoint);
        ResponseEntity<Float> response = kcalConsumed.run(
                () -> restTemplate.exchange(kcalConsumedEndpoint, HttpMethod.GET, request, Float.class, barcode, quantity),
                throwable -> {
                    throw new FoodDetailsNotAvailableException("Food details not available");
                }
        );

        return response.getBody();
    }

}
