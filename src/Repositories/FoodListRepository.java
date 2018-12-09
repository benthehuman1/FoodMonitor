package Repositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import Models.FoodItem;
import javafx.scene.shape.Line;

public class FoodListRepository {
	ArrayList<FoodItem> data;
	String filePath;
	
	
	public FoodListRepository(String filePath){
		this.filePath = filePath;
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
		return this.data;
	}

	public void addFoodItem(FoodItem foodItem) {
		this.saveItems();
	}
	
	
	private String serializeFoodItem(FoodItem foodItem) {
		String result = "";
		String ID = foodItem.getId().toString().replaceAll("-", "").substring(0, 24);
		String name = foodItem.getName();
		String calories = "" + foodItem.getCalories();
		String fatGrams = "" + foodItem.getFatGrams();
		String carbGrams = "" + foodItem.getCarboHydrateGrams();
		String fiberGrams = "" + foodItem.getFiberGrams();
		String proteinGrams = "" + foodItem.getProteinGrams();
		
		result += ID + ",";
		result += name + ",";
		result += "calories," + calories + ",";
		result += "fat," + fatGrams + ",";
		result += "carbohydrate," + carbGrams + ",";
		result += "fiber," + fiberGrams + ",";
		result += "protein," + proteinGrams;
		
		return result;
	}
	
	public void saveItems() {
		ArrayList<String> result = new ArrayList<String>();
		for(FoodItem foodItem : this.data) {
			result.add(serializeFoodItem(foodItem));
		}
		
		try {
			FileWriter fileWriter;
			fileWriter = new FileWriter(this.filePath);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			result.stream().forEach(line -> printWriter.print(line + "\n"));

		    printWriter.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
