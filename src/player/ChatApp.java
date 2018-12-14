package player;

import java.io.Serializable;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

/**
 * This Class paints the player's Chat window in the JavaFx UI
 * 
 * @author DigitalVilla
 *
 */
public class ChatApp implements Serializable {

	/**
	 * Auto generated id.
	 */
	private static final long serialVersionUID = -8916491602356590099L;
	// Setters and Getters
	/**
	 * The entire window/container of the chat application.
	 */
	private Pane chatApp;
	/**
	 * The screen of the chat application
	 */
	private Label displayText;
	/**
	 * The text field of the chat application
	 */
	private TextField txtFld;
	/**
	 * The text log of all the sent/received messages.
	 */
	private String message;
	/**
	 * True if the chat has not been used yet.
	 */
	private boolean firstText;
	/**
	 * The instance of the Player Manager
	 */
	private Player player;
	/**
	 * The button that sends the content of the text field to the server.
	 */
	private Button sendBtn;

	/**
	 * Constructor
	 * 
	 * @param player
	 *            the instance of the Player Manager
	 */
	public ChatApp(Player player) {
		this.player = player;
		init();
	}

	/**
	 * This method sets the class to default values
	 */
	public void init() {
		message = "";
	}
	// GETTERS AND SETTERS

	/**
	 * Returns the text field of the chat application
	 * 
	 * @return txtFld
	 */
	public TextField getTxtFld() {
		return txtFld;
	}

	/**
	 * Sets the text field of the chat application
	 * 
	 * @param txtFld
	 */
	public void setTxtFld(TextField txtFld) {
		this.txtFld = txtFld;
	}

	/**
	 * Returns the text log of all the sent/received messages.
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns if the flag that determines if the chat has not been used yet.
	 * 
	 * @return firstText
	 */
	public boolean isFirstText() {
		return firstText;
	}

	/**
	 * Sets if the flag that determines if the chat has not been used yet.
	 * 
	 * @param firstText
	 */
	public void setFirstText(boolean firstText) {
		this.firstText = firstText;
	}

	/**
	 * Sets the text log of all the sent/received messages.
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * It Retrieves the button that sends the content of the text field to the
	 * server.
	 * 
	 * @return sendBtn
	 */
	public Button getSendBtn() {
		return sendBtn;
	}

	/**
	 * It sets the button that sends the content of the text field to the server.
	 * 
	 * @param sendBtn
	 */
	public void setSendBtn(Button sendBtn) {
		this.sendBtn = sendBtn;
	}

	/**
	 * Retrieves the pane containing the entire chat application.
	 * 
	 * @return chatApp
	 */
	public Pane getChatApp() {
		return chatApp;
	}

	/**
	 * It retrieves The screen of the chat application
	 * 
	 * @return displayText
	 */
	public Label getDisplayText() {
		return displayText;
	}

	/**
	 * @param displayText
	 */
	public void setDisplayText(Label displayText) {
		this.displayText = displayText;
	}

	/**
	 * @return txtFld
	 */
	public TextField getTxt() {
		return txtFld;
	}

	/**
	 * @param txtFld
	 */
	public void setTxt(TextField txtFld) {
		this.txtFld = txtFld;
	}

	public void paint() {

		// CHAT Window
		chatApp = new Pane();
		int chatAppW = 360;
		int chatAppH = player.getScreen_H() - 120;

		// System.out.println(chatAppH);

		chatApp.setMinSize(chatAppW, chatAppH);
		chatApp.setLayoutX(player.getScreen_W() - 390);
		chatApp.setLayoutY(50);
		chatApp.setId("chatApp");

		ScrollPane display = new ScrollPane();
		display.setFitToWidth(true);
		display.setId("chatDisplay");
		display.setVvalue(1.0);
		// Setting a horizontal scroll bar is always display
		display.setHbarPolicy(ScrollBarPolicy.NEVER);
		// Setting vertical scroll bar is never displayed.
		display.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		display.setMaxSize(chatAppW - 20, chatAppH - 100);
		display.setMinSize(chatAppW - 20, chatAppH - 100);
		display.setLayoutY(10);
		display.setLayoutX(10);

		// Chat displayText
		displayText = new Label();
		displayText.setText("Waiting for an enemy player... \n");
		displayText.setId("chatText");
		displayText.setAlignment(Pos.TOP_LEFT);
		displayText.setWrapText(true);
		displayText.setTextAlignment(TextAlignment.JUSTIFY);
		// displayText.setMaxSize(chatAppW - 20, chatAppH - 100);
		displayText.setMinHeight(chatAppH - 100);
		displayText.setMaxWidth(chatAppW - 0);
		displayText.setLayoutY(10);
		displayText.setLayoutX(10);
		displayText.heightProperty().addListener((observable, oldValue, newValue) -> {
			chatApp.layout();
			display.setVvalue(1.0d);
		});

		// Add to display
		display.setContent(displayText);
		display.getStyleClass().add("scroll-bar");
		// Chat Text Field
		txtFld = new TextField();
		txtFld.setId("chatTxtF");
		txtFld.setMinSize(chatAppW - 100, 50);
		txtFld.setLayoutY(chatAppH - 70);
		txtFld.setLayoutX(10);
		// Avoid TEXT INPUT

		txtFld.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					sendText();
				}
			}
		});

		// Chat Button

		ImageView img = new ImageView(this.getClass().getResource("/res/icons/paper-plane.png").toExternalForm());
		img.setFitWidth(20);
		img.setPreserveRatio(true);
		sendBtn = new Button("Send");
		sendBtn.setGraphic((img));
		sendBtn.setId("chatBtn");
		sendBtn.setMinSize(100, 50);
		sendBtn.setLayoutX(chatAppW - 110);
		sendBtn.setLayoutY(chatAppH - 70);
		// sendBtn.setDisable(true);
		// sendBtn.setOnAction(e -> sendText());

		// Without LAMDAs
		sendBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				sendText();
			}
		});

		chatApp.getChildren().addAll(display, txtFld, sendBtn);

	}

	private void sendText() {
		String msg = txtFld.getText().trim();
		if (msg == null || msg.length() == 0)
			return;
		String playerName = player.getPlayerName();
		player.getPacket().setMessage(playerName + ": " + msg + "\n");
		player.getPacket().setPacketType(1);
		player.sendPacket();
		txtFld.setText("");
	}

	public void sendText(String message) {
		String playerName = player.getPlayerName();
		String msg = playerName + ": " + message;
		player.getPacket().setMessage(msg + "\n");
		player.getPacket().setPacketType(1);
		player.sendPacket();
	}

	public void printText() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String text = player.getPacket().getMessage();
				if (text.length() > 0 && text != null)
					if (firstText) {
						// clear login message
						message = "";
						firstText = false;
					}
				displayText.setText(message += text);
			}
		});
	}

	/**
	 * This method opens the chat after a connection with another client is
	 * established
	 */
	public void enableChat() {
		// sendBtn.setDisable(true);
		txtFld.setEditable(true);
		printText();
	}
}
