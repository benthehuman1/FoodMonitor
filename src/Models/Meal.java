package Models;

import java.util.ArrayList;
import java.util.UUID;


public class Meal {
	private ArrayList<MealItem> mealItems;
	private String name;
	private UUID ID;
	
	
	public UUID getID() {
		return ID;
	}

	public void setID(UUID iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MealItem> getMealItems() {
		return mealItems;
	}

	public void setMealItems(ArrayList<MealItem> mealItems) {
		this.mealItems = mealItems;
	}
	
	
}
