package flyingpeople.flyer.enemy;

import java.awt.Image;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.projectile.gun.BasicProjectileFactory;
import flyingpeople.flyer.projectile.gun.Weapon;

// Activates invulnerability shortly after being hit
// Invulnerability triggers only once, lasts a short time, duration decreases when hit
public class Protector extends Enemy {
	protected static final int PROTECTION_UNUSABLE = -1; // flag for protection countdown, must be negative
	protected static final int PROTECTION_DURATION = Settings.valueInt("fps") * 3;
	protected int protection = 0; // countdown, can only trigger once, PROTECTION_UNUSABLE = can no longer trigger

	protected boolean timeToProtect = false;
	protected static final int REACTION_TIME = Settings.valueInt("fps") / 5;
	protected int reactionCounter = 0; // countup
	protected static final int FIRE_DELAY = Settings.valueInt("fps") * 2;
	protected Weapon gun;

	public Protector(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() {
		health = 175;
		scoreValue = 600;
		vy = 1.75;
	}
	protected void initializeWeapon() {
		gun = new Weapon(this, new BasicProjectileFactory("ProtectorFire",20, 0,5), FIRE_DELAY, sx/2 - 7, sy);
	}

	protected Image getFrameToDraw() {
		if (protection > 0) {
			return sprite.getFrame("protected");
		}
		return super.getFrameToDraw();
	}
	protected void postDt() {
		if (protection > 0) { // protection decay
			protection--;
			if (protection == 0) protection = PROTECTION_UNUSABLE; // negative = protection already used
		} else if (protection == 0 && timeToProtect) { // protection trigger
			if (timeToProtect) { // reaction time before protection
				reactionCounter++; // allows player to chain shots to destroy before protection triggers
				if (reactionCounter >= REACTION_TIME) {
					reactionCounter = 0;
					timeToProtect = false;
					protection = PROTECTION_DURATION;
				}
			}
		}
		gun.dt();
	}
	public void hitBy(Flyer attacker, int damage) { // overridden to undo collision damage
		super.hitBy(attacker, damage);
		if (protection > 0) {
			health += damage; // revert damage taken
			protection--; // added to avoid insane damage blocking
			if (protection == 0) protection = PROTECTION_UNUSABLE; // negative = protection already used
		} else if (health > 0 && protection == 0) { // no protect if one-shotted
			timeToProtect = true;
		}
	}
}
