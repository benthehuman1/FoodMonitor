package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Stores relevant information to display a meal
 * @author A-Team 71
 */
public class MealViewModel {
	
	private String mealName;								//Name of meal
	private HashMap<Nutrient, Double> nutrientInfo; 		//Mapping the nutrient to the nutrient value totals
	private HashMap<Nutrient, Double> nutrientBarProgress; 	//Mapping the nutrient to the proper value of the nutrient bar
	private ArrayList<FoodViewModel> foods;					//List of foods in the meal (only viewModels to display)
	private UUID ID;										//ID of the meal

	/**
	 * Creates an instance of MealViewModel with the given parameters
	 * @param meal Meal item
	 * @param foods List of viewModels corresponding to constituent foods of the meal
	 */
	public MealViewModel(Meal meal, ArrayList<FoodDataItem> foods) {
		
		this.ID = meal.getID();
		this.foods = new ArrayList<FoodViewModel>();
		this.nutrientInfo = getZeroedOutNutrientMapper();
		this.nutrientBarProgress = getZeroedOutNutrientMapper();
		this.mealName = meal.getName();
		
		HashMap<UUID, FoodDataItem> foodMapper = new HashMap<UUID, FoodDataItem>();
		foods.stream().forEach(food -> foodMapper.put(food.getId(), food));
		
		for(MealItem item : meal.getMealItems())
			this.foods.add(new FoodViewModel(foodMapper.get(item.getFood()), item.getQuantity()));
		
		for(FoodViewModel vm : this.foods) {
			nutrientInfo.put(Nutrient.CALORIES, nutrientInfo.get(Nutrient.CALORIES) + vm.getCalories());
			nutrientInfo.put(Nutrient.FATGRAMS, nutrientInfo.get(Nutrient.FATGRAMS) + vm.getFatGrams());
			nutrientInfo.put(Nutrient.CARBGRAMS, nutrientInfo.get(Nutrient.CARBGRAMS) + vm.getCarboHydrateGrams());
			nutrientInfo.put(Nutrient.FIBERGRAMS, nutrientInfo.get(Nutrient.FIBERGRAMS) + vm.getFiberGrams());
			nutrientInfo.put(Nutrient.PROTEINGRAMS, nutrientInfo.get(Nutrient.PROTEINGRAMS) + vm.getProteinGrams());
		}
		
		nutrientBarProgress.put(Nutrient.CALORIES, nutrientInfo.get(Nutrient.CALORIES) / 2000);
		nutrientBarProgress.put(Nutrient.FATGRAMS, nutrientInfo.get(Nutrient.FATGRAMS) / 65);
		nutrientBarProgress.put(Nutrient.CARBGRAMS, nutrientInfo.get(Nutrient.CARBGRAMS) / 300);
		nutrientBarProgress.put(Nutrient.FIBERGRAMS, nutrientInfo.get(Nutrient.FIBERGRAMS) / 25);
		nutrientBarProgress.put(Nutrient.PROTEINGRAMS, nutrientInfo.get(Nutrient.PROTEINGRAMS) / 50);
		
	}
	
	/**
	 * @return A HashMap with all nutrients mapped to zero (specifically a double)
	 */
	private HashMap<Nutrient, Double> getZeroedOutNutrientMapper() {
		
		HashMap<Nutrient, Double> result = new HashMap<Nutrient, Double>();
		result.put(Nutrient.CALORIES, 0.0);
		result.put(Nutrient.FATGRAMS, 0.0);
		result.put(Nutrient.CARBGRAMS, 0.0);
		result.put(Nutrient.FIBERGRAMS, 0.0);
		result.put(Nutrient.PROTEINGRAMS, 0.0);
		
		return result;
	}
	
	//Basic getters
	public UUID getID() {
		return ID;
	}
	public String getMealName() {
		return mealName;
	}
	public HashMap<Nutrient, Double> getNutrientInfo() {
		return nutrientInfo;
	}
	public HashMap<Nutrient, Double> getNutrientBarProgress() {
		return nutrientBarProgress;
	}
	public ArrayList<FoodViewModel> getFoods() {
		return foods;
	}
	
}
