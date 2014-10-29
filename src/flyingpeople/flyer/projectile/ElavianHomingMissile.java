package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Projectile;

public class ElavianHomingMissile extends Projectile {
	protected static final int HOMING_MAXSPEED = 16;
	protected static final int HOMING_DELAY = Settings.valueInt("fps") / 2;
	protected int homingWarmup = HOMING_DELAY;
	protected double speed;
	protected double angle;
	protected Flyer target;

	public ElavianHomingMissile(Flyer owner, double xpos, double ypos) {
		super(owner, xpos, ypos);
		damage = 131;
		targetting = TargettingType.ENEMY;
		vy = -8;
		angle = -Math.PI/2;
		speed = Math.abs(vy);
	}

	protected void preDt() {
		if (homingWarmup > 0) {
			homingWarmup--;
		} else {
			if (target == null || !container.hasFlyer(target)) {
				target = container.getAnEnemy();
			}
			if (target != null) {
				angle = angleBetween(target.getPx() + target.getSx()/2, target.getPy() + target.getSy()/2);
				//System.out.println("Target at " + (angle * 360 / 2 / Math.PI));
			}
			if (speed < HOMING_MAXSPEED) {
				speed += 2;
				if (speed > HOMING_MAXSPEED) speed = HOMING_MAXSPEED;
			}

			vx = speed * Math.cos(angle);
			vy = speed * Math.sin(angle);
			//System.out.println("Angle: " + angle + " vx: " + vx + " vy: " + vy);
		}
	}
}
