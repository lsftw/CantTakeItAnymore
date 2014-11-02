package ctia.engine.data.art;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JOptionPane;

import ctia.engine.core.Entity;
import ctia.engine.core.Sprite;
import ctia.engine.data.Settings;
import ctia.engine.data.Utility;

public final class Art {
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	private static HashMap<String, Image> images = new HashMap<String, Image>();

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

	// Image & Sprite loading

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
		String imageList = Settings.getImageListFile();
		ImagesInfoReader fileReader = new ImagesInfoReader(new File(imageList));
		try {
			fileReader.readStream();
		} catch (FileNotFoundException ex) {
			try { // if not found, search inside jar
				if (runningInJar) {
					fileReader = new ImagesInfoReader(Art.class.getResourceAsStream("/" + imageList));
					fileReader.readStream();
					return;
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(comp, "Image list file missing from " + imageList + "!" +
					" Game cannot load and will now exit. Perhaps the data folder is missing?",
					"Missing Image List File", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	private static void loadSprites(Component comp) {
		String spriteList = Settings.getSpriteListFile();
		SpritesInfoReader fileReader = new SpritesInfoReader(new File(spriteList));
		try {
			fileReader.readStream();
		} catch (FileNotFoundException ex) {
			try { // if not found, search inside jar
				if (runningInJar) {
					fileReader = new SpritesInfoReader(Art.class.getResourceAsStream("/" + spriteList));
					fileReader.readStream();
					return;
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(comp, "Sprite list file missing from " + spriteList + "!" +
					" Game cannot load and will now exit. Perhaps the data folder is missing?",
					"Missing Sprite List File", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	static Image loadImage(String imageName) {
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

	static void addImage(String imageName, Image image) {
		images.put(imageName, image);
	}
	static void addSprite(String spriteName, Sprite sprite) {
		sprites.put(spriteName, sprite);
	}

	public static boolean isRunningInJar() {
		return runningInJar;
	}

	// Art - how to access
	public static Sprite getSprite(Entity entity) {
		return sprites.get(entity.getClass().getSimpleName());
	}
	public static Sprite getSprite(String spriteName) {
		return sprites.get(spriteName);
	}
	public static Image getImage(String imageName) {
		return images.get(imageName);
	}
}
