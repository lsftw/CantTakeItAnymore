package ctia.entity;

import ctia.core.Entity;

public abstract class ExplosiveProjectile extends Projectile {
	protected int explosionSize;
	protected int explosionDamage = 0;

	public ExplosiveProjectile(Entity owner, double xpos, double ypos, int explosionSize) {
		super(owner, xpos, ypos);
		this.explosionSize = explosionSize;
	}
	public void explode() {
		Explosion e = new Explosion(owner, px+sx/2, py+sy/2,
				explosionSize, explosionDamage);
		container.addentity(e);
	}
}