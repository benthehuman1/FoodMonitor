package Models;

import java.util.UUID;

/**
 * Stores all data for an individual food item
 * @author A-Team 71
 */
public class FoodDataItem implements Comparable<FoodDataItem> {
	
	private UUID id;					//Unique ID for each food
	private String givenID;				//User inputed ID
	private String name;				//Name of the food item
	
	//Amount of nutrients in the food
	private double calories;
	private double fatGrams;
	private double carboHydrateGrams;
	private double fiberGrams;
	private double proteinGrams;
	
	/**
	 * Compares two food items on the basis of name
	 */
	public int compareTo(FoodDataItem other) {
		return this.name.compareTo(other.name);
	}
	
	//Getters and setters
	public String getGivenID() { 
		return givenID;
	}
	public void setGivenID(String givenID) {
		this.givenID = givenID;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCalories() {
		return calories;
	}
	public void setCalories(double calories) {
		this.calories = calories;
	}
	public double getFatGrams() {
		return fatGrams;
	}
	public void setFatGrams(double fatGrams) {
		this.fatGrams = fatGrams;
	}
	public double getCarboHydrateGrams() {
		return carboHydrateGrams;
	}
	public void setCarboHydrateGrams(double carboHydrateGrams) {
		this.carboHydrateGrams = carboHydrateGrams;
	}
	public double getFiberGrams() {
		return fiberGrams;
	}
	public void setFiberGrams(double fiberGrams) {
		this.fiberGrams = fiberGrams;
	}
	public double getProteinGrams() {
		return proteinGrams;
	}
	public void setProteinGrams(double proteinGrams) {
		this.proteinGrams = proteinGrams;
	}
	
}
