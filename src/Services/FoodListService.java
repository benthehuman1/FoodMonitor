package Services;

import java.awt.event.ItemEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Models.*;
import Repositories.FoodListRepository;
public class FoodListService {
	String filePath;
	//BPTree<UUID, FoodItem> foodList; //This is where ALL the data is stored. Were just querying it.
	ArrayList<FoodItem> foodList;
	
	
	
	public void SwitchToNewDataFile(String filePath){
		//Switches the file this data is based on.
		//Calls the repository to get the data from that file //Rebuilds the foodList BPTree with the new data.
		FoodListRepository foodListRepository = new FoodListRepository(filePath);
		this.foodList = foodListRepository.getAllFoodItems();
		
	}
	
	public void BuildNewFoodBPTree(ArrayList<FoodItem> data){
		//Clears the existing BPTree
		//Adds all of the items in data to the BPTree, rebuilding it.
		
		//UPDATE: Do i actually need this?
	}
	
	public ArrayList<FoodItem> Query(FoodQuery queryInfo){
		Stream<FoodItem> stream = foodList.stream();
		if(queryInfo.getRules() != null) {
			for(FoodQueryRule rule : queryInfo.getRules()) {
				stream = stream.filter(food -> foodItemMatchesFoodQueryRule(food, rule));
			}
		}
		if(queryInfo.getSearchTarget() != "") { stream = stream.filter(food -> food.getName().toLowerCase().contains(queryInfo.getSearchTarget().toLowerCase()));}
		
		return (ArrayList<FoodItem>)stream.collect(Collectors.toList());
	}
	
	public ArrayList<FoodItem> getFoodsForFoodIds(ArrayList<UUID> foodIDs){
		return (ArrayList<FoodItem>) this.foodList.stream()
				.filter(item -> foodIDs.contains(item.getId()))
				.collect(Collectors.toList());
	}
	
	private boolean foodItemMatchesFoodQueryRule(FoodItem food, FoodQueryRule rule) {
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
}
