package flyingpeople.flyer.projectile;

import java.awt.Point;

import flyingpeople.core.Flyer;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Projectile;

public class ScoutSensor extends Projectile {
	protected static final int SPEED = 7; // TODO speed based off of owner speed

	protected Point targetPoint;
	protected double angle;

	protected int duration = Settings.valueInt("fps") * 2;

	public ScoutSensor(Flyer owner, Point target) {
		super(owner, owner.getPx(), owner.getPy());
		targetPoint = target;
		targetting = TargettingType.OTHER;
		angle = angleBetween(target.x, target.y);
		capSpeed();
	}
	protected void capSpeed() {
		vx = SPEED * Math.cos(angle);
		vy = SPEED * Math.sin(angle);
	}
	public void postDt() {
		duration--;
		if (duration <= 0) {
			container.removeFlyer(this);
		}
	}
}
