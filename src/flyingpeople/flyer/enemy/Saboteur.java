package flyingpeople.flyer.enemy;

import java.awt.Image;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;

// TODO finish implementing enemy and test
// TODO implement: cloaking evades projectiles, no collision & slow movement while cloaking
// TODO implement: fire powerful bullets, destroy projectiles
// TODO make sure cloaking no collision also does no collision for other flyer so it doesn't trigger protector or anything

// cloaking evades projectiles, destroys projectiles, fires powerful bullets
public class Saboteur extends Enemy {
	protected static final int CLOAK_DURATION = Settings.valueInt("fps");
	protected int cloakDuration = 0;

	protected static final double CLOAKED_SPEED_MULTIPLIER = .2;

	protected static final int FIRE_DELAY = Settings.valueInt("fps") * 2;
	protected int fireCooldown = FIRE_DELAY;
	protected static final int FIRE_DURATION = Settings.valueInt("fps") / 10;
	protected int fireDuration = 0;

	public Saboteur(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() { // TODO tweak stats
		health = 100;
		scoreValue = 400;
		maxSpeed = 2.5;
		vy = maxSpeed;
	}

	protected Image getFrameToDraw() {
		if (cloakDuration > 0) {
			return sprite.getFrame("cloaked");
		}
		return super.getFrameToDraw();
	}
	protected void preDt() {
		if (cloakDuration > 0) {
			vy = maxSpeed * CLOAKED_SPEED_MULTIPLIER;
			cloakDuration--;
		} else {
			vy = maxSpeed;
		}
	}
	protected void postDt() {
		if (cloakDuration <= 0 && rand.nextInt(Settings.valueInt("fps")) == 0) cloakDuration = rand.nextInt(Settings.valueInt("fps") * 10);
		//		if (protection > 0) { // protection decay
		//			protection--;
		//			if (protection == 0) protection = -1; // -1 = protection already used
		//		} else if (protection == 0 && timeToProtect) { // protection trigger
		//			if (timeToProtect) { // reaction time before protection
		//				reactionCounter++; // allows player to chain shots to destroy before protection triggers
		//				if (reactionCounter >= REACTION_TIME) {
		//					reactionCounter = 0;
		//					timeToProtect = false;
		//					protection = PROTECTION_DURATION;
		//				}
		//			}
		//		}

		if (fireCooldown <= 0) {
			fireDuration = FIRE_DURATION;
			fireCooldown = FIRE_DELAY;
		}
		if (fireDuration > 0) {
			fireProjectile();
			fireDuration--;
		} else {
			if (fireCooldown > 0) fireCooldown--;
		}
	}
	protected void fireProjectile() { //TODO fire saboteurbullet
		//		Projectile fired = new ProtectorFire(this, px + sx/2 - 7, py + sy);
		//		container.addFlyer(fired);
	}
}
