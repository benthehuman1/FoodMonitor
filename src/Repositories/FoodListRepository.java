package Repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import Models.FoodDataItem;

/**
 * The class that handles File I/O for FoodItems
 * @author A-Team 71
 */
public class FoodListRepository {
	
	ArrayList<FoodDataItem> data; //A deserialized representation of the data in the specified filePath
	String filePath; //The path to the File Associated with this repository. 
	
	/**
	 * Deserializes the file specified with filePath, populating this.data/
	 * @param filePath
	 */
	public FoodListRepository(String filePath){
		
		this.filePath = filePath;
		File inputF = new File(filePath);
		
		try {
			
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			boolean isCleanFile = br.lines().findFirst().get().split(",").length == 12;
			
			if(isCleanFile) {
				
				//If its a new file, deserialize it, add a proper UUID to each Food Item, then serialize it again. 
				this.data = (ArrayList<FoodDataItem>) br.lines()
						.filter(Line -> Line.equals(",,,,,,,,,,,") == false)
						.map(Line -> Line.split(","))
						.map(delimitedLine -> generateFoodItemFromString_Clean(delimitedLine))
						.collect(Collectors.toList());
				
				this.data.stream().forEach(item -> item.setId(UUID.randomUUID()));
				this.saveItems();
				
			}
			else
				this.data = (ArrayList<FoodDataItem>) br.lines()
						.filter(Line -> Line.equals(",,,,,,,,,,,") == false)
						.map(Line -> Line.split(","))
						.map(delimitedLine -> generateFoodItemFromString_Dirty(delimitedLine))
						.collect(Collectors.toList());
			
			br.close();
			
		} catch (FileNotFoundException e) { e.printStackTrace();
		} catch (IOException e) {e.printStackTrace(); }
		
	}
	
	/**
	 * Generates a FoodDataItem based on a delimited (by commas) line of the data file.
	 * Assumes that delimited line has a proper UUID that we have added.
	 * @param delimitedLine Line from data file
	 * @return FoodDataItem created from data line
	 */
	private FoodDataItem generateFoodItemFromString_Clean(String[] delimitedLine) {
		
		FoodDataItem foodItem = new FoodDataItem();
		foodItem.setGivenID(delimitedLine[0]);
		foodItem.setName(delimitedLine[1]);
		foodItem.setCalories(Double.parseDouble(delimitedLine[3]));
		foodItem.setFatGrams(Double.parseDouble(delimitedLine[5]));
		foodItem.setCarboHydrateGrams(Double.parseDouble(delimitedLine[7]));
		foodItem.setFiberGrams(Double.parseDouble(delimitedLine[9]));
		foodItem.setProteinGrams(Double.parseDouble(delimitedLine[11]));
		
		return foodItem;
		
	}
	
	/**
	 * Generates a FoodDataItem based on a delimited (by commas) line of the data file.
	 * Assumes that delimited line DOES NOT have a proper UUID that we have added.
	 * @param delimitedLine Line from data file
	 * @return FoodDataItem created from data line
	 */
	private FoodDataItem generateFoodItemFromString_Dirty(String[] delimitedLine) {
		
		FoodDataItem foodItem = new FoodDataItem();
		foodItem.setId(UUID.fromString(delimitedLine[0]));
		foodItem.setGivenID(delimitedLine[1]);
		foodItem.setName(delimitedLine[2]);
		foodItem.setCalories(Double.parseDouble(delimitedLine[4]));
		foodItem.setFatGrams(Double.parseDouble(delimitedLine[6]));
		foodItem.setCarboHydrateGrams(Double.parseDouble(delimitedLine[8]));
		foodItem.setFiberGrams(Double.parseDouble(delimitedLine[10]));
		foodItem.setProteinGrams(Double.parseDouble(delimitedLine[12 ]));
		
		return foodItem;
		
	}
	
	/**
	 * @return All food items that this repository has parsed from the given DataFile.
	 */
	public ArrayList<FoodDataItem> getAllFoodItems(){ 
		return this.data;
	}
	
	/**
	 * Turns a FoodDataItem into a string that can be saved into a csv file.
	 * @param foodItem Food item to convert
	 * @return Processed string
	 */
	private String serializeFoodItem(FoodDataItem foodItem) {
		
		String result = "";
		String ID = foodItem.getId().toString();
		String givenID = foodItem.getGivenID();
		String name = foodItem.getName();
		String calories = "" + foodItem.getCalories();
		String fatGrams = "" + foodItem.getFatGrams();
		String carbGrams = "" + foodItem.getCarboHydrateGrams();
		String fiberGrams = "" + foodItem.getFiberGrams();
		String proteinGrams = "" + foodItem.getProteinGrams();
		
		result += ID + ",";
		result += givenID + ",";
		result += name + ",";
		result += "calories," + calories + ",";
		result += "fat," + fatGrams + ",";
		result += "carbohydrate," + carbGrams + ",";
		result += "fiber," + fiberGrams + ",";
		result += "protein," + proteinGrams;
		
		return result;
		
	}
	
	/**
	 * Re-serializes the data stored by this repository into it's file.
	 */
	public void saveItems() {
		
		ArrayList<String> result = new ArrayList<String>();
		for(FoodDataItem foodItem : this.data) {
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
