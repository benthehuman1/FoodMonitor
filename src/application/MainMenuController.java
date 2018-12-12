package application;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import Models.*;
import Services.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Creates and Manages the UI elements in the main menu, and hooks them up with backend code
 * @author A-Team 71.
 */
public class MainMenuController {
	
	//Logical Members
	private static final String DEFAULT_DATASET_PATH = "./Data/FoodDataSets/defaultFoodData.csv"; //The path to the default foodDataFile, reletive to the projct root.
	private Main main; //Reference to the Main runner class that launched this class.
	private String currentFoodDataFile; //Reference to the dataFile that the current displayed food is from.
	private FoodListItem currentFoodItem; //Reference to the food that the user currently has selected.
	
	FoodListService foodService; //The Foodservice class that handles logic and data operations for FoodItems
	MealListService mealService; //The MealSerice class that handles logic and data operations for Meals.
	
	private MealViewModel currentViewedMeal; //The ViewModel that contains data for the currently selected/ analyzed meal.
	
	//Graphical Members
	private static final Tooltip quantityToolTip = new Tooltip("Press enter to save / refresh quantities."); //Tooltip for editing the quantity of food items in a meal.
	private static final Tooltip dailyValueToolTip = new Tooltip("USDA Recomended daily value."); //Tooltip explaining the meaning/source of the daily value totals in the meal analisis.
	private Label dataSetNameLabel; //Label showing what foodDataFile is being used.
	private ListView<FoodListItem> foodList; //The list of food on the left-hand side of the main menu.
	private ListView<MealListItem> mealList; //The list of meals on the right-hand side of the main menu.
	private Label foodCountLabel; //A label with the number of fod items currently displayed in the foodlist.
	
		//Meal DetailSection
		private VBox mealDetailsSection; //The VBox caintinging the information/ analyisis of the currently selected meal.
		private Label selectedMeal_Name; //A Label displaying the name of the currently displayed meal;
		private VBox selectedMeal_NutrientBarContainer;// A VBox with each of the "Nutrient bars, showing the nutrient usage for the currently displayed meal.
		private GridPane nutritionTable; //A Table with nutrient data for each FoodItem in the currently selected meal.
	
	/**
	 * A class used to pair the name of a food with it's ID
	 * @author A-Team 71.
	 */
	class FoodListItem{
		private String foodName;
		private UUID ID;
		
		public FoodListItem(FoodDataItem food) {
			this.foodName = food.getName();
			this.ID = food.getId();
		}
		
		public String toString() { return this.foodName; }//This is what's being displayed in the list.
		public UUID getFoodID() { return this.ID;}
	}
	
	/**
	 * A class used to pair the name of a meal with it's ID
	 * @author A-Team 71.
	 */
	class MealListItem{
		private String mealName;
		private UUID ID;
		
		public MealListItem(Meal meal) {
			this.mealName = meal.getName();
			this.ID = meal.getID();
		}
		public String toString() { return this.mealName; }//This is what's being displayed in the list.
		public UUID getMealID() { return this.ID;}
	}
	
	//-------------------------------------- Graphics Setup / Generators --------------------------------------//
	
	/**
	 * Creates / Sets up the graphical elements of the page. 
	 * This method is to be called in the constructor, so when you need access to 
	 * graphical elements, feel free to add an instance variable, and assign it in the method below.
	 * @param pageRoot
	 */
	public void initializeElements_Main(BorderPane pageRoot) {
		pageRoot.setPadding(new Insets(10.0));	
		
		HBox pageHeader = new HBox();
		pageHeader.setPrefHeight(35);
		
		Button loadNewDataSetButton = new Button();
		loadNewDataSetButton.setText("Load Data Set");
		
		loadNewDataSetButton.setOnAction(e -> loadDataFile());
		
		this.dataSetNameLabel = new Label();
		this.dataSetNameLabel.setText("defaultFoodData.csv");
		
		pageHeader.getChildren().addAll(loadNewDataSetButton, this.dataSetNameLabel);
		
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
			
			//Lambda expression of the search eventHandler
			searchButton.setOnAction(T -> {
				
				int i = 0;
				FoodQuery query = new FoodQuery();
				
				for(Node n : searchOptionsVBox.getChildren()) {
					Comparator c = null;
					Nutrient nut = null;
					double value = -1;
					
					if(n instanceof HBox) {
						for(Node m : ((HBox) n).getChildren()) {		
					
							if(m instanceof CheckBox) {
								if(!((CheckBox) m).isSelected()) {
									break;
								}
								nut = Nutrient.values()[i];
							}
							
							if(m instanceof ComboBox<?>) {
								String s = ((ComboBox<String>) m).getValue();
								
								if(s == null) {
									break;
								}
								if(s.equals("<=")) {
									c = Comparator.LESSTHAN;
								}
								else if(s.equals(">=")) {
									c = Comparator.GREATERTHAN;
								}
								else {
									c = Comparator.EQUALTO;
								}
							}
							
							if(m instanceof TextField) {
								try {
									value = Double.parseDouble(((TextField) m).getText());
								}
								catch(Exception e) {
									value = -1;
								}
							}
							if(c != null && nut != null && value >= 0) { 
								FoodQueryRule f = new FoodQueryRule();
								f.setComparator(c);
								f.setNutrient(nut);
								f.setValue(value);
								query.getRules().add(f);
							}
							
						}
						i++;
					}
					
				}
				
				query.setSearchTarget(searchBar.getText());
				
				ArrayList<FoodListItem> searchedFood = (ArrayList<FoodListItem>) this.foodService.Query(query)
						.stream()
						.map(foodItem -> new FoodListItem(foodItem))
						.collect(Collectors.toList());
				this.foodList.getItems().clear();
				this.foodList.getItems().addAll(searchedFood);
				
				this.foodCountLabel.setText("Food Item Count:" + this.foodList.getItems().size() + " Items");
				
			});
			
			searchFieldHBox.getChildren().add(searchBar);
			searchFieldHBox.getChildren().add(searchButton);
			
			foodSearchSectionVBox.getChildren().add(searchLabel);
			foodSearchSectionVBox.getChildren().add(searchOptionsVBox);
			foodSearchSectionVBox.getChildren().add(searchFieldHBox);
			foodSearchSection.getChildren().add(foodSearchSectionVBox);
			
		//Setup List View: 100% Not final
		this.foodList = new ListView<FoodListItem>();
		this.foodList.setPrefHeight(475);
		this.foodList.setPrefWidth(250);
		
		this.foodList.setOnMouseClicked(e -> pressFoodItem(this.foodList.getSelectionModel().getSelectedItem()));
			
		
		//SelectedItem - We can get the ID out of this. 
		FoodListItem sData = this.foodList.getSelectionModel().getSelectedItem();
		
		
		Button newFoodButton = new Button();
		newFoodButton.setPrefWidth(250);
		newFoodButton.setText("+ New Food");
		newFoodButton.setOnAction(e -> pressAddFood());
		
		this.foodCountLabel = new Label();
		
		foodSection.getChildren().add(foodSearchSection);
		foodSection.getChildren().add(this.foodList);
		foodSection.getChildren().add(newFoodButton);
		foodSection.getChildren().add(foodCountLabel);
		//END foodSection 
		
		//Setup Meal Details Section
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();
		this.mealDetailsSection = new VBox();
		this.mealDetailsSection.setAlignment(Pos.BASELINE_CENTER);
		this.mealDetailsSection.setPadding(new Insets(20));
			
			// Setup mealRefresh_MealName
			HBox mealRefresh_MealName = new HBox();
			mealRefresh_MealName.setAlignment(Pos.CENTER);
			mealRefresh_MealName.setPrefHeight(100);
		
		
			this.selectedMeal_Name = new Label();
			this.selectedMeal_Name.setFont(new Font("System", 60));
			this.selectedMeal_Name.setAlignment(Pos.CENTER);
			
			mealRefresh_MealName.getChildren().add(this.selectedMeal_Name);
			
			//Setup nutritionBars
			this.selectedMeal_NutrientBarContainer = new VBox();
			this.selectedMeal_NutrientBarContainer.setAlignment(Pos.CENTER);
			this.selectedMeal_NutrientBarContainer.setPrefWidth(700);
			this.selectedMeal_NutrientBarContainer.setPrefHeight(200);
			this.selectedMeal_NutrientBarContainer.setMinHeight(200);
			
			//Setup foodNutritionTable
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setPrefWidth(715);
			scrollPane.setMaxWidth(715);
			//scrollPane.setMaxHeight(330);
			
			
			this.nutritionTable = new GridPane();
			this.nutritionTable.setPrefWidth(700);
			
			scrollPane.setContent(nutritionTable);
			
			//Setup addFoodToMealButton
			Button addFoodToMealButton = new Button("+ Add Currently Selected Food to Meal");
			addFoodToMealButton.setPrefWidth(700);
			addFoodToMealButton.setPrefHeight(30);
			addFoodToMealButton.setOnAction(e -> pressAddSelectedFoodToMeal());
			
		
		this.mealDetailsSection.getChildren().add(mealRefresh_MealName);
		this.mealDetailsSection.getChildren().add(this.selectedMeal_NutrientBarContainer);
		this.mealDetailsSection.getChildren().add(getFoodNutritionTableHeading());
		this.mealDetailsSection.getChildren().add(scrollPane);
		this.mealDetailsSection.getChildren().add(addFoodToMealButton);
		//END MealDetailsSection
		
		//Setup Meal SelectionSection
		VBox mealSelectionSection = new VBox();
		mealSelectionSection.setPrefWidth(250);
			
			Label mealsLabel = new Label("Meals");
			mealsLabel.setAlignment(Pos.CENTER);
			mealsLabel.setFont(new Font("System", 20));
			
			this.mealList = new ListView<MealListItem>();
			this.mealList.setPrefHeight(640);
			this.mealList.setPrefWidth(250);
			
			this.mealList.setOnMouseClicked(e -> pressMeal(this.mealList.getSelectionModel().getSelectedItem()));
			
			Button newMealButton = new Button();
			newMealButton.setPrefWidth(250);
			newMealButton.setText("+ New Meal");
			newMealButton.setOnAction(e -> pressAddMeal());
			
		mealSelectionSection.getChildren().add(mealsLabel);
		mealSelectionSection.getChildren().add(this.mealList);
		mealSelectionSection.getChildren().add(newMealButton);
		

		pageRoot.setTop(pageHeader);
		pageRoot.setLeft(foodSection);
		pageRoot.setCenter(mealDetailsSection);
		pageRoot.setRight(mealSelectionSection);
	}
	
