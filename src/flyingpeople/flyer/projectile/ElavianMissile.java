package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.flyer.ExplosiveProjectile;

public class ElavianMissile extends ExplosiveProjectile {
	public ElavianMissile(Flyer owner, double xpos, double ypos) { this(owner, xpos, ypos, false); }
	public ElavianMissile(Flyer owner, double xpos, double ypos, boolean charged) {
		super(owner, xpos, ypos, 300);
		damage = 30;
		targetting = TargettingType.ENEMY;
		vy = -10;

		explosionDamage = damage;
		explosionSize = (sx + sy)*5;

		if (charged) {
			vy *= 3;
		}
	}
}
