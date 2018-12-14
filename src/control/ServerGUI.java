package control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class contains the main method to lunch the game's server.
 * 
 * @author DigitalVilla
 *
 */
public class ServerGUI extends Application {


	@Override
	public void start(Stage window) throws Exception {
		window.setScene(new ServerUI().getMyScene());
		window.initStyle(StageStyle.UNDECORATED);
		window.toFront();
		window.show();
	}

	/**
	 * This class creates the servers GUI window
	 * 
	 * @author DigitalVilla
	 *
	 */
	public static class ServerUI {
		// Setters and Getters
		private Pane serverApp;
		private static Label displayText;
		public TextField txtFld;
		public static String message = "";
		private double serverAppW;
		private double serverAppH;
		private Button sendBtn;
		private Connection connect;
		private int counter;
		private ImageView img;

		public ServerUI() {
			serverAppW = 550;
			serverAppH = 280;
			connect = new Connection();
		}

		/**
		 * This method creates all the JavaFx elements inside the window
		 */
		public void paintScene() {
			serverApp = new Pane();
			serverApp.setId("serverApp");

			ScrollPane display = new ScrollPane();
			display.setFitToWidth(true);
			display.setId("serverDisplay");
			display.setVvalue(1.0);
			// Setting a horizontal scroll bar is always display
			display.setHbarPolicy(ScrollBarPolicy.NEVER);
			// Setting vertical scroll bar is never displayed.
			display.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			display.setMaxSize(serverAppW - 20, serverAppH - 90);
			display.setMinSize(serverAppW - 20, serverAppH - 90);
			display.setLayoutY(10);
			display.setLayoutX(10);

			// Display
			displayText = new Label();
			displayText.setText("\nBATTLESHIP is ready to start new connection... \n");
			displayText.setId("serverText");
			displayText.setAlignment(Pos.TOP_LEFT);
			displayText.setWrapText(true);
			displayText.setTextAlignment(TextAlignment.JUSTIFY);
			// displayText.setMaxSize(serverAppW - 20, serverAppH - 100);
			displayText.setMinHeight(serverAppH - 60);
			displayText.setMaxWidth(serverAppW - 0);
			displayText.setLayoutY(10);
			displayText.setLayoutX(10);
			displayText.heightProperty().addListener((observable, oldValue, newValue) -> {
				serverApp.layout();
				display.setVvalue(1.0d);
			});

			// Add to display
			display.setContent(displayText);
			display.getStyleClass().add("scroll-bar");

			img = new ImageView(this.getClass().getResource("/res/icons/play.png").toExternalForm());
			img.setFitWidth(20);
			img.setPreserveRatio(true);
			sendBtn = new Button(" Connect");
			sendBtn.setGraphic((img));
			sendBtn.setId("serverBtn");
			sendBtn.setMinSize(200, 50);
			sendBtn.setLayoutX(serverAppW / 2 - 100);
			sendBtn.setLayoutY(serverAppH - 65);
			sendBtn.setOnAction(e -> {
				if (++counter == 1) {
					connect.start();
					img = new ImageView(this.getClass().getResource("/res/icons/power-off.png").toExternalForm());
					img.setFitWidth(20);
					img.setPreserveRatio(true);
					sendBtn.setGraphic((img));
					sendBtn.setText(" Disconnect");
				}
				if (counter == 3) {
					Platform.exit();
				}
			});

			serverApp.getChildren().addAll(display, sendBtn);
		}

		// Method to print to the screen

		/**
		 * This method prints to the GUI scereen.
		 * 
		 * @param text
		 *            The text to be printed
		 */
		public static void out(String text) {
			if (text.length() > 0 && text != null)
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						displayText.setText(message += text);
					}
				});
		}

		/**
		 * @return returns the Scene of the Interdace
		 */
		public Scene getMyScene() {
			paintScene();
			Scene scene = new Scene(serverApp, serverAppW, serverAppH);
			scene.setCursor(Cursor.HAND);
			scene.getStylesheets().add(this.getClass().getResource("/control/server.css").toExternalForm());
			return scene;
		}
	}

}