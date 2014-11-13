package ctia.game;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Projectile;
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

		vx = dx / distance * 3.0;
		vy = dy / distance * 3.0;

		time++;
		time %= 100;
		
		if (time % 10 == 0) {

			double angle = Math.atan2(ppy - (this.py + this.sy / 2), ppx
					- (this.px + this.sx / 2));
			Projectile bullet0 = new PiercingBullet(this, angle - (Math.PI / 3));
			Projectile bullet1 = new PiercingBullet(this, angle - (Math.PI / 6));
			Projectile bullet2 = new PiercingBullet(this, angle);
			Projectile bullet3 = new PiercingBullet(this, angle + (Math.PI / 6));
			Projectile bullet4 = new PiercingBullet(this, angle + (Math.PI / 3));
			container.addEntity(bullet0);
			container.addEntity(bullet1);
			container.addEntity(bullet2);
			container.addEntity(bullet3);
			container.addEntity(bullet4);
		}

		super.preDt();
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 100000;
	}

}
