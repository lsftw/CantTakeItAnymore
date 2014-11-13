package ctia.game;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Projectile;
import ctia.game.entity.projectile.Bullet;
import ctia.game.entity.projectile.PiercingBullet;

public class TestBoss extends Enemy {
	
	public TestBoss(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		// TODO Auto-generated constructor stub
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
		
		int bullets = 5;
		
		if (time % 10 == 0) {

			double angle = Math.atan2(ppy - (this.py + this.sy / 2), ppx
					- (this.px + this.sx / 2));
			for (int i = 0; i < bullets; i++)
			{
				Projectile b;
				double offset = Math.random() * Math.PI / 36 * (double) (i - bullets / 2);
				if (Math.random() < 0.5)
				{
					b = new Bullet(this, angle + offset);
					
				}
				else
				{
					b = new PiercingBullet(this, angle + offset);
				}
				container.addEntity(b);
			}
			
		}

		super.preDt();
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 100000;
	}

}
