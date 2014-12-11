package ctia.game.entity.enemy;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;

public class Rammer extends Enemy {
	
	@Override
	public void preDt()
	{
		chasePlayer(9.0);
		super.preDt();
	}
	
	public Rammer(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 2000;
	}

}
