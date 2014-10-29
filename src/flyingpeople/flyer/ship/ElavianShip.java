package flyingpeople.flyer.ship;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.projectile.ElavianMissile;

public class ElavianShip extends Player {
	public int getBaseHealth() { return 1000; }
	public int getBaseFireDelay() { return Settings.valueInt("fps") / 2; }
	public int getBaseSpecialDelay() { return Settings.valueInt("fps") * 2; }
	public int getBaseSpeed() { return 6; }
	// Special Ability: Charge
	protected static final double CHARGE_FACTOR = 2.5; // speed multiplier when charging
	protected static final int CONTROL_LOCK_DURATION = Settings.valueInt("fps") / 2; // minimum charge time before controls unlocked
	protected int chargeTime = 0;
	protected boolean charging = false;

	public ElavianShip(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	protected void fireProjectile() { // fire the missile!, cancels charge
		Projectile fired = new ElavianMissile(this, px+sx/2 - 5, py - 20, stopCharging());
		container.addFlyer(fired);
	}
	protected void preDt() {
		if (charging) {
			if (chargeTime < CONTROL_LOCK_DURATION) {
				chargeTime++;
			} else { // allow controls
				this.preventMovement = false;
			}
		}
		super.preDt();
	}
	protected void moved() { stopCharging(); }
	protected boolean stopCharging() {
		if (charging) {
			charging = false;
			this.preventMovement = false;
			vy = 0;
			this.collisionDamage = BASE_COLLISION_DAMAGE;
			return true;
		}
		return false;
	}
	protected void useSpecial() { // Rush: charges forward, cannot attack/move for short time, afterwards keys cancel charge
		say(1,"Elavian Rush!", Settings.valueInt("fps")/5);
		chargeTime = 0;
		if (vy != 0 || (vx == 0 && vy == 0)) {
			vy = moveSpeed * CHARGE_FACTOR * (vy <= 0 ? -1 : 1); // charge backwards if moving backwards
		}
		if (vx != 0) {
			vx = moveSpeed * CHARGE_FACTOR * (vx <= 0 ? -1 : 1);
		}
		charging = true;
		applyMovementModifiers();
		this.preventMovement = true;
		this.collisionDamage /= 4;
	}
}
