package ctia.game.entity;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;

public class TestRammer extends Enemy {
	
	@Override
	public void preDt()
	{
		double ppx = container.getAPlayer().getPx();
		double ppy = container.getAPlayer().getPy();
		
		double dx = ppx - px;
		double dy = ppy - py;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

		vx = dx / distance * 9.0;
		vy = dy / distance * 9.0;
	}
	
	public TestRammer(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 100;
	}

}
