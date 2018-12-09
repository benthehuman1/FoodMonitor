package Repositories;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static java.lang.Math.toIntExact;

import java.awt.event.ItemEvent;

import javax.naming.OperationNotSupportedException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import Models.Meal;
import Models.MealItem;
import Models.MealList;

public class MealListRepository {
	HashMap<String, MealList> data;
	JSONObject obj;
	private static final String MEALS_FILE_PATH = "./Data/Meals.json";
	
	public MealListRepository()   {
		//throw new OperationNotSupportedException("Not implemented");
		//TODO: IMPLEMENT
		//USES SOME JSON Library to parse out the data from Meals.json into this.data. 
		//See Data Readme.txt for structure.
		this.data = new HashMap<String, MealList>();
		
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(MEALS_FILE_PATH));
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
		MealList result = new MealList(foodFileName);
		
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
	
	public void saveDataFile() {
		//GENERATE THE PROPER JSON
		JSONObject rootObject = new JSONObject();
		JSONArray foodFiles_MealList = new JSONArray();
		
		for(String foodFileName : this.data.keySet()) {
			MealList mealList = this.data.get(foodFileName);
			if(mealList.getMeals() == null) {
				foodFiles_MealList.add(getEmptyJSONMealList(foodFileName));
			}
			else {
				foodFiles_MealList.add(getJSONMealList(mealList));
			}
		}
		rootObject.put("FoodDataSetFiles", foodFiles_MealList);
		
		//SAVE THE JSON TO DISK
		BufferedWriter writer;
		try {
			String s1 = rootObject.toString();
			writer = new BufferedWriter(new FileWriter(MEALS_FILE_PATH));
			writer.write(rootObject.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private JSONObject getEmptyJSONMealList(String foodFileName) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("dataSetFileName", foodFileName);
		rootObject.put("meals", new JSONArray());
		
		return rootObject;
		
	}
	
	private JSONObject getJSONMealList(MealList mealList) {
		JSONObject rootObject = new JSONObject();
		
		rootObject.put("dataSetFileName", mealList.getFoodFileName());
		
		JSONArray mealsJson = new JSONArray();
		mealList.getMeals().stream().forEach(meal -> mealsJson.add(getJSONMeal(meal)));
		rootObject.put("meals", mealsJson);
		
		return rootObject;
		
	}

	private JSONObject getJSONMeal(Meal meal) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", meal.getName());
		jsonObject.put("ID", meal.getID().toString());
		
		JSONArray mealItems = new JSONArray();
		for(MealItem item : meal.getMealItems()) {
			JSONObject mealItemJSON = new JSONObject();
			mealItemJSON.put("quantity", item.getQuantity());
			mealItemJSON.put("foodID", item.getFood().toString());
			
			mealItems.add(mealItemJSON);
		}
		
		jsonObject.put("mealItems", mealItems);
		
		return jsonObject;
	}
	
	public void addNewFoodDataFile(String foodFileName) {
		if(foodFileName.contains("defaultFoodData.csv")) {return;}
		this.data.put(foodFileName, new MealList(foodFileName));
	}
	
	public boolean hasDataForFoodFile(String foodFileName) {
		return this.data.containsKey(foodFileName);
	}
	
	public void addNewMeal(Meal meal, String foodDataSetPath) {
		MealList mealList = this.data.get(foodDataSetPath);
		if(mealList.getMeals() == null) { mealList.setMeals(new ArrayList<Meal>());}
		mealList.getMeals().add(meal);
		
		this.saveDataFile();
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
