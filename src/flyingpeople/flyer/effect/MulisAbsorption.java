package flyingpeople.flyer.effect;

import flyingpeople.core.Flyer;
import flyingpeople.data.Settings;
import flyingpeople.flyer.FlyerEffect;
import flyingpeople.flyer.ship.MulisShip;

public class MulisAbsorption extends FlyerEffect {
	protected static final int X_OFFSET = -32;
	protected static final int Y_OFFSET = -33;

	protected static final int DURATION = Settings.valueInt("fps") * 3 / 2;
	protected int duration = DURATION;

	public MulisAbsorption(Flyer owner) {
		super(owner, X_OFFSET, Y_OFFSET);
	}
	public void postDt() {
		duration--;
		if (duration <= 0) container.removeFlyer(this);
	}
	public void dt() {
		preDt();
		super.dt();
		checkCollision();
		postDt();
	}
	///////////////////////////////////////////
	//               Collision               //
	///////////////////////////////////////////
	protected void checkCollision() {
		Flyer collided = container.getProjectileCollided(this);
		if (collided != null) {
			sayAbove(5,"Absorbed " + collided + "!", Settings.valueInt("fps")/2);
			if (owner instanceof MulisShip) {
				((MulisShip)owner).absorbedProjectile();
			}
			container.removeFlyer(collided);
			container.removeFlyer(this);
		}
	}
}
