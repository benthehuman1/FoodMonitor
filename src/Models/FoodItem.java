package Models;

import java.util.UUID;

public class FoodItem {
	
	private UUID id;
	private String name;
	private double calories;
	private double fatGrams;
	private double carboHydrateGrams;
	private double fiberGrams;
	private double proteinGrams;
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
