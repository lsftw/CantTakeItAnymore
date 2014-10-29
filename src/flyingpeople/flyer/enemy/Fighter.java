package flyingpeople.flyer.enemy;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;

// Basic weak enemy
// Charges forward, moving faster over time
public class Fighter extends Enemy {
	protected static final int CHARGE_DELAY = Settings.valueInt("fps") * 2;
	protected int chargeWarmup = CHARGE_DELAY;

	public Fighter(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() {
		health = 50;
		scoreValue = 100;
		vy = 1;
		maxSpeed = 5; // max speed for charge
	}

	protected void preDt() {
		if (chargeWarmup > 0) {
			chargeWarmup--;
		} else {
			vy++;
		}
	}
}
