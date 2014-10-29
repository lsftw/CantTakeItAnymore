package flyingpeople.data.level;

import java.io.File;
import java.io.InputStream;

import flyingpeople.data.SequentialStreamReader;

public class LevelListReader extends SequentialStreamReader {
	protected Level loaded;

	public LevelListReader(File file) {
		super(file);
		cleanLines = true;
		ignoreBlankLines = true;
	}
	public LevelListReader(InputStream is) {
		super(is);
		cleanLines = true;
		ignoreBlankLines = true;
	}

	protected void processLine(String curLine, int lineNumber) {
		String levelName = curLine.substring(0, curLine.indexOf(' '));
		loaded = Level.loadLevel(curLine.substring(curLine.indexOf(' ') + 1));
		loaded.name = levelName;
		Level.levelsByName.put(levelName, loaded);
		Level.allLevels.add(loaded);
	}
}