package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("default")
public class FoodDetailsIntegratorServiceDefaultImpl extends FoodDetailsIntegratorServiceAbstr {


    protected FoodDetailsIntegratorServiceDefaultImpl(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        super(restTemplate, circuitBreakerFactory);
    }


    @Override
    public FoodDetailDto getFoodDetails(String barcode) {

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return super.getFoodDetailsApi(barcode, request);
    }

    @Override
    public Float getKcalConsumed(String barcode, float quantity) {

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return super.getKcalConsumedApi(barcode, quantity, request);
    }





}
