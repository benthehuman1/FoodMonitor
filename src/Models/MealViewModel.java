package Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javafx.util.Pair;

public class MealViewModel {
	private String mealName;
	private HashMap<Nutrient, Double> nutrientInfo; //Maping the nutrient to the nutient value totals
	private HashMap<Nutrient, Double> nutrientBarProgress; //Maping the nutrient to the proper value of the nutrient bar
	private ArrayList<FoodViewModel> foods;
	private UUID ID;
	
	
	
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

	public MealViewModel(Meal meal, ArrayList<FoodDataItem> foods) {
		this.ID = meal.getID();
		this.foods = new ArrayList<FoodViewModel>();
		this.nutrientInfo = getZeroedOutNutrientMapper();
		this.nutrientBarProgress = getZeroedOutNutrientMapper();
		this.mealName = meal.getName();
		
		HashMap<UUID, FoodDataItem> foodMapper = new HashMap<UUID, FoodDataItem>();
		foods.stream().forEach(food -> foodMapper.put(food.getId(), food));
		
		for(MealItem item : meal.getMealItems()) {
			this.foods.add(new FoodViewModel(foodMapper.get(item.getFood()), item.getQuantity()));
		}
		
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
	
	private HashMap<Nutrient, Double> getZeroedOutNutrientMapper() {
		HashMap<Nutrient, Double> result = new HashMap<Nutrient, Double>();
		result.put(Nutrient.CALORIES, 0.0);
		result.put(Nutrient.FATGRAMS, 0.0);
		result.put(Nutrient.CARBGRAMS, 0.0);
		result.put(Nutrient.FIBERGRAMS, 0.0);
		result.put(Nutrient.PROTEINGRAMS, 0.0);
		
		return result;
	}
}
