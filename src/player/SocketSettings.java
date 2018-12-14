package player;

import java.io.Serializable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * This GUI Class allows the user to set the parameters to connect with the
 * server.
 * 
 * @author DigitalVilla
 *
 */
public class SocketSettings implements Serializable {

	private static final long serialVersionUID = -9199115594674630242L;
	private static TextField port_text;
	private static TextField name_text;
	private static TextField ip_text;
	private double height;
	private Pane settings;
	private Player player;
	// private boolean ready = false;

	/**
	 * Class constructor
	 * 
	 * @param player
	 *            An instance of the Player manager
	 */
	public SocketSettings(Player player) {
		this.player = player;
		this.height = 400;
		display();
	}

	/**
	 * This method paints the GUI screen
	 */
	public void display() {
		double wBase = 20;
		double hBase = height / 12;
		int hGap = 20;
		int hTxtFld = 35;

		////////////// Player Name
		Label name_lbl = new Label("Player Name :");
		name_lbl.getStyleClass().add("lbl");
		name_lbl.setLayoutX(wBase + 5);
		name_lbl.setLayoutY(hBase);

		name_text = new TextField();
		name_text.getStyleClass().add("text-field");
		name_text.setId("name_text");
		name_text.setLayoutX(wBase);
		name_text.setLayoutY(hBase + hGap);

		///////////// Port number
		Label port_lbl = new Label("Port Number :");
		port_lbl.getStyleClass().add("lbl");
		port_lbl.setLayoutX(wBase);
		port_lbl.setLayoutY(hBase + (hGap * 2) + hTxtFld);

		port_text = new TextField();
		port_text.getStyleClass().add("text-field");
		port_text.setLayoutX(wBase);
		port_text.setLayoutY(hBase + (hGap * 3) + (hTxtFld));
		// port_text.setEditable(false);

		/////////////// IP address
		Label ip_lbl = new Label("IP Address :");
		ip_lbl.getStyleClass().add("lbl");
		ip_lbl.setLayoutX(wBase);
		ip_lbl.setLayoutY(hBase + (hGap * 4) + (hTxtFld * 2));

		ip_text = new TextField();
		ip_text.getStyleClass().add("text-field");
		ip_text.setLayoutX(wBase);
		ip_text.setLayoutY(hBase + (hGap * 5) + (hTxtFld * 2));

		// AutoFill fields
		fieldSetup();

		Button save_btn = new Button("  Connect & Play  ");
		save_btn.getStyleClass().add("btn");
		save_btn.setGraphic(new ImageView(this.getClass().getResource("/res/icons/play.png").toExternalForm()));

		save_btn.setLayoutX(wBase + 30);
		save_btn.setLayoutY(hBase + (hGap * 7) + (hTxtFld * 3));
		save_btn.setOnAction(e -> startConnection());

		settings = new Pane();
		settings.getChildren().addAll(name_lbl, name_text, ip_text, ip_lbl, port_lbl, port_text, save_btn);
		settings.setId("window");
		settings.setLayoutX((player.getScreen_W() / 2) - 145);
		settings.setLayoutY(80);

		// Variable to store the focus on stage load
		final BooleanProperty firstTime = new SimpleBooleanProperty(true);

		// CHEAT AUTOFOCUS
		name_text.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && firstTime.get()) {
				// Delegate the focus to container
				player.getWindow().requestFocus();
				// Variable value changed for future references
				firstTime.setValue(false);
			}
		});

	}

	/**
	 * This method is invoked when connect button is selected.
	 */
	private void startConnection() {
		saveFields(name_text.getText(), ip_text.getText(), port_text.getText());
		player.connect();
	}

	public Pane getSettings() {
		return settings;
	}

	public void setSettings(Pane settings) {
		this.settings = settings;
	}

	/**
	 * This method updates the Player's related fields with the users ivalues.
	 * 
	 * @param name
	 *            Username of the player
	 * @param ip
	 *            The IPv4 address of the server
	 * 
	 * @param port
	 *            Port number to connect
	 */
	private void saveFields(String name, String ip, String port) {
		try {
			if (name.length() > 0)
				player.setPlayerName(name);
			if (ip.length() > 0)
				player.setIpAddress(ip);
			if (port.length() > 0)
				player.setPortNumber(Integer.parseInt(port));
			fieldSetup();
		} catch (Exception e) {
			player.pop.out("Invalid Port / IP Address. Please type better", 16, 300, 150, 1);
		}
		// stage.close();
	}

	/**
	 * This method sets the default values of the text fields.
	 */
	public void fieldSetup() {
		name_text.setPromptText(player.getPlayerName());
		name_text.setText("");
		port_text.setPromptText(player.getPortNumber() + "");
		port_text.setText("");
		ip_text.setPromptText(player.getIpAddress());
		ip_text.setText("");
	}
}