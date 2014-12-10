package ctia.game.entity.enemy;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Projectile;
import ctia.game.entity.projectile.PiercingBullet;

public class TestBoss extends Enemy {
	
	public TestBoss(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	private int time;

	@Override
	public void preDt() {
		double ppx = container.getAPlayer().getPx();
		double ppy = container.getAPlayer().getPy();
		
		double dx = ppx - px;
		double dy = ppy - py;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

		vx = dx / distance * 1.0;
		vy = dy / distance * 1.0;

		time++;
		time %= 100;
		
		int bullets = 20;
		
		if (time % 10 == 0) {

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

		super.preDt();
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 10000;
	}
}
