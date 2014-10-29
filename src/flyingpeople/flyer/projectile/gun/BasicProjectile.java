package flyingpeople.flyer.projectile.gun;

import flyingpeople.core.Flyer;
import flyingpeople.core.Sprite;
import flyingpeople.display.Art;
import flyingpeople.flyer.Projectile;

// basic projectiles simplify projectiles that have constant damage and velocities
public class BasicProjectile extends Projectile {
	// this.getClass().getSimpleName() convention from Flyer's toString()
	protected String name = "BasicProjectile";
	protected boolean ready = false;

	protected BasicProjectile(String name, Flyer owner, double px, double py) {
		super(owner, px, py);
		this.name = name;
		ready = true;
		initialize();
	}
	protected void initialize() {
		if (ready) {
			super.initialize();
		}
	}
	protected Sprite getSpriteToLoad() {
		return Art.getSprite(this.toString());
	}

	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return name;
	}
}
