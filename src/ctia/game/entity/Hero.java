package ctia.game.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.entity.Player;
import ctia.engine.entity.Projectile;

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
	public void fireProjectile(Point p) {
		double angle = Math.atan2(p.y - (this.py + this.sy / 2), p.x - (this.px + this.sx / 2));
		Projectile bullet = new Bullet(this, angle);
		container.addEntity(bullet);
	}

	public void draw(Graphics g) {
		super.draw(g);
		drawDebugInfo(g);
	}

	private void drawDebugInfo(Graphics g) {
		int beginX = (int)px + sx - this.container.getXscroll();
		int beginY = (int)py - this.container.getYscroll();
		// Don't let text run offscreen, gradually flip side to the left after passing middle of screen
		final int threshold = Settings.getMaxX() / 2;
		if (beginX > threshold) {
			int diff = Math.min(beginX - threshold, 60 + sx);
			beginX = beginX - diff;
		}
		g.setColor(Color.BLUE);
		g.drawString("HP: " + health, beginX, beginY + sy / 3);
		g.drawString("X: " + px, beginX, beginY + sy * 2 / 3);
		g.drawString("Y: " + py, beginX, beginY + sy);
	}
}
