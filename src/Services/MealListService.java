package Services;

import java.util.ArrayList;

import Models.*;

public class MealListService {
	MealList meals;
	
	//There will be a list of meals associated with each FoodList, to ensure that we don’t have meals with foods from a different foods list loaded.
	String FoodListFilePath;
	
	public void setMeals(MealList meals) {
		this.meals = meals;
	}
	public void setFoodListFilePath(String foodListFilePath) {
		FoodListFilePath = foodListFilePath;
	}
	
	
	
	public void UpdateToNewDataFile(String foodListFilePath){
		//Update this.FoodListFilePath
		//Calls the MealListRepo to get the meal lists associated with this FilePath.
		//Updates this.meals based on that call to the repo.
		
		//TODO: IMPLEMENT
		
	}
	public ArrayList<Meal> GetAllMeals(){
		//Returns all meals (for the current FoodListFilePath)
		return new ArrayList<Meal>();
		
		//TODO: IMPLEMENT
	}
}
