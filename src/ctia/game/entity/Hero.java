package ctia.game.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.entity.Player;
import ctia.engine.entity.Projectile;
import ctia.game.entity.projectile.PlayerBullet;

public class Hero extends Player {
	private static final int HEALTH = 10000;
	private static final int FIRE_DELAY = Settings.getFps() / 10;
	private static final int SPEED = 7;

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
	public Projectile makeProjectile(Point p) {
		double angle = Math.atan2(p.y - (this.py + this.sy / 2), p.x - (this.px + this.sx / 2));
		Projectile bullet = new PlayerBullet(this, angle);
		return bullet;
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
		String[] toDisplay = {
				"ATK: " + fireDelay,
				"xDMG: " + damageBoost,
				"MOVE: " + moveSpeed
		};
		for (int i = 0; i < toDisplay.length; i++) {
			g.drawString(toDisplay[i], beginX, beginY + sy * (i+1) / toDisplay.length);
		}
	}
}
