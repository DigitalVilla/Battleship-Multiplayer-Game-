package player;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class paints the menu for the game board
 * 
 * @author DigitalVilla
 *
 */
public class Menu implements Serializable {
	private static final long serialVersionUID = -1720956747260677182L;
	/**
	 * This is the counter to toggle the audio images
	 */
	private int toggle = 0;
	private Button audio_btn;
	private Button exit_btn;
	private VBox menu;
	private static MediaPlayer mPlayer;
	private int screen_H;
	public Button next_btn;
	private Button off_btn;
	private int counter = 0;
	private Battleship game;
	public Button refresh_btn;
	public Button quit_btn;

	/**
	 * This constructor creates an exit button for the welcome screen
	 */
	public Menu() {
		exitBtn();
	}

	/**
	 * This constructor generates the menu for the game board
	 * 
	 * @param screen_H
	 *            the height of the menu
	 * @param game
	 *            the instance of the Battleship to access its game logic
	 */
	public Menu(int screen_H, Battleship game) {
		this.screen_H = screen_H;
		paintMenu();
		this.game = game;
	}

	/// GETTERS & SETTERS

	/**
	 * Retrieves the entire menu bar
	 * 
	 * @return menu
	 */
	public Pane getMenu() {
		return menu;
	}

	/**
	 * Returns the exit_btn for the welcome window
	 * 
	 * @return exit_btn
	 */
	public Button getExit_btn() {
		return exit_btn;
	}

	/**
	 * This method paints the menu bar and its elements
	 */
	public void paintMenu() {
		menu = new VBox(30);
		double boxWith = 100;
		double boxHeight = screen_H - 120;
		menu.setId("menu");
		menu.setMinSize(boxWith, boxHeight);
		menu.setMaxSize(boxWith, boxHeight);
		menu.setLayoutX(860);
		menu.setLayoutY(50);

		menu.setAlignment(Pos.CENTER);

		next_btn = new Button();
		ImageView img = new ImageView(this.getClass().getResource("/res/icons/play.png").toExternalForm());
		img.setFitWidth(30);
		img.setPreserveRatio(true);
		// next_btn.setStyle("-fx-opacity:.7");
		next_btn.setGraphic(img);
		next_btn.setOnAction(e -> {
			// next_btn.setDisable(true);
			// game.gameOn();
		});

		next_btn.getStyleClass().add("menu_btns");
		next_btn.setLayoutX(10);
		next_btn.setLayoutY(20);
		next_btn.setDisable(true);

		Button help_btn = new Button();
		img = new ImageView(this.getClass().getResource("/res/icons/question.png").toExternalForm());
		img.setFitHeight(35);
		img.setPreserveRatio(true);
		// next_btn.setStyle("-fx-opacity:.7");
		help_btn.setGraphic(img);
		help_btn.setOnAction(null);
		help_btn.getStyleClass().add("menu_btns");
		help_btn.setLayoutX(10);
		help_btn.setLayoutY(20);
		// help_btn.setDisable(true);

		refresh_btn = new Button();
		img = new ImageView(this.getClass().getResource("/res/icons/refresh.png").toExternalForm());
		img.setFitHeight(35);
		img.setPreserveRatio(true);
		// next_btn.setStyle("-fx-opacity:.7");
		refresh_btn.setGraphic(img);
		refresh_btn.setOnAction(e -> game.rematch());
		refresh_btn.getStyleClass().add("menu_btns");
		refresh_btn.setLayoutX(10);
		refresh_btn.setLayoutY(20);
		refresh_btn.setDisable(true);

		quit_btn = new Button();
		img = new ImageView(this.getClass().getResource("/res/icons/flag.png").toExternalForm());
		img.setFitHeight(35);
		img.setPreserveRatio(true);
		// next_btn.setStyle("-fx-opacity:.7");
		quit_btn.setGraphic(img);
		quit_btn.setOnAction(e -> game.quitAndPlay());
		quit_btn.getStyleClass().add("menu_btns");
		quit_btn.setLayoutX(10);
		quit_btn.setLayoutY(20);
		quit_btn.setDisable(true);

		Button turn_btn = new Button();
		img = new ImageView(this.getClass().getResource("/res/icons/repeat.png").toExternalForm());
		img.setFitHeight(35);
		img.setPreserveRatio(true);
		// next_btn.setStyle("-fx-opacity:.7");
		turn_btn.setGraphic(img);
		turn_btn.setOnAction(e -> {
			game.vertical = !game.vertical;
		});
		turn_btn.getStyleClass().add("menu_btns");
		turn_btn.setLayoutX(10);
		turn_btn.setLayoutY(20);
		// turn_btn.setDisable(true);

		off_btn = new Button();
		img = new ImageView(this.getClass().getResource("/res/icons/power-off.png").toExternalForm());
		img.setFitWidth(25);
		img.setPreserveRatio(true);
		// next_btn.setStyle("-fx-opacity:.7");
		off_btn.setGraphic(img);
		off_btn.setOnAction(e -> closeProgram());
		off_btn.getStyleClass().add("menu_btns");
		off_btn.setLayoutX(10);
		off_btn.setLayoutY(20);

		// Background music
		Media soundtrack = new Media(this.getClass().getResource("/res/media/soundtrack.wav").toExternalForm());

		mPlayer = new MediaPlayer(soundtrack);
		mPlayer.setCycleCount(4);
		mPlayer.setVolume(1);
		// mPlayer.setAutoPlay(true);

		// mPlayer.setBalance(-3.0);

		// Audio BUTTON
		audio_btn = new Button();
		audio_btn.setGraphic(new ImageView(this.getClass().getResource("/res/icons/volume-off.png").toExternalForm()));
		audio_btn.setOnAction(e -> {
			mPlayer.play();
			audioAction(audio_btn);
		});
		audio_btn.getStyleClass().add("menu_btns");
		audio_btn.setLayoutX(20);
		menu.getChildren().addAll(off_btn, turn_btn, audio_btn, help_btn, quit_btn, refresh_btn, next_btn);

	}

