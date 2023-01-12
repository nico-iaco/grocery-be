package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.service.FoodDetailsIntegratorService;
import it.iacovelli.grocerybe.utils.GoogleCredentialsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
@RequiredArgsConstructor
public class FoodDetailsIntegratorServiceImpl implements FoodDetailsIntegratorService {

    private final RestTemplate restTemplate;

    private final GoogleCredentialsUtils googleCredentialsUtils;

    private final CircuitBreakerFactory circuitBreakerFactory;

    //private static final Log LOGGER = LogFactory.getLog(FoodDetailsIntegratorServiceImpl.class);

    @Value("${grocery-be.external.food-details-integrator-be.details-path}")
    private String foodDetailsEndpoint;

    @Value("${grocery-be.external.food-details-integrator-be.kcal-consumed-path}")
    private String kcalConsumedEndpoint;

    @Override
    public FoodDetailDto getFoodDetails(String barcode) {
        CircuitBreaker foodDetails = circuitBreakerFactory.create("foodDetails");
        log.info("Calling food details integrator BE with endpoint: " + foodDetailsEndpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(googleCredentialsUtils.getAccessToken());

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
        log.info("Calling food details integrator BE with endpoint: " + kcalConsumedEndpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(googleCredentialsUtils.getAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Float> response = kcalConsumed.run(
                () -> restTemplate.exchange(kcalConsumedEndpoint, HttpMethod.GET, request, Float.class, barcode, quantity),
                throwable -> {
                    throw new FoodDetailsNotAvailableException("Food details not available");
                }
        );

        return response.getBody();
    }





}
