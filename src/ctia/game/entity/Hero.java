package ctia.game.entity;

import java.awt.Color;
import java.awt.Graphics;

import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.entity.Player;

public class Hero extends Player {
	private static final int HEALTH = 10000;
	private static final int FIRE_DELAY = Settings.getFps();
	private static final int SPEED = 5;

	public Hero(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	public int getBaseHealth() {
		return HEALTH;
	}
	public int getBaseFireDelay() {
		return FIRE_DELAY;
	}
	public int getBaseSpeed() {
		return SPEED;
	}

	@Override
	protected void fireProjectile() {
		// TODO Auto-generated method stub
	}

	public void draw(Graphics g) {
		super.draw(g);
		drawDebugInfo(g);
	}

	private void drawDebugInfo(Graphics g) {
		int beginX = (int)px + sx;
		// Don't let text run offscreen, gradually flip side to the left after passing middle of screen
		final int threshold = Settings.getMaxX() / 2;
		if (beginX > threshold) {
			int diff = Math.min(beginX - threshold, 60 + sx);
			beginX = beginX - diff;
		}
		g.setColor(Color.BLUE);
		g.drawString("HP: " + health, beginX, (int)py + sy / 3);
		g.drawString("X: " + px, beginX, (int)py + sy * 2 / 3);
		g.drawString("Y: " + py, beginX, (int)py + sy);
	}
}
