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
		if(this.mealListRepository.hasDataForFoodFile(foodListFilePath)) {
			this.meals = this.mealListRepository.GetForFoodListPath(foodListFilePath);
		}
		this.foodListFilePath = foodListFilePath;
		
	}
	
	public void addMeal(Meal meal) {
		if(this.meals.getMeals() == null) {this.meals.setMeals(new ArrayList<Meal>());}
		this.meals.getMeals().add(meal);
		this.mealListRepository.saveDataFile();
		//this.mealListRepository.addNewMeal(meal, this.foodListFilePath);
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
	
	public boolean hasAnyDataForFoodFile(String foodDataFilePath) {
		return this.mealListRepository.hasDataForFoodFile(foodDataFilePath);
	}
	
	public void addNewFoodFile(String foodDataFilePath) {
		//this.mealListRepository.addNewFoodDataFile("Nope");
		if(!this.hasAnyDataForFoodFile(foodDataFilePath)) {
			this.mealListRepository.addNewFoodDataFile(foodDataFilePath);
			this.mealListRepository.saveDataFile();
		}
	}
}
