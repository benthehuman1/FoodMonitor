package Models;

public class FoodQueryRule {
	
	private Nutrient nutrient;
	private Comparator comparator;
	private double value;
	
	public Nutrient getNutrient() {
		return nutrient;
	}
	public void setNutrient(Nutrient nutrient) {
		this.nutrient = nutrient;
	}
	public Comparator getComparator() {
		return comparator;
	}
	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
