package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import control.ServerGUI.ServerUI;
import utilities.Debug;

/**
 * This is Class runs the ServerSocket for the Game. \nIt is always open for
 * incoming connections pairing Socket clients within their own Client Handler.
 * is the main server
 * 
 * @author DigitalVilla
 *
 */
public class Connection extends Thread {

	// Holds clients waiting to be paired inside a ClientHandler thread.
	public ArrayList<Socket> queueList;
	// This class handles dual connection to allow 1vs1 battleship duels
	private ClientHandler handler;
	// Counts the number of servers threaded.
	private int serverCount;

	// GETTERS and SETTERS
	public synchronized ArrayList<Socket> getQueueList() {
		return queueList;
	}

	public void setQueueList(ArrayList<Socket> queueList) {
		this.queueList = queueList;
	}

	@Override
	public void run() {

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Debug.setLevel(1);
		try {
			ServerSocket server = null;
			int portNumber = 1212;
			serverCount = 1;
			ServerUI.out("\nConnection opended: " + dateFormat.format(new Date()));
			server = new ServerSocket(portNumber);
			handler = new ClientHandler("Server " + (serverCount++));
			queueList = new ArrayList<Socket>(2);
			ServerUI.out("\nPort number: " + portNumber);
			ServerUI.out("\nWaiting for clients...\n");
			boolean run = true;
			// keep listening for new connections
			while (run) {
				Socket client = server.accept();
				getQueueList().add(client);
				Debug.out("1) Conection Queue: " + getQueueList().size());
				runGameServer();
				Debug.out("2) Conection Queue: " + getQueueList().size());
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initiate a new ClientHandler Thread if queueList contains 2 live Sockets.
	 */
	public synchronized void runGameServer() {
		if (queueList.size() == 2 && checkConnection()) {
			for (int i = 1; i >= 0; i--) {
				handler.addClient(getQueueList().remove(i));
			}
			if (handler.isReady()) {
				handler.start();
				handler = new ClientHandler("Server " + (serverCount++));
			}
		}
	}

	/**
	 * Removes closed sockets from the queueList
	 * 
	 * @return return true if all the sockets are live/connected.
	 */
	private boolean checkConnection() {
		boolean connected = true;
		for (int i = 1; i >= 0; i--) {
			if (queueList.get(i).isClosed()) {
				queueList.remove(i);
				connected = false;
			}
		}
		return connected;
	}
}
