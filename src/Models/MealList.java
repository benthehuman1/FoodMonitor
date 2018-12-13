package Models;

import java.util.ArrayList;

/**
 * Stores info for every meal
 * @author A-Team 71=
 */
public class MealList {
	
	ArrayList<Meal> meals;	//List of every meal
	String foodFileName;	//Name of the data file storing food informations

	/**
	 * Creates a new MealList instance
	 * @param foodFileName Name of database file
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
