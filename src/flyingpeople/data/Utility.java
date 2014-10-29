package flyingpeople.data;

/**
 * Centralizes logging messages so they can all be disabled or changed at one location.
 */
public final class Utility {
	private Utility() { }

	public static void printInformation(Object infoFormatString, Object ... infoParams) {
		System.out.printf("[LOG] " + infoFormatString, infoParams);
	}
	public static void printInformation(Object information) {
		System.out.println("[LOG] " + information);
	}
	public static void printWarning(Object warnFormatString, Object ... warnParams) {
		System.out.printf("[WAR] " + warnFormatString, warnParams);
	}
	public static void printWarning(Object warning) {
		System.out.println("[WAR] " + warning);
	}
	public static void printError(Object errFormatString, Object ... errParams) {
		System.err.printf("[ERR] " + errFormatString, errParams);
	}
	public static void printError(Object error) {
		System.err.println("[ERR] " + error);
	}
	/**
	 * Removes comments and trims a <code>String</code>.<br>
	 * Intended to be used on <code>String</code>s parsed from text data files.
	 */
	public static String cleanTextString(String fileText) {
		if (fileText.contains("//")) {
			return fileText.substring(0, fileText.indexOf("//")).trim();
		}
		return fileText;
	}
}
