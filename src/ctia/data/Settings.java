package ctia.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import ctia.display.Art;
import ctia.scene.BattleScene;

// Stores user settings read from settings file
// Case-insensitive (stored as ALLCAPS) for key (sensitive for value)
// Note: DO NOT DIRECTLY put(key, value) W/O ALLCAPPING KEY FIRST!
public final class Settings {
	public static final String SETTINGS_FILEPATH = "data/settings.txt";
	static HashMap<String, String> settings = new HashMap<String, String>();
	private enum VariableType {STRING, BOOLEAN, INTEGER}; // STRING = no restriction
	private static SequentialStreamReader settingsReader;

	private Settings() { }

	static {
		loadSettings();
	}

	public static String value(String key) {
		return settings.get(key.toUpperCase());
	}
	public static boolean valueBoolean(String key) {
		return Boolean.parseBoolean(value(key));
	}
	public static int valueInt(String key) {
		return Integer.parseInt(value(key));
	}

	private static void loadSettings() {
		settingsReader = new SettingsReader(new File(SETTINGS_FILEPATH));
		try {
			settingsReader.readStream();
		} catch (FileNotFoundException ex) {
			try {
				if (Art.isRunningInJar()) {
					settingsReader = new SettingsReader(Settings.class.getResourceAsStream("/" + SETTINGS_FILEPATH));
					settingsReader.readStream();
					return;
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(null, "Settings file missing from " + SETTINGS_FILEPATH + "! Game cannot load and will now exit. Perhaps the data folder is missing?",
					"Missing Settings File", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
	// helper of loadSettings(), verify type of settings, ex: boolean, int
	static void verifySettings() { // make sure all the settings are set, if not set, set them to default values
		// TODO put these in a static array variable or something
		verify("startingLevel", "string", "CentralAirspace");
		verify("fps", "integer", "50");
		verify("show_fps", "boolean", "false");
		verify("hud_on_top", "boolean", "true");

		verify("background_red", "integer", "200");
		verify("background_blue", "integer", "255");
		verify("background_green", "integer", "255");
		verify("window_width", "integer", "800");
		verify("window_height", "integer", "600");

		verify("show_tips", "boolean", "true");
		verify("font", "string", "data/font/fp_font.txt");
		verify("enable_print", "boolean", "false");

		verify("default_graphics", "boolean", "false");

		verify("hardmode", "boolean", "false");
		verify("debug", "boolean", "false");

		verify("undying", "boolean", "false");
		verify("spinmode", "boolean", "false");

		if (Settings.valueInt("window_width") < BattleScene.HUD_WIDTH) {
			Utility.printWarning("Window width (" + Settings.valueInt("window_width") + ") is recommended to be at least " + BattleScene.HUD_WIDTH + ".");
		}
		if (Settings.valueInt("window_height") < BattleScene.HUD_HEIGHT) {
			Utility.printError("Window height (" + Settings.valueInt("window_height") + ") cannot be less than " + BattleScene.HUD_HEIGHT
					+ ", setting height to " + BattleScene.HUD_HEIGHT + ".");
			settings.put("window_height", BattleScene.HUD_HEIGHT + "");
		}
	}
	/**
	 * Ensures that the value for the given <code>key</code> matches the given variable <code>type</code>.<br>
	 * If the value for the <code>key</code> is of another type, or the value is <code>null</code>,
	 *  the value is set to <code>defaultValue</code>.<br> 
	 * <br>
	 * Gotchas (no handholding provided for these)<br>
	 * - type = "String" only checks to see if the key has a value<br>
	 * - make sure <code>defaultValue</code> is of type <code>type</code>
	 */
	private static void verify(String key, String type, String defaultValue) {
		key = key.toUpperCase();
		String value = settings.get(key);

		if (value == null) {
			settings.put(key, defaultValue);
		} else {
			switch (VariableType.valueOf(type.toUpperCase())) {
			case STRING: break; // No check necessary
			case BOOLEAN:
				if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
					settings.put(key, defaultValue);
				}
				break;
			case INTEGER:
				try {
					Integer.parseInt(value);
				} catch (NumberFormatException ex) {
					settings.put(key, defaultValue);
				}
				break;
			default: // shouldn't happen unless programmer botches up the method call
				Utility.printError("Programmer hardcoded error: Type \"" + type + "\" not supported in verify(String,String,String).");
				break;
			}
		}
	}
}
