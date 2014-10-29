package ctia.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import ctia.data.Settings;
import ctia.data.Utility;

// displays text on screen, handling fonts and word wrap
// TODO make less verbose to use, handle word wrap and multiple fonts?
public final class MessageDisplay {
	public static MessageDisplay instance;
	private ArrayList<TextMessage> messages = new ArrayList<TextMessage>();
	private Font curFont;

	private MessageDisplay(Font f) {
		this.curFont = f;
	}

	public static void initializeDisplay(Font defaultFont) {
		instance = new MessageDisplay(defaultFont);
	}
	public static MessageDisplay getInstance() {
		return instance;
	}

	public void addMessage(TextMessage message) { messages.add(message); }
	public void removeMessage(TextMessage message) { messages.remove(message); }

	public void tick() {
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).tick();
		}
	}
	public void draw(Graphics g) { // draws all the text messages
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).draw(g);
		}
	}

	// TODO make proper drawing in center
	protected void drawCenter(Graphics g, int i, char c) {
		drawAt(g, 0, Settings.valueInt("window_height") / 2, i, c);
	}
	protected void drawAt(Graphics g, int x, int y, int i, char c) {
		drawAt(g, x, y, i, c, null);
	}
	protected void drawAt(Graphics g, int x, int y, int i, char c, Color color) {
		if (c == ' ') return; // skip drawing character == drawing space
		if (c >= 'a' && c <= 'z') c = (char)(c - 'a' + 'A'); // capitalize

		Image image;
		if (color != null) {
			image = curFont.getGlyph(c, color);
		} else { // if color == null, doesn't color the text
			image = curFont.getGlyph(c);
		}
		if (image == null) { // TODO add support for tab '\t' character
			Utility.printError("Unsupported character: \'" + c + "\' (#" + (int)c + ") for position " + i + " for font \"" + curFont + "\".");
		} else {
			g.drawImage(image, i * curFont.getWidth() + x, y, null);
		}
	}

	public int getFontWidth() { return curFont.getWidth(); }
	public int getFontHeight() { return curFont.getHeight(); }
}
