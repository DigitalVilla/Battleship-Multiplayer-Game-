package player;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * This Class holds useful attributes for a board's cell
 * 
 * @author DigitalVilla
 *
 */
public class Cell extends Rectangle implements Serializable {
	/**
	 * Auto generated id.
	 */
	private static final long serialVersionUID = -1512940617289535927L;
	/**
	 * Horizontal location of the cell
	 */
	public int x;
	/**
	 * Vertical location of the cell
	 */
	public int y;
	/**
	 * Index of the containing ship. -1: No Ship | 0:Carrier | 1:Destroyer |
	 * 2:Battleship | 3:Submarine | 4:Coast Guard
	 */
	public int shipIndx = -1;
	/**
	 * True if the cell was hit already
	 */
	public boolean wasShot = false;
	/**
	 * True if the cell belongs to the enemy's board
	 */
	public boolean isEnemy;

	/**
	 * Class' constructor
	 * 
	 * @param size
	 *            The size of one side of he squared cell
	 * @param enemy
	 *            True if the cell belongs to the enemy's board
	 */
	public Cell(int size, boolean enemy) {
		super(size, size);
		this.isEnemy = enemy;
		setStrokeWidth(1);
		setStroke(Color.AZURE);
		getStyleClass().add("gridLines");
		setStrokeType(StrokeType.INSIDE);
	}

	/**
	 * This method sets both X and Y properties.
	 * 
	 * @param x
	 *            Horizontal location of the cell
	 * @param y
	 *            Vertical location of the cell
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
}