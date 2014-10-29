package flyingpeople.flyer.projectile.gun;

import flyingpeople.core.Flyer;
import flyingpeople.flyer.Projectile;

// TODO add cooldown
// TODO use it, Flyer should provide method to create Weapons
// TODO scaling options
public class Weapon {
	protected Flyer owner;
	protected BasicProjectileFactory bpf;
	protected int fireDelay;
	protected int fireCooldown;
	protected double[] offX, offY;

	public Weapon(Flyer owner, BasicProjectileFactory bpf, int fireDelay, double offX, double offY) {
		this(owner, bpf, fireDelay, new double[]{offX}, new double[]{offY});
	}
	public Weapon(Flyer owner, BasicProjectileFactory bpf, int fireDelay, double[] offX, double[] offY) {
		this.owner = owner;
		this.bpf = bpf;
		this.fireDelay = fireDelay;
		this.offX = offX;
		this.offY = offY;
		this.fireCooldown = fireDelay;
	}

	public void dt() {
		if (isReadyToFire()) {
			fire();
			fireCooldown = fireDelay;
		} else {
			coolDown();
		}
	}
	public void coolDown() {
		if (fireCooldown > 0) fireCooldown--;
	}

	protected void fire() {
		for (int i = 0; i < offX.length; i++) {
			Projectile p = bpf.makeInstance(owner, owner.getPx() + offX[i], owner.getPy() + offY[i]);
			owner.getZone().addFlyer(p);
		}
	}

	public double[] getOffX() { return offX; }
	public void setOffX(double[] offX) { this.offX = offX; }
	public void setOffX(double offX) { this.offX = new double[]{offX}; }
	public double[] getOffY() { return offY; }
	public void setOffY(double[] offY) { this.offY = offY; }
	public void setOffY(double offY) { this.offY = new double[]{offY}; }

	public int getFireDelay() { return fireDelay; }
	public void setFireDelay(int fireDelay) { this.fireDelay = fireDelay; }

	public boolean isReadyToFire() { return fireCooldown <= 0; }
}
