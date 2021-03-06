package ctia.engine.entity;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import ctia.engine.core.Level;

// Players are beings that respond to key presses 
public abstract class Player extends Being implements KeyListener {
	// Scoring
	protected long score = 0;
	// Firing
	protected int fireDelay = 10;
	protected int fireCooldown = 0;
	// Movement
	protected double moveSpeed = 5;
	// Buffs
	protected int damageBoost = 0;
	// Controls - Constants
	public enum Action {MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT};
	public static final int[] DEFAULT_KEYS = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
	// Controls
	private ArrayList<Action> controlAction = new ArrayList<Action>();
	private ArrayList<Integer> controlKey = new ArrayList<Integer>();
	private ArrayList<Boolean> controlHeld = new ArrayList<Boolean>();
	// Controls Modification
	protected boolean preventMovement = false; // Prevents player from controlling movement
	protected boolean preventFiring = false; // Prevents player from controlling firing

	public Player(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		health = getBaseHealth();
		fireDelay = getBaseFireDelay();
		moveSpeed = getBaseSpeed();

		for (int i = 0; i < Action.values().length; i++) {
			controlAction.add(Action.values()[i]);
			controlKey.add(DEFAULT_KEYS[i]);
			controlHeld.add(false);
		}
	}
	public abstract int getBaseHealth();
	public abstract int getBaseFireDelay();
	public abstract int getBaseSpeed();

	public void mapKey(Action action, int keyCode) {
		controlKey.set(action.ordinal(), keyCode);
	}
	public void keyPressed(KeyEvent ke) {
		Action action = actionOf(ke.getKeyCode());
		if (action == null) return;
		holdKey(action);
	}
	public void keyReleased(KeyEvent ke) {
		Action action = actionOf(ke.getKeyCode());
		if (action == null) return;
		releaseKey(action);

		switch (action) {
		case MOVE_UP:
			if (vy < 0) { vy = 0; }
			break;
		case MOVE_DOWN:
			if (vy > 0) { vy = 0; }
			break;
		case MOVE_LEFT:
			if (vx < 0) { vx = 0; }
			break;
		case MOVE_RIGHT:
			if (vx > 0) { vx = 0; }
			break;
		}
	}
	public void keyTyped(KeyEvent ke) { }

	protected void holdKey(Action action) {
		controlHeld.set(action.ordinal(), true);
	}
	protected void releaseKey(Action action) {
		controlHeld.set(action.ordinal(), false);
	}
	protected boolean keyHeld(Action action) {
		return controlHeld.get(action.ordinal());
	}
	protected Action actionOf(int keyCode) {
		int index = controlKey.indexOf(keyCode);
		if (index != -1) {
			return controlAction.get(index);
		}
		return null;
	}

	protected void preDt() { // handle shooting and movement
		super.preDt();
		if (!preventMovement) {
			if (keyHeld(Action.MOVE_UP)) {
				vy = -moveSpeed; this.moved();
			} else if (keyHeld(Action.MOVE_DOWN)) {
				vy = moveSpeed; this.moved();
			}
			if (keyHeld(Action.MOVE_LEFT)) {
				vx = -moveSpeed; this.moved();
			} else if (keyHeld(Action.MOVE_RIGHT)) {
				vx = moveSpeed; this.moved();
			}
		}
		fireCooldown--;
	}

	protected void moved() { } // Called whenever player tries to move
	public void tryToFire(Point p) {
		if (fireCooldown <= 0) {
			Projectile[] projectiles = makeProjectiles(p);
			for (Projectile projectile : projectiles) {
				projectile.boostDmg(damageBoost);
				container.addEntity(projectile);
			}
			fireCooldown = fireDelay;
		}
	}
	public Projectile[] makeProjectiles(Point p) {
		return new Projectile[]{makeProjectile(p)};
	}
	public abstract Projectile makeProjectile(Point p);
	public void addDamageBoost(int damageBoost) {
		this.damageBoost += damageBoost;
	}

	public void addScore(long points) { score += points; }
	public void setScore(long points) { score = points; }
	public long getScore() { return score; }
}
