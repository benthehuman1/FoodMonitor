package application;
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import Models.Comparator;
import Models.FoodItem;
import Models.FoodQuery;
import Models.FoodQueryRule;
import Models.Nutrient;
import Repositories.FoodListRepository;
import Repositories.MealListRepository;
import Services.FoodListService;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	private Stage primaryStage;
	private MainMenuController mainPage;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1280,720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			this.mainPage = new MainMenuController(root, this);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void showAddFoodItemStage() {
		FoodItem foodItem = new FoodItem();
		
		Stage addDialogueStage = new Stage();
		addDialogueStage.setTitle("Add New FoodItem");
		addDialogueStage.initModality(Modality.WINDOW_MODAL);
		addDialogueStage.initOwner(primaryStage);
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 300, 500);
		
		Label mainLabel = new Label("Add New FoodItem");
		
		HashMap<String, TextField> inputMap = new HashMap<String, TextField>();
		
		VBox inputs = new VBox();
			HBox foodNameInputContainer = new HBox();
			foodNameInputContainer.setPadding(new Insets(5));
			Label foodNameLabel = new Label("Name:");
			foodNameLabel.setPrefWidth(100);
			TextField foodNameFeild = new TextField();
			foodNameInputContainer.getChildren().addAll(foodNameLabel, foodNameFeild);
			inputMap.put("Name", foodNameFeild);
			
			HBox foodCaloriesInputContainer = new HBox();
			foodCaloriesInputContainer.setPadding(new Insets(5));
			Label foodCaloriesLabel = new Label("Calories:");
			foodCaloriesLabel.setPrefWidth(100);
			TextField foodCaloriesFeild = new TextField();
			foodCaloriesInputContainer.getChildren().addAll(foodCaloriesLabel, foodCaloriesFeild);
			inputMap.put("Calories", foodCaloriesFeild);
			
			HBox foodFatGramsInputContainer = new HBox();
			foodFatGramsInputContainer.setPadding(new Insets(5));
			Label foodFatGramsLabel = new Label("Fat Grams:");
			foodFatGramsLabel.setPrefWidth(100);
			TextField foodFatGramsFeild = new TextField();
			foodFatGramsInputContainer.getChildren().addAll(foodFatGramsLabel, foodFatGramsFeild);
			inputMap.put("Fat Grams", foodFatGramsFeild);
			
			HBox foodCarbGramsInputContainer = new HBox();
			foodCarbGramsInputContainer.setPadding(new Insets(5));
			Label foodCarbGramsLabel = new Label("Carbohydrate Grams:");
			foodCarbGramsLabel.setPrefWidth(100);
			TextField foodCarbGramsFeild = new TextField();
			foodCarbGramsInputContainer.getChildren().addAll(foodCarbGramsLabel, foodCarbGramsFeild);
			inputMap.put("Carb Grams", foodCarbGramsFeild);
			
			HBox foodFiberGramsInputContainer = new HBox();
			foodFiberGramsInputContainer.setPadding(new Insets(5));
			Label foodFiberGramsLabel = new Label("Fiber Grams:");
			foodFiberGramsLabel.setPrefWidth(100);
			TextField foodFiberGramsFeild = new TextField();
			foodFiberGramsInputContainer.getChildren().addAll(foodFiberGramsLabel, foodFiberGramsFeild);
			inputMap.put("Fiber Grams", foodFiberGramsFeild);
			
			HBox foodProteinGramsInputContainer = new HBox();
			foodProteinGramsInputContainer.setPadding(new Insets(5));
			Label foodProteinGramsLabel = new Label("Protein Grams:");
			foodProteinGramsLabel.setPrefWidth(100);
			TextField foodProteinGramsFeild = new TextField();
			foodProteinGramsInputContainer.getChildren().addAll(foodProteinGramsLabel, foodProteinGramsFeild);
			inputMap.put("Protein Grams", foodProteinGramsFeild);
		
			inputs.getChildren().add(foodNameInputContainer);
			inputs.getChildren().add(foodCaloriesInputContainer);
			inputs.getChildren().add(foodFatGramsInputContainer);
			inputs.getChildren().add(foodCarbGramsInputContainer);
			inputs.getChildren().add(foodFiberGramsInputContainer);
			inputs.getChildren().add(foodProteinGramsInputContainer);
			
			
		Button submitButton = new Button();
		submitButton.setText("Add New FoodItem");
		submitButton.setOnAction(e -> pressAddFoodButton(inputMap, addDialogueStage));
			
		root.setTop(mainLabel);
		root.setCenter(inputs);
		root.setBottom(submitButton);
		
		addDialogueStage.setScene(scene);
		addDialogueStage.showAndWait();
			
	}
	private UUID getRandomGUID_ProperFormat() {
		UUID base = UUID.randomUUID();
		String baseAsString = base.toString();
		baseAsString = baseAsString.replaceAll("\\-", "");
		String resultAsString = baseAsString.substring(0, 24);
		String formatedGuidString = resultAsString.substring(0, 8) + "-" + resultAsString.substring(8, 12) + "-" + resultAsString.substring(12, 16) + "-" + resultAsString.substring(16, 20) + "-" + resultAsString.substring(20) + "00000000";
		return UUID.fromString(formatedGuidString);
	}
	
	
	private void pressAddFoodButton(HashMap<String, TextField> inputMap, Stage addDialog) {
		FoodItem foodItem = new FoodItem();
		foodItem.setName(inputMap.get("Name").getText());
		foodItem.setId(getRandomGUID_ProperFormat());
		foodItem.setCalories(getDoubleValueFromLabel(inputMap.get("Calories")));
		foodItem.setFatGrams(getDoubleValueFromLabel(inputMap.get("Fat Grams")));
		foodItem.setCarboHydrateGrams(getDoubleValueFromLabel(inputMap.get("Carb Grams")));
		foodItem.setFiberGrams(getDoubleValueFromLabel(inputMap.get("Fiber Grams")));
		foodItem.setProteinGrams(getDoubleValueFromLabel(inputMap.get("Protein Grams")));
		
		
		
		mainPage.addFoodItem(foodItem);
		addDialog.close();
	}
	
	public static double getDoubleValueFromLabel(TextField field) {
		String text = field.getText();
		if(text.isEmpty() || !isValidDoubleString(text)) { return 0.0; }
		else {
			return Double.parseDouble(text);
		}
	}
	
	public static boolean isValidDoubleString(String input) {
		String validChars = ".0123456789";
		for(char c : input.toCharArray()) {
			if(!validChars.contains("" + c )) { return false; }
		}
		
		return true;
	}
	
	public void showAddMealStage() {
		Stage addDialogueStage = new Stage();
		addDialogueStage.setTitle("Add New Meal");
		addDialogueStage.initModality(Modality.WINDOW_MODAL);
		addDialogueStage.initOwner(primaryStage);
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 300, 110);
		
		Label mainLabel = new Label("Add New Meal:");//borderpane top
		
		TextField nameInput = new TextField();
		nameInput.setText("Meal Name");
		
		Button addButton = new Button();
		addButton.setText("Add");
		addButton.setOnAction(e -> pressAddMealButton(nameInput.getText(), addDialogueStage));
		
		root.setTop(mainLabel);
		root.setCenter(nameInput);
		root.setBottom(addButton);
		
		addDialogueStage.setScene(scene);
		addDialogueStage.showAndWait();
	}
	
	private void pressAddMealButton(String mealName, Stage addDialog) {
		this.mainPage.addMeal(mealName);
		addDialog.close();
	}
	
	public Stage getMainStage() {
		return this.primaryStage;
	}
	
	public static void main(String[] args) {
		
		for(int i = 0; i < 20; i++) {
			String result = "";
			result += (99 + (Math.random() * 2.0));
			System.out.println(result.substring(0, 8));
		}
		MealListRepository mealListRepository = new MealListRepository();
		
		launch(args);
	}
}
