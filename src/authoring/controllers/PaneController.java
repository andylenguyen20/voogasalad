package authoring.controllers;

import java.io.File;

import authoring.Canvas;
import authoring.right_components.LevelPane;
import javafx.scene.image.Image;

/**
 * @author jennychin
 * @author elizabethschulman
 * Handles pane interactions (i.e. one pane needing to update another)
 */
public class PaneController {

	private LevelPane levelPane;
	private Canvas canvas;
	
	public PaneController(LevelPane lp, Canvas c){
		levelPane = lp;
		canvas = c;
	}

	/**
	 *
	 * @param image Sets the background to a specified image
	 */
	public void setBackground(String fname){
		Image im = new Image(fname);
		canvas.updateBackground(im);
	}
	
	public void resetBackground(){
		canvas.setDefaultBackground();
	}
}
