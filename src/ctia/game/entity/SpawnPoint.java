package ctia.game.entity;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;

public class SpawnPoint extends Enemy {

	private int time;

	@Override
	public void preDt() {
		time++;
		time %= 100;

		if (time == 0) {
			container.addEntity(new TestEnemy(container, px + 100, py + 100));
		}
	}

	public SpawnPoint(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 99999999;
	}

}
