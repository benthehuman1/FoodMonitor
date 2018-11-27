package Repositories;

import java.util.ArrayList;

import Models.FoodItem;

public class FoodListRepository {
	ArrayList<FoodItem> data;
	
	
	public FoodListRepository(String filePath){
		//parses the file specified by filePath //populates this.data with the data parsed.
		//Use some csv library, IDC
	}
	
	
	ArrayList<FoodItem> getAllFoodItems(){ 
		//Returns all FoodItems;
		//TODO: IMPLEMENT
		
		return new ArrayList<FoodItem>();
	}
}
