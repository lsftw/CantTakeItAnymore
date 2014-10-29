package ctia.display;

import java.awt.Graphics;

//TODO make it fade instead of just vanishing
public class FadeMessage extends TextMessage {
	protected int x, y;
	protected int duration;
	public FadeMessage(String text, int x, int y, int duration) {
		super(text);
		this.x = x;
		this.y = y;
		this.duration = duration;
	}

	public void tick() {
		super.tick();
		if (ticks > duration) {
			removeSelf();
		}
	}
	public void draw(Graphics g) {
		for (int i = 0; i < text.length(); i++) {
			MessageDisplay.getInstance().drawAt(g, x, y, i, text.charAt(i));
		}
	}
}
