package flyingpeople.data;

import java.io.File;
import java.io.InputStream;

import flyingpeople.display.Art;

// case insensitive key, case sensitive value
public class SettingsReader extends SequentialStreamReader {
	public SettingsReader(File file) {
		super(file);
		cleanLines = true;
		ignoreBlankLines = true;
	}
	public SettingsReader(InputStream is) {
		super(is);
		cleanLines = true;
		ignoreBlankLines = true;
	}

	protected void processLine(String curLine, int lineNumber) {
		String curKey, curValue;
		if (curLine.contains("=")) {
			curKey = curLine.substring(0, curLine.indexOf('=')).trim().toUpperCase();
			curValue = curLine.substring(curLine.indexOf('=') + 1).trim(); // Notice: case NOT discarded
			Settings.settings.put(curKey, curValue);
		} else {
			Utility.printError("Setting \"" + curLine + "\" on line " + lineNumber + " lacks a value, ignoring line.");
		}
	}

	protected void endOfStream() {
		Settings.verifySettings();
		Art.loadFont(Settings.value("font"));
	}
}