package player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.StrokeType;
import utilities.Debug;
import utilities.Pop_Box;

/**
 * This Class holds the main actions and logic for Battleship Titanium.
 * 
 * @author DigitalVilla
 *
 */
public class Battleship implements Serializable {
	/**
	 * Auto generated id.
	 */
	private static final long serialVersionUID = 1002020204668371295L;
	/**
	 * Tracks the number of ships left on the board.
	 */
	public int totalShips;
	/**
	 * Index based on the ArrayList<Ship> ships
	 */
	public int currentShip;
	/**
	 * Instance of the GameBoard GUI
	 */
	public GameBoard board;
	/**
	 * Instance of the Player manager
	 */
	public Player player;
	/**
	 * True if the current player can attack
	 */
	public boolean myTurn;
	/**
	 * True if the placing ships is on
	 */
	public boolean placingShips;
	/**
	 * It defines the orientation for the placement of the ships
	 */
	public boolean vertical;
	/**
	 * It stores the 5 ships of the game:[ 0:Carrier, 1:Destroyer, 2:Battleship,
	 * 3:Submarine, 4:Coast Guard]
	 */
	private ArrayList<Ship> ships;
	/**
	 * The cells list that defines the placing limits for a ship.
	 */
	private ArrayList<Cell> selected;
	/**
	 * All of my ships positions in one array to be sent
	 */
	private ArrayList<Cell> mySelection;
	/**
	 * 2D array with the cells of the enemy's board.
	 */
	public ArrayList<ArrayList<Cell>> myRadar;
	/**
	 * 2D array with the cells of the player's board.
	 */
	public ArrayList<ArrayList<Cell>> myMap;
	/**
	 * instance of the dialogue/alert box
	 */
	public Pop_Box pop = new Pop_Box();
	/**
	 * Determines if this player won the match.
	 */
	public boolean winner;
	/**
	 * True if the current ship clashes with a placed ship
	 */
	private boolean overlay;
	/**
	 * True if the attack phase has started.
	 */
	public boolean gameOn;
	/**
	 * False if the hits array has not been reseted.
	 */
	private boolean resetHits;

	/**
	 * Tracks the number of true hits in the board
	 */
	private int trueHits;

	/**
	 * Class' constructor
	 * 
	 * @param player
	 *            the instance of the Player manager
	 */
	public Battleship(Player player) {
		this.player = player;
		init();
		board = new GameBoard(this);
	}

	private void init() {
		myMap = new ArrayList<ArrayList<Cell>>(10);
		myRadar = new ArrayList<ArrayList<Cell>>(10);
		ships = new ArrayList<Ship>(totalShips);
		currentShip = 0;
		trueHits = 0;
		totalShips = 5;
		placingShips = true;
		gameOn = false;
		vertical = false;
		winner = true;
		myTurn = false;
		resetHits = false;
		selected = new ArrayList<Cell>();
		ships.add(new Ship("Air Carrier", 5, this));
		ships.add(new Ship("Destroyer", 4, this));
		ships.add(new Ship("Battleship", 3, this));
		ships.add(new Ship("Submarine", 3, this));
		ships.add(new Ship("Coast Guard", 2, this));
	}

	/**
	 * This method Starts the GUI Platform for the battleship game.
	 */
	public void displayGUI() {
		player.getWindow().setScene(board.getMyScene());
	}

	//////////////////////////////////////////////////////
	//////////// GAME LOGIC | PLACING PHASE /////////////
	////////////////////////////////////////////////////

	/**
	 * The Cells' hover effect during the placing stage
	 * 
	 * @param cell
	 *            the cell that trigger the event
	 */
	public void inHoverPlacing(Cell cell) {
		overlay = false;
		if (ships.get(currentShip).isPlaced()) {
			paintShip(cell.x, cell.y, 2);
		} else {
			paintShip(cell.x, cell.y, 1);
		}
	}

