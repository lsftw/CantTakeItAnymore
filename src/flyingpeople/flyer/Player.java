package flyingpeople.flyer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;
import flyingpeople.data.Settings;

// don't forget to add a key listener to the player!
public abstract class Player extends Being implements KeyListener {
	// Scoring
	protected long score = 0;
	// Firing
	protected int fireDelay = 10;
	protected int fireCooldown = 0;
	// Special Ability
	protected int specialDelay = Settings.valueInt("fps") * 3;
	protected int specialCooldown = 0;
	// Movement
	protected double moveSpeed = 5;
	protected double hasteFactor = 0;
	protected int hasteDuration = 0;
	protected double slowFactor = 0;
	protected int slowDuration = 0;
	protected int snareDuration = 0;
	// Controls - Constants
	public enum Action {MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, ACT_FIRE, ACT_SPECIAL};
	public static final int[] DEFAULT_KEYS = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_X, KeyEvent.VK_Z};
	// Controls
	private ArrayList<Action> control_action = new ArrayList<Action>();
	private ArrayList<Integer> control_key = new ArrayList<Integer>();
	private ArrayList<Boolean> control_held = new ArrayList<Boolean>();
	// Controls Modification
	protected boolean preventMovement = false; // Prevents player from controlling movement
	protected boolean preventFiring = false; // Prevents player from controlling firing

	public Player(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
		health = getBaseHealth();
		fireDelay = getBaseFireDelay();
		specialDelay = getBaseSpecialDelay();
		moveSpeed = getBaseSpeed();

		for (int i = 0; i < Action.values().length; i++) {
			control_action.add(Action.values()[i]);
			control_key.add(DEFAULT_KEYS[i]);
			control_held.add(false);
		}
	}
	public abstract int getBaseHealth();
	public abstract int getBaseFireDelay();
	public abstract int getBaseSpecialDelay();
	public abstract int getBaseSpeed();

	public void mapKey(Action action, int keyCode) {
		control_key.set(action.ordinal(), keyCode);
		// TODO warning if keys overlap (and offer to reset keys)
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
		case ACT_FIRE:
			break;
		case ACT_SPECIAL:
			break;
		}
	}
	public void keyTyped(KeyEvent ke) { }

	protected void holdKey(Action action) {
		control_held.set(action.ordinal(), true);
	}
	protected void releaseKey(Action action) {
		control_held.set(action.ordinal(), false);
	}
	protected boolean keyHeld(Action action) {
		return control_held.get(action.ordinal());
	}
	protected Action actionOf(int keyCode) {
		int index = control_key.indexOf(keyCode);
		if (index != -1) {
			return control_action.get(index);
		}
		return null;
	}

	protected void preDt() { // handle shooting and movement
		super.preDt();
		if (!preventFiring) { // also stops firing cooldown
			if (keyHeld(Action.ACT_FIRE)) {
				if (fireCooldown <= 0) {
					fireCooldown = fireDelay;
					fireProjectile();
				}
			}
			if (fireCooldown > 0) fireCooldown--;
		}
		if (keyHeld(Action.ACT_SPECIAL)) {
			if (specialCooldown <= 0) {
				specialCooldown = specialDelay;
				useSpecial();
			}
		}
		if (specialCooldown > 0) specialCooldown--;

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

			applyMovementModifiers();
		}
	}
	protected void postDt() { // prevent moving out of bounds
		if (px < Flyer.getMinX()) px = Flyer.getMinX();
		if (px + sx > Flyer.getMaxX()) px = Flyer.getMaxX() - sx;
		if (py < Flyer.getMinY()) py = Flyer.getMinY();
		if (py + sy > Flyer.getMaxY()) py = Flyer.getMaxY() - sy;
		// haste/slow/snare decay
		if (hasteDuration > 0) hasteDuration--;
		if (slowDuration > 0) slowDuration--;
		if (snareDuration > 0) snareDuration--;
	}
	// apply Haste/Slow/Snare should be applied in that order whenever player tries to move
	protected void applyMovementModifiers() {
		applyHaste();
		applySlow();
		applySnare();
	}
	protected void applyHaste() {
		if (hasteDuration > 0) {
			vx *= hasteFactor;
			vy *= hasteFactor;
		}
	}
	protected void applySlow() {
		if (slowDuration > 0) {
			vx /= slowFactor;
			vy /= slowFactor;
		}
	}
	protected void applySnare() {
		if (snareDuration > 0) { // immobilize player if snared
			vx = 0;
			vy = 0;
		}
	}

	protected void moved() { } // Called whenever player tries to move
	protected abstract void fireProjectile();
	protected abstract void useSpecial();

	// for players, the message appears above them instead of below by default
	public void say(int messageNum, String message, int duration) {
		sayAbove(messageNum, message, duration);
	}

	public void addScore(long points) { score += points; }
	public void setScore(long points) { score = points; }
	public long getScore() { return score; }

	public void speedUp(double factor, int tickDuration) {
		// TODO allow stacking instead of overwrite
		if (factor == 0) tickDuration = 0; // avoid div by 0
		hasteFactor = factor;
		hasteDuration = tickDuration;
	}
	public void slowDown(double factor, int tickDuration) {
		// TODO allow stacking instead of overwrite
		if (factor == 0) tickDuration = 0; // avoid div by 0
		slowFactor = factor;
		slowDuration = tickDuration;
	}
	public void snare(int tickDuration) { // non-stacking
		snareDuration = Math.max(snareDuration, tickDuration);
	}
}
