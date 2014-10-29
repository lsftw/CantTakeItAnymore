package flyingpeople.flyer.enemy;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.projectile.GuardianOrb;

// Durable shooter enemy
// Alternates firing orbs to left and right
// Orbs are affected by "gravity" (accelerate down the screen)
public class Guardian extends Enemy {
	protected static final int FIRE_DELAY = Settings.valueInt("fps");
	protected int fireCooldown = FIRE_DELAY;
	protected boolean fireLeft = true;

	public Guardian(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() {
		health = 200;
		scoreValue = 400;
		vy = 2;
	}

	protected void postDt() {
		if (fireCooldown <= 0) {
			fireProjectile();
			fireCooldown = FIRE_DELAY;
		}
		if (fireCooldown > 0) fireCooldown--;
	}
	protected void fireProjectile() {
		Projectile fired = new GuardianOrb(this, px+sx/2 - 6, py - sy/2, fireLeft);
		container.addFlyer(fired);
		fireLeft = !fireLeft;
	}
}
