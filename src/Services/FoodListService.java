package Services;

import java.awt.event.ItemEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.*;
import Models.*;
import Repositories.FoodListRepository;

public class FoodListService {
	String filePath;
	//This is where ALL the data is stored. We're just querying it.
	BPTree<Double, FoodDataItem> fiber;
	BPTree<Double, FoodDataItem> fat; 
	BPTree<Double, FoodDataItem> carbs; 
	BPTree<Double, FoodDataItem> calories; 
	BPTree<Double, FoodDataItem> protein; 
	private static final int BRANCHINGFACTOR = 3;
	
	ArrayList<FoodDataItem> foodList;
	private FoodListRepository foodListRepository;
	
	
	public void SwitchToNewDataFile(String filePath){
		//Switches the file this data is based on.
		//Calls the repository to get the data from that file //Rebuilds the foodList BPTree with the new data.
		this.foodListRepository = new FoodListRepository(filePath);
		this.foodList = foodListRepository.getAllFoodItems();
		
	}
	
	/**
	 * Given a list of data, constructs a new B+ Tree for every nutrient
	 * @param data List of all food data
	 */
	public void BuildNewFoodBPTree(ArrayList<FoodDataItem> data) {
		
		fiber = new BPTree<Double, FoodDataItem>(BRANCHINGFACTOR);
		fat =  new BPTree<Double, FoodDataItem>(BRANCHINGFACTOR);
		carbs =  new BPTree<Double, FoodDataItem>(BRANCHINGFACTOR);
		calories =  new BPTree<Double, FoodDataItem>(BRANCHINGFACTOR);
		protein =  new BPTree<Double, FoodDataItem>(BRANCHINGFACTOR);
		
		for(FoodDataItem food : data) {
			fiber.insert(food.getFiberGrams(), food);
			fat.insert(food.getFatGrams(), food);
			carbs.insert(food.getCarboHydrateGrams(), food);
			calories.insert(food.getCalories(), food);
			protein.insert(food.getProteinGrams(), food);
		}
		
	}
	
	/**
	 * Runs the give query on the data
	 * @param queryInfo
	 * @return Results of the query
	 */
	public List<FoodDataItem> Query(FoodQuery queryInfo) {
		
		Stream<FoodDataItem> stream = foodList.stream();
		if(queryInfo.getRules() != null) {
			for(FoodQueryRule rule : queryInfo.getRules()) {
				stream = stream.filter(food -> foodItemMatchesFoodQueryRule(food, rule));
			}
		}
		//TODO: What is the point of search target
		if(queryInfo.getSearchTarget() != "") { stream = stream.filter(food -> food.getName().toLowerCase().contains(queryInfo.getSearchTarget().toLowerCase()));}
		return stream.sorted().collect(Collectors.toList());
		
	}
	
	private boolean verifyContains(List<FoodDataItem> ... items) {
		return false;
	}
	
	public ArrayList<FoodDataItem> getFoodsForFoodIds(ArrayList<UUID> foodIDs){
		return (ArrayList<FoodDataItem>) this.foodList.stream()
				.filter(item -> foodIDs.contains(item.getId()))
				.collect(Collectors.toList());
	}
	
	private boolean foodItemMatchesFoodQueryRule(FoodDataItem food, FoodQueryRule rule) {
		if(rule.getNutrient() == Nutrient.CALORIES) { return nutrientSatisfiesQueryRule(food.getCalories(), rule.getComparator(), rule.getValue()); }
		else if(rule.getNutrient() == Nutrient.FATGRAMS) { return nutrientSatisfiesQueryRule(food.getFatGrams(), rule.getComparator(), rule.getValue()); }
		else if(rule.getNutrient() == Nutrient.CARBGRAMS) { return nutrientSatisfiesQueryRule(food.getCarboHydrateGrams(), rule.getComparator(), rule.getValue()); }
		else if(rule.getNutrient() == Nutrient.FIBERGRAMS) { return nutrientSatisfiesQueryRule(food.getFiberGrams(), rule.getComparator(), rule.getValue()); }
		else { return nutrientSatisfiesQueryRule(food.getProteinGrams(), rule.getComparator(), rule.getValue()); }
	}
	
	private boolean nutrientSatisfiesQueryRule(double value, Comparator comp, double target) {
		if(comp == Comparator.EQUALTO) { return value == target; }
		else if(comp == Comparator.GREATERTHAN) { return value > target; }
		else { return value < target; }
	}
	
	/**
	 * Adds the given food item into the database
	 * @param foodItem Item to be added
	 */
	public void addFoodItem(FoodDataItem foodItem) {
		
		fiber.insert(foodItem.getFiberGrams(), foodItem);
		fat.insert(foodItem.getFatGrams(), foodItem);
		carbs.insert(foodItem.getCarboHydrateGrams(), foodItem);
		calories.insert(foodItem.getCalories(), foodItem);
		protein.insert(foodItem.getProteinGrams(), foodItem);
		
		this.foodList.add(foodItem);
		this.foodListRepository.saveItems();
		
	}
	
}
