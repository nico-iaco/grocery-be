package it.iacovelli.grocerybe.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NutrimentsDto {
	@JsonProperty("potassium_100g")
	private int potassium100g;
	@JsonProperty("calcium_unit")
	private String calciumUnit;
	@JsonProperty("proteins_unit")
	private String proteinsUnit;
	@JsonProperty("vitamin-a_label")
	private String vitaminALabel;
	@JsonProperty("monounsaturated-fat_100g")
	private int monounsaturatedFat100g;
	@JsonProperty("iron_unit")
	private String ironUnit;
	@JsonProperty("vitamin-c_unit")
	private String vitaminCUnit;
	@JsonProperty("saturated-fat")
	private int saturatedFat;
	@JsonProperty("trans-fat")
	private int transFat;
	private int proteins;
	private int fat;
	@JsonProperty("salt_unit")
	private String saltUnit;
	@JsonProperty("iron_label")
	private String ironLabel;
	@JsonProperty("sugars_unit")
	private String sugarsUnit;
	@JsonProperty("calcium_label")
	private String calciumLabel;
	@JsonProperty("satured-fat_unit")
	private String saturatedFatUnit;
	@JsonProperty("trans-fat_label")
	private String transFatLabel;
	@JsonProperty("nutrition-score-fr")
	private int nutritionScoreFr;
	@JsonProperty("sodium_unit")
	private String sodiumUnit;
	private double carbohydrates;
	private int alcohol;
	@JsonProperty("fiber_unit")
	private String fiberUnit;
	private int fiber;
	private double salt;
	private int sugars;
	private int calcium;
	@JsonProperty("trans-fat_unit")
	private String transFatUnit;
	@JsonProperty("nutrition-score-uk")
	private int nutritionScoreUk;
	@JsonProperty("cholesterol_100g")
	private int cholesterol100g;
	@JsonProperty("energy-kcal_unit")
	private String energyKcalUnit;
	private double sodium;
	@JsonProperty("fat_unit")
	private String fatUnit;
	@JsonProperty("nova-group")
	private int novaGroup;
	@JsonProperty("polyunsaturated-fat_100g")
	private int polyunsaturatedFat100g;
	@JsonProperty("vitamin-a")
	private int vitaminA;
	@JsonProperty("vitamin-c_label")
	private String vitaminCLabel;
	@JsonProperty("vitamin-c")
	private int vitaminC;
	private int iron;
	@JsonProperty("vitamin-a_unit")
	private String vitaminAUnit;
	@JsonProperty("alcohol_unit")
	private String alcoholUnit;
	@JsonProperty("carbohydrates_unit")
	private String carbohydratesUnit;
	@JsonProperty("energy-kcal")
	private double energyKcal;
}