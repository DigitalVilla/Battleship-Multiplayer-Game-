package player;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * This class paints the ship box that allows the user to place a ship.
 * 
 * @author DigitalVilla
 *
 */
public class ShipBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2755006005599275355L;
	private ImageView carrier;
	private ImageView destroyer;
	private ImageView battleship;
	private ImageView submarine;
	private ImageView coastGuard;
	private Battleship game;
	private int tileSize;
	private Pane shipBox;
	/**
	 * A list containing all the widths of the ships.
	 */
	private ArrayList<Integer> shWidth;
	/**
	 * 
	 */
	private ArrayList<ImageView> ships;
	/**
	 * List that contains all the String names of the ships
	 */
	private String[] imgs;

	/**
	 * Class constructor
	 * 
	 * @param tileSize
	 *            The number of cells this ship should occupy
	 * @param game
	 *            and instance to the Battleship game.
	 */
	public ShipBox(int tileSize, Battleship game) {
		imgs = new String[] { "carrier", "destroyer", "battleship", "submarine", "coastGuard" };
		this.tileSize = tileSize;
		this.game = game;
		setupBox();
	}

	/**
	 * This class paints the box and its ships.
	 */
	public void setupBox() {
		shWidth = new ArrayList<Integer>(5);
		shWidth.add(400);
		shWidth.add(320);
		shWidth.add(240);
		shWidth.add(240);
		shWidth.add(160);

		shipBox = new Pane();
		double boxWith = tileSize * 20 + 40;
		shipBox.setId("shipBox");
		shipBox.setMinSize(boxWith, 180);
		shipBox.setMaxSize(boxWith, 180);
		shipBox.setLayoutX(30);
		shipBox.setLayoutY(50);

		// Ships
		carrier = new ImageView(this.getClass().getResource("/res/images/s." + imgs[0] + ".png").toExternalForm());
		// carrier.setFitHeight(100);
		carrier.setFitWidth(shWidth.get(0));
		carrier.setPreserveRatio(true);
		carrier.setLayoutX(10);
		carrier.setLayoutY(10);
		carrier.setOnMousePressed(e -> selected(0, carrier));

		destroyer = new ImageView(this.getClass().getResource("/res/images/s." + imgs[1] + ".png").toExternalForm());
		destroyer.setFitWidth(shWidth.get(1));
		destroyer.setPreserveRatio(true);
		destroyer.setLayoutX(boxWith / 2 + 40);
		destroyer.setLayoutY(15);
		destroyer.setId("destroyer");
		destroyer.setOnMousePressed(e -> selected(1, destroyer));

		battleship = new ImageView(this.getClass().getResource("/res/images/s." + imgs[2] + ".png").toExternalForm());
		battleship.setFitWidth(shWidth.get(2));
		battleship.setPreserveRatio(true);
		battleship.setLayoutX(30);
		battleship.setLayoutY(90);
		battleship.setOnMousePressed(e -> selected(2, battleship));

		submarine = new ImageView(this.getClass().getResource("/res/images/s." + imgs[3] + ".png").toExternalForm());
		submarine.setFitWidth(shWidth.get(3));
		submarine.setPreserveRatio(true);
		submarine.setLayoutX(325);
		submarine.setLayoutY(110);
		submarine.setOnMousePressed(e -> selected(3, submarine));

		coastGuard = new ImageView(this.getClass().getResource("/res/images/s." + imgs[4] + ".png").toExternalForm());
		coastGuard.setFitWidth(shWidth.get(4));
		coastGuard.setPreserveRatio(true);
		coastGuard.setLayoutX(620);
		coastGuard.setLayoutY(100);
		coastGuard.setOnMousePressed(e -> selected(4, coastGuard));

		// add ships to box
		shipBox.getChildren().addAll(carrier, destroyer, battleship, submarine, coastGuard);
		selected(0, carrier);
	}

	/**
	 * the ship box
	 * 
	 * @return shipBox
	 */
	public Pane getShipBox() {
		return shipBox;
	}

	/**
	 * @param shipBox
	 */
	public void setShipBox(Pane shipBox) {
		this.shipBox = shipBox;
	}

	/**
	 * this helper method adds the effect of being clicked.
	 * 
	 * @param shipNo
	 * @param source
	 */
	private void selected(int shipNo, ImageView source) {
		unselect();
		if (!game.placingShips)
			return;
		game.currentShip = shipNo;
		source.setFitWidth(shWidth.get(shipNo) * 1.05);
		source.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(250,250,250,0.9), 4, 4, 2, 2);");
	}

	/**
	 * This helper method sets the ship back to normal.
	 */
	public void unselect() {
		ships = new ArrayList<ImageView>(5);
		ships.add(carrier);
		ships.add(destroyer);
		ships.add(battleship);
		ships.add(submarine);
		ships.add(coastGuard);

		for (int i = 0; i < ships.size(); i++) {
			ships.get(i).setFitWidth(shWidth.get(i));
			ships.get(i).setStyle("-fx-effect: none;");
		}
	}

	/**
	 * This helper method changes the damage when a ship is hit.
	 * 
	 * @param shipIndex
	 *            the index of the ship [ 0:Carrier, 1:Destroyer, 2:Battleship,
	 *            3:Submarine, 4:Coast Guard]
	 * @param damage
	 *            the counter to select the right damage image (relative to the size
	 *            of the ship)
	 */
	public void setHitImage(int shipIndex, int damage) {
		ships.get(shipIndex).setImage(new Image(
				this.getClass().getResource("/res/images/s." + imgs[shipIndex] + damage + ".png").toExternalForm()));
		ships.get(shipIndex).setFitWidth(shWidth.get(shipIndex));
		carrier.setPreserveRatio(true);
	}
}
