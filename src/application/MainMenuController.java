package application;



import java.awt.Checkbox;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.lang.model.element.VariableElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import Models.FoodItem;
import Models.FoodQuery;
import Models.Meal;
import Models.MealViewModel;
import Models.Nutrient;
import Services.FoodListService;
import Services.MealListService;
/*
import Models.FoodItem;
import Models.Meal;
import Services.FoodListService;
import Services.MealListService;
*/
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenuController {
	
	//Logical Members
	private static final String DEFAULT_DATASET_PATH = "./Data/FoodDataSets/defaultFoodData.csv";
	private Main main;
	
	FoodListService foodService; 
	MealListService mealService;
	
	//Graphical Members
	private Label lbl;
	private ListView<FoodListItem> foodList;
	private ListView<MealListItem> mealList;
	
		//Meal DetailSection
		private Label selectedMeal_Name;
		private VBox selectedMeal_NutrientBarContainer;
	
	
	class FoodListItem{
		private String foodName;
		private UUID ID;
		
		public FoodListItem(FoodItem food) {
			this.foodName = food.getName();
			this.ID = food.getId();
		}
		
		public String toString() {
			return this.foodName;
		}
		
		public UUID getFoodID() {
			return this.ID;
		}
	}
	
	class MealListItem{
		private String mealName;
		private UUID ID;
		
		public MealListItem(Meal meal) {
			this.mealName = meal.getName();
			this.ID = meal.getID();
		}
		
		public String toString() {
			return this.mealName;
		}
		
		public UUID getMealID() {
			return this.ID;
		}
	}
	
	/**
	 * Creates / Sets up the graphical elements of the page. 
	 * NOTE: As of 11/27/2018 other methods have no way to access graphical items.
	 * This method is to be called in the constructor, so when you need access to 
	 * graphical elements, feel free to add an instance variable, and assign it in the method below.
	 * @param pageRoot
	 */
	public void initializeElements_Main(BorderPane pageRoot) {
		//Set pageRoot properties
		pageRoot.setPadding(new Insets(10.0));
		
		//Setup applicationTitleLabel
		Label applicationTitleLabel = new Label();
		applicationTitleLabel.setText("CS-400 Finalz Project");
		applicationTitleLabel.setFont(new Font("System", 18.0));
		pageRoot.setAlignment(applicationTitleLabel, Pos.CENTER);
		
		//Setup foodSection
		VBox foodSection = new VBox();
		foodSection.setPrefWidth(250);
		
			//Setup foodSearchSection
			Pane foodSearchSection = new Pane();
			foodSearchSection.setPrefHeight(210);
			
			VBox foodSearchSectionVBox = new VBox();
			foodSearchSectionVBox.setPrefHeight(210);
			
			Label searchLabel = new Label();
			searchLabel.setText("Search Options");
			searchLabel.setPrefHeight(20);
				
				//Setup search options VBox
				VBox searchOptionsVBox = new VBox();
				searchOptionsVBox.setPrefHeight(100);
				searchOptionsVBox.getChildren().add(getSearchRuleHBox("Calories"));
				searchOptionsVBox.getChildren().add(getSearchRuleHBox("Fat Grams"));
				searchOptionsVBox.getChildren().add(getSearchRuleHBox("Carb Grams"));
				searchOptionsVBox.getChildren().add(getSearchRuleHBox("Fiber Grams"));
				searchOptionsVBox.getChildren().add(getSearchRuleHBox("Protein Grams"));
			
			//Setup searchBar
			HBox searchFieldHBox = new HBox();
			searchFieldHBox.setPrefHeight(30);
			
			TextField searchBar = new TextField();
			searchBar.setPrefWidth(200);
			
			Button searchButton = new Button();
			searchButton.setPrefWidth(50);
			searchButton.setText("GO!");
			
			searchFieldHBox.getChildren().add(searchBar);
			searchFieldHBox.getChildren().add(searchButton);
			
			foodSearchSectionVBox.getChildren().add(searchLabel);
			foodSearchSectionVBox.getChildren().add(searchOptionsVBox);
			foodSearchSectionVBox.getChildren().add(searchFieldHBox);
			foodSearchSection.getChildren().add(foodSearchSectionVBox);
			
		
		ScrollPane foodList = new ScrollPane();
		foodList.setPrefWidth(250);
		foodList.setPrefHeight(490);
			
			//Setup List View: 100% Not final
			this.foodList = new ListView<FoodListItem>();
			this.foodList.setPrefHeight(490);
			
		
			//SelectedItem - We can get the ID out of this. 
			FoodListItem sData = this.foodList.getSelectionModel().getSelectedItem();
		
			foodList.setContent(this.foodList);
		
		
		Button newFoodButton = new Button();
		newFoodButton.setText("+ New Food");
		
		foodSection.getChildren().add(foodSearchSection);
		foodSection.getChildren().add(foodList);
		foodSection.getChildren().add(newFoodButton);
		//END foodSection 
		
		//Setup Meal Details Section
		VBox mealDetailsSection = new VBox();
		mealDetailsSection.setPadding(new Insets(20));
			
			// Setup mealRefresh_MealName
			HBox mealRefresh_MealName = new HBox();
			mealRefresh_MealName.setPrefHeight(100);
		
			Button refreshButton = new Button();
			refreshButton.setText("Refresh");
			refreshButton.setFont(new Font("System", 19));
			refreshButton.setPrefHeight(75);
			refreshButton.setPrefWidth(100);
		
			this.selectedMeal_Name = new Label();
			this.selectedMeal_Name.setFont(new Font("System", 60));
			this.selectedMeal_Name.setAlignment(Pos.CENTER);
			
			mealRefresh_MealName.getChildren().add(refreshButton);
			mealRefresh_MealName.getChildren().add(this.selectedMeal_Name);
			
			//Setup nutritionBars
			this.selectedMeal_NutrientBarContainer = new VBox();
			this.selectedMeal_NutrientBarContainer.setPrefHeight(200);
			//DATA is temporary
			//nutrientBars.getChildren().add(getMealNutritionBar("Calories", 123, 0.6));
			//nutrientBars.getChildren().add(getMealNutritionBar("Fat Grams", 11, 0.4));
			//nutrientBars.getChildren().add(getMealNutritionBar("Carb Grams", 45, 0.9));
			//nutrientBars.getChildren().add(getMealNutritionBar("Fiber Grams", 64, 0.1));
			//nutrientBars.getChildren().add(getMealNutritionBar("Protein Grams", 90, 0.4));
			
			//Setup FoodsLabel
			Label foodLabel = new Label();
			foodLabel.setText("Foods");
			foodLabel.setFont(new Font("System", 13));
			
			//Setup foodNutritionTable
			GridPane nutritionTabe = new GridPane();
			
			nutritionTabe.addRow(0, getFoodNutritionTableRow("Hot Dogs", new int[] {2, 5, 1, 8, 3, 6}));
			nutritionTabe.addRow(1, getFoodNutritionTableRow("Tummy Yummy", new int[] {1, 44, 1, 42, 3, 4}));
			
			//Setup addFoodToMealButton
			Button addFoodToMealButton = new Button("+ Add Currently Selected Food to Meal");
			addFoodToMealButton.setPrefWidth(600);
			addFoodToMealButton.setPrefHeight(30);
			
		
		mealDetailsSection.getChildren().add(mealRefresh_MealName);
		mealDetailsSection.getChildren().add(this.selectedMeal_NutrientBarContainer);
		mealDetailsSection.getChildren().add(foodLabel);
		mealDetailsSection.getChildren().add(nutritionTabe);
		mealDetailsSection.getChildren().add(addFoodToMealButton);
		//END MealDetailsSection
		
		//Setup Meal SelectionSection
		VBox mealSelectionSection = new VBox();
		mealSelectionSection.setPrefWidth(250);
			
			Label mealsLabel = new Label("Meals");
			mealsLabel.setAlignment(Pos.CENTER);
			mealsLabel.setFont(new Font("System", 20));
			
			ScrollPane mealList_Pane = new ScrollPane();
			mealList_Pane.setPrefWidth(250);
			mealList_Pane.setPrefHeight(640);
			
			this.mealList = new ListView<MealListItem>();
			mealList.setPrefHeight(640);
			
			mealList_Pane.setContent(mealList);
			
			Button newMealButton = new Button();
			newMealButton.setPrefWidth(250);
			newMealButton.setText("+ New Meal");
			
		mealSelectionSection.getChildren().add(mealsLabel);
		mealSelectionSection.getChildren().add(mealList_Pane);
		mealSelectionSection.getChildren().add(newMealButton);
		
		
		
		pageRoot.setTop(applicationTitleLabel);
		pageRoot.setLeft(foodSection);
		pageRoot.setCenter(mealDetailsSection);
		pageRoot.setRight(mealSelectionSection);
	}
	
	private GridPane getFoodNutritionTableRow(String food, int[] values/*Length: 6. Temporary*/) {
		GridPane row = new GridPane();//400
		row.setGridLinesVisible(true);
		row.setPadding(new Insets(5));
		
		Label foodLabel = new Label(food);
		foodLabel.setPrefWidth(125);
		foodLabel.setAlignment(Pos.CENTER);
		
		TextField quantitiy = new TextField();
		quantitiy.setPrefWidth(75);
		quantitiy.setText("" + values[0]);
		
		Label calories = new Label("" + values[1]);
		calories.setAlignment(Pos.CENTER);
		calories.setPrefWidth(75);
		
		Label fatGrams = new Label("" + values[2]);
		fatGrams.setPrefWidth(75);
		fatGrams.setAlignment(Pos.CENTER);
		
		Label carbGrams = new Label("" + values[3]);
		carbGrams.setPrefWidth(75);
		carbGrams.setAlignment(Pos.CENTER);
		
		Label fiberGrams = new Label("" + values[4]);
		fiberGrams.setPrefWidth(75);
		fiberGrams.setAlignment(Pos.CENTER);
		
		Label proteinGrams = new Label("" + values[5]);
		proteinGrams.setPrefWidth(75);
		proteinGrams.setAlignment(Pos.CENTER);
		
		row.addColumn(0, foodLabel);
		row.addColumn(1, quantitiy);
		row.addColumn(2, calories);
		row.addColumn(3, fatGrams);
		row.addColumn(4, carbGrams);
		row.addColumn(5, fiberGrams);
		row.addColumn(6, proteinGrams);
		return row;
	}
	
	//TEMPORARY
	private HBox getMealNutritionBar(String nutrient, double value, double barVal) {
		HBox hBox = new HBox();
		hBox.setPrefHeight(35);
		
		Label nutrientLabel = new Label();
		nutrientLabel.setText(nutrient);
		nutrientLabel.setPrefWidth(200);
		
		Label nutrientValue = new Label();
		nutrientValue.setText("" + value);
		nutrientValue.setPrefWidth(50);
		
		ProgressBar bar = new ProgressBar();
		bar.setPrefHeight(30);
		bar.setPrefWidth(300);
		bar.setProgress(barVal);
		
		hBox.getChildren().setAll(nutrientLabel, nutrientValue, bar);
		
		return hBox;
	}
	
	private HBox getSearchRuleHBox(String nutrient) {
		HBox hBox = new HBox();
		hBox.setPrefHeight(20);
		hBox.setPrefWidth(250);
		
		CheckBox applyRuleCheck = new CheckBox();
		applyRuleCheck.setPrefWidth(130);
		applyRuleCheck.setText(nutrient);
		
		ComboBox<String> comparator = new ComboBox<String>();
		comparator.setPrefWidth(70);
		comparator.setPrefHeight(30);
		comparator.getItems().add("");
		comparator.getItems().add("=");
		comparator.getItems().add(">=");
		comparator.getItems().add("<=");
		
		TextField value = new TextField();
		value.setPrefWidth(50);
		value.setFont(new Font("System", 13));
		
		hBox.getChildren().add(applyRuleCheck);
		hBox.getChildren().add(comparator);
		hBox.getChildren().add(value);
		
		return hBox;
	}
	
	
	
	
	
	
	//Here is where the logic begins.
	public MainMenuController(BorderPane pageRoot, Main main){
		this.main = main;
		initializeElements_Main(pageRoot);
		this.foodService = new FoodListService();
		this.foodService.SwitchToNewDataFile(DEFAULT_DATASET_PATH);
		this.mealService = new MealListService(DEFAULT_DATASET_PATH);
		
		LoadDefault();
	}
	
	public void launchNewWindow(String text) {
		System.out.println("WHAT DO?");
		main.showBonusStage(text);
		
	}
	
	
	
	public void LoadDefault(){
		//Loads the three sections of content onto the view, including the FoodItems list (All Items, assumes blank query), the meal info pane. (Empty), and the meal list, with all meals for that particular datafile.
		//Pre-Conditions: foodservice and mealService have had their data already loaded in and are ready to be queried.
		this.LoadDefaultFoodItems();
		Meal firstMeal = this.mealService.GetAllMeals().get(0);
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(firstMeal.getID());
		this.LoadMealInfoSection(firstMealViewModel);
		this.LoadDefaultMealList();
		
	}
	public void LoadDefaultFoodItems(){
		ArrayList<FoodListItem> listContents = (ArrayList<FoodListItem>) this.foodService.Query(new FoodQuery())
				.stream()
				.map(foodItem -> new FoodListItem(foodItem))
				.collect(Collectors.toList());
		this.foodList.getItems().addAll(listContents);
	}
	
	public void LoadMealInfoSection(MealViewModel vm){
		//Sets the MealInfoSection int the view to blank
		this.selectedMeal_Name.setText(vm.getMealName());
		this.selectedMeal_NutrientBarContainer.getChildren().clear();
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Calories", vm.getNutrientInfo().get(Nutrient.CALORIES), vm.getNutrientBarProgress().get(Nutrient.CALORIES)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fat Grams", vm.getNutrientInfo().get(Nutrient.FATGRAMS), vm.getNutrientBarProgress().get(Nutrient.FATGRAMS)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Carb Grams", vm.getNutrientInfo().get(Nutrient.CARBGRAMS), vm.getNutrientBarProgress().get(Nutrient.CARBGRAMS)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fiber Grams", vm.getNutrientInfo().get(Nutrient.FIBERGRAMS), vm.getNutrientBarProgress().get(Nutrient.FIBERGRAMS)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Protein Grams", vm.getNutrientInfo().get(Nutrient.PROTEINGRAMS), vm.getNutrientBarProgress().get(Nutrient.PROTEINGRAMS)));
		
		
	}
	
	public void LoadDefaultMealList(){
		//calls the mealService to get a MealsViewModel containing all meals // MealsViewModel vm = mealService.getAllMeals();
		//Fills the mealsList view with the results from that call
		// this.fillMealList(vm);
		
		ArrayList<MealListItem> mealListContents = (ArrayList<MealListItem>) this.mealService.GetAllMeals()
				.stream()
				.map(meal -> new MealListItem(meal))
				.collect(Collectors.toList());
		this.mealList.getItems().addAll(mealListContents);
		
		
	}
	/*
	public void fillFoodList(ArrayList<FoodItem> vm){
		//fills the foodList view with data from the FoodsViewModel
	}
	
	public void fillMealList(ArrayList<Meal> vm){
		//fills the mealsList view with data from the MealsViewModel
	}
	*/
	
	public void LoadFoodFile(String filePath){
		//Updates the contents of foodService and mealService to reflect the new data //Loads that data into the view.
		// foodService.UpdateToNewDataFile(filePath);
		// mealService.UpdateToNewDataFile(filePath);
		// loadDefault();
	}
	
	
}
