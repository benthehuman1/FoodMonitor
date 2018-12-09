package Models;

import java.util.ArrayList;

public class MealList {
	ArrayList<Meal> meals;
	String foodFileName;

	public MealList(String foodFileName) {
		this.foodFileName = foodFileName;
	}
	
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
