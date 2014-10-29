package flyingpeople.flyer.ship;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.projectile.KohsairBullet;

public class KohsairShip extends Player {
	protected static final int[] BULLET_XOFFSET = {-15, -8, 1, 7};

	public int getBaseHealth() { return 800; }
	public int getBaseFireDelay() { return Settings.valueInt("fps") / 7; }
	public int getBaseSpecialDelay() { return Settings.valueInt("fps"); } // TODO adjust delay
	public int getBaseSpeed() { return 9; }

	public KohsairShip(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	protected void fireProjectile() { // quad scatter shot
		boolean doppler = false;
		if (vx == 0) {
			if (vy < 0) { // move forward = multiply damage
				say(1,"DOPPLER SHOT", getBaseFireDelay());
				doppler = true;
			} else if (vy > 0) { // move backward = quick fire
				say(0,"BULLET RAIN", getBaseFireDelay());
				fireCooldown /= 2;
			}
		}

		Projectile fired;
		for (int i = 0; i < BULLET_XOFFSET.length; i++) {
			fired = new KohsairBullet(this, px+sx/2 + BULLET_XOFFSET[i], py - 10);
			if (doppler) fired.multDmg(3);
			container.addFlyer(fired);
		}
	}
	protected void useSpecial() {
		// TODO add kohsair special
	}
}
