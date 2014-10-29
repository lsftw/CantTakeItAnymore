package flyingpeople.flyer.enemy.boss;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.enemy.Boss;
import flyingpeople.flyer.projectile.HunterDisabler;
import flyingpeople.flyer.projectile.HunterSnare;
import flyingpeople.flyer.projectile.gun.BasicProjectileFactory;
import flyingpeople.flyer.projectile.gun.Weapon;

//  move horizontally and position to hit player with disabler, then unleash eliminator
//  toss snares occasionally, toss them more often over time
public class Hunter extends Boss {
	protected static final int FIRE_DELAY = Settings.valueInt("fps");
	protected int fireCooldown = FIRE_DELAY;
	protected enum HunterAction {DISABLE, ELIMINATE}
	protected HunterAction currentAction = HunterAction.DISABLE;

	protected int eliminateChargeTime = Settings.valueInt("fps");
	protected int eliminateCharge = 0;
	protected int eliminateClip = 5;
	protected int eliminateAmmo = eliminateClip;
	protected Weapon gun;

	protected int minSnareDelay = Settings.valueInt("fps");
	protected int snareDelay = Settings.valueInt("fps") * 3;
	protected int snareCooldown = snareDelay;

	protected static final double ELIMINATE_SPD_MULT = .5;
	protected Flyer target = null;
	protected double moveSpeed;

	public Hunter(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() {
		health = 2500;
		scoreValue = 10000;
		maxSpeed = 5.5;
		vy = 1; // boss moves forward for entrance
	}
	protected void initializeWeapon() {
		double[] xOffsets = {sx/2 - 24, sx/2 - 12, sx/2, sx/2 + 12, sx/2 + 24};
		double[] yOffsets = {sy, sy, sy, sy, sy};
		gun = new Weapon(this, new BasicProjectileFactory("HunterBullet",10, 0,20), 0,
				xOffsets, yOffsets);
	}

	protected void preDt() {
		super.preDt();
		if (enteredLevel()) { // has hunter finished its entrance?
			vy = 0;

			if (target == null) {
				target = container.getAPlayer();
			}

			if (target != null) { // keep center aligned with player center
				// also cap velocity at movespeed, but account for fact that velocity may be negative
				vx = (target.getPx() + target.getSx()/2) - (px + sx/2);
				vx = Math.min(Math.abs(vx), moveSpeed) * Math.signum(vx);
			} // else shouldn't happen.
		}
	}
	protected void postDt() { // slow player, unleash strong attack
		super.postDt();
		if (!enteredLevel()) { return; } // let the boss enter before acting

		switch (currentAction) {
		case DISABLE:
			moveSpeed = maxSpeed;
			if (fireCooldown <= 0) { // disable then eliminate
				fireCooldown = FIRE_DELAY;
				fireDisabler();
				currentAction = HunterAction.ELIMINATE;
			}
			if (fireCooldown > 0) fireCooldown--;
			break;

		case ELIMINATE:
			moveSpeed = maxSpeed * ELIMINATE_SPD_MULT;
			if (eliminateAmmo > 0) { // charge/fire eliminator
				if (eliminateCharge < eliminateChargeTime) {
					say(2,"CHARGING", 1);
					eliminateCharge++;
				} else {
					say(2,"ELIMINATE!", 1);
					gun.dt();
					eliminateAmmo--;
				}
			} else { // finished firing eliminator
				eliminateCharge = 0;
				eliminateAmmo = eliminateClip;
				currentAction = HunterAction.DISABLE;
			}
			break;
		}

		if (snareCooldown <= 0) {
			snareCooldown = snareDelay;
			fireSnare();

			if (snareDelay > minSnareDelay) snareDelay--;
		} else {
			snareCooldown--;
		}
	}

	protected void fireDisabler() {
		say(0,"DISABLE!", Settings.valueInt("fps")/2);
		Projectile fired = new HunterDisabler(this, px+sx/2 - 10, py + sy);
		container.addFlyer(fired);
	}
//	protected void fireEliminator() {
//		say(2,"ELIMINATE!", 1);
//		Projectile fired = new HunterBullet(this, px+sx/2 - 24, py + sy);
//		container.addFlyer(fired);
//		fired = new HunterBullet(this, px+sx/2 - 12, py + sy);
//		container.addFlyer(fired);
//		fired = new HunterBullet(this, px+sx/2, py + sy);
//		container.addFlyer(fired);
//		fired = new HunterBullet(this, px+sx/2 + 12, py + sy);
//		container.addFlyer(fired);
//		fired = new HunterBullet(this, px+sx/2 + 24, py + sy);
//		container.addFlyer(fired);
//	}
	protected void fireSnare() {
		say(1,"SNARE!", 20);
		Projectile fired = new HunterSnare(this, px+sx/2 - 10, py + sy, target);
		container.addFlyer(fired);
	}
}