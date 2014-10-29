package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;

// slows down hit flyer
public class HunterDisabler extends Projectile {
	private static final int SLOWDOWN_FACTOR = 5;
	private static final int SLOWDOWN_DURATION = Settings.valueInt("fps") * 3/2;
	//
	public HunterDisabler(Flyer owner, double xpos, double ypos) {
		super(owner, xpos, ypos);
		damage = 10;
		vy = 55;
	}
	protected void hit(Flyer collided) {
		// TODO generic slow for all fliers
		Player p;
		if (collided instanceof Player) {
			p = (Player)collided;
			p.slowDown(SLOWDOWN_FACTOR, SLOWDOWN_DURATION);
		}
		super.hit(collided);
	}
}