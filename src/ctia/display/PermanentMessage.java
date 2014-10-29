package ctia.display;

import java.awt.Graphics;

public class PermanentMessage extends TextMessage {
	protected int x,y;
	protected boolean autoPosition = true;
	public PermanentMessage(String text) {
		super(text);
	}
	public PermanentMessage(String text, int x, int y) {
		super(text);
		this.x = x;
		this.y = y;
		autoPosition = false;
	}
	public void tick() { } // no need to tick, message is permanent
	public void draw(Graphics g) {
		if (autoPosition) {
			super.draw(g);
		} else {
			for (int i = 0; i < text.length(); i++) {
				MessageDisplay.getInstance().drawAt(g, x, y, i, text.charAt(i));
			}
		}
	}
}