	/**
	 * The Cells' hover effect during the placing stage
	 * 
	 * @param cell
	 */
	public void outHoverPlacing(Cell cell) {
		paintShip(cell.x, cell.y, 0);
	}

	/**
	 * Places the selected ship permanently on the board
	 * 
	 * @param cell
	 *            The cell that triggered this method
	 * @param event
	 *            MouseEvent that triggered this method
	 */
	public void placeShips(Cell cell, MouseEvent event) {
		if (ships.get(currentShip).isPlaced()) {
			return;
		}
		if (overlay == false) {
			// Select current ship
			Ship ship = ships.get(currentShip);
			for (Cell c : selected) {
				// Add Ships' index location to the cell's
				c.shipIndx = currentShip;
				// add cell to ship's location
				ship.addLocation(c);
			}
			// True when current ship has been placed
			ship.isPlaced(true);
			// reduce the number of ships to be placed
			if ((--totalShips) == 0) {
				// End placing phase
				placingShips = false;
				// Reset ships counter to track winner
				totalShips = 5;
				// Disable all ship's buttons in the ship box
				board.mySBox.unselect();
				// notify the handler that you are ready
				player.getPacket().setPacketType(7);
				player.sendPacket();
				// Print to self
				pop.out("Fantastic! \nOnce the enemy is ready \nthe match will start!", 20, 400, 200, 3);
			}
		}
	}

	/**
	 * This method sends all the ships's location to the opposite player.
	 */
	public void sendLocation() {
		// Send my ships location to enemy
		mySelection = new ArrayList<Cell>(17);
		for (Ship s : ships) {
			for (int i = 0; i < s.location.size(); i++) {
				Cell cell = s.location.get(i);
				mySelection.add(cell);
			}
		}
		// avoid sending msg at the same time;
		try {
			Thread.sleep((int) (Math.random() * 2000 + 500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player.getPacket().setEnemySelection(mySelection);
		player.getPacket().setPacketType(2);
		player.sendPacket();
		// setEnemyShips() will receive this package
	}

	/**
	 * This method receives the enemy's ships location. \nThis is the final set up
	 * method before the game starts.
	 */
	public void setEnemyShips() {
		Debug.out("EnemyShips recieved");
		// set enemy locations on myRadar
		for (int i = 0; i < 17; i++) {
			Cell c = (Cell) player.getPacket().getEnemySelection().get(i);
			myRadar.get(c.x).get(c.y).isEnemy = true;
			myRadar.get(c.x).get(c.y).shipIndx = c.shipIndx;
			// style to check accuracy
			// cellStyle(myRadar.get(c.x).get(c.y), 2);
		}

		// inform handler that ships were received.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player.getPacket().setPacketType(7);
		player.sendPacket();
		// received the package ready to start attacks
		gameOn = true;
	}

	//////////////////////////////////////////////////////
	/////////// GAME LOGIC | ATTACKING PHASE ////////////
	////////////////////////////////////////////////////
	/**
	 * The Cells' hover effect during the attacking stage
	 * 
	 * @param cell
	 *            The cell that triggered this method
	 * @param event
	 *            MouseEvent that triggered this method
	 */
	public void inHoverAttack(Cell cell, MouseEvent e) {
		if (!gameOn || !myTurn || cell.wasShot)
			return;
		cellStyle(cell, 3);
	}

	/**
	 * The Cells' hover effect during the attacking stage
	 * 
	 * @param cell
	 *            The cell that triggered this method
	 * @param event
	 *            MouseEvent that triggered this method
	 */
	public void outHoverAttack(Cell cell, MouseEvent event) {
		if (!gameOn || !myTurn || cell.wasShot)
			return;
		cellStyle(cell, 0);
	}

	/**
	 * This method sets the hit value (hit/miss) of the selected cell.
	 * 
	 * @param cell
	 *            The cell that triggered this method
	 * @param event
	 *            MouseEvent that triggered this method
	 */
	public void attackShips(Cell cell, MouseEvent event) {
		if (!gameOn || cell.wasShot || !myTurn)
			return;
		cell.wasShot = true;
		myTurn = false;

		if (!resetHits) {
			// Clear hits array to not mix data
			player.getPacket().resetHits();
			resetHits = true;
		}

		// GUI UPDATE
		if (cell.shipIndx == -1) {
			// The cell is a regular ship
			setShootImage(cell, false);
		} else if (cell.shipIndx != -1) {
			// The cell belongs to a ship
			setShootImage(cell, true);
			// If trueHits is not 17, an extra try is granted
			myTurn = (++trueHits != 17);
		}

		// Add current hit to the list.
		player.getPacket().addHit(cell);
		// when turn ends send the update
		if (!myTurn) {
			board.menu.next_btn.setDisable(true);
			player.getPacket().setPacketType(2);
			player.sendPacket();
		}
	}

	/**
	 * This method receives the enemy's attacks and updates the GUI.
	 * 
	 */
	public void playTurn() {
		try {
			int indx = 0;
			String msg = "";
			Debug.out(player.getPacket().getHits().size());
			// Update Player GUI
			for (Cell c : player.getPacket().getHits()) {
				indx = c.shipIndx;
				// If the cell was true hit
				if (indx != -1) {
					// Add a hit to the ship
					ships.get(indx).hit();
					// Update board image
					setShootImage(getCell(c.x, c.y, false), true);
					// Update ShipBox damage image;
					int damage = ships.get(indx).size - ships.get(indx).health;
					board.mySBox.setHitImage(indx, damage);
					// If a ship got sunk
					if (!ships.get(indx).isAlive()) {
						// Update the number of ships left
						totalShips--;
						// Add the sunk ship alert to msg
						msg += player.getPlayerName() + ": My " + ships.get(indx).type + " has sunk!\n";
					}
				} else { // If the cell is a miss instead
					setShootImage(getCell(c.x, c.y, false), false);
				}
				// A small delay to play boats animation
				Thread.sleep(500);
			}
			// Send message of ships destroyed for the chat window
			player.getPacket().setMessage(msg);
			player.getPacket().setPacketType(1);
			player.sendPacket();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Check winning condition
		winCondition();
	}

	/**
	 * Updates the hit/miss background image of a cell
	 * 
	 * @param cell
	 *            Cell to change background image
	 * @param hit
	 *            True if the ship was hit
	 */
	public void setShootImage(Cell cell, boolean hit) {
		String name = (hit) ? "hit" : "miss";
		Image img = new Image(this.getClass().getResource("/res/icons/" + name + ".png").toExternalForm());
		cell.setStrokeWidth(2);
		cell.setStroke(Color.web("#FF5042"));
		cell.setStrokeType(StrokeType.INSIDE);
		cell.setFill(new ImagePattern(img));
	}

	//////////////////////////////////////////////////////
	//////////// GAME CONDITIONS / CONNECTIONS //////////
	////////////////////////////////////////////////////

	/**
	 * This method validates if the player can play next turn or if the game is
	 * over.
	 */
	public void winCondition() {
		if (totalShips == 0) { // All ships were sunk
			winner = false;
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					player.getPacket().setPacketType(5);
					player.sendPacket();
				}
			}, 2 * 1000);
		} else { // The player's turn starts.
			myTurn = true;
			resetHits = false;
			board.menu.next_btn.setDisable(false);
		}
	}

	/**
	 * This method allows the users to keep paired and play another duel.
	 */
	public void rematch() {
		resetGame();
		// Skip reconnection
		player.setUpBoard();
		// inform the server about the rematch
		player.getPacket().setPacketType(7);
		player.sendPacket();
	}

	/**
	 * This method quits the current
	 */
	public void quitAndPlay() {
		// notify enemy player you quit
		player.getPacket().setPacketType(4);
		player.sendPacket();
		resetGame();
		player.connect();
	}

	/**
	 * This method resets the field values
	 */
	public void resetGame() {
		// Refresh packet data
		player.getPacket().init();
		// Refresh messages reset screen
		player.chat.getDisplayText().setText("Waiting for the enemy player... \n");
		player.chat.setFirstText(true);
		player.chat.getTxtFld().setEditable(false);
		player.chat.init();
		// Reset GUI
		player.game = new Battleship(player);
	}

	/**
	 * This method sends a warning to the enemy player and closes the entire program
	 */
	public void closeGame() {
		Debug.out("Closing Program");
		player.getPacket().setPacketType(4);
		player.sendPacket();
		board.pop.out("Thanks for playing \n Battleship Titanium! ", 24, 370, 180, 1);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Finishes the JavaFx app.
				Platform.exit();
			}
		}, 2 * 1000);

	}

	////////////////////////////////////////////
	//////////// SUPPORT METHODS /////////////
	//////////////////////////////////////////

	/**
	 * This method paints the selected cells containing a ship.
	 * 
	 * @param x
	 *            Horizontal location of the cell
	 * @param y
	 *            Vertical location of the cell
	 * @param style
	 *            0:none | 1:green | 2:red
	 */
	private void paintShip(int x, int y, int style) {
		Ship ship = ships.get(currentShip);
		int shipSize = ship.size;
		if (placingShips) {
			selected = new ArrayList<Cell>();
		}
		for (int i = 0; i < shipSize; i++) {
			if (y < 10 && x < 10) {
				// Check if the cell doesn't belong to a ship
				if (getCell(x, y, false).shipIndx == -1) {
					// find cell, give it style and add it to current selection
					selected.add(cellStyle(getCell(x, y, false), style));
				} else {
					// if ship overlays another ship quit painting
					overlay = true;
					return;
				}
				if (vertical)
					y++; // grow vertically
				else
					x++; // grow horizontally
			} else {
				// if ship size is bigger than board size, start painting at the end - size
				// regardless.
				if (vertical)
					paintShip(x, y - shipSize, style);
				else
					paintShip(x - shipSize, y, style);
			}
		}
	}

	/**
	 * This cell retrieves the specific cell from the specific board
	 * 
	 * @param x
	 *            horizontal location
	 * @param y
	 *            vertical location
	 * @param enemy
	 *            set true if the cell belongs to enemy board
	 */
	public Cell getCell(int x, int y, boolean enemy) {
		return (!enemy) ? myMap.get(x).get(y) : myRadar.get(x).get(y);
	}

	/**
	 * This method paints a single cell with the specified style
	 * 
	 * @param cell
	 *            the cell to be styled
	 * @param style
	 *            0:none | 1:green | 2:red
	 * @return the styled cell
	 */
	private Cell cellStyle(Cell cell, int style) {
		switch (style) {
		case 0:
			cell.setStrokeWidth(1);
			cell.setStroke(Color.AZURE);
			cell.setStrokeType(StrokeType.INSIDE);
			// if the game started do not change fill background
			if (placingShips)
				cell.setFill(null);
			break;
		case 1:
			cell.setFill(Color.web("#30FF42"));
			break;
		case 2:
			cell.setFill(Color.web("#FF7062"));
			break;
		case 3:
			cell.setStrokeWidth(4);
			cell.setStroke(Color.web("#FF5042"));
			cell.setStrokeType(StrokeType.INSIDE);
			cell.setFill(Color.web("#FF504200"));
			break;
		}
		return cell;
	}

	/**
	 * Unimplemented class that is called by the play button. clicked.
	 */
	public void gameOn() {
		if (gameOn)
			return;
		// readyToPlay();
	}

	/**
	 * This method calls the pop Dialogue/alert window to print the message within
	 * the DataPacket The DataPacket must also contain a valid diologueParams
	 */
	public void out() {
		int[] params = player.getPacket().getDialogueParams();
		pop.out(player.getPacket().getMessage(), params[0], params[1], params[2], params[3]);
	}
}
