package ctia.game.entity.powerup;

import ctia.engine.core.Level;
import ctia.engine.entity.Player;

// heal player, drops separately from enemy
public class HealthPowerup extends Powerup {
	public HealthPowerup(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	protected void buffPlayer(Player player) {
		int healPercent = 5;
		int newPlayerHealth = player.getHealth() + (player.getBaseHealth() / healPercent);
		player.setHealth(newPlayerHealth);
	}
}
