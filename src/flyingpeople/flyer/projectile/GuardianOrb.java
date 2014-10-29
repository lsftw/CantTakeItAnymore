package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.flyer.Projectile;

public class GuardianOrb extends Projectile {
	public GuardianOrb(Flyer owner, double xpos, double ypos, boolean flyLeft) {
		super(owner, xpos, ypos);
		damage = 20;
		// vx & vy to cause the projectile to "spew" up and out
		vx = (flyLeft ? -1 : 1) * 15;
		vy = -15;
	}

	protected void preDt() {
		if (vy <= 0) {
			vy = vy*3/4 + 1;
		} else {
			if (vy < 30) {
				vy++;
			}
		}
	}
}
