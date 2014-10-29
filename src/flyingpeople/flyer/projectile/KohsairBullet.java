package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.flyer.Projectile;

public class KohsairBullet extends Projectile {
	private static final int SPEED = 16;

	public KohsairBullet(Flyer owner, double xpos, double ypos) {
		this(owner, xpos, ypos, Math.PI/2);
	}
	public KohsairBullet(Flyer owner, double xpos, double ypos, double angle) {
		super(owner, xpos, ypos);
		damage = 8;
		targetting = TargettingType.ENEMY;
		vx = Math.cos(angle)*SPEED;
		vy = -Math.sin(angle)*SPEED;
	}
}
