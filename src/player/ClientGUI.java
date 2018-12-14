package player;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilities.Debug;

/**
 * This class contains the main method to lunch the game. It extends application
 * to launch the javaFx welcome screen
 * 
 * @author DigitalVilla
 *
 */
public class ClientGUI extends Application {

	private Player player;

	@Override
	public void start(Stage primaryStage) throws Exception {
		player = new Player(primaryStage);
		SocketSettings app = new SocketSettings(player);
		Menu btns = new Menu();
		Debug.setLevel(1);
		// Background Image

		// ImageView bg = new
		// ImageView(this.getClass().getResource("/res/images/BG_login1.jpg").toExternalForm());

		Pane pane = new Pane();
		pane.getStyleClass().add("pane");
		pane.getChildren().addAll(btns.getExit_btn(), app.getSettings());

		Scene scene = new Scene(pane, player.getScreen_W(), player.getScreen_H());
		scene.getStylesheets().add(this.getClass().getResource("/player/view.css").toExternalForm());
		scene.setCursor(Cursor.HAND);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Battleship | 2018 Titanium Edition ");
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.toFront();
		primaryStage.show();

	}

	@Override
	public void stop() {
		System.out.println("colsing");
		// player.game.closeGame();
	}

}