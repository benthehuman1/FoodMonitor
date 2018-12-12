package Models;

import java.util.ArrayList;

/**
 * Stores every comparison query and a string search query into one object
 * @author A-Team 71
 */
public class FoodQuery {
	
	private String searchTarget;				//String to search for in food names
	private ArrayList<FoodQueryRule> rules;		//A list of comparison queries
	
	/**
	 * Constructs a new query with both types empty
	 */
	public FoodQuery() {
		this.searchTarget = "";
		this.rules = new ArrayList<FoodQueryRule>();
	}
	
	//Getters and setters
	public String getSearchTarget() {
		return searchTarget;
	}
	public void setSearchTarget(String searchTarget) {
		this.searchTarget = searchTarget;
	}
	public ArrayList<FoodQueryRule> getRules() {
		return rules;
	}
	public void setRules(ArrayList<FoodQueryRule> rules) {
		this.rules = rules;
	}
	
}
