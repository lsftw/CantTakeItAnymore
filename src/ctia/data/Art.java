package ctia.data;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JOptionPane;

import ctia.core.Entity;
import ctia.core.Sprite;

public final class Art {
	private static final String IMAGE_LIST = "data/imagelist.txt";
	private static final String SPRITE_LIST = "data/spritelist.txt";
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();	private static HashMap<String, Image> images = new HashMap<String, Image>();

	private static MediaTracker mt;
	private static boolean loaded = false;
	private static int countImages = 0;

	private static boolean runningInJar;

	private Art() { }

	static {
		String classJar = Art.class.getResource("/" + Art.class.getName().replace('.', '/') + ".class").toString();
		if (classJar.startsWith("jar:")) {
			runningInJar = true;
		} else {
			runningInJar = false;
		}
	}

	public static void load(Component comp) { // Not in a static block due to use of MediaTracker 
		if (loaded) {
			Utility.warn("Loading art files after already having loaded art files.");
		}
		mt = new MediaTracker(comp);
		loadImages(comp);
		loadSprites(comp);
		loaded = true;
	}
	private static void loadImages(Component comp) {
		ImagesInfoReader fileReader = new ImagesInfoReader(new File(IMAGE_LIST));
		try {
			fileReader.readStream();
		} catch (FileNotFoundException ex) {
			try { // if not found, search inside jar
				if (runningInJar) {
					fileReader = new ImagesInfoReader(Art.class.getResourceAsStream("/" + IMAGE_LIST));
					fileReader.readStream();
					return;
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(comp, "Image list file missing from " + IMAGE_LIST + "! Game cannot load and will now exit. Perhaps the data folder is missing?",
					"Missing Image List File", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	private static void loadSprites(Component comp) {
		SpritesInfoReader fileReader = new SpritesInfoReader(new File(SPRITE_LIST));
		try {
			fileReader.readStream();
		} catch (FileNotFoundException ex) {
			try { // if not found, search inside jar
				if (runningInJar) {
					fileReader = new SpritesInfoReader(Art.class.getResourceAsStream("/" + SPRITE_LIST));
					fileReader.readStream();
					return;
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(comp, "Sprite list file missing from " + SPRITE_LIST + "! Game cannot load and will now exit. Perhaps the data folder is missing?",
					"Missing Sprite List File", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	private static Image loadImage(String imageName) {
		Image imageToLoad;
		boolean loadingFromFile = false;
		if (runningInJar) {
			// use default graphics stored in jar instead of res folder
			imageToLoad = loadFromJar(imageName);
		} else {
			loadingFromFile = true;
			imageToLoad = loadFromFile(imageName);
		}
		if (!awaitLoad(imageName, imageToLoad)) {
			if (loadingFromFile && runningInJar) { // try loading from jar
				imageToLoad = loadFromJar(imageName);
				if (awaitLoad(imageName, imageToLoad)) {
					return imageToLoad;
				}
			}
			Utility.error("Failed to load image: \"" + imageName + "\"!");
		}
		return imageToLoad;
	}
	protected static boolean awaitLoad(String imageName, Image imageToLoad) {
		mt.addImage(imageToLoad, countImages);
		countImages++;
		try {
			mt.waitForAll();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (imageToLoad.getWidth(null) == -1) {
			return false;
		}
		return true;
	}
	private static Image loadFromFile(String imageName) {
		return Toolkit.getDefaultToolkit().getImage(imageName);
	}
	private static Image loadFromJar(String imageName) {
		URL location = Art.class.getResource("/" + imageName);
		return Toolkit.getDefaultToolkit().getImage(location);
	}

	public static boolean isRunningInJar() {
		return runningInJar;
	}

	public static Sprite getSprite(Entity entity) {
		return sprites.get(entity.getClass().getSimpleName());
	}
	public static Sprite getSprite(String spriteName) {
		return sprites.get(spriteName);
	}
	public static Image getImage(String imageName) {
		return images.get(imageName);
	}
	//
	// File reader for images file
	//
	protected static class ImagesInfoReader extends SequentialStreamReader {
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
					if (images.get(curCategory) != null) {
						Utility.error("Duplicate image for \"" + curCategory + "\" on line " + lineNumber + ".");
					}
					images.put(curCategory, loadImage(curLine));
				}
			} else {
				curCategory = curLine;
			}
		}
	}
	//
	// File reader for sprites file
	//
	protected static class SpritesInfoReader extends SequentialStreamReader {
		private String curCategory = null;
		private Sprite curSprite = null;
		public SpritesInfoReader(File file) {
			super(file);
			cleanLines = true;
			ignoreBlankLines = true;
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
						curSprite.addFrame(loadImage(curLine));
					}
				} else {
					if (curCategory == null) {
						Utility.error("Syntax error: Sprite image \"" + curLine + "\" not named in " + SPRITE_LIST + " on line " + lineNumber + " of " + file);
					} else {
						curSprite.addFrame(curLine.substring(0,delimpos), loadImage(curLine.substring(delimpos+1)));
					}
				}
			} else {
				if (curCategory != null) {
					sprites.put(curCategory, curSprite);
				}
				curCategory = curLine;
				curSprite = getSprite(curCategory);
				if (curSprite == null) {
					curSprite = new Sprite();
				}
			}
			if (curCategory != null) {
				sprites.put(curCategory, curSprite);
			}
		}
	}
}