	/**
	 * Generates a row in the nutrition table, given information about that food.
	 * @param food
	 * @param quantity
	 * @param calories
	 * @param fatGrams
	 * @param carbGrams
	 * @param fiberGrams
	 * @param proteinGrams
	 * @param foodID
	 * @return
	 */
	private GridPane getFoodNutritionTableRow(String food, int quantity, double calories, double fatGrams, double carbGrams, double fiberGrams, double proteinGrams, UUID foodID) {
		GridPane row = new GridPane();
		row.setPrefWidth(700);
		row.setUserData(foodID);
		
		row.setGridLinesVisible(true);
		row.setPadding(new Insets(5));
		
		Label foodLabel = new Label(food);
		foodLabel.setPrefWidth(150);
		foodLabel.setAlignment(Pos.CENTER);
		
		TextField quantitiy = new TextField();
		quantitiy.setTooltip(quantityToolTip);
		quantitiy.setPrefWidth(90);
		quantitiy.setText("" + quantity);
		quantitiy.textProperty().addListener((obs, oldText, newText) ->  this.main.removeNonNumericCharachters(newText, quantitiy));
		quantitiy.setOnAction(e ->	refreshMealDetailsSection());
		
		Label caloriesLabel = new Label("" + calories);
		caloriesLabel.setAlignment(Pos.CENTER);
		caloriesLabel.setPrefWidth(90);
		
		Label fatGramsLabel = new Label("" + fatGrams);
		fatGramsLabel.setPrefWidth(90);
		fatGramsLabel.setAlignment(Pos.CENTER);
		
		Label carbGramsLabel = new Label("" + carbGrams);
		carbGramsLabel.setPrefWidth(90);
		carbGramsLabel.setAlignment(Pos.CENTER);
		
		Label fiberGramsLabel = new Label("" + fiberGrams);
		fiberGramsLabel.setPrefWidth(90);
		fiberGramsLabel.setAlignment(Pos.CENTER);
		
		Label proteinGramsLabel = new Label("" + proteinGrams);
		proteinGramsLabel.setPrefWidth(90);
		proteinGramsLabel.setAlignment(Pos.CENTER);
		
		Button removeFoodButton = new Button();
		removeFoodButton.setPrefWidth(10);
		removeFoodButton.setText("X");
		removeFoodButton.setOnAction(e -> pressRemoveFoodFromMeal(foodID));
		
		row.addColumn(0, foodLabel);
		row.addColumn(1, quantitiy);
		row.addColumn(2, caloriesLabel);
		row.addColumn(3, fatGramsLabel);
		row.addColumn(4, carbGramsLabel);
		row.addColumn(5, fiberGramsLabel);
		row.addColumn(6, proteinGramsLabel);
		row.addColumn(7, removeFoodButton);
		return row;
	}
	
	/**
	 * Generates a GridPane with heading information for each column of the nutrition table.
	 */
	private GridPane getFoodNutritionTableHeading() {
		GridPane row = new GridPane();
		
		row.setAlignment(Pos.CENTER);
		row.setPrefWidth(700);
		row.setPrefHeight(40);
		//row.setPadding(value);
		
		row.setGridLinesVisible(false);
		row.setPadding(new Insets(5));
		
		Label foodLabel = new Label("FoodName");
		foodLabel.setPrefWidth(150);
		
		HBox quantityLabels = new HBox();
		quantityLabels.setPrefWidth(85);
		
			Label quantitiy = new Label("Quantity");
		
			Label quantitiyTip = new Label(" ? ");
			quantitiyTip.setTooltip(quantityToolTip);
			quantitiyTip.setTextFill(javafx.scene.paint.Color.BLUE);
			
		quantityLabels.getChildren().addAll(quantitiy, quantitiyTip);
		
		Label caloriesLabel = new Label("Calories");
		caloriesLabel.setPrefWidth(84);
		
		Label fatGramsLabel = new Label("Fat Grams");
		fatGramsLabel.setPrefWidth(84);
		
		Label carbGramsLabel = new Label("Carb Grams");
		carbGramsLabel.setPrefWidth(84);
		
		Label fiberGramsLabel = new Label("Fiber Grams");
		fiberGramsLabel.setPrefWidth(85);
		
		Label proteinGramsLabel = new Label("Protein Grams");
		proteinGramsLabel.setPrefWidth(90);
		
		Label empty = new Label(" ");
		empty.setPrefWidth(10);
		
		row.addColumn(0, foodLabel);
		row.addColumn(1, quantityLabels);
		row.addColumn(2, caloriesLabel);
		row.addColumn(3, fatGramsLabel);
		row.addColumn(4, carbGramsLabel);
		row.addColumn(5, fiberGramsLabel);
		row.addColumn(6, proteinGramsLabel);
		row.addColumn(7, empty);
		return row;
	}
	
	/**
	 * Generates an HBox with information about the amount of a nutrient in a meal
	 * @param nutrient
	 * @param value
	 * @param barVal
	 * @param dailyValue
	 * @return
	 */
	private HBox getMealNutritionBar(String nutrient, double value, double barVal, String dailyValue) {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setPrefWidth(700);
		hBox.setPrefHeight(35);
		
		Label nutrientLabel = new Label();
		nutrientLabel.setText(nutrient);
		nutrientLabel.setPrefWidth(150);
		
		Label nutrientValue = new Label();
		nutrientValue.setText("" + value);
		nutrientValue.setPrefWidth(60);
		nutrientValue.setMinWidth(60);
		
		ProgressBar bar = new ProgressBar();
		bar.setPrefHeight(30);
		bar.setPrefWidth(400);
		bar.setProgress(barVal);
		
		Label dailyValueLabel = new Label(dailyValue);
		dailyValueLabel.setPrefWidth(70);
		dailyValueLabel.setMinWidth(70);
		
		hBox.getChildren().setAll(nutrientLabel, nutrientValue, bar, dailyValueLabel);
		
		return hBox;
	}
	
	/**
	 * Generates an HBox with heading information for each of the nutrition bars for a Meal.
	 * @return
	 */
	private HBox getMealNutritionBarHeading() {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setPrefWidth(700);
		hBox.setPrefHeight(35);
		
		Label nutrientLabel = new Label();
		nutrientLabel.setText("Nutrient");
		nutrientLabel.setPrefWidth(150);
		
		Label nutrientValueLabel = new Label();
		nutrientValueLabel.setText("Value:");
		nutrientValueLabel.setPrefWidth(60);
		nutrientValueLabel.setMinWidth(60);
		
		Label barLabel = new Label("Proportion of recomended values.");
		barLabel.setPrefHeight(30);
		barLabel.setPrefWidth(400);
		
		Label dailyValueLabel = new Label("DV");
		dailyValueLabel.setPrefWidth(50);
		dailyValueLabel.setMinWidth(50);
		
		Label dailyValueQuestion = new Label("?");
		dailyValueQuestion.setTextFill(javafx.scene.paint.Color.BLUE);
		dailyValueQuestion.setTooltip(dailyValueToolTip);
		dailyValueQuestion.setPrefWidth(20);
		dailyValueQuestion.setMinWidth(20);

		hBox.getChildren().setAll(nutrientLabel, nutrientValueLabel, barLabel, dailyValueLabel, dailyValueQuestion);
		
		return hBox;
	}
	
	/**
	 * Generates an Hbox with controls used to filter the foodlist by a certain nutrient. 
	 * @param nutrient
	 * @return
	 */
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
		value.textProperty().addListener((obs, oldText, newText) ->  this.main.removeNonNumericCharachters(newText, value));
		
		hBox.getChildren().add(applyRuleCheck);
		hBox.getChildren().add(comparator);
		hBox.getChildren().add(value);
		
