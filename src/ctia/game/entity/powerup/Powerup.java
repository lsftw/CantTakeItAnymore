package ctia.game.entity.powerup;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;
import ctia.engine.entity.Player;

public abstract class Powerup extends Entity {
	public Powerup(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	public void addSpread(double maxSpreadSpeed) {
		double randAngle = rand.nextDouble() * (2 * Math.PI);
		double spreadSpeed = rand.nextDouble() * maxSpreadSpeed;
		double vx = Math.cos(randAngle) * spreadSpeed;
		double vy = Math.sin(randAngle) * spreadSpeed;
		this.vx = vx;
		this.vy = vy;
	}

	public void preDt() {
		double speedDecayMult = .9;
		this.vx *= speedDecayMult;
		this.vy *= speedDecayMult;
		super.preDt();
	}

	public void postDt() {
		super.postDt();
		Player player = container.getAPlayer();
		if (player != null && this.collidesWith(player)) {
			buffPlayer(player);
			container.removeEntity(this);
		}
	}

	protected abstract void buffPlayer(Player player);
}
