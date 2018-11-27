package Repositories;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.OperationNotSupportedException;

import Models.MealList;

public class MealListRepository {
	HashMap<String, MealList> data;
	
	public MealListRepository() {
		//throw new OperationNotSupportedException("Not implemented");
		//TODO: IMPLEMENT
		//USES SOME JSON Library to parse out the data from Meals.json into this.data. 
		//See Data Readme.txt for structure.
	}
	
	public MealList GetForFoodListPath(String foodListFilePath){ //Returns the meallist for the specified FoodListFilePath
		return new MealList();
		//TODO Implement
	}
}
