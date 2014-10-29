package flyingpeople.flyer.projectile;

import flyingpeople.core.Flyer;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;

public class MulisBeam extends Projectile {
	protected double oy; // original ypos, the y pos when fired

	public MulisBeam(Flyer owner, double xpos, double ypos) {
		super(owner, xpos + owner.getVx(), ypos);
		damage = 3;
		targetting = TargettingType.ENEMY;
		// graphical
		oy = owner.getPy();
		tiledVertically = true;
		getCollidedAndResize();
		addAttribute(ATT_LASER);
	}
	protected void postDt() {
		container.removeFlyer(this);
	}
	protected Flyer checkCollision() {
		Flyer collided = getCollidedAndResize();
		super.hit(collided);
		return collided;
	}
	protected Flyer getCollidedAndResize() {
		py = 0; sy = (int)oy;
		Flyer collided = super.getCollided();
		if (collided != null) { // change size of laser to match
			py = collided.getPy() + collided.getSy();
			// make sure the laser doesn't overlap the ship that fired it
			if (owner instanceof Player) { // players fire forward (upward)
				if (py > owner.getPy()) py = owner.getPy();
			} else { // enemies fire backward (downward)
				if (py < owner.getPy()) py = owner.getPy();
			}
		}
		sy = (int)(oy - py);
		if (sy == 0) sy = 1; // avoid div by 0
		return collided;
	}
}
