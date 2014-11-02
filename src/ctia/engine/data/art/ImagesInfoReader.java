package ctia.engine.data.art;

import java.io.File;
import java.io.InputStream;

import ctia.engine.data.SequentialStreamReader;
import ctia.engine.data.Utility;

class ImagesInfoReader extends SequentialStreamReader {
	private String curCategory = null;
	public ImagesInfoReader(File file) {
		super(file);
		cleanLines = true;
		ignoreBlankLines = true;
	}
	public ImagesInfoReader(InputStream is) {
		super(is);
		cleanLines = true;
		ignoreBlankLines = true;
	}

	protected void processLine(String curLine, int lineNumber) {
		if (curLine.indexOf('.') > 0) { // has delim and text before the delim, is file name
			if (curCategory == null) {
				Utility.warn("Loaded an image without a name on line " + lineNumber + ".");
			} else {
				if (Art.getImage(curCategory) != null) {
					Utility.error("Duplicate image for \"" + curCategory + "\" on line " + lineNumber + ".");
				}
				Art.addImage(curCategory, Art.loadImage(curLine));
			}
		} else {
			curCategory = curLine;
		}
	}
}