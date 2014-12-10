package ctia.game.entity.powerup;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;
import ctia.engine.entity.Player;

public abstract class Powerup extends Entity {
	public Powerup(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	public void postDt() {
		Player player = container.getAPlayer();
		if (player != null && this.collidesWith(player)) {
			buffPlayer(player);
			container.removeEntity(this);
		}
	}

	protected abstract void buffPlayer(Player player);
}
