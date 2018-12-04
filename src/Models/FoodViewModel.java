package Models;

import java.util.UUID;

public class FoodViewModel {
	private UUID ID;
	private String name;
	private int quantity;
	
	//These are adjusted for quanity
	private double calories;
	private double fatGrams;
	private double carboHydrateGrams;
	private double fiberGrams;
	private double proteinGrams;
	
	public FoodViewModel(FoodItem food, int quantity) {
		this.ID = food.getId();
		this.name = food.getName();
		this.quantity = quantity;
		
		this.calories = food.getCalories() * (double) quantity;
		this.fatGrams = food.getFatGrams() * (double) quantity;
		this.carboHydrateGrams = food.getCarboHydrateGrams() * (double) quantity;
		this.fiberGrams = food.getFiberGrams() * (double) quantity;
		this.proteinGrams = food.getProteinGrams() * (double) quantity;
	}

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
