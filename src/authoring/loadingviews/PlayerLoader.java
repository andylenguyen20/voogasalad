package authoring.loadingviews;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.keys.AuthRes;

public class PlayerLoader extends BaseLoader {

	public PlayerLoader(Stage stage) {
		super(stage);
	}

	@Override
	public Pane finishScene(GridPane gridpane) {
		addTitle(gridpane, AuthRes.getString("Play"));
		return addCoreFinishingElements(gridpane);
	}

	@Override
	public void buildThumbnails(VBox vb) {
		// TODO Auto-generated method stub
		
	}

}
