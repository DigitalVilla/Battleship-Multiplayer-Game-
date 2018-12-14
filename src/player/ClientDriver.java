package player;

import javafx.application.Application;

public class ClientDriver {

	/**
	 * Initiates Java FX's GUI through Application.launch(args).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(ClientGUI.class, args);
		System.exit(0);
	}

}
