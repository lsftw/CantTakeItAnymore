package ctia.game.entity;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;

public class TestEnemy extends Enemy {
	
	@Override
	public void preDt()
	{
		if (px >= 400 || py >= 400)
		{
			vx = -3;
			vy = -3;
		}
		else if (px <= 100 || py <= 100)
		{
			vx = 3;
			vy = 3;
		}
		super.preDt();
	}
	
	public TestEnemy(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 100;
	}

}
