package control;

import javafx.application.Application;

public class ServerDriver {

	/**
	 * initiates Java FX's GUI through Application.launch(args).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(ServerGUI.class, args);
		System.exit(0);
	}
}
