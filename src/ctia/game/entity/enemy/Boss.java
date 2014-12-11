package ctia.game.entity.enemy;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Player;
import ctia.engine.entity.Projectile;
import ctia.game.entity.projectile.PiercingBullet;

public class Boss extends Enemy {

	private int fireTime;
	private double maxSpeed = 1.0;

	public Boss(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		itemDropChance = 50;
		maxItemsDropped = 150;
		maxHealthDropped = 150;
	}

	@Override
	public void preDt() {
		chasePlayer(maxSpeed);
		tryFire();

		super.preDt();
	}

	private void tryFire() {
		Player player = container.getAPlayer();
		double ppx = player.getPx();
		double ppy = player.getPy();
		fireTime++;
		fireTime %= 100;
		
		int bullets = 10;
		
		if (fireTime % 10 == 0) {

			double angle = Math.atan2(ppy - (this.py + this.sy / 2), ppx
					- (this.px + this.sx / 2));
			for (int i = 0; i < bullets; i++)
			{
				Projectile b;
				double offset = Math.random() * Math.PI / 6 * (double) (i - bullets / 2) / bullets;
				b = new PiercingBullet(this, angle + offset);
				container.addEntity(b);
			}
			
		}
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 30000;
	}
}
