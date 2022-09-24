package it.iacovelli.grocerybe.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FoodDetailDto{
	private NutrimentsDto nutriments;
	private String quantity;
	@JsonProperty("image_url")
	private String imageUrl;
	@JsonProperty("generic_name")
	private String genericName;
	@JsonProperty("image_nutrition_url")
	private String imageNutritionUrl;
}