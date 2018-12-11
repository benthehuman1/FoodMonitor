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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainMenuController {
	
	//Logical Members
	private static final String DEFAULT_DATASET_PATH = "./Data/FoodDataSets/defaultFoodData.csv";
	private Main main;
	private String currentFoodDataFile;
	private FoodListItem currentFoodItem;
	
	FoodListService foodService; 
	MealListService mealService;
	
	private MealViewModel currentViewedMeal;
	
	//Graphical Members
	private Label dataSetNameLabel;
	private ListView<FoodListItem> foodList;
	private ListView<MealListItem> mealList;
	private Label foodCountLabel;
	
		//Meal DetailSection
		private VBox mealDetailsSection;
		private Label selectedMeal_Name;
		private VBox selectedMeal_NutrientBarContainer;
		private GridPane nutritionTable;
	
	
	class FoodListItem{
		private String foodName;
		private UUID ID;
		
		public FoodListItem(FoodDataItem food) {
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
	 * This method is to be called in the constructor, so when you need access to 
	 * graphical elements, feel free to add an instance variable, and assign it in the method below.
	 * @param pageRoot
	 */
	public void initializeElements_Main(BorderPane pageRoot) {
		//Set pageRoot properties
		pageRoot.setPadding(new Insets(10.0));
		
		//Setup applicationTitleLabel
		Label applicationTitleLabel = new Label();
		//applicationTitleLabel.setText("CS-400 Finalz Project");
		//applicationTitleLabel.setFont(new Font("System", 18.0));
		//pageRoot.setAlignment(applicationTitleLabel, Pos.CENTER);
		
		HBox pageHeader = new HBox();
		pageHeader.setPrefHeight(35);
		
		Button loadNewDataSetButton = new Button();
		loadNewDataSetButton.setText("Load New Data Set");
		
		loadNewDataSetButton.setOnAction(e -> openFileChooser());
		
		
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
		this.mealDetailsSection.setPrefWidth(screen.getMaxX() / 8);
		this.mealDetailsSection.setPadding(new Insets(20));
		this.mealDetailsSection.setTranslateX(screen.getMaxX() / 7 - this.mealDetailsSection.getWidth());
			
			// Setup mealRefresh_MealName
			HBox mealRefresh_MealName = new HBox();
			mealRefresh_MealName.setPrefHeight(100);
		
		
			this.selectedMeal_Name = new Label();
			this.selectedMeal_Name.setFont(new Font("System", 60));
			this.selectedMeal_Name.setAlignment(Pos.CENTER);
			
			mealRefresh_MealName.getChildren().add(this.selectedMeal_Name);
			
			//Setup nutritionBars
			this.selectedMeal_NutrientBarContainer = new VBox();
			this.selectedMeal_NutrientBarContainer.setPrefWidth(700);
			this.selectedMeal_NutrientBarContainer.setPrefHeight(200);
			
			//Setup foodNutritionTable
			this.nutritionTable = new GridPane();
			this.nutritionTable.setPrefWidth(700);
			
			//Setup addFoodToMealButton
			Button addFoodToMealButton = new Button("+ Add Currently Selected Food to Meal");
			addFoodToMealButton.setPrefWidth(700);
			addFoodToMealButton.setPrefHeight(30);
			addFoodToMealButton.setOnAction(e -> {pressRefreshMealDetailsSection();
			pressAddSelectedFoodToMeal();});
			
		
		this.mealDetailsSection.getChildren().add(mealRefresh_MealName);
		this.mealDetailsSection.getChildren().add(this.selectedMeal_NutrientBarContainer);
		this.mealDetailsSection.getChildren().add(this.nutritionTable);
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
		quantitiy.setPrefWidth(90);
		quantitiy.setText("" + quantity);
		
		quantitiy.setOnAction(e ->	pressRefreshMealDetailsSection());
		
		
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
	
	private GridPane getFoodNutritionTableHeading() {
		GridPane row = new GridPane();
		row.setPrefWidth(700);
		row.setPrefHeight(30);
		
		row.setGridLinesVisible(false);
		row.setPadding(new Insets(5));
		
		Label foodLabel = new Label("FoodName");
		foodLabel.setPrefWidth(150);
		
		Label quantitiy = new Label("Quantity");
		quantitiy.setPrefWidth(85);
		
		Label caloriesLabel = new Label("Calories");
		caloriesLabel.setPrefWidth(85);
		
		Label fatGramsLabel = new Label("Fat Grams");
		fatGramsLabel.setPrefWidth(85);
		
		Label carbGramsLabel = new Label("Carb Grams");
		carbGramsLabel.setPrefWidth(85);
		
		Label fiberGramsLabel = new Label("Fiber Grams");
		fiberGramsLabel.setPrefWidth(85);
		
		Label proteinGramsLabel = new Label("Protein Grams");
		proteinGramsLabel.setPrefWidth(90);
		
		Label empty = new Label(" ");
		empty.setPrefWidth(10);
		
		row.addColumn(0, foodLabel);
		row.addColumn(1, quantitiy);
		row.addColumn(2, caloriesLabel);
		row.addColumn(3, fatGramsLabel);
		row.addColumn(4, carbGramsLabel);
		row.addColumn(5, fiberGramsLabel);
		row.addColumn(6, proteinGramsLabel);
		row.addColumn(7, empty);
		return row;
	}
	
	private HBox getMealNutritionBar(String nutrient, double value, double barVal, String dailyValue) {
		HBox hBox = new HBox();
		hBox.setPrefWidth(700);
		hBox.setPrefHeight(35);
		
		Label nutrientLabel = new Label();
		nutrientLabel.setText(nutrient);
		nutrientLabel.setPrefWidth(150);
		
		Label nutrientValue = new Label();
		nutrientValue.setText("" + value);
		nutrientValue.setPrefWidth(50);
		
		ProgressBar bar = new ProgressBar();
		bar.setPrefHeight(30);
		bar.setPrefWidth(400);
		bar.setProgress(barVal);
		
		Label dailyValueLabel = new Label(dailyValue);
		dailyValueLabel.setPrefWidth(50);
		
		hBox.getChildren().setAll(nutrientLabel, nutrientValue, bar, dailyValueLabel);
		
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
		
		this.currentFoodDataFile = DEFAULT_DATASET_PATH;
		
		LoadDefault();
	}
	
	public void pressMeal(MealListItem meal) {
		System.out.println(meal.toString() + ": " + meal.ID.toString());
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(meal.ID);
		this.LoadMealInfoSection(firstMealViewModel);
		
	}
	
	public void pressFoodItem(FoodListItem foodItem) {
		if(foodItem != null)
			System.out.println(foodItem.toString() + ": " + foodItem.ID.toString());
		this.currentFoodItem = foodItem;
	}
	
	public void pressAddFood() {
		main.showAddFoodItemStage();
	}
	
	
	public void pressAddMeal() {
		main.showAddMealStage();
	}
	
	public void pressAddSelectedFoodToMeal() {
		FoodListItem foodListItem = this.foodList.getSelectionModel().getSelectedItem();
		MealViewModel mealViewModel = this.currentViewedMeal;
		if(foodListItem == null) { return; }
		this.mealService.addFoodToMeal(mealViewModel.getID(), foodListItem.ID);
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(mealViewModel.getID());
		this.LoadMealInfoSection(firstMealViewModel);
	}
	
	public void pressRemoveFoodFromMeal(UUID foodID) {
		this.mealService.removeFoodFromMeal(this.currentViewedMeal.getID(), foodID);
		MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(this.currentViewedMeal.getID());
		this.LoadMealInfoSection(firstMealViewModel);
	}
	
	public void pressRefreshMealDetailsSection() {
		MealViewModel mealViewModel = this.currentViewedMeal;
		
		HashMap<UUID, Integer> quantityMap = new HashMap<UUID, Integer>();
		for(int i = 1; i < this.nutritionTable.getChildren().size(); i++) {
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
	
	public void addFoodItem(FoodDataItem foodItem) {
		FoodListItem item = new FoodListItem(foodItem);
		this.foodList.getItems().add(item);
		this.foodCountLabel.setText("Food Item Count:" + this.foodList.getItems().size() + " Items");
		this.foodService.addFoodItem(foodItem);
	}
	
	
	
	public void openFileChooser() {
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
		
		//Handle MealLists
		this.mealService.addNewFoodFile(selectedFile.getPath());
		 
		
		LoadNewFoodFile(selectedFile.getPath());
		
	}
	
	public void LoadNewFoodFile(String foodDataFilePath) {
		this.foodService.SwitchToNewDataFile(foodDataFilePath);
		
		this.mealService.UpdateToNewDataFile(foodDataFilePath);
		
		LoadDefault();
	}
	
	
	public void LoadDefault(){
		//Loads the three sections of content onto the view, including the FoodItems list (All Items, assumes blank query), the meal info pane. (Empty), and the meal list, with all meals for that particular datafile.
		//Pre-Conditions: foodservice and mealService have had their data already loaded in and are ready to be queried.
		this.foodList.getItems().clear();
		this.mealList.getItems().clear();
		this.LoadDefaultFoodItems();
		if(this.mealService.GetAllMeals() == null || this.mealService.GetAllMeals().isEmpty()) {
			this.mealDetailsSection.setVisible(false);
			
		}
		else {
			this.mealDetailsSection.setVisible(true);
			Meal firstMeal = this.mealService.GetAllMeals().get(0);
			MealViewModel firstMealViewModel = this.mealService.getMealViewModelForMealID(firstMeal.getID());
			this.LoadMealInfoSection(firstMealViewModel);
			this.LoadDefaultMealList();
		}
		
		
	}
	public void LoadDefaultFoodItems(){
		ArrayList<FoodListItem> listContents = (ArrayList<FoodListItem>) this.foodService.Query(new FoodQuery())
				.stream()
				.map(foodItem -> new FoodListItem(foodItem))
				.collect(Collectors.toList());
		this.foodList.getItems().addAll(listContents);
		this.foodCountLabel.setText("Food Item Count:" + this.foodList.getItems().size() + " Items");
	}
	
	public void LoadMealInfoSection(MealViewModel vm){
		this.currentViewedMeal = vm;
		//Sets the MealInfoSection int the view to blank
		this.mealDetailsSection.setDisable(false);
		
		this.selectedMeal_Name.setText(vm.getMealName());
		this.selectedMeal_NutrientBarContainer.getChildren().clear();
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Calories", vm.getNutrientInfo().get(Nutrient.CALORIES), vm.getNutrientBarProgress().get(Nutrient.CALORIES), "2000 (cal)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fat Grams", vm.getNutrientInfo().get(Nutrient.FATGRAMS), vm.getNutrientBarProgress().get(Nutrient.FATGRAMS), "65 (g)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Carb Grams", vm.getNutrientInfo().get(Nutrient.CARBGRAMS), vm.getNutrientBarProgress().get(Nutrient.CARBGRAMS), "300 (g)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fiber Grams", vm.getNutrientInfo().get(Nutrient.FIBERGRAMS), vm.getNutrientBarProgress().get(Nutrient.FIBERGRAMS), "25 (g)"));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Protein Grams", vm.getNutrientInfo().get(Nutrient.PROTEINGRAMS), vm.getNutrientBarProgress().get(Nutrient.PROTEINGRAMS), "50 (g)"));
		
		
		this.nutritionTable.getChildren().clear();
		this.nutritionTable.addRow(0, getFoodNutritionTableHeading());
		for(int i = 0; i < vm.getFoods().size(); i++) {
			ArrayList<FoodViewModel> foods = vm.getFoods();
			FoodViewModel foodVM = foods.get(i);
			this.nutritionTable.addRow(i+ 1, getFoodNutritionTableRow(foodVM.getName(), foodVM.getQuantity(), foodVM.getCalories(), foodVM.getFatGrams(), foodVM.getCarboHydrateGrams(), foodVM.getFiberGrams(), foodVM.getProteinGrams(), foodVM.getID()));
		}
	}
	
	public void LoadEMPTYMealInfoSection() {
		this.mealDetailsSection.setDisable(true);
	}
	
	public void LoadDefaultMealList(){
		ArrayList<MealListItem> mealListContents = (ArrayList<MealListItem>) this.mealService.GetAllMeals()
				.stream()
				.map(meal -> new MealListItem(meal))
				.collect(Collectors.toList());
		this.mealList.getItems().addAll(mealListContents);	
	}
}
