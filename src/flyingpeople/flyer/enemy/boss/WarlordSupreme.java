package flyingpeople.flyer.enemy.boss;

import flyingpeople.core.Zone;
import flyingpeople.flyer.projectile.gun.BasicProjectileFactory;
import flyingpeople.flyer.projectile.gun.Weapon;

// summons 3x more fighters and fires 3 missiles
public class WarlordSupreme extends Warlord {
	public WarlordSupreme(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
		scoreValue = 25000;
		maxSpeed = 4;
	}
	protected void initializeWeapon() {
		double[] xOffsets = {sx/2 - 10, sx/2};
		double[] yOffsets = {sy, sy};
		gun = new Weapon(this, new BasicProjectileFactory("WarlordMissile",50, 0,18), BASE_FIRE_DELAY,
				xOffsets, yOffsets);
	}

	protected void summonFighters() { // summons 7 fighters
		summonFighter(-119);
		summonFighter(-79);
		super.summonFighters();
		summonFighter(71);
		summonFighter(111);
	}
}
