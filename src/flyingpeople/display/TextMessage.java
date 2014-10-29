package flyingpeople.display;

import java.awt.Graphics;

public abstract class TextMessage {
	protected String text;
	protected long ticks = 0;

	public TextMessage(String text) {
		this.text = text;
	}

	public void tick() { ticks++; }
	public void draw(Graphics g) {
		for (int i = 0; i < text.length(); i++) {
			MessageDisplay.getInstance().drawCenter(g, i, text.charAt(i));
		}
	}
	protected final void removeSelf() {
		MessageDisplay.getInstance().removeMessage(this);
	}

	public void setText(String text) {
		this.text = text;
	}
}
