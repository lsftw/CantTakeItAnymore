package flyingpeople.flyer.ship;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.effect.MulisAbsorption;
import flyingpeople.flyer.projectile.MulisBeam;

public class MulisShip extends Player {
	public int getBaseHealth() { return 600; }
	public int getBaseFireDelay() { return Settings.valueInt("fps") / 50; } // NOT ALWAYS 1 for balance purposes, this way Mulis won't be stronger at higher FPS
	public int getBaseSpecialDelay() { return Settings.valueInt("fps") * 2; }
	public int getBaseSpeed() { return 7; }

	protected static final double ABSORPTION_BOOST_FACTOR = 2; // damage multiplier when energy is stored	
	protected static final int ABSORB_PROJECTILE_ENERGY = Settings.valueInt("fps") *3; // energy gained per projectile absorbed
	protected static final int ABSORB_ENERGY_MAX = ABSORB_PROJECTILE_ENERGY * 10; // maximum energy stored
	protected int absorbedEnergy = 0; // if >0, deal more damage

	public MulisShip(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	public void preDt() {
		if (absorbedEnergy > 0) absorbedEnergy--;
		super.preDt();
	}
	protected void fireProjectile() { // laser beam
		Projectile fired = new MulisBeam(this, px+sx/2 - 6, py - 80);
		if (absorbedEnergy > 0) fired.multDmg(ABSORPTION_BOOST_FACTOR);
		container.addFlyer(fired);
	}
	protected void useSpecial() {
		MulisAbsorption aura = new MulisAbsorption(this);
		container.addFlyer(aura);
	}

	public void absorbedProjectile() {
		absorbedEnergy += ABSORB_PROJECTILE_ENERGY;
		if (absorbedEnergy > ABSORB_ENERGY_MAX) absorbedEnergy = ABSORB_ENERGY_MAX;
	}
}
