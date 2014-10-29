package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;

// prevents hit flyer from moving
public class HunterSnare extends Projectile {
	private static final double MIN_MOVING_SPEED = .01; // considered moving is velocity is at least this

	private static final int SNARE_DURATION = Settings.valueInt("fps") / 5;

	public HunterSnare(Flyer owner, double xpos, double ypos, Flyer target) {
		super(owner, xpos, ypos);
		damage = 20;
		vx = (target.getPx() - xpos)/8;
		vy = (target.getPy() - ypos)/8;
	}

	protected void preDt() {
		if (moving()) {
			vx = vx * 8/9;
			vy = vy * 8/9;
		} else {
			vx = 0;
			vy = 0;
		}
	}
	protected void hit(Flyer collided) {
		if (!moving()) { // if snare is not moving, it is deployed and can hit fliers
			// slowing code taken from HunterDisabler
			// TODO generic snare for all fliers
			if (collided instanceof Player) {
				((Player)collided).snare(SNARE_DURATION);
				collided.sayAbove(7,"Snared!", SNARE_DURATION);
			}
			super.hit(collided);
		}
	}

	protected boolean moving() {
		return Math.abs(vx) >= MIN_MOVING_SPEED && Math.abs(vy) >= MIN_MOVING_SPEED;
	}
}