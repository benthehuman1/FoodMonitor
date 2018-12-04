package Services;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import Models.*;
import Repositories.MealListRepository;

public class MealListService {
	private MealList meals;
	
	//There will be a list of meals associated with each FoodList, to ensure that we don’t have meals with foods from a different foods list loaded.
	private String foodListFilePath;
	
	private MealListRepository mealListRepository;
	
	
	public MealListService(String foodListFilePath) {
		this.mealListRepository =  new MealListRepository();
		this.meals = this.mealListRepository.GetForFoodListPath(foodListFilePath);
		this.foodListFilePath = foodListFilePath;
	}
		
	
	public void UpdateToNewDataFile(String foodListFilePath){
		this.meals = this.mealListRepository.GetForFoodListPath(foodListFilePath);
		this.foodListFilePath = foodListFilePath;
		
	}
	
	public ArrayList<Meal> GetAllMeals(){
		return this.meals.getMeals();
	}
	
	public MealViewModel getMealViewModelForMealID(UUID mealID) {
		FoodListService foodListService = new FoodListService();
		foodListService.SwitchToNewDataFile(foodListFilePath);
		
		
		
		Meal targetMeal = this.meals.getMeals().stream().filter(meal -> meal.getID().equals(mealID)).findFirst().get();
		ArrayList<UUID> foodIds = (ArrayList<UUID>) targetMeal.getMealItems().stream().map(mealItem -> mealItem.getFood()).collect(Collectors.toList());
		ArrayList<FoodItem> mealFoods = foodListService.getFoodsForFoodIds(foodIds);
		
		return new MealViewModel(targetMeal, mealFoods);
	}
}
