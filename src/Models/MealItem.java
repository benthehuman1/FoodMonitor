package Models;

import java.util.UUID;

public class MealItem {
	private UUID foodID;
	private int quantity;
	
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
