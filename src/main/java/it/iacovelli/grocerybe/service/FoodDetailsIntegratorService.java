package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.model.dto.FoodDetailDto;

public interface FoodDetailsIntegratorService {

    FoodDetailDto getFoodDetails(String barcode);

    Float getKcalConsumed(String barcode, float quantity);

}
