package ctia.game.entity.powerup;

import ctia.engine.core.Level;
import ctia.engine.entity.Player;

public class DamagePowerup extends Powerup {
	public DamagePowerup(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	protected void buffPlayer(Player player) {
		player.addDamageBoost(30);
	}
}
