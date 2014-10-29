package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.flyer.Projectile;

// TODO moves at an angle based on scout's angle

// very small bullet
public class ScoutBullet extends Projectile {
	private static final int SPEED = 10;

	public ScoutBullet(Flyer owner, double xpos, double ypos) {
		this(owner, xpos, ypos, Math.PI*3/2);
	}
	public ScoutBullet(Flyer owner, double xpos, double ypos, double angle) {
		super(owner, xpos, ypos);
		damage = 10;
		vx = Math.cos(angle)*SPEED;
		vy = -Math.sin(angle)*SPEED;
	}
}
