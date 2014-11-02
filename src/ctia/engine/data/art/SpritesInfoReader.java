package ctia.engine.data.art;

import java.io.File;
import java.io.InputStream;

import ctia.engine.core.Sprite;
import ctia.engine.data.SequentialStreamReader;
import ctia.engine.data.Utility;

class SpritesInfoReader extends SequentialStreamReader {
	private String curCategory = null;
	private Sprite curSprite = null;
	private String fileName = null;
	public SpritesInfoReader(File file) {
		super(file);
		cleanLines = true;
		ignoreBlankLines = true;
		fileName = file.getName();
	}
	public SpritesInfoReader(InputStream is) {
		super(is);
		cleanLines = true;
		ignoreBlankLines = true;
	}

	protected void processLine(String curLine, int lineNumber) {
		if (curLine.indexOf('.') > 0) { // has delim and text before the delim, is file name
			int delimpos = curLine.indexOf(' '); // delim between image "name" and image path
			if (delimpos == -1) {
				if (curCategory == null) {
					Utility.warn("Loaded a sprite image without a category on line " + lineNumber + " of " + file);
				} else {
					curSprite.addFrame(Art.loadImage(curLine));
				}
			} else {
				if (curCategory == null) {
					Utility.error("Syntax error: Sprite image \"" + curLine + "\" not named in " + fileName + " on line " + lineNumber + " of " + file);
				} else {
					curSprite.addFrame(curLine.substring(0,delimpos), Art.loadImage(curLine.substring(delimpos+1)));
				}
			}
		} else {
			if (curCategory != null) {
				Art.addSprite(curCategory, curSprite);
			}
			curCategory = curLine;
			curSprite = Art.getSprite(curCategory);
			if (curSprite == null) {
				curSprite = new Sprite();
			}
		}
		if (curCategory != null) {
			Art.addSprite(curCategory, curSprite);
		}
	}
}