package player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This Class represents a Ship element for the game logic
 * 
 * @author DigitalVilla
 *
 */
public class Ship implements Serializable {
	private static final long serialVersionUID = -3412002264160410698L;
	/**
	 * The number of Cells this Ship occupies
	 */
	public int size;
	/**
	 * Equals this Ship's size and it decreases as it gets hit.
	 */
	public int health;
	/**
	 * The list of cells in the board where this boat was placed.
	 */
	public ArrayList<Cell> location;

	/**
	 * False if this boat has not been placed yet.
	 */
	private boolean placed;
	/**
	 * The type/name of this ship
	 */
	public String type;

	/**
	 * Class constructor
	 * 
	 * @param type
	 *            The type/name of this ship
	 * @param size
	 *            The number of Cells this Ship occupies
	 * @param game
	 *            The instance of the Battleship class
	 */
	public Ship(String type, int size, Battleship game) {
		location = new ArrayList<Cell>(size);
		this.size = size;
		this.type = type;
		this.health = size;
	}

	/**
	 * This method reduces the health of the ship by 1
	 */
	public void hit() {
		--health;
	}

	/**
	 * @return True if the ship's health is more than 0.
	 */
	public boolean isAlive() {
		return health > 0;
	}

	/**
	 * Set the placed parameter
	 * 
	 * @param placed
	 *            true if the ship is set on the board
	 */
	public void isPlaced(boolean placed) {
		this.placed = placed;
	}

	/**
	 * Verify if the board is set already set on the board
	 * 
	 * @return true if the ship is set on the board
	 */
	public boolean isPlaced() {
		return placed;
	}

	/**
	 * Add the cell that belongs to this ship
	 * 
	 * @param cell
	 */
	public void addLocation(Cell cell) {
		this.location.add(cell);
	}

}
