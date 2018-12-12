package Models;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Stores information about a single meal
 * @author A-Team 71
 */
public class Meal {
	
	private ArrayList<MealItem> mealItems;	//An array of meal items (i.e. meal components)
	private String name;					//Name of the meal
	private UUID ID;						//Unique ID of the meal
	
	//Getters and setters
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
