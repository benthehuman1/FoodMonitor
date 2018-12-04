package Repositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import Models.FoodItem;
import javafx.scene.shape.Line;

public class FoodListRepository {
	ArrayList<FoodItem> data;
	
	
	public FoodListRepository(String filePath){
		File inputF = new File(filePath);
		try {
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			
			this.data = (ArrayList<FoodItem>) br.lines()
				.filter(Line -> Line.equals(",,,,,,,,,,,") == false)
				.map(Line -> Line.split(","))
				.map(delimitedLine -> generateFoodItemFromString(delimitedLine))
				.collect(Collectors.toList());
			
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		
	}
	
	private FoodItem generateFoodItemFromString(String[] delimitedLine) {
		FoodItem foodItem = new FoodItem();
		foodItem.setId(getProperUUIDFromGivenID(delimitedLine[0]));
		foodItem.setName(delimitedLine[1]);
		foodItem.setCalories(Double.parseDouble(delimitedLine[3]));
		foodItem.setFatGrams(Double.parseDouble(delimitedLine[5]));
		foodItem.setCarboHydrateGrams(Double.parseDouble(delimitedLine[7]));
		foodItem.setFiberGrams(Double.parseDouble(delimitedLine[9]));
		foodItem.setProteinGrams(Double.parseDouble(delimitedLine[11]));
		
		return foodItem;
	}
	
	private UUID getProperUUIDFromGivenID(String given) {
		String formatedGuidString = given.substring(0, 8) + "-" + given.substring(8, 12) + "-" + given.substring(12, 16) + "-" + given.substring(16, 20) + "-" + given.substring(20) + "00000000";
		return UUID.fromString(formatedGuidString);
	}
	
	public ArrayList<FoodItem> getAllFoodItems(){ 
		//Returns all FoodItems;
		//TODO: IMPLEMENT
		
		return this.data;
	}
}
