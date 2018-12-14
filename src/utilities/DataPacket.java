package utilities;

import java.io.Serializable;
import java.util.ArrayList;

import player.Cell;

/**
 * This is the communication data protocol sent through the net.
 * 
 * @author DigitalVilla
 *
 */
public class DataPacket implements Serializable {

	/**
	 * Auto generated key
	 */
	private static final long serialVersionUID = -6244599500903946101L;
	private String message;
	private int packetType;
	private ArrayList<Cell> hits;
	private int[] dialogueParams;
	private ArrayList<Cell> enemySelection;

	/**
	 * Class' constructor\n It calls refresh to set up the field's values;
	 */
	public DataPacket() {
		init();
	}

	// SETTERS and GETTERS
	/**
	 * @return Retrieves the enemy's board cells that contain a ship.
	 */
	public ArrayList<Cell> getEnemySelection() {
		return enemySelection;
	}

	/**
	 * @return The message sent
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            The message to be sent
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * It resets the contents of DataPacket
	 */
	public void init() {
		this.message = "";
		this.packetType = -1;
		this.dialogueParams = new int[4];
		this.hits = new ArrayList<Cell>();
	}

	/**
	 * It sets the parameters for the Pop_box.out() method. dialogueParams 1) The
	 * pixel size of the text font 2) Width - Horizontal size of window 3) Height -
	 * Vertical size of window 4) BackgrouSnd-color: 1:Orange / 2:Green / 3:Blue
	 * 
	 * @return dialogueParams
	 */
	public int[] getDialogueParams() {
		return dialogueParams;
	}

	/**
	 * @param dialogueParams
	 *            1) The pixel size of the text font 2) Width - Horizontal size of
	 *            window 3) Height - Vertical size of window 4) BackgrouSnd-color:
	 *            1:Orange / 2:Green / 3:Blue
	 */
	public void setDialogueParams(int[] dialogueParams) {
		this.dialogueParams = dialogueParams;
	}

	/**
	 * It retrieves the protocol standard followed by this DataPacket.\n 1: Message
	 * | 2: Game logic | 3:Dialogue window | 4:disconnection | 5: win/refresh | 7:
	 * Connection and Phase logic;
	 * 
	 * @return packetType
	 */
	public int getPacketType() {
		return packetType;
	}

	/**
	 * 1: Message | 2: Game logic | 3:Dialogue window | 4:disconnection | 5:
	 * win/refresh | 7: Connection and Phase logic;
	 * 
	 * @param packetType
	 */
	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}

	/**
	 * Add a hit cell to the hits ArrayList
	 * 
	 * @param cell
	 *            the hit cell
	 */
	public void addHit(Cell cell) {
		hits.add(cell);
	}

	/**
	 * Clear the hits array
	 */
	public void resetHits() {
		this.hits = new ArrayList<Cell>();
		this.hits.clear();
	}

	/**
	 * it retrieves the cells that were hit during each player's turn.
	 * 
	 * @return
	 */
	public synchronized ArrayList<Cell> getHits() {
		return hits;
	}

	/**
	 * This method clones the selection of cells into emeySelection List.
	 * 
	 * @param selction
	 */
	@SuppressWarnings("unchecked")
	public void setEnemySelection(ArrayList<Cell> selction) {
		enemySelection = (ArrayList<Cell>) selction.clone();
	}
}
