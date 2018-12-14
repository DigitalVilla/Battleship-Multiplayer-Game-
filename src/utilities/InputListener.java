package utilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is always listening for incoming packages to update its Observer.
 * 
 * @author DigitalVilla
 *
 */
/**
 * @author DigitalVilla
 *
 */
public class InputListener extends Observable implements Runnable {
	private ObjectInputStream reader;
	private DataPacket packet;
	private Socket client;
	private int clientNumber;

	/**
	 * Class' constructor
	 * 
	 * @param observer
	 *            the Observer who threaded this class.
	 * @param client
	 *            the Socket of the client to connect
	 * @param clientNumber
	 *            a number to track the instance of this class
	 * @throws IOException
	 */
	public InputListener(Observer observer, Socket client, int clientNumber) throws IOException {
		this.client = client;
		addObserver(observer);
		Debug.setLevel(1);
		this.clientNumber = clientNumber;
	}

	// GETTERS & SETTERS
	/**
	 * @return
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
	 * @return clientNumber
	 */
	public int getClientNumber() {
		return clientNumber;
	}

	@Override
	public void run() {
		try {
			// Create an input stream to receive text from client
			reader = new ObjectInputStream(client.getInputStream());
			while (client.isConnected()) {
				packet = (DataPacket) reader.readObject();
				setChanged();
				notifyObservers();

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Debug.out("Listener: Client Disconnected");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This methods is a shorthand call to run the implementation of Runnable as if
	 * it were extending Thread.
	 */
	public void start() {
		Thread t1 = new Thread(this);
		t1.start();
	}

}
