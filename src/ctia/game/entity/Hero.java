package ctia.game.entity;

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
}
