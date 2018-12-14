package player;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import utilities.Pop_Box;

/**
 * This class Generates the game board GUI
 * 
 * @author DigitalVilla
 *
 */
public class GameBoard extends Thread implements Serializable {

	private static final long serialVersionUID = 5078280370652907276L;
	/**
	 * The size of each individual cell
	 */
	private static final int TILE_SIZE = 38;
	/**
	 * The number of cells per row
	 */
	private static final int TILE_NO = 10;
	/**
	 * An instance of Battleship
	 */
	public Battleship game;
	/**
	 * The with of the window
	 */
	private int screen_W;
	/**
	 * The height of the window
	 */
	private int screen_H;
	private Scene scene;
	/**
	 * The players board map
	 */
	public GridPane map;
	/**
	 * The enemy's board map
	 */
	public GridPane radar;
	public Pop_Box pop;
	public Menu menu;
	public ShipBox mySBox;

	/**
	 * Class constructor
	 * 
	 * @param game
	 */
	public GameBoard(Battleship game) {
		screen_W = game.player.getScreen_W();
		screen_H = game.player.getScreen_H();
		this.game = game;
		pop = new Pop_Box();
		genBoard();
	}

	// Getters and SETTERS

	/**
	 * Returns the scene containing the entire board game
	 * 
	 * @return
	 */
	public Scene getMyScene() {
		return scene;
	}

	/**
	 * This method paints the players boards
	 */
	public void genBoard() {
		mySBox = new ShipBox(TILE_SIZE, game);
		menu = new Menu(screen_H, game);
		ChatApp chat = game.player.getChat();
		chat.paint();

		// My board
		map = genGrid("map", false);
		map.setLayoutX(30);
		map.setLayoutY(270);

		// Enemy board
		radar = genGrid("radar", true);
		radar.setLayoutX(TILE_SIZE * 10 + 60);
		radar.setLayoutY(270);

		Pane gameBoard = new Pane();
		gameBoard.setId("gameBoard");
		gameBoard.getChildren().addAll(menu.getMenu(), mySBox.getShipBox(), map, radar, chat.getChatApp());

		scene = new Scene(gameBoard, screen_W, screen_H);
		scene.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
		scene.setCursor(Cursor.HAND);
	}

	/**
	 * This helper method generates the grid for each player board
	 * 
	 * @param name
	 *            A name that will be used as ID for the CSS file
	 * @param enemy
	 *            True if it will paint the enemy's board
	 * @return The grid of Cells
	 */
	private GridPane genGrid(String name, boolean enemy) {
		ArrayList<Cell> rows = new ArrayList<Cell>(10);
		GridPane grid = new GridPane();
		grid.setMinSize(TILE_SIZE * 10, TILE_SIZE * 10);
		grid.getStyleClass().add("boardGrid");
		grid.setId(name);
		grid.setVgap(0);
		grid.setHgap(0);
		int x = 0;
		int y = 0;

		for (int i = 0; i < (TILE_NO * TILE_NO); i++) {
			Cell cell = new Cell(TILE_SIZE, enemy);
			grid.add(cell, x, y);
			cell.setStrokeWidth(1);
			cell.setStroke(Color.AZURE);
			cell.setStrokeType(StrokeType.INSIDE);
			cell.getStyleClass().add("gridLines");
			cell.setXY(GridPane.getColumnIndex(cell), GridPane.getRowIndex(cell));
			cell.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> inGameHover(cell, e));
			cell.addEventHandler(MouseEvent.MOUSE_EXITED, e -> outGameHover(cell, e));
			cell.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gameCheck(cell, e));
			rows.add(cell);
			if ((y++ == 9)) {
				if (enemy) {
					game.myRadar.add(rows);
				} else {
					game.myMap.add(rows);
				}
				rows = new ArrayList<Cell>(10);
				y = 0;
				x++;
			}
		}
		return grid;
	}

	/**
	 * This methods handles a buttonClicked event
	 * 
	 * @param cell
	 *            The cell that trigger the event
	 * @param e
	 *            the event itself
	 */
	private void gameCheck(Cell cell, MouseEvent e) {
		if (cell.isEnemy && game.gameOn)
			game.attackShips(cell, e);
		else if (!cell.isEnemy && game.placingShips)
			game.placeShips(cell, e);
	}

	/**
	 * This methods handles a buttonEntered event
	 * 
	 * @param cell
	 *            The cell that trigger the event
	 * @param e
	 *            The event itself
	 */
	private void inGameHover(Cell cell, MouseEvent e) {
		if (cell.isEnemy && game.gameOn)
			game.inHoverAttack(cell, e);
		else if (!cell.isEnemy && game.placingShips)
			game.inHoverPlacing(cell);
	}

	/**
	 * This methods handles a buttonExited event
	 * 
	 * @param cell
	 *            The cell that trigger the event
	 * @param e
	 *            The event itself
	 */
	private void outGameHover(Cell cell, MouseEvent e) {
		if (cell.isEnemy && game.gameOn)
			game.outHoverAttack(cell, e);
		else if (!cell.isEnemy && game.placingShips)
			game.outHoverPlacing(cell);
	}

	/**
	 * This method enables the GUI buttons to allow users a rematch or a complete
	 * new total game.
	 * 
	 * @param msg
	 *            The String to display
	 * @param styleType
	 *            BackgrouSnd color: 1:Orange / 2:Green / 3:Blue
	 * @param height
	 *            The height of the box
	 */
	public void newConection(String msg, int styleType, int height) {
		// java.lang.IllegalStateException: Not on FX application thread; FIX
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// enable GUI
				menu.next_btn.setDisable(true);
				game.player.chat.getTxtFld().setEditable(false);
				if (styleType != 1) {
					menu.refresh_btn.setDisable(false);
					game.player.chat.getTxtFld().setEditable(true);
				}
				pop.out(msg, 20, 350, height, styleType);
				menu.quit_btn.setDisable(false);
			}
		});
	}
}
