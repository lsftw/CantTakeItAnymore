package ctia.game.entity;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Projectile;

public class TestEnemy extends Enemy {
	
	private int time;
	
	@Override
	public void preDt()
	{
		double ppx = container.getAPlayer().getPx();
		double ppy = container.getAPlayer().getPy();
		
		double dx = ppx - px;
		double dy = ppy - py;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		
		vx = dx / distance * 3.0;
		vy = dy / distance * 3.0;
		
		time++;
		time %= 100;
		
		if (time == 0)
		{
			System.out.println("Should be bullet");
			double angle = Math.atan2(ppy - (this.py + this.sy / 2), ppx - (this.px + this.sx / 2));
			Projectile bullet = new Bullet(this, angle);
			container.addEntity(new TestEnemy(container, px + 100, py + 100));
			container.addEntity(bullet);
		}
		
		super.preDt();
	}
	
	public TestEnemy(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		time = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 100;
	}

}
