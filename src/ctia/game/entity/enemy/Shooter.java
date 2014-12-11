package ctia.game.entity.enemy;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Player;
import ctia.engine.entity.Projectile;
import ctia.game.entity.projectile.Bullet;

public class Shooter extends Enemy {

	private int fireTime;

	@Override
	public void preDt() {
		chasePlayer(3.0);

		Player player = container.getAPlayer();
		double ppx = player.getPx();
		double ppy = player.getPy();

		double dist = this.getDistanceTo(player);
//		double maxFireDist = Math.max(Settings.getWindowWidth(), Settings.getWindowHeight());
		double maxFireDist = Double.MAX_VALUE; // disable max fire dist

		if (dist < maxFireDist) {
			fireTime++;
			fireTime %= 100;
			
			if (fireTime % 10 == 0) {
	
				double angle = Math.atan2(ppy - (this.py + this.sy / 2), ppx
						- (this.px + this.sx / 2));
				Projectile bullet = new Bullet(this, angle);
				container.addEntity(bullet);
			}
		}

		super.preDt();
	}

	public Shooter(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		fireTime = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 1000;
	}

}
