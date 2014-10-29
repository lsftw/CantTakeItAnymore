package flyingpeople.data.level;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;

import flyingpeople.data.SequentialStreamReader;
import flyingpeople.data.Utility;

public class LevelReader extends SequentialStreamReader {
	protected Level lastLevel;
	protected LinkedList<LevelEvent> eventBuffer;

	public LevelReader(File file) {
		super(file);
		lastLevel = new Level();
		eventBuffer = new LinkedList<LevelEvent>();
		cleanLines = true;
		ignoreBlankLines = true;
	}
	public LevelReader(InputStream is) {
		super(is);
		lastLevel = new Level();
		eventBuffer = new LinkedList<LevelEvent>();
		cleanLines = true;
		ignoreBlankLines = true;
	}
	// TODO complete syntax checking for bad lines
	protected void processLine(String curLine, int lineNumber) {
		String[] tokens; // temp var
		EventType etype = null;
		// parse the current event
		tokens = curLine.split("\\s");
		try { // verify number of tokens
			etype = EventType.valueOf(tokens[1].toUpperCase());

			if (etype == EventType.PRINT) { // special case: print event can take string parameter
				eventBuffer.add(new LevelEvent(Long.parseLong(tokens[0]), etype, spacedString(tokens, 2)));
			} else {
				if (tokens.length == 2) {
					try {
						eventBuffer.add(new LevelEvent(Long.parseLong(tokens[0]), etype));
					} catch (NumberFormatException ex) {
						Utility.printError("Expected number (long value) on line " + lineNumber + ", given \"" + tokens[0] + "\". Ignoring line.");
					}
				} else if (tokens.length == 3) { // TODO make print able to handle any number of tokens to be able to print spaces
					try {
						eventBuffer.add(new LevelEvent(Long.parseLong(tokens[0]), etype, tokens[2].split(",")));
					} catch (NumberFormatException ex) {
						Utility.printError("Expected number (long value) on line " + lineNumber + ", given \"" + tokens[0] + "\". Ignoring line.");
					}
				} else {
					Utility.printWarning("Expected either 2 or 3 tokens on line " + lineNumber + ", given " + tokens.length + ". Ignoring line.");
				}
			}
		} catch (IllegalArgumentException ex) {
			Utility.printError("Invalid token: \"" + tokens[1].toUpperCase() + "\" on line " + lineNumber + ". Ignoring line.");
		} catch (ArrayIndexOutOfBoundsException ex) { // messy handling...
			Utility.printWarning("Expected either 2 or 3 tokens on line " + lineNumber + ", given " + tokens.length + ". Ignoring line.");
		}
	}
	// ['A','string','tokenized','becomes','reformed.'] => "A string tokenized becomes reformed."
	private static String spacedString(String[] array, int startIndex) {
		StringBuffer tmp = new StringBuffer(array.length * 5); // 5 = estimated average word length
		for (int i = startIndex; i < array.length; i++) {
			tmp.append(array[i]);
			if (i != array.length - 1) tmp.append(' ');
		}
		return tmp.toString();
	}
	protected void endOfStream() {
		lastLevel.events = eventBuffer.toArray(new LevelEvent[eventBuffer.size()]);
	}
	public Level getLastLoadedLevel() {
		return lastLevel;
	}
}