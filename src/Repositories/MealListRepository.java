package Repositories;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static java.lang.Math.toIntExact;

import javax.naming.OperationNotSupportedException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Models.Meal;
import Models.MealItem;
import Models.MealList;

public class MealListRepository {
	HashMap<String, MealList> data;
	JSONObject obj;
	
	public MealListRepository()   {
		//throw new OperationNotSupportedException("Not implemented");
		//TODO: IMPLEMENT
		//USES SOME JSON Library to parse out the data from Meals.json into this.data. 
		//See Data Readme.txt for structure.
		this.data = new HashMap<String, MealList>();
		
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(new FileReader("./Data/Meals.json"));
			JSONArray foodDataSets = (JSONArray) json.get("FoodDataSetFiles");
			
			for(Object obj : foodDataSets) {
				JSONObject objJson = (JSONObject) obj;
				String dataSetFileName = (String) objJson.get("dataSetFileName");
				MealList mealList = getMealListForJSONData((JSONArray) objJson.get("meals"), dataSetFileName);
				
				this.data.put(dataSetFileName, mealList);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
		}
	}
	
	private MealList getMealListForJSONData(JSONArray meals, String foodFileName) {
		MealList result = new MealList();
		result.setFoodFileName(foodFileName);
		
		ArrayList<Meal> listOfMeals = new ArrayList<Meal>();
		for(Object meal : meals) {
			Meal resultMeal = new Meal();
			JSONObject mealJson = (JSONObject) meal;
			resultMeal.setID(UUID.fromString((String) mealJson.get("ID")));
			resultMeal.setName((String) mealJson.get("name"));
			
			ArrayList<MealItem> mealItems = new ArrayList<MealItem>();
			for(Object mealItem : (JSONArray) mealJson.get("mealItems")) {
				MealItem resultMealItem = new MealItem();
				JSONObject mealItemJson = (JSONObject) mealItem;
				int quantity = toIntExact((Long)mealItemJson.get("quantity"));
				resultMealItem.setQuantity(quantity);
				resultMealItem.setFood(UUID.fromString((String) mealItemJson.get("foodID")));
				
				mealItems.add(resultMealItem);
			}
			resultMeal.setMealItems(mealItems);
			listOfMeals.add(resultMeal);
		}
		
		result.setMeals(meals);
		result.setMeals(listOfMeals);
		return result;
	}
	
	
	public MealList GetForFoodListPath(String foodListFilePath){ //Returns the meallist for the specified FoodListFilePath
		return this.data.get(foodListFilePath);
								
	}
	
	public Meal GetMealForID(String foodListFilePath, UUID mealID) {
		return this.GetForFoodListPath(foodListFilePath)
				.getMeals()
				.stream()
				.filter(meal -> meal.getID().equals(mealID))
				.findFirst().get();
	}
}
