package application;



import java.awt.Checkbox;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.lang.model.element.VariableElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

import Models.Comparator;
import Models.FoodItem;
import Models.FoodQuery;
import Models.FoodViewModel;
import Models.FoodQueryRule;

import Models.Meal;
import Models.MealItem;
import Models.MealViewModel;
import Models.Nutrient;
import Services.FoodListService;
import Services.MealListService;
import javafx.event.EventHandler;
/*
import Models.FoodItem;
import Models.Meal;
import Services.FoodListService;
import Services.MealListService;
*/
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainMenuController {
	
	//Logical Members
	private static final String DEFAULT_DATASET_PATH = "./Data/FoodDataSets/defaultFoodData.csv";
	private Main main;
	private String currentFoodDataFile;
	
	FoodListService foodService; 
	MealListService mealService;
	
	//Graphical Members
	private Label dataSetNameLabel;
	private ListView<FoodListItem> foodList;
	private ListView<MealListItem> mealList;
	
		//Meal DetailSection
		private VBox mealDetailsSection;
		private Label selectedMeal_Name;
		private VBox selectedMeal_NutrientBarContainer;
		private GridPane nutritionTabe;
	
	
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
				
			});
			
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
		newFoodButton.setOnAction(e -> pressAddFood());
		
		
		foodSection.getChildren().add(foodSearchSection);
		foodSection.getChildren().add(foodList);
		foodSection.getChildren().add(newFoodButton);
		//END foodSection 
		
		//Setup Meal Details Section
		this.mealDetailsSection = new VBox();
		this.mealDetailsSection.setPadding(new Insets(20));
			
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
			this.nutritionTabe = new GridPane();
			
			//nutritionTabe.addRow(0, getFoodNutritionTableRow("Hot Dogs", new int[] {2, 5, 1, 8, 3, 6}));
			//nutritionTabe.addRow(1, getFoodNutritionTableRow("Tummy Yummy", new int[] {1, 44, 1, 42, 3, 4}));
			
			//Setup addFoodToMealButton
			Button addFoodToMealButton = new Button("+ Add Currently Selected Food to Meal");
			addFoodToMealButton.setPrefWidth(600);
			addFoodToMealButton.setPrefHeight(30);
			
		
		this.mealDetailsSection.getChildren().add(mealRefresh_MealName);
		this.mealDetailsSection.getChildren().add(this.selectedMeal_NutrientBarContainer);
		this.mealDetailsSection.getChildren().add(foodLabel);
		this.mealDetailsSection.getChildren().add(nutritionTabe);
		this.mealDetailsSection.getChildren().add(addFoodToMealButton);
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
			newMealButton.setOnAction(e -> pressAddMeal());
			
		mealSelectionSection.getChildren().add(mealsLabel);
		mealSelectionSection.getChildren().add(mealList_Pane);
		mealSelectionSection.getChildren().add(newMealButton);
		
		
		
		pageRoot.setTop(pageHeader);
		pageRoot.setLeft(foodSection);
		pageRoot.setCenter(mealDetailsSection);
		pageRoot.setRight(mealSelectionSection);
	}
	
	private GridPane getFoodNutritionTableRow(String food, int quantity, double calories, double fatGrams, double carbGrams, double fiberGrams, double proteinGrams) {
		GridPane row = new GridPane();//400
		row.setGridLinesVisible(true);
		row.setPadding(new Insets(5));
		
		Label foodLabel = new Label(food);
		foodLabel.setPrefWidth(125);
		foodLabel.setAlignment(Pos.CENTER);
		
		TextField quantitiy = new TextField();
		quantitiy.setPrefWidth(75);
		quantitiy.setText("" + quantity);
		
		Label caloriesLabel = new Label("" + calories);
		caloriesLabel.setAlignment(Pos.CENTER);
		caloriesLabel.setPrefWidth(75);
		
		Label fatGramsLabel = new Label("" + fatGrams);
		fatGramsLabel.setPrefWidth(75);
		fatGramsLabel.setAlignment(Pos.CENTER);
		
		Label carbGramsLabel = new Label("" + carbGrams);
		carbGramsLabel.setPrefWidth(75);
		carbGramsLabel.setAlignment(Pos.CENTER);
		
		Label fiberGramsLabel = new Label("" + fiberGrams);
		fiberGramsLabel.setPrefWidth(75);
		fiberGramsLabel.setAlignment(Pos.CENTER);
		
		Label proteinGramsLabel = new Label("" + proteinGrams);
		proteinGramsLabel.setPrefWidth(75);
		proteinGramsLabel.setAlignment(Pos.CENTER);
		
		row.addColumn(0, foodLabel);
		row.addColumn(1, quantitiy);
		row.addColumn(2, caloriesLabel);
		row.addColumn(3, fatGramsLabel);
		row.addColumn(4, carbGramsLabel);
		row.addColumn(5, fiberGramsLabel);
		row.addColumn(6, proteinGramsLabel);
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
		
		this.currentFoodDataFile = DEFAULT_DATASET_PATH;
		
		LoadDefault();
	}
	
	public void pressAddFood() {
		main.showAddFoodItemStage();
	}
	
	
	public void pressAddMeal() {
		main.showAddMealStage();
	}
	
	
	public void addMeal(String mealName) {
		//{"mealItems":[{"quantity":5,"foodID":"54fe2c2a-4e9d-7e6f-76e3-f56a00000000"}],"name":"Lunch","ID":"1939df07-8aba-4c67-82d0-82c96c1c40d5"}]},
		Meal result = new Meal();
		result.setMealItems(new ArrayList<MealItem>());
		result.setName(mealName);
		result.setID(UUID.randomUUID());
		
		this.mealService.addMeal(result);
		
		this.mealList.getItems().add(new MealListItem(result));
	}
	
	public void addFoodItem(FoodItem foodItem) {
		FoodListItem item = new FoodListItem(foodItem);
		this.foodList.getItems().add(item);
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
	}
	
	public void LoadMealInfoSection(MealViewModel vm){
		//Sets the MealInfoSection int the view to blank
		this.mealDetailsSection.setDisable(false);
		
		this.selectedMeal_Name.setText(vm.getMealName());
		this.selectedMeal_NutrientBarContainer.getChildren().clear();
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Calories", vm.getNutrientInfo().get(Nutrient.CALORIES), vm.getNutrientBarProgress().get(Nutrient.CALORIES)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fat Grams", vm.getNutrientInfo().get(Nutrient.FATGRAMS), vm.getNutrientBarProgress().get(Nutrient.FATGRAMS)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Carb Grams", vm.getNutrientInfo().get(Nutrient.CARBGRAMS), vm.getNutrientBarProgress().get(Nutrient.CARBGRAMS)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Fiber Grams", vm.getNutrientInfo().get(Nutrient.FIBERGRAMS), vm.getNutrientBarProgress().get(Nutrient.FIBERGRAMS)));
		this.selectedMeal_NutrientBarContainer.getChildren().add(getMealNutritionBar("Protein Grams", vm.getNutrientInfo().get(Nutrient.PROTEINGRAMS), vm.getNutrientBarProgress().get(Nutrient.PROTEINGRAMS)));
		
		
		this.nutritionTabe.getChildren().clear();
		for(int i = 0; i < vm.getFoods().size(); i++) {
			FoodViewModel foodVM = vm.getFoods().get(i);
			this.nutritionTabe.addRow(i, getFoodNutritionTableRow(foodVM.getName(), foodVM.getQuantity(), foodVM.getCalories(), foodVM.getFatGrams(), foodVM.getCarboHydrateGrams(), foodVM.getFiberGrams(), foodVM.getProteinGrams()));
		}
	}
	
	public void LoadEMPTYMealInfoSection() {
		this.mealDetailsSection.setDisable(true);
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
