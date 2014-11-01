package ctia.engine.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// simplifies file parsing
public abstract class SequentialStreamReader {
	protected boolean processed = false;
	// these settings should always default to false for consistency
	//  and by default false should have no effect on line processing
	// which is why it is ignoreBlankLines instead of processBlankLines
	protected boolean cleanLines = false; // trims and removes comments
	protected boolean ignoreBlankLines = false;

	protected Scanner streamReader;
	protected InputStream is;
	protected File file;

	public SequentialStreamReader(File file) {
		this.file = file;
	}
	public SequentialStreamReader(InputStream is) {
		this.is = is;
	}
	public boolean readStream() throws FileNotFoundException {
		if (processed) return false;
		if (file != null) {
			is = new FileInputStream(file);
		}
		streamReader = new Scanner(is);
		processStream();
		streamReader.close();
		processed = true;
		return true;
	}
	protected void processStream() {
		int lineNumber = 0;
		String curLine;
		while (streamReader.hasNextLine()) {
			lineNumber++;
			curLine = streamReader.nextLine();
			if (cleanLines) {
				curLine = Utility.cleanTextString(curLine);
			}
			if (curLine.length() > 0 || !ignoreBlankLines) {
				processLine(curLine, lineNumber);
			}
		}
		endOfStream();
	}
	protected abstract void processLine(String curLine, int lineNumber);
	/**
	 * Called when reached end of stream
	 */
	protected void endOfStream() { }
}
