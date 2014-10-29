package flyingpeople.flyer.enemy;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.effect.SentinelSensor;
import flyingpeople.flyer.projectile.gun.BasicProjectileFactory;
import flyingpeople.flyer.projectile.gun.Weapon;

// strafes to dodge projectiles, fires towards player
public class Sentinel extends Enemy {
	protected Flyer toAvoid = null;
	protected static final double NORMAL_SPEED_MULTIPLIER = .2; // when not strafing

	protected static final int STRAFE_DURATION = Settings.valueInt("fps") / 2;
	protected int strafeDuration = 0;
	protected static final int SENSOR_COOLDOWN = Settings.valueInt("fps");
	protected int sensorCooldown = 0;
	protected static final int FIRE_DELAY = Settings.valueInt("fps");
	protected Weapon gun;

	public Sentinel(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() {
		health = 70;
		scoreValue = 400;
		maxSpeed = 8;
		vy = maxSpeed * NORMAL_SPEED_MULTIPLIER;
	}
	protected void initializeWeapon() {
		double[] xOffsets = {sx/2 - sx/6 -1, sx/2 + sx/10};
		double[] yOffsets = {sy, sy};
		gun = new Weapon(this, new BasicProjectileFactory("SentinelBullet",15, 0,10), FIRE_DELAY,
				xOffsets, yOffsets);
	}

	public void preDt() {
		if (toAvoid != null) { // dodge projectile
			if (strafeDuration < STRAFE_DURATION) {
				strafeDuration++;
				vy = 0;
			} else {
				vx = 0;
				toAvoid = null;
				strafeDuration = 0;
			}
		} else { // move forward and activate sensor when possible
			vy = maxSpeed * NORMAL_SPEED_MULTIPLIER;
			if (sensorCooldown > 0) {
				sensorCooldown--;
			} else {
				activateSensor();
			}
		}
		// decrease speed to avoid going off bounds
		if (vx < 0 && px + vx < 0) {
			vx = -px;
		} else if (vx > 0 && px + sx + vx > Settings.valueInt("window_width")) {
			vx = Settings.valueInt("window_width") - sx - px;
		}
	}
	protected void postDt() {
		gun.dt();
	}
	protected void activateSensor() {
		SentinelSensor aura = new SentinelSensor(this);
		container.addFlyer(aura);
		sensorCooldown = SENSOR_COOLDOWN;
	}
	public void sensedProjectile(Flyer collided) {
		toAvoid = collided;
		// move right if on left half of screen (don't go out of bounds)
		if (px < Settings.valueInt("window_width")/2) {
			vx = maxSpeed;
		} else {
			vx = -maxSpeed;
		}
	}
}
