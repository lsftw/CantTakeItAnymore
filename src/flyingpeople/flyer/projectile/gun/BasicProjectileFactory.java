package flyingpeople.flyer.projectile.gun;

import flyingpeople.core.Flyer;

// everyone loves factory pattern
public class BasicProjectileFactory {
	protected String name;
	protected int damage;
	protected double vx, vy;

	public BasicProjectileFactory(String name, int damage, double vx, double vy) {
		this.name = name;
		this.damage = damage;
		this.vx = vx;
		this.vy = vy;
	}

	public BasicProjectile makeInstance(Flyer owner, double px, double py) {
		BasicProjectile proj = new BasicProjectile(name, owner, px, py);
		proj.setDamage(damage);
		proj.setVx(vx);
		proj.setVy(vy);
		return proj;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getDamage() { return damage; }
	public void setDamage(int damage) { this.damage = damage; }
	public double getVx() { return vx; }
	public void setVx(double vx) { this.vx = vx; }
	public double getVy() { return vy; }
	public void setVy(double vy) { this.vy = vy; }
}
