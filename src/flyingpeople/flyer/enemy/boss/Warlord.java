package flyingpeople.flyer.enemy.boss;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.enemy.Boss;
import flyingpeople.flyer.enemy.Fighter;
import flyingpeople.flyer.projectile.gun.BasicProjectileFactory;
import flyingpeople.flyer.projectile.gun.Weapon;

public class Warlord extends Boss {
	protected int BASE_FIRE_DELAY = Settings.valueInt("fps") *4/5;
	protected static final int SUMMON_DELAY = Settings.valueInt("fps");
	protected int summonCooldown = SUMMON_DELAY;
	// When buff is used, moveSpeed increases, fire cooldown decreases
	protected static final int BUFF_DELAY = Settings.valueInt("fps") * 3;
	protected int buffCooldown = BUFF_DELAY;
	protected static final int MAX_BUFFS = 5;
	protected int buffsDone = 0;
	protected static final double MOVESPEED_BUFF = .5;
	protected Weapon gun;

	public Warlord(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	public void resetStats() {
		health = 2500;
		scoreValue = 10000;
		maxSpeed = 3;
		vy = 1; // boss moves forward for entrance
	}
	protected void initializeWeapon() {
		gun = new Weapon(this, new BasicProjectileFactory("WarlordMissile",50, 0,18), BASE_FIRE_DELAY,
				sx/2 - 10, sy);
	}

	protected void preDt() {
		super.preDt();
		if (enteredLevel()) { // has warlord finished its entrance?
			vy = 0;
			// then bounce left and right like a TI-83+ Phoenix boss
			if (px <= 0 || vx == 0) {
				vx = maxSpeed;
			} else if (px + sx >= Settings.valueInt("window_width")) {
				vx = -maxSpeed;
			}
		}
	}
	// fire missiles, summon fighters, and get faster
	protected void postDt() {
		super.postDt();
		if (!enteredLevel()) { return; } // let the boss enter before acting

		gun.dt();

		if (summonCooldown <= 0) {
			summonCooldown = SUMMON_DELAY;
			summonFighters();
		}
		if (summonCooldown > 0) summonCooldown--;

		if (buffCooldown <= 0) {
			buffCooldown = BUFF_DELAY;
			if (buffsDone < MAX_BUFFS) {
				gun.setFireDelay(gun.getFireDelay() - 1);
				maxSpeed += MOVESPEED_BUFF;
				say(1,"WARFURY!", Settings.valueInt("fps")/2);
				buffsDone++;
			}
		}
		if (buffCooldown > 0) buffCooldown--;
	}
	protected void summonFighters() {
		say(0,"REINFORCEMENTS!", Settings.valueInt("fps")/2);
		summonFighter(-39);
		summonFighter(1);
		summonFighter(41);
	}
	protected void summonFighter(double xOffset) {
		// y offset to avoid collision with warlord
		Fighter spawn = new Fighter(container, px + xOffset, py + sy + 1);
		// "spawn" attribute to avoid score farming
		spawn.morphAttributes("spawned", "weak", "fast");
		spawn.applyAttributes();
		container.addFlyer(spawn);
	}
}
