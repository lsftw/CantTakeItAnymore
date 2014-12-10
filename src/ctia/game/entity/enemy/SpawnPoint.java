package ctia.game.entity.enemy;

import ctia.engine.core.Level;
import ctia.engine.entity.Enemy;

public class SpawnPoint extends Enemy {
	private int spawnTime;

	public SpawnPoint(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		maxItemsDropped = 5;
		maxHealthDropped = 5;
	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		health = 10000;
	}

	@Override
	public void preDt() {
		spawnTime++;
		spawnTime %= 100;

		if (spawnTime % 50 == 0) {
			container.addEntity(new Rammer(container, px + 100, py + 100));
			if (spawnTime == 0)
			{
				container.addEntity(new Shooter(container, px - 100, py - 100));
			}
		}
	}
}
