package utilities;

import java.io.Serializable;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class is the customized dialogue/alert window for the application.
 * 
 * @author DigitalVilla
 *
 */
public class Pop_Box implements Serializable {

	/**
	 * auto generated id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * class' constructor
	 * 
	 * @param message
	 *            Dialogue to display
	 * @param font
	 *            Pixel size of the text
	 * @param with
	 *            Horizontal size of window
	 * @param height
	 *            Vertical size of window
	 * @param style
	 *            BackgrouSnd color: 1:Orange / 2:Green / 3:Blue
	 */
	public void out(String message, int font, int with, int height, int style) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initStyle(StageStyle.TRANSPARENT);
				// stage.setTitle(title);
				// lbl.setId(title + "_lbl");

				Label lbl = new Label();
				lbl.setStyle("-fx-text-fill: #ffffff;" + " -fx-font-size:" + font + "px;");
				lbl.setText(message);
				lbl.setAlignment(Pos.TOP_LEFT);
				lbl.setWrapText(true);
				lbl.setTextAlignment(TextAlignment.CENTER);

				ImageView img = new ImageView(this.getClass().getResource("/res/icons/check.png").toExternalForm());
				img.setFitWidth(20);
				img.setPreserveRatio(true);
				Button okBtn = new Button(" Done   ");
				okBtn.setGraphic((img));
				okBtn.setMinWidth(100);
				// okBtn.setId(title + "_btn");
				okBtn.setStyle("-fx-background-color: \r\n" + "        #090a0c,\r\n"
						+ "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\r\n"
						+ "        linear-gradient(#20262b, #191d22),\r\n"
						+ "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.5), rgba(255,255,255,0));\r\n"
						+ "    -fx-background-radius: 10;\r\n" + "    -fx-background-insets: 0,1,2,0;\r\n"
						+ "    -fx-text-fill: white;\r\n"
						+ "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\r\n"
						+ "    -fx-font-family: \"Arial\";\r\n"
						+ "    -fx-text-fill: linear-gradient(white, #d0d0d0);\r\n" + "    -fx-font-size: 12px;\r\n"
						+ "    -fx-padding: 10; ");
				okBtn.setOnAction(e -> stage.close());

				BorderPane pane = new BorderPane();
				pane.setStyle(colorStyle(style));
				// pane.setId(title + "_bg");
				pane.setCenter(lbl);
				pane.setBottom(okBtn);
				BorderPane.setAlignment(okBtn, Pos.TOP_CENTER);

				Scene scene = new Scene(pane, with, height);
				scene.setCursor(Cursor.HAND);
				stage.setScene(scene);
				stage.showAndWait();
			}
		});

	}

	/**
	 * Predetermined fx/css styles for the box.\n BackgrouSnd color: 1:Orange /
	 * 2:Green / 3:Blue
	 * 
	 * @param style
	 *            the preferred style.
	 * @return the fx/css style as a string.
	 */
	public String colorStyle(int style) {
		String orange = "   -fx-padding: 8 15 15 15;\r\n"
				+ "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\r\n" + "    -fx-background-radius: 0;\r\n"
				+ "    -fx-background-color: \r\n"
				+ "        linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),\r\n"
				+ "        #9d4024,\r\n" + "        #d86e3a,\r\n"
				+ "        radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);\r\n"
				+ "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\r\n"
				+ "    -fx-font-weight: bold;\r\n" + "    -fx-font-size: 1.1em;";
		String green = "   -fx-padding: 8 15 15 15;\r\n" + "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\r\n"
				+ "    -fx-background-radius: 0;\r\n" + "    -fx-background-color: \r\n"
				+ "        linear-gradient(from 0% 93% to 0% 100%, #439313 0%, #308b12 100%),\r\n"
				+ "        #4d8024,\r\n" + "        #68ce3a,\r\n"
				+ "        radial-gradient(center 50% 50%, radius 100%, #68ce3a, #45be2c);\r\n"
				+ "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\r\n"
				+ "    -fx-font-weight: bold;\r\n" + "    -fx-font-size: 1.1em;";
		String blue = "   -fx-padding: 8 15 15 15;\r\n" + "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\r\n"
				+ "    -fx-background-radius: 0;\r\n" + "    -fx-background-color: \r\n"
				+ "        linear-gradient(from 0% 93% to 0% 100%, #1343a3 0%, #103b92 100%),\r\n"
				+ "        #2d4094,\r\n" + "        #386eda,\r\n"
				+ "        radial-gradient(center 50% 50%, radius 100%, #386eda, #254ecc);\r\n"
				+ "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\r\n"
				+ "    -fx-font-weight: bold;\r\n" + "    -fx-font-size: 1.1em;";
		if (style == 1)
			return orange;
		if (style == 2)
			return green;
		if (style == 3)
			return blue;
		return orange;
	}
}