package Models;

import java.util.UUID;

/**
 * Stores information about an individual item in a meal
 * @author A-Team 71
 */
public class MealItem {
	
	private UUID foodID;	//Unique ID of the food component
	private int quantity;	//Amount of the component in this meal
	
	//Getters and setters
	public UUID getFood() {
		return foodID;
	}
	public void setFood(UUID food) {
		this.foodID = food;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