	/**
	 * This button closes the entire application and warns the opposite player about
	 * the disconnection. This button requires to be double clicked in order to
	 * close.
	 */
	private void closeProgram() {
		Timer timer = new Timer();
		// double click fool proof
		if (counter++ >= 1) {
			game.closeGame();
		}
		// reset counter after 2 seconds
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				counter = 0;
			}
		}, 2 * 1000);
	}

	/**
	 * This method returns the exit button that will be placed on the welcome screen
	 */
	private void exitBtn() {
		exit_btn = new Button();
		ImageView off = new ImageView(this.getClass().getResource("/res/icons/power-off.png").toExternalForm());
		off.setFitWidth(30);
		off.setPreserveRatio(true);
		off.setStyle("-fx-opacity:.9");
		exit_btn.setGraphic(off);

		exit_btn.setOnAction(e -> System.exit(0));
		exit_btn.getStyleClass().add("util_btns");
		exit_btn.setLayoutX(10);
		exit_btn.setLayoutY(20);
	}

	/**
	 * This method sets 3 different volume icons.
	 * 
	 * @param audio
	 *            Button element to assign the volume icon
	 */
	private void audioAction(Button audio) {
		switch (toggle) {
		case 2:
			audio.setGraphic(new ImageView(this.getClass().getResource("/res/icons/volume-off.png").toExternalForm()));
			mPlayer.setVolume(0);
			toggle = 0;
			break;
		case 0:
			audio.setGraphic(new ImageView(this.getClass().getResource("/res/icons/volume-down.png").toExternalForm()));
			mPlayer.setVolume(0.4);
			toggle++;
			break;
		case 1:
			audio.setGraphic(new ImageView(this.getClass().getResource("/res/icons/volume-up.png").toExternalForm()));
			mPlayer.setVolume(1);
			toggle++;
			break;
		}
	}
}
