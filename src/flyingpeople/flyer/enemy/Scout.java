package flyingpeople.flyer.enemy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;
import flyingpeople.flyer.projectile.ScoutSensor;

// TODO finish implementing enemy and test
// TODO bullet x offset: 5

// curved movement, chases player, fires forward
public class Scout extends Enemy {
	protected double targetSpeed;
	protected Flyer target;
	protected Point targetPos;

	// nw,sw,se,ne directional multipliers
	protected final int[] DIR_MULTIPLIER_X = {-1,-1,1,1};
	protected final int[] DIR_MULTIPLIER_Y = {-1,1,1,-1};
	protected List<ScoutSensor> sensors = new ArrayList<ScoutSensor>();

	public Scout(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() { // TODO tweak stats
		health = 30;
		scoreValue = 100;
		maxSpeed = 3;
		vy = 3*0; // move forward to enter screen
	}
	public void preDt() {
		if (enteredLevel()) {
			if (target == null) {
				target = container.getAPlayer();
			}

			if (target != null) {
				if (targetPos == null) {
					findDestination();
				}
				if (targetPos != null) {
					if (reachedTarget()) {
						// TODO fire						
					} else {
						// TODO move towards
						targetSpeed = maxSpeed / 2;
						angle += Math.PI / 600;
						adjustSpeed();
					}
				}
			}
		}
	}

	protected void findDestination() {
		// Scout sends out 4 sensors in pulses
		for (int i = 0; i < sensors.size(); i++) {
			if (!sensors.get(i).isActive()) {
				sensors.remove(i);
				i--;
			}
		}
		if (sensors.size() > 0) {
			return;
		}

		// 5*"radius" distance from center of target in each direction
		double centerX = target.getPx() + target.getSx()/2;
		double centerY = target.getPy() + target.getSy()/2;
		double radiusX = target.getSx()/2;
		double radiusY = target.getSy()/2;
		int tx, ty;
		ScoutSensor sensor;

		for (int i = 0; i < DIR_MULTIPLIER_X.length; i++) {
			tx = (int)(centerX + DIR_MULTIPLIER_X[i]*5*radiusX);
			ty = (int)(centerY + DIR_MULTIPLIER_Y[i]*5*radiusY);
			sensor = new ScoutSensor(this, new Point(tx, ty));
			sensors.add(sensor);
			container.addFlyer(sensor);
		}
	}
	protected void sensedSafe(Point safeTarget) {
		if (targetPos != null) {
			targetPos = safeTarget;
		}
	}

	protected void adjustSpeed() {
		double dirAngle = angle + Math.PI / 2;
		vx = targetSpeed * Math.cos(dirAngle);
		vy = targetSpeed * Math.sin(dirAngle);
	}
	protected boolean reachedTarget() {
		// TODO Auto-generated method stub
		return (Math.abs((int)px - targetPos.x) > 0) || (Math.abs((int)py - targetPos.y) > 0);
	}
}
