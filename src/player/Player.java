package player;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.stage.Stage;
import utilities.DataPacket;
import utilities.Debug;
import utilities.InputListener;
import utilities.Pop_Box;

/**
 * This class is the brains of the application. It handles the connection
 * between player and server. As well as it decides the order in which order the
 * methods in the Battleship class are called. for the player's connection. It
 * implements the Observer interface and observes InputLitener class' updates.
 * 
 * @author DigitalVilla
 *
 */
public class Player implements Observer, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2671535564864176130L;
	// NETWORK VARIABLES
	/**
	 * Username of the player
	 */
	private static String playerName = System.getProperty("user.name");
	/**
	 * IPv4 Address of the server
	 */
	private String ipAddress = "localhost";

	/**
	 * Port in which the server communicates
	 */
	private int portNumber = 1212;
	/**
	 * Writer to send messages to the server.
	 */
	private ObjectOutputStream writer;
	/**
	 * The communication data protocol sent through the net.
	 */
	private DataPacket packet;
	/**
	 * The the socket that connects to the server
	 */
	private Socket client;

	// GUI VARIABLES

	/**
	 * the screen size of the host system.
	 */
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * Width of the application
	 */
	private int screen_W = 1360;
	/**
	 * Height of the application
	 */
	private int screen_H = 740;
	public Pop_Box pop;
	/**
	 * The primaryStage of the Application
	 */
	private Stage window;
	public ChatApp chat;
	public Battleship game;
	/**
	 * True when both players are connected
	 */
	public boolean matchFound = false;

	/**
	 * Class constructor
	 * 
	 * @param primaryStage
	 *            the primaryStage from the JavaFX application
	 */
	public Player(Stage primaryStage) {
		window = primaryStage;
		packet = new DataPacket();
		chat = new ChatApp(this);
		pop = new Pop_Box();
		game = new Battleship(this);
	}

	// SETTERS & GETTERS
	/**
	 * @return chat
	 */
	public ChatApp getChat() {
		return chat;
	}

	/**
	 * @return packet
	 */
	public DataPacket getPacket() {
		return packet;
	}

	/**
	 * @param packet
	 */
	public void setPacket(DataPacket packet) {
		this.packet = packet;
	}

	/**
	 * @return playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param name
	 */
	public void setPlayerName(String name) {
		playerName = name;
	}

	/**
	 * @return ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return portNumber
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * @param portNumber
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * @return screen_W
	 */
	public int getScreen_W() {
		return screen_W;
	}

	/**
	 * @return screen_H
	 */
	public int getScreen_H() {
		return screen_H;
	}

	/**
	 * @return window
	 */
	public Stage getWindow() {
		return this.window;
	}

	/**
	 * Initiates the connection with the Game server
	 */
	public void connect() {
		// Start connection
		Debug.out("Trying to conect...");
		try {
			client = new Socket(ipAddress, portNumber);
			InputListener in = new InputListener(this, client, 0);
			in.start();
			// Creating an output stream to send text to the client
			writer = new ObjectOutputStream(client.getOutputStream());
			setUpBoard();
		} catch (UnknownHostException e) {
			pop.out("We Could not connect with the Server. Please try again", 16, 300, 150, 1);
			e.printStackTrace();
		} catch (IOException e) {
			pop.out("The server is offline. \nPlease try again later", 18, 270, 150, 1);
			e.printStackTrace();
		} catch (Exception e) {
			pop.out("The server is offline. \nPlease try again later", 18, 270, 150, 1);
		}
	}

	/**
	 * This method initiates the GUI board
	 */
	public void setUpBoard() {
		matchFound = false;
		game.displayGUI();
		pop.out("Welcome " + getPlayerName() + "! \n" + "Set all your ships  while waiting for the enemy player...", 18,
				350, 200, 2);
	}

	@Override
	public void update(Observable listener, Object arg) {
		packet = ((InputListener) listener).getPacket();
		switch (packet.getPacketType()) {
		case 1:
			updateChat();
			break;
		case 2:
			updateGameLogic();
			break;
		case 3:
			popWindow();
			break;
		case 4:
		case 5:
			refreshGame(packet.getPacketType());
			break;
		case 0:
			setFirstPlayer();
			break;
		case 6:
			startGame();
			break;
		case 7:
			setUpGame();
			break;
		}
	}

	/**
	 * This method alerts the players that the game has started.
	 */
	private void startGame() {
		// Alert that the game has started
		packet.setDialogueParams(new int[] { 20, 400, 450, 3 });
		String msg = (game.myTurn) ? "YOU ARE FIRST!" : "YOU ARE SECOND!";
		msg += "\n\n When the PLAY button is ON\nyou can shoot the enemy once.";
		msg += "\n\nWhen the PLAY button is OFF\n it is your enemy's turn to attack.";
		msg += "\n\nNOTE: If you hit a target\n you get an extra shot!";
		// Print Message
		packet.setMessage(msg);
		game.out();
	}

	/**
	 * This method is called when the placing phase has ended
	 */
	private void setUpGame() {
		Debug.out("setUpGame: Loacation");
		game.sendLocation();
	}

	/**
	 * This method randomly selects the first player.
	 */
	private void setFirstPlayer() {
		// You are going first
		game.myTurn = true;
		game.board.menu.next_btn.setDisable(false);
		Debug.out("setFirstPlayer recieved");
	}

	/**
	 * This method calls the Battleshipo's out method
	 */
	private void popWindow() {
		game.out();
	}

	/**
	 * This method refreshes the game once the game is over.
	 * 
	 * @param packageType
	 */
	private void refreshGame(int packageType) {
		String msg = "\n";
		int styleType = 0;
		if (packageType == 5) {
			msg += (game.winner) ? "YOU HAVE WON!" : "YOU HAVE LOST!";
			msg += "\n\nPress the REFRESH button \n for a Rematch!";
			msg += "\n\nPress the FLAG to Quit \n and find a new oponent!";
			styleType = 2;
		} else if (packageType == 4) {
			msg = "The enemy has disconnected.\n\nPress the FLAG button \n to find a new oponent";
			styleType = 1;
		}
		getPacket().setMessage(msg);
		int height = (packageType == 4) ? 250 : 370;
		game.board.newConection(packet.getMessage(), styleType, height);
	}

	/**
	 * This method receives the enemy's positions to set up in the enemy's board.
	 */
	private void updateGameLogic() {
		// if game has not started
		if (!game.gameOn) {
			game.setEnemyShips();
			return;
		}
		game.playTurn();
	}

	/**
	 * This method prints to the chat
	 */
	private void updateChat() {
		matchFound = true;
		chat.enableChat();
	}

	/**
	 * This method send the DataPacket to the server.
	 */
	public void sendPacket() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					writer.writeObject(packet);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
