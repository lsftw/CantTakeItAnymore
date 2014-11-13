package ctia.game.entity.projectile;

import ctia.engine.core.Entity;
import ctia.engine.entity.Projectile;

public class Bullet extends Projectile {
	private static final int SPEED = 30;
	public Bullet(Entity owner, double angle) {
		super(owner, owner.getPx(), owner.getPy());
		px = px + owner.getSx() / 2 - sx / 2;
		py = py + owner.getSy() / 2 - sy / 2;
		damage = 20;
		vx = Math.cos(angle) * SPEED;
		vy = Math.sin(angle) * SPEED;
		targetting = TargettingType.OTHER;
	}
}
