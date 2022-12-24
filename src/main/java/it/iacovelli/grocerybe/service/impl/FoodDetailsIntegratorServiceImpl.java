package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.service.FoodDetailsIntegratorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public FoodDetailDto getFoodDetails(String barcode) {
        CircuitBreaker foodDetails = circuitBreakerFactory.create("foodDetails");
        LOGGER.info("Calling food details integrator BE with endpoint: " + foodDetailsEndpoint);
        return foodDetails.run(
                () -> restTemplate.getForObject(foodDetailsEndpoint, FoodDetailDto.class, barcode),
                throwable -> {
                    throw new FoodDetailsNotAvailableException("Food details not available");
                }
        );
    }

    @Override
    public Float getKcalConsumed(String barcode, float quantity) {
        CircuitBreaker kcalConsumed = circuitBreakerFactory.create("kcalConsumed");
        LOGGER.info("Calling food details integrator BE with endpoint: " + kcalConsumedEndpoint);
        return kcalConsumed.run(
                () -> restTemplate.getForObject(kcalConsumedEndpoint, Float.class, barcode, quantity),
                throwable -> {throw new FoodDetailsNotAvailableException("Food details not available");}
        );
    }

}
