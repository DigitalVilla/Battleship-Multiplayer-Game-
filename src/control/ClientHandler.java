package control;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import control.ServerGUI.ServerUI;
import utilities.DataPacket;
import utilities.Debug;
import utilities.InputListener;

/**
 * This class handles dual connection to allow 1vs1 battleship duels. If a
 * player disconnects, the connected player is sent back to the server queue.
 * and this
 * 
 * @author DigitalVilla
 *
 */
public class ClientHandler extends Thread implements Observer {

	// holds the connected Socket clients
	private ArrayList<Socket> clients;
	// Object writers for each Socket
	private ObjectOutputStream writerA;
	private ObjectOutputStream writerB;
	// Main communication protocol
	private DataPacket packet;
	// used by connectionLogic() to change game phases.
	private int counter;

	/**
	 * Main constructor that initiates clients, server and counter.
	 * 
	 * @param string
	 *            The number that identifies this Thread
	 * 
	 * @param connection
	 *            Connection class that adds Socket clients to ClientHandler
	 */
	public ClientHandler(String name) {
		super(name);
		clients = new ArrayList<Socket>(2);
		Debug.setLevel(1);
	}

	@Override
	public void run() {
		ServerUI.out("\n" + this.getName() + " has connected " + clients.size() + " clients.");
		try {
			for (int i = 0; i < clients.size(); i++) {
				InputListener in = new InputListener(this, clients.get(i), i + 1);
				in.start();
			}
			setWriters();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// GETTERS AND SETTERS
	/**
	 * Add a Socket client to ArrayList<Socket> clients.
	 * 
	 * @param client
	 *            the Socket to be added
	 */
	public void addClient(Socket client) {
		clients.add(client);
	}

	/**
	 * This method informs the current capacity of the ArrayList<Socket> clients
	 * 
	 * @return It will return true only when there are 2 Socket clients in the
	 *         ArrayList<Socket> clients.
	 */
	public boolean isReady() {
		if (clients.size() == 2)
			return true;
		return false;
	}

	@Override
	public void update(Observable listener, Object args) {
		packet = ((InputListener) listener).getPacket();
		switch (packet.getPacketType()) {
		case 2:
		case 3:
			// UNICAST
			gameLogic(((InputListener) listener).getClientNumber());
			break;
		case 4:
			// UNICAST close
			disconnect(((InputListener) listener).getClientNumber());
			break;
		case 1:
		case 5:
			// Broadcast
			sendPacket();
			break;
		case 7:
			connectionLogic();
			break;
		}
	}

	/**
	 * This method sends a DataPacket opposite to the sender.
	 * 
	 * @param clientNumber
	 *            the clientNumber of the sender.
	 */
	private synchronized void gameLogic(int clientNumber) {
		try {
			// Send packet only to the opposite socket
			if (clientNumber == 2) {
				writerA.writeObject(packet);
				writerA.flush();
			} else if (clientNumber == 1) {
				writerB.writeObject(packet);
				writerB.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method: 1) Sends a DataPacket opposite to the sender. 2) Closes the
	 * sender's Socket. 3) Sends the remaining Socket client back to the
	 * Connection's queueList.
	 * 
	 * @param clientNumber
	 *            the clientNumber of the sender.
	 */
	private void disconnect(int clientNumber) {
		try {
			// Send packet to the opposite socket and close it
			if (clientNumber == 2) {
				if (!clients.get(0).isClosed()) {
					writerA.writeObject(packet);
					writerA.flush();
				}
			} else if (clientNumber == 1) {
				if (!clients.get(1).isClosed()) {
					writerB.writeObject(packet);
					writerB.flush();
				}
			}
			clients.get(clientNumber - 1).close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method alerts players of main phases within the game using int counter.
	 * At 0: Randomly select the First. At 2: Players must interchange their board
	 * cells. At 4: Game attacks starts. At 5: Sets a 10 second timer to disconnect
	 * if a player does not accept a rematch. At 6: When both players agree on a
	 * rematch startNewGame() is called.
	 */
	private void connectionLogic() {
		if (counter++ == 0) {
			Debug.out("1) Alert the player that is going first");
			// Alert the player that is going first
			int random = (int) (Math.random() * 2 + 1);
			packet.setPacketType(0);
			gameLogic(random);
		} else if (counter == 2) {
			Debug.out("2) Ask players for ship location and to set gameOn");
			// Ask players for ship location and to set gameOn
			packet.setPacketType(7);
			sendPacket();
		} else if (counter == 4) {
			// Start game notification!
			Debug.out("3) Start game notification!");
			packet.setPacketType(6);
			sendPacket();
		} else if (counter == 5) {
			// Timer to allow both opponents to accept a rematch before being disconnected
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (counter == 6) {
						return;
					} else {
						for (int i = 0; i < clients.size(); i++) {
							if (!clients.get(i).isClosed()) {
								int clientNumber = (i == 0) ? 2 : 1;
								packet.setPacketType(4);
								gameLogic(clientNumber);
								return;
							}
						}
					}
				}
			}, 5 * 1000);
		} else if (counter == 6) {
			startNewGame();
		}
	}

	/**
	 * Set the ObjectOutputReaders for each Socket Client and calls startNewGame()
	 * 
	 * @throws IOException
	 *             Exception thrown by ObjectOutputStream.
	 */
	private void setWriters() throws IOException {
		writerA = new ObjectOutputStream(clients.get(0).getOutputStream());
		writerB = new ObjectOutputStream(clients.get(1).getOutputStream());
		startNewGame();
	}

	/**
	 * This method resets the ClientHandler to start a new game with the same
	 * players. It notifies clients about each other
	 * 
	 * @throws InterruptedException
	 *             Exception thrown by Thread.sleep(milliseconds).
	 */
	public void startNewGame() {
		counter = 0;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				packet = new DataPacket();
				packet.setMessage("The Enemy is now connected!\n");
				packet.setPacketType(1);
				sendPacket();
			}
		}, 2 * 1000);
	}

	/**
	 * Sends a packet to both clients, usually used for the chat connection It is
	 * synchronized to avoid multiple calls at the same time.
	 */
	public synchronized void sendPacket() {
		try {
			writerA.writeObject(packet);
			writerA.flush();
			writerB.writeObject(packet);
			writerB.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
