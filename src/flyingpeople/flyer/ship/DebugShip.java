package flyingpeople.flyer.ship;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.enemy.Enemy;
import flyingpeople.flyer.projectile.ElavianMissile;
import flyingpeople.flyer.projectile.KohsairBullet;
import flyingpeople.flyer.projectile.MulisBeam;

public class DebugShip extends Player {
	public int getBaseHealth() { return 10000; }
	public int getBaseFireDelay() { return Settings.valueInt("fps") / 5; }
	public int getBaseSpecialDelay() { return Settings.valueInt("fps") / 5; }
	public int getBaseSpeed() { return 10; }

	public DebugShip(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	protected void fireProjectile() { // shoot debug
		//////////////////////////////////
		// Kohsair Upgrade: MultiBullet //
		//////////////////////////////////
		/*
		Projectile fired = new KohsairBullet(this, px+sx/2 - 15, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 - 12, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 - 8, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 - 4, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 + 1, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 + 3, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 + 7, py - 10);
		container.addFlyer(fired);
		 */
		//////////////////////////////////////////
		// Kohsair Upgrade 2: MultiSpreadBullet //
		//////////////////////////////////////////
		/*Projectile fired;
		for (int i = 2; i < 5; i++) {
			fired = new KohsairBullet(this, px+sx/2 - 2, py - 10, Math.PI * i / 6);
			container.addFlyer(fired);
		}*/
		//////////////////////
		// Debug Super Fire //
		//////////////////////
		Projectile fired = new MulisBeam(this, px+sx/2 - 6, py - 80);
		container.addFlyer(fired);
		fired = new MulisBeam(this, px+sx/2 + 4, py - 80);
		container.addFlyer(fired);
		fired = new MulisBeam(this, px+sx/2 - 16, py - 80);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 - 25, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 - 35, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 + 20, py - 10);
		container.addFlyer(fired);
		fired = new KohsairBullet(this, px+sx/2 + 30, py - 10);
		container.addFlyer(fired);
		fired = new ElavianMissile(this, px+sx/2 - 5, py - 30);
		container.addFlyer(fired);
	}
	protected void useSpecial() {
		Enemy e = container.getAnEnemy();
		if (e != null) {
			health += e.getHealth();
			e.hitBy(this, health);
			e.setSx(e.getSx() /10 + 2);
			e.setPx(px+sx/2 - e.getSx()/2);
			e.say(3,"Hull Steal!", getBaseSpecialDelay());
		}
	}
}
