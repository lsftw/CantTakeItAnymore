package ctia.display;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import ctia.data.Utility;

// TODO update font file loading to use current file parsing framework
// Not to be confused with Java's built-in Font class
// Font support for only fixed-width, fixed-height characters during loading
// No font support for custom space characters
public final class Font {
	private String name;
	private int width, height;
	private HashMap<Character, ColoredImageSet> glyphs = new HashMap<Character, ColoredImageSet>();

	private Font(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	protected static Font makeFont(String fontLayoutFile, int lineNumber, String fontImageFileName, int size, boolean loadInJar) {
		Scanner reader = null;
		try {
			reader = new Scanner(new File(fontLayoutFile));
		} catch (FileNotFoundException ex) {
			if (Art.isRunningInJar()) { // if not found, search in jar
				InputStream is = Art.class.getResourceAsStream("/" + fontLayoutFile);
				reader = new Scanner(is);
				if (is == null) {
					JOptionPane.showMessageDialog(null, "Font layout file missing from " + fontLayoutFile + "! Game cannot load and will now exit. Perhaps the data folder is missing?",
							"Missing Font Layout File", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Font layout file missing from " + fontLayoutFile + "! Game cannot load and will now exit. Perhaps the data folder is missing?",
						"Missing Font Layout File", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}

		for (int i = 0; i < lineNumber; i++) { // skip lines not describing font
			reader.nextLine();
		}
		BufferedImage fontImage = null;
		try {
			if (loadInJar) {
				URL location = Font.class.getResource("/" + fontImageFileName);
				fontImage = ImageIO.read(location);
			} else {
				fontImage = ImageIO.read(new File(fontImageFileName));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		int x = 0, y = 0; // position of glyph in image
		int width = size; int height = size;
		Font font = new Font(fontLayoutFile, width, height);

		String curLine;
		String[] chars; // tokens from split(), each String ought to be 1 character long

		while (reader.hasNextLine()) {
			x = 0;
			curLine = reader.nextLine().trim();
			chars = curLine.split(" ");
			for (int i = 0; i < chars.length; i++) {
				if (chars[i].length() > 1) {
					Utility.printWarning("Token #" + (i+1) + " on line " + lineNumber + " of font layout file \"" + fontLayoutFile + "\" contains multiple characters. Skipping token.");
				} else {
					ColoredImageSet cis = new ColoredImageSet(fontImage.getSubimage(x * width, y * height, width, height));
					font.glyphs.put(chars[i].charAt(0), cis);
				}
				x++;
			}
			y++;
		}
		reader.close();
		return font;
	}



	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Image getGlyph(char c) {
		return glyphs.get(c).getOriginal();
	}
	public Image getGlyph(char c, Color color) {
		return glyphs.get(c).getVariant(color);
	}
	public String toString() {
		return name;
	}
	///////////////////////////////////////////////////////
	//               Class ColoredImageSet               //
	//                                                   //
	//       a set of images in colored variations       //
	///////////////////////////////////////////////////////
	protected static class ColoredImageSet {
		private Image originalImage;
		@SuppressWarnings("serial")
		// inspired by gridworld's code, caches colored images
		private Map<Color, Image> coloredImages = new LinkedHashMap<Color, Image>(20) {
			protected boolean removeEldestEntry(Map.Entry<Color, Image> eldest) {
				return size() > 100;
			}
		};

		public ColoredImageSet(Image image) {
			this.originalImage = image;
		}

		public void putVariant(Color color, Image image) {
			coloredImages.put(color, image);
		}
		public Image getVariant(Color color) {
			Image variant = coloredImages.get(color);
			if (variant == null) { // add that variant
				variant = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(originalImage.getSource(), new ColorSwapFilter(color)));
				coloredImages.put(color, variant);
			}
			return variant;
		}
		public Image getOriginal() {
			return originalImage;
		}
	}
	///////////////////////////////////////////////////////
	//               Class ColorSwapFilter               //
	//                                                   //
	//    converts an image to all of a single color     //
	///////////////////////////////////////////////////////
	protected static class ColorSwapFilter extends RGBImageFilter {
		protected int red, green, blue;
		// this filter only works properly on single color images
		public ColorSwapFilter(Color color) {
			this.canFilterIndexColorModel = true;
			red = color.getRed();
			green = color.getGreen();
			blue = color.getBlue();
		}
		// turn all colors into filter colors
		public int filterRGB(int x, int y, int rgb) {
			int alphacomp = rgb & 0xff000000; // preserve transparency
			return alphacomp | (red<<16) | (green<<8) | (blue);
		}
	}
}
