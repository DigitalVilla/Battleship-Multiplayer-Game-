package utilities;

/**
 * This class gives control to the System.out.ptint() calls used to test the
 * application.
 * 
 * @author DigitalVilla
 *
 */
public class Debug {

	/**
	 * if this method is not 1 the print logs will be hidden
	 */
	private static int level = 1;

	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		Debug.level = level;
	}

	public static void out(Object msg) {
		if (level == 1) {
			System.out.println(msg + "");
		} else {
			// hide
		}

	}

}