		return hBox;
	}
	
	//-------------------------------------- Event Handlers --------------------------------------//
	
	/**
	 * Called when the user clicks on a meal from the MealList.
	 * @param meal
	 */
	public void pressMeal(MealListItem meal) {
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(meal.ID);
		this.LoadMealInfoSection(firstMealViewModel);
		
	}
	
	/**
	 * Called when the user clicks on a food from the foodList.
	 * @param foodItem
	 */
	public void pressFoodItem(FoodListItem foodItem) {
		this.currentFoodItem = foodItem;
	}
	
	/**
	 * Called when the user clicks to add a new food Item.
	 */
	public void pressAddFood() {
		main.showAddFoodItemStage();
	}
	
	/**
	 * Called when the user clicks to add a new mealItem.
	 */
	public void pressAddMeal() {
		main.showAddMealStage();
	}
	
	/**
	 * Called when the user clicks to add the selected food item to the currently viewed meal.
	 */
	public void pressAddSelectedFoodToMeal() {
		refreshMealDetailsSection();
		
		FoodListItem foodListItem = this.foodList.getSelectionModel().getSelectedItem();
		MealViewModel mealViewModel = this.currentViewedMeal;
		
		if(foodListItem == null) { return; }
		
		this.mealService.addFoodToMeal(mealViewModel.getID(), foodListItem.ID);
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(mealViewModel.getID());
		this.LoadMealInfoSection(firstMealViewModel);
	}
	
	/**
	 * Called when the user clicks to remove a food item from a meal.
	 * @param foodID
	 */
	public void pressRemoveFoodFromMeal(UUID foodID) {
		this.mealService.removeFoodFromMeal(this.currentViewedMeal.getID(), foodID);
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(this.currentViewedMeal.getID());
		this.LoadMealInfoSection(firstMealViewModel);
	}
	

	//-------------------------------------- Logical Methods --------------------------------------//
	
	
	public MainMenuController(BorderPane pageRoot, Main main){
		this.main = main;
		//Creates all the elements of the mainMenu.
		initializeElements_Main(pageRoot);
		this.foodService = new FoodListService();
		this.foodService.SwitchToNewDataFile(DEFAULT_DATASET_PATH);
		this.mealService = new MealListService(DEFAULT_DATASET_PATH);
		
		this.currentFoodDataFile = DEFAULT_DATASET_PATH;
		
		LoadDefault();
	}
	
	/**
	 * Runs/ Refreshes the meal analysis of the currently selected meal. 
	 */
	public void refreshMealDetailsSection() {
		MealViewModel mealViewModel = this.currentViewedMeal;
		
		//Create and populate a map between the the ID of a food in the meal and it's displayed/specified quantity.
		HashMap<UUID, Integer> quantityMap = new HashMap<UUID, Integer>();
		for(int i = 0; i < this.nutritionTable.getChildren().size(); i++) {
			GridPane actualRow = (GridPane) this.nutritionTable.getChildren().get(i);
			UUID foodID = (UUID) actualRow.getUserData();
			TextField quantityInput = (TextField) actualRow.getChildren().get(2);
			Integer quantity = new Integer((int) Main.getDoubleValueFromLabel(quantityInput));
			quantityMap.put(foodID, quantity);
		}
		
		this.mealService.updateMealItemQuantities(mealViewModel.getID(), quantityMap);
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(mealViewModel.getID());
		this.LoadMealInfoSection(firstMealViewModel);
	}
	
	/**
	 * Adds a new Meal with the name "mealName" to the mealList and to the DataFile. Called from Main.
	 * @param mealName
	 */
	public void addMeal(String mealName) {
		Meal result = new Meal();
		result.setMealItems(new ArrayList<MealItem>());
		result.setName(mealName);
		result.setID(UUID.randomUUID());
		
		this.mealService.addMeal(result);
		
		this.mealList.getItems().add(new MealListItem(result));
		if(this.mealList.getItems().size() == 1) {
			this.LoadDefault();
		}
	}
	
	/**
	 * Adds a new FoodDataItem to the foodList and to the DataFile. Called from Main.
	 * @param foodItem
	 */
	public void addFoodItem(FoodDataItem foodItem) {
		FoodListItem item = new FoodListItem(foodItem);
		this.foodList.getItems().add(item);
		this.foodCountLabel.setText("Food Item Count:" + this.foodList.getItems().size() + " Items");
		this.foodService.addFoodItem(foodItem);
	}
	
	/**
	 * Opens a file open dialog, loads the selected food data file into memory, and displays the food items of that file on the foodList.
	 */
	public void loadDataFile() {
		//Get the opened file.
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(this.main.getMainStage());
		if(selectedFile == null) { return; }
		
		this.dataSetNameLabel.setText(selectedFile.getName());
		
		if(selectedFile.getPath().contains("defaultFoodData.csv")) {
			LoadNewFoodFile(DEFAULT_DATASET_PATH);
			return;
		}
		this.currentFoodDataFile = selectedFile.getPath();
		
		LoadNewFoodFile(selectedFile.getPath());
	}
	
	/**
	 * Handles back-end operations involved with adding in a new DataFile
	 * @param foodDataFilePath
	 */
	public void LoadNewFoodFile(String foodDataFilePath) {
		this.mealService.addNewFoodFile(foodDataFilePath);
		this.foodService.SwitchToNewDataFile(foodDataFilePath);
		
		this.mealService.UpdateToNewDataFile(foodDataFilePath);
		
		LoadDefault();
	}
	/**
	 * /Loads the three sections of content onto the view, including the FoodItems list (All Items, assumes blank query), 
	 * the meal info pane. (Empty), and the meal list, with all meals for that particular datafile.
	 */
	public void LoadDefault(){
		//Pre-Conditions: FoodService and mealService have had their data already loaded in and are ready to be queried.
		this.foodList.getItems().clear();
		this.mealList.getItems().clear();
		this.LoadDefaultFoodItems();
		//If there are no meals assotiated with that the current data file, hide the mealDetailsSection.
		if(this.mealService.GetAllMeals() == null || this.mealService.GetAllMeals().isEmpty()) {
			this.mealDetailsSection.setVisible(false);
		}
		//Setup the mealDetailsSection
		else {
			this.mealDetailsSection.setVisible(true);
			Meal firstMeal = this.mealService.GetAllMeals().get(0);
			MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(firstMeal.getID());
			this.LoadMealInfoSection(firstMealViewModel);
			this.LoadDefaultMealList();
		}
	}
	
	/**
	 * Displays all food items from the data file in the foodList.
	 */
	public void LoadDefaultFoodItems(){
		ArrayList<FoodListItem> listContents = (ArrayList<FoodListItem>) this.foodService.Query(new FoodQuery())
				.stream()
				.map(foodItem -> new FoodListItem(foodItem))
				.collect(Collectors.toList());
		this.foodList.getItems().addAll(listContents);
		this.foodCountLabel.setText("Food Item Count:" + this.foodList.getItems().size() + " Items");
	}
	
	/**
	 * Displays the mealDetailsSection based on the data in a given MealViewModel
	 * @param vm
	 */
	public void LoadMealInfoSection(MealViewModel vm){
		this.currentViewedMeal = vm;
		
		//Sets the MealInfoSection in the view to blank
		this.mealDetailsSection.setDisable(false);
		
		this.selectedMeal_Name.setText(vm.getMealName());
		this.selectedMeal_NutrientBarContainer.getChildren().clear();
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBarHeading());
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Calories", vm.getNutrientInfo().get(Nutrient.CALORIES), vm.getNutrientBarProgress().get(Nutrient.CALORIES), "2000 (cal)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fat Grams", vm.getNutrientInfo().get(Nutrient.FATGRAMS), vm.getNutrientBarProgress().get(Nutrient.FATGRAMS), "65 (g)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Carb Grams", vm.getNutrientInfo().get(Nutrient.CARBGRAMS), vm.getNutrientBarProgress().get(Nutrient.CARBGRAMS), "300 (g)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fiber Grams", vm.getNutrientInfo().get(Nutrient.FIBERGRAMS), vm.getNutrientBarProgress().get(Nutrient.FIBERGRAMS), "25 (g)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Protein Grams", vm.getNutrientInfo().get(Nutrient.PROTEINGRAMS), vm.getNutrientBarProgress().get(Nutrient.PROTEINGRAMS), "50 (g)"));
		
		
		this.nutritionTable.getChildren().clear();
		
		for(int i = 0; i < vm.getFoods().size(); i++) {
			ArrayList<FoodViewModel> foods = vm.getFoods();
			FoodViewModel foodVM = foods.get(i);
			this.nutritionTable.addRow(i, getFoodNutritionTableRow(foodVM.getName(), foodVM.getQuantity(), foodVM.getCalories(), foodVM.getFatGrams(), foodVM.getCarboHydrateGrams(), foodVM.getFiberGrams(), foodVM.getProteinGrams(), foodVM.getID()));
		}
	}
	
	/**
	 * Displays all meals assotiated with the current foodDataFile on the mealList.
	 */
	public void LoadDefaultMealList(){
		ArrayList<MealListItem> mealListContents = (ArrayList<MealListItem>) this.mealService.GetAllMeals()
				.stream()
				.map(meal -> new MealListItem(meal))
				.collect(Collectors.toList());
		this.mealList.getItems().addAll(mealListContents);	
	}
}
