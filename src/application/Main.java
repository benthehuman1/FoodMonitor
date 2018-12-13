package application;
	
import java.util.HashMap;
import java.util.UUID;

import Models.FoodDataItem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Launches and manages communication between the pages / windows of the application.
 * @author A-Team 71
 */
public class Main extends Application {
	
	private Stage primaryStage; //Reference to the stage that the mainMenu sits on.
	private MainMenuController mainPage; //Reference to the main menu.
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		try {
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			this.mainPage = new MainMenuController(root, this);
			primaryStage.show();
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			primaryStage.setMaximized(true);
			primaryStage.setResizable(false);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Assuming "text" is the text content of "field", removes all non-numeric characters from fields text. 
	 * @param: text: the current text content of "field'.
	 */
	public void removeNonNumericCharachters(String text, TextField field) {
		if (!text.matches("\\d*"))
			field.setText(text.replaceAll("[^\\d|\\.]", ""));
	}
	
	/**
	 * Assuming "text" is the text content of "field", removes all commas from fields text. Used to prevent CSV issues.
	 * @param: text: the current text content of "field'.
	 */
	public void removeCommas(String text, TextField field) {
		field.setText(text.replace(",", ""));
	}
	
	/**
	 * Sets up and displays the AddFoodItem stage. 
	 */
	public void showAddFoodItemStage() {
		
		Stage addDialogueStage = new Stage();
		addDialogueStage.setTitle("Add New FoodItem");
		addDialogueStage.initModality(Modality.WINDOW_MODAL);
		addDialogueStage.initOwner(primaryStage);
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 300, 350);
		
		Label mainLabel = new Label("Add New FoodItem");
		
		HashMap<String, TextField> inputMap = new HashMap<String, TextField>();
		
		//In hindsight, I shouldv'e extracted these into their own method, as we do in MainMenuController
		VBox inputs = new VBox();
		
		HBox foodNameInputContainer = new HBox();
		foodNameInputContainer.setPadding(new Insets(5));
		
		Label foodNameLabel = new Label("Name:");
		foodNameLabel.setPrefWidth(100);
		
		TextField foodNameFeild = new TextField();
		
		//When the text is updated, remove commas. Essentially not allowing the user to type commas.
		foodNameFeild.textProperty().addListener((obs, oldText, newText) -> removeCommas(newText, foodNameFeild));
		foodNameInputContainer.getChildren().addAll(foodNameLabel, foodNameFeild);
		inputMap.put("Name", foodNameFeild);
		
		HBox foodGivenIDInputContainer = new HBox();
		foodGivenIDInputContainer.setPadding(new Insets(5));
		
		Label foodGivenIDLabel = new Label("ID:");
		foodGivenIDLabel.setPrefWidth(100);
		
		TextField foodGivenIDFeild = new TextField();
		foodGivenIDFeild.textProperty().addListener((obs, oldText, newText) -> removeCommas(newText, foodGivenIDFeild));
		foodGivenIDInputContainer.getChildren().addAll(foodGivenIDLabel, foodGivenIDFeild);
		inputMap.put("ID", foodGivenIDFeild);
		
		HBox foodCaloriesInputContainer = new HBox();
		foodCaloriesInputContainer.setPadding(new Insets(5));
		
		Label foodCaloriesLabel = new Label("Calories:");
		foodCaloriesLabel.setPrefWidth(100);
		
		TextField foodCaloriesFeild = new TextField();
		foodCaloriesFeild.textProperty().addListener((obs, oldText, newText) -> removeNonNumericCharachters(newText, foodCaloriesFeild));
		foodCaloriesInputContainer.getChildren().addAll(foodCaloriesLabel, foodCaloriesFeild);
		inputMap.put("Calories", foodCaloriesFeild);
		
		HBox foodFatGramsInputContainer = new HBox();
		foodFatGramsInputContainer.setPadding(new Insets(5));
		
		Label foodFatGramsLabel = new Label("Fat Grams:");
		foodFatGramsLabel.setPrefWidth(100);
		
		TextField foodFatGramsFeild = new TextField();
		foodFatGramsFeild.textProperty().addListener((obs, oldText, newText) -> removeNonNumericCharachters(newText, foodFatGramsFeild));
		foodFatGramsInputContainer.getChildren().addAll(foodFatGramsLabel, foodFatGramsFeild);
		inputMap.put("Fat Grams", foodFatGramsFeild);
		
		HBox foodCarbGramsInputContainer = new HBox();
		foodCarbGramsInputContainer.setPadding(new Insets(5));
		
		Label foodCarbGramsLabel = new Label("Carbohydrate Grams:");
		foodCarbGramsLabel.setPrefWidth(100);
		
		TextField foodCarbGramsFeild = new TextField();
		foodCarbGramsFeild.textProperty().addListener((obs, oldText, newText) -> removeNonNumericCharachters(newText, foodCarbGramsFeild));
		foodCarbGramsInputContainer.getChildren().addAll(foodCarbGramsLabel, foodCarbGramsFeild);
		inputMap.put("Carb Grams", foodCarbGramsFeild);
		
		HBox foodFiberGramsInputContainer = new HBox();
		foodFiberGramsInputContainer.setPadding(new Insets(5));
		
		Label foodFiberGramsLabel = new Label("Fiber Grams:");
		foodFiberGramsLabel.setPrefWidth(100);
		
		TextField foodFiberGramsFeild = new TextField();
		foodFiberGramsFeild.textProperty().addListener((obs, oldText, newText) -> removeNonNumericCharachters(newText, foodFiberGramsFeild));
		foodFiberGramsInputContainer.getChildren().addAll(foodFiberGramsLabel, foodFiberGramsFeild);
		inputMap.put("Fiber Grams", foodFiberGramsFeild);
		
		HBox foodProteinGramsInputContainer = new HBox();
		foodProteinGramsInputContainer.setPadding(new Insets(5));
		
		Label foodProteinGramsLabel = new Label("Protein Grams:");
		foodProteinGramsLabel.setPrefWidth(100);
		
		TextField foodProteinGramsFeild = new TextField();
		foodProteinGramsFeild.textProperty().addListener((obs, oldText, newText) -> removeNonNumericCharachters(newText, foodProteinGramsFeild));
		foodProteinGramsInputContainer.getChildren().addAll(foodProteinGramsLabel, foodProteinGramsFeild);
		inputMap.put("Protein Grams", foodProteinGramsFeild);
	
		inputs.getChildren().add(foodNameInputContainer);
		inputs.getChildren().add(foodGivenIDInputContainer);
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
	
	/**
	 * Handled what should happen when the user clicks the add button. 
	 * @param inputMap: A HashMap that matches a nutrient/fields label to its actual field.
	 * @param addDialog The stage that this button was clicked from.
	 */
	private void pressAddFoodButton(HashMap<String, TextField> inputMap, Stage addDialog) {
		
		//Setup FoodItem
		FoodDataItem foodItem = new FoodDataItem();
		foodItem.setName(inputMap.get("Name").getText());
		foodItem.setGivenID(inputMap.get("ID").getText());
		foodItem.setId(UUID.randomUUID());
		foodItem.setCalories(getDoubleValueFromLabel(inputMap.get("Calories")));
		foodItem.setFatGrams(getDoubleValueFromLabel(inputMap.get("Fat Grams")));
		foodItem.setCarboHydrateGrams(getDoubleValueFromLabel(inputMap.get("Carb Grams")));
		foodItem.setFiberGrams(getDoubleValueFromLabel(inputMap.get("Fiber Grams")));
		foodItem.setProteinGrams(getDoubleValueFromLabel(inputMap.get("Protein Grams")));
		
		//Passes that foodItem to the main page, witch will display it and save it to the data. 
		mainPage.addFoodItem(foodItem);
		addDialog.close();
		
	}
	
	/**
	 * Extracts the numerical information from a TextFeild. If there are any characters other than [0-9] & ".", returns 0.0
	 * @param field the field in question.
	 * @return
	 */
	public static double getDoubleValueFromLabel(TextField field) {
		String text = field.getText();
		return text.isEmpty() || !isValidDoubleString(text) ? 0.0 : Double.parseDouble(text);
	}
	
	/**
	 * Verifies that the input string can be parsed as a double.
	 * @param input
	 * @return true if the input string can be parsed as a double. 
	 */
	public static boolean isValidDoubleString(String input) {
		String validChars = ".0123456789";
		for(char c : input.toCharArray())
			if(!validChars.contains("" + c ))
				return false;
		return true;
	}
	
	/**
	 * Sets up and shows the AddMeal Dialog/Stage.
	 */
	public void showAddMealStage() {
		
		Stage addDialogueStage = new Stage();
		addDialogueStage.setTitle("Add New Meal");
		addDialogueStage.initModality(Modality.WINDOW_MODAL);
		addDialogueStage.initOwner(primaryStage);
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 300, 125);
		
		Label mainLabel = new Label("Add New Meal:");//borderpane top
		
		TextField nameInput = new TextField();
		nameInput.setText("Meal Name");
		nameInput.textProperty().addListener((obs, oldText, newText) ->  removeCommas(newText, nameInput));
		
		Button addButton = new Button();
		addButton.setText("Add");
		addButton.setOnAction(e -> pressAddMealButton(nameInput.getText(), addDialogueStage));
		
		root.setTop(mainLabel);
		root.setCenter(nameInput);
		root.setBottom(addButton);
		
		addDialogueStage.setScene(scene);
		addDialogueStage.showAndWait();
		
	}
	
	/**
	 * Handles everything that should happen in the back-end when the user clicks to add the meal to the database. 
	 * @param mealName
	 * @param addDialog
	 */
	private void pressAddMealButton(String mealName, Stage addDialog) {
		this.mainPage.addMeal(mealName);
		addDialog.close();
	}
	
	/**
	 * @return the stage that contains the main menu.
	 */
	public Stage getMainStage() {
		return this.primaryStage;
	}
	
	/**
	 * Launches the program.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
