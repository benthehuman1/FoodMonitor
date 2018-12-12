package Models;

import java.util.UUID;

/**
 * Stores information for multiple of a single food items, e.g. "5 oranges", to make 
 * it easier to display in a meal
 * @author A-Team 71
 */
public class FoodViewModel {
	
	private UUID ID;		//Unique ID of the food
	private String name;	//Name of the food
	private int quantity;	//Amount of the food
	
	//Amount of nutrients adjusted for quantity
	private double calories;
	private double fatGrams;
	private double carboHydrateGrams;
	private double fiberGrams;
	private double proteinGrams;
	
	/**
	 * Creates an instance of FoodViewModel with the given parameters
	 * @param food Food item
	 * @param quantity Amount of the given food item
	 */
	public FoodViewModel(FoodDataItem food, int quantity) {
		
		this.ID = food.getId();
		this.name = food.getName();
		this.quantity = quantity;
		
		this.calories = food.getCalories() * (double) quantity;
		this.fatGrams = food.getFatGrams() * (double) quantity;
		this.carboHydrateGrams = food.getCarboHydrateGrams() * (double) quantity;
		this.fiberGrams = food.getFiberGrams() * (double) quantity;
		this.proteinGrams = food.getProteinGrams() * (double) quantity;
		
	}

	//Getters and setters
	public UUID getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public int getQuantity() {
		return quantity;
	}
	public double getCalories() {
		return calories;
	}
	public double getFatGrams() {
		return fatGrams;
	}
	public double getCarboHydrateGrams() {
		return carboHydrateGrams;
	}
	public double getFiberGrams() {
		return fiberGrams;
	}
	public double getProteinGrams() {
		return proteinGrams;
	}
	
}
