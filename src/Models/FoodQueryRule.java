package Models;

/**
 * Stores all info regarding a query to be performed
 * @author A-Team 71
 */
public class FoodQueryRule {
	
	private Nutrient nutrient;		//Nutrient to based query on
	private Comparator comparator;	//Comparison to query
	private double value;			//Value to base comparison on
	
	//Getters and setters
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
