package application;
	
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import Models.Comparator;
import Models.FoodItem;
import Models.FoodQuery;
import Models.FoodQueryRule;
import Models.Nutrient;
import Repositories.FoodListRepository;
import Repositories.MealListRepository;
import Services.FoodListService;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	Stage primaryStage;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1280,720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			MainMenuController mainPage = new MainMenuController(root, this);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showBonusStage(String text) {
		Stage addDialogueStage = new Stage();
		addDialogueStage.setTitle("Add New Subject");
		addDialogueStage.initModality(Modality.WINDOW_MODAL);
		addDialogueStage.initOwner(primaryStage);
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 100, 300);
		
		Button b = new Button();
		b.setText(text);
		root.setCenter(b);
		
		addDialogueStage.setScene(scene);
		addDialogueStage.showAndWait();
		
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
