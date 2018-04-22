package game_player;

import java.util.ArrayList;

import authoring.GameChooserScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 
 * @author Brandon Dalla Rosa, Dana Park
 *Class that represents all Menu items contained in HBox on top of the screen
 */

public class Menu {
	
	private HBox pane;
	private PulldownFactory pullDownFactory;
	private Button gameSelectionButton;
	private Stage gameSelectionStage;
	private DataManager dataManager;
	private SettingsMenu settings;
	private GameSelectionMenu gameMenu;
	
	public Menu(DataManager data, PulldownFactory pdf) {
		pane = new HBox(80);
		pane.setAlignment(Pos.TOP_CENTER);
		//pane.getStyleClass().add("pane-back");

		dataManager = data;
		pullDownFactory = pdf;
		makePullDownMenus();
		settings = new SettingsMenu();
		settings.makeSettingsMenu(pane);
		settings.makeSettingsStage();
		gameMenu = new GameSelectionMenu();
		gameMenu.makeGameSelectionMenu(pane);
		KeyPrefMenu kpm = new KeyPrefMenu(dataManager,pane);
	}
	/**
	 * Method to add the menu into the VBox for the View Manager
	 * 
	 * @param root
	 */
	public void addMenu(Pane root) {
		root.getChildren().add(pane);
	}
	
    /**
	 * Method to add ComboBoxes from PulldownFactory to Menu
	 * 
	 */
	private void makePullDownMenus() {
		
		pane.getChildren().add(pullDownFactory.getSpeedBox());
		pane.getChildren().add(pullDownFactory.getStatusBox());
		pane.getChildren().add(pullDownFactory.getSaveLoadBox());
		
	}
	
	/**
	 * Method to make gameSelectionButton that when clicked calls the showGameSelectionMenu method
	 * 
	 */
//	private void makeGameSelectionMenu() {
//		gameSelectionButton = new Button("Game Selection");
//		gameSelectionButton.getStyleClass().add("button-nav");
//		gameSelectionButton.setOnAction(click->{showGameSelectionMenu();});
//		pane.getChildren().add(gameSelectionButton);
//		
//	}
	/**
	 * getter method for the Brightness Slider on the Settings Stage
	 * 
	 */
	public Slider getBrightnessSlider() {
		return settings.getBrightnessSlider();
	}
	/**
	 * getter method for the Volume Slider on the Settings Stage
	 * 
	 */
	public Slider getVolumeSlider() {
		
		return settings.getBrightnessSlider();
		
	}
	
	/**
	 * method to show new Stage when gameSelectionButton is pressed
	 * 
	 */
//	public void showGameSelectionMenu() {
//		//TODO Make this choose game to play, not edit
//		gameSelectionStage = new Stage();
//		GameChooserScreen gc = new GameChooserScreen(gameSelectionStage);
//		//gameSelectionStage.setScene(gc.display());
//		gameSelectionStage.show();
//	}	

}
