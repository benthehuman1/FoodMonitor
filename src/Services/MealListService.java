package Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import Models.*;
import Repositories.MealListRepository;

/**
 * Handles data processing for meals, and acts as a separator from the UI and File IO.
 * @author A-Team 71
 *
 */
public class MealListService {
	private MealList meals;
	
	//There will be a list of meals associated with each FoodList, to ensure that we don’t have meals with foods from a different foods list loaded.
	private String foodListFilePath;
	
	private MealListRepository mealListRepository; //The MealList repository that handles File IO for this set of meals.
	
	
	public MealListService(String foodListFilePath) {
		this.mealListRepository =  new MealListRepository();
		this.meals = this.mealListRepository.GetForFoodListPath(foodListFilePath);
		this.foodListFilePath = foodListFilePath;
	}
		
	/**
	 * Switches this Service's MealList to the one assotiated with the specified FoodDataFilePath
	 * @param foodListFilePath
	 */
	public void UpdateToNewDataFile(String foodListFilePath){
		if(this.mealListRepository.hasDataForFoodFile(foodListFilePath)) {
			this.meals = this.mealListRepository.GetForFoodListPath(foodListFilePath);
		}
		this.foodListFilePath = foodListFilePath;
		
	}
	
	/**
	 * Adds a new meal to this Services MealList and saves that change to Meals.json
	 * @param meal
	 */
	public void addMeal(Meal meal) {
		if(this.meals.getMeals() == null) {this.meals.setMeals(new ArrayList<Meal>());}
		this.meals.getMeals().add(meal);
		this.mealListRepository.saveDataFile();
	}
	
	/**
	 * Add the specified food to the specified meal. Saves change in Meals.json
	 * @param mealID
	 * @param foodID
	 */
	public void addFoodToMeal(UUID mealID, UUID foodID) {
		MealItem mealItem = new MealItem();
		mealItem.setFood(foodID);
		mealItem.setQuantity(1);
		
		Meal targetMeal = this.meals.getMeals().stream().filter(meal -> meal.getID().equals(mealID)).findFirst().get();
		
		//If the food is already in the meal, increment at quantity instead of adding the food again.
		if(targetMeal.getMealItems().stream().anyMatch(item -> item.getFood().equals(foodID))) {
			MealItem food = targetMeal.getMealItems().stream().filter(item -> item.getFood().equals(foodID)).findFirst().get();
			food.setQuantity(food.getQuantity() + 1);
		}
		else {
			targetMeal.getMealItems().add(mealItem);			
		}
		this.mealListRepository.saveDataFile();
	}
	
	/**
	 * Add the specified food from the specified meal. Saves change in Meals.json
	 * @param mealID
	 * @param foodID
	 */
	public void removeFoodFromMeal(UUID mealID, UUID foodID) {
		Meal targetMeal = this.meals.getMeals().stream().filter(meal -> meal.getID().equals(mealID)).findFirst().get();
		
		if(targetMeal.getMealItems().stream().anyMatch(item -> item.getFood().equals(foodID))) {
			ArrayList<MealItem> newMealItems = (ArrayList<MealItem>) targetMeal.getMealItems().stream()
					.filter(item -> !item.getFood().equals(foodID))
					.collect(Collectors.toList());
			targetMeal.setMealItems(newMealItems);
		}
		
		this.mealListRepository.saveDataFile();
	}
	
	/**
	 * Updates the quatities of the foods as specifies by quantity map in the specified meal.
	 * @param mealID
	 * @param quantityMap: A mapping between the FoodID and the new quanitiy of that food in the specified meal.
	 */
	public void updateMealItemQuantities(UUID mealID, HashMap<UUID, Integer> quantityMap) {
		Meal targetMeal = this.meals.getMeals().stream().filter(meal -> meal.getID().equals(mealID)).findFirst().get();
		for(MealItem mealItem : targetMeal.getMealItems()) {
			int qt = quantityMap.get(mealItem.getFood());
			mealItem.setQuantity(qt);
		}
		
		this.mealListRepository.saveDataFile();
	}
	
	/**
	 * @return All the meals assotiated with the food data file that this MealListService is currently using. 
	 */
	public ArrayList<Meal> GetAllMeals(){
		return this.meals.getMeals();
	}
	
	public MealViewModel getMealViewModelForMealID(UUID mealID) {
		FoodListService foodListService = new FoodListService();
		foodListService.SwitchToNewDataFile(foodListFilePath);
		Meal targetMeal = this.meals.getMeals().stream().filter(meal -> meal.getID().equals(mealID)).findFirst().get();
		ArrayList<UUID> foodIds = (ArrayList<UUID>) targetMeal.getMealItems().stream().map(mealItem -> mealItem.getFood()).collect(Collectors.toList());
		ArrayList<FoodDataItem> mealFoods = foodListService.getFoodsForFoodIds(foodIds);
		
		return new MealViewModel(targetMeal, mealFoods);
	}
	
	/**
	 * 
	 * @param foodDataFilePath
	 * @return true if there are any meals assotiated with the specifiedFoodDataFilePath
	 */
	public boolean hasAnyDataForFoodFile(String foodDataFilePath) {
		return this.mealListRepository.hasDataForFoodFile(foodDataFilePath);
	}
	
	/**
	 * Adds a new FoodFile to Meals.json
	 * @param foodDataFilePath
	 */
	public void addNewFoodFile(String foodDataFilePath) {
		if(!this.hasAnyDataForFoodFile(foodDataFilePath)) {
			this.mealListRepository.addNewFoodDataFile(foodDataFilePath);
			this.mealListRepository.saveDataFile();
		}
	}
}
