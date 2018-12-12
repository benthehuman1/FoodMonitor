package Models;

import java.util.ArrayList;

public class MealList {
	
	ArrayList<Meal> meals;	//List of every meal
	String foodFileName;	//Name of the data file storing food informations

	/**
	 * Single variable constructor
	 * @param foodFileName 
	 */
	public MealList(String foodFileName) {
		this.foodFileName = foodFileName;
	}
	
	//Getters and setters
	public ArrayList<Meal> getMeals() {
		return meals;
	}
	public void setMeals(ArrayList<Meal> meals) {
		this.meals = meals;
	}
	public String getFoodFileName() {
		return foodFileName;
	}
	public void setFoodFileName(String foodFileName) {
		this.foodFileName = foodFileName;
	}
	
}
