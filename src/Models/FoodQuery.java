package Models;

import java.util.ArrayList;

public class FoodQuery {
	
	private String searchTarget;
	private ArrayList<FoodQueryRule> rules;
	
	public FoodQuery() {
		this.searchTarget = "";
		this.rules = new ArrayList<FoodQueryRule>();
	}
	
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
