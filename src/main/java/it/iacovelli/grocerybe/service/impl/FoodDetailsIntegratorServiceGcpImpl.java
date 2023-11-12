package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.utils.GoogleCredentialsUtils;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("gcp")
public class FoodDetailsIntegratorServiceGcpImpl extends FoodDetailsIntegratorServiceAbstr {

    private final GoogleCredentialsUtils googleCredentialsUtils;

    protected FoodDetailsIntegratorServiceGcpImpl(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory, GoogleCredentialsUtils googleCredentialsUtils) {
        super(restTemplate, circuitBreakerFactory);
        this.googleCredentialsUtils = googleCredentialsUtils;
    }

    @Override
    public FoodDetailDto getFoodDetails(String barcode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(googleCredentialsUtils.getAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return super.getFoodDetailsApi(barcode, request);
    }

    @Override
    public Float getKcalConsumed(String barcode, float quantity) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(googleCredentialsUtils.getAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);


        return super.getKcalConsumedApi(barcode, quantity, request);
    }





}
