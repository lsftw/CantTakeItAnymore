package flyingpeople.flyer.enemy;

import flyingpeople.core.Zone;

public abstract class Boss extends Enemy {
	public Boss(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
	protected void destructionTrigger() {
		super.destructionTrigger();
		container.getLevel().bossDestroyed();
	}
}
