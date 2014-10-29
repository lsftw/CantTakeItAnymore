package flyingpeople.flyer.effect;

import flyingpeople.core.Flyer;
import flyingpeople.data.Settings;
import flyingpeople.flyer.FlyerEffect;
import flyingpeople.flyer.enemy.Sentinel;

// used by sentinel to sense projectiles to dodge
public class SentinelSensor extends FlyerEffect {
	protected static final int X_OFFSET = 0;
	protected static final int Y_OFFSET = -40;

	protected static final int DURATION = Settings.valueInt("fps");
	protected int duration = DURATION;

	public SentinelSensor(Flyer owner) {
		super(owner, X_OFFSET, Y_OFFSET);
		sx = owner.getSx();
		sy = owner.getSy() * 5;
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

	protected void checkCollision() {
		Flyer collided = container.getProjectileCollided(this);
		// detects projectiles moving upwards
		if (collided != null && collided.getVy() < 0) {
			if (owner instanceof Sentinel) {
				((Sentinel)owner).sensedProjectile(collided);
			}
			container.removeFlyer(this);
		}
	}
}
