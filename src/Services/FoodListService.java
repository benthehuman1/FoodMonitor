package Services;

import java.util.ArrayList;
import java.util.UUID;

import Models.*;
public class FoodListService {
	String filePath;
	BPTree<UUID, FoodItem> foodList; //This is where ALL the data is stored. Were just querying it.
	
	public void UpdateToNewDataFile(){
		//Switches the file this data is based on.
		//Calls the repository to get the data from that file //Rebuilds the foodList BPTree with the new data.
	}
	
	public void BuildNewFoodBPTree(ArrayList<FoodItem> data){
		//Clears the existing BPTree
		//Adds all of the items in data to the BPTree, rebuilding it.
	}
	
	ArrayList<FoodItem> Query(FoodQuery queryInfo){
		//Queries the BPTree, returning the results from the query as an arrayList of FoodItems
		//TODO: IMPLEMENT
		
		return new ArrayList<FoodItem>();
	}
}
