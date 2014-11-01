package ctia.entity.enemy;

import ctia.core.Entity;
import ctia.core.Zone;
import ctia.data.Settings;
import ctia.entity.Being;
import ctia.entity.Player;

public abstract class Enemy extends Being {
	protected Entity lastAttacker;
	protected long scoreValue = 0;

	protected double maxSpeed = 1000000;

	public Enemy(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
		resetStats();
	}
	public abstract void resetStats(); // set movespeed and health here

	public boolean enteredLevel() {
		return py >= Settings.getMinY();
	}

	public void dt() {
		if (health > 0) {
			if (vx > maxSpeed) vx = maxSpeed;
			if (vy > maxSpeed) vy = maxSpeed;

			super.dt();
			Being collided = container.getBeingCollided(this, false);
			if (collided != null) hit(collided);
		} else {
			destructionTrigger();
			container.removeEntity(this);
		}
	}

	protected void hit(Being collided) { // collide with another entity, damaging both fliers
		this.hitBy(collided, this.collisionDamage);
		collided.hitBy(this, collided.getCollisionDamage());
	}
	public void hitBy(Entity attacker, int damage) { // for giving points on destruction
		lastAttacker = attacker;
		super.hitBy(attacker, damage);
	}
	public void awardAttacker(long points) {
		if (lastAttacker instanceof Player) {
			((Player)lastAttacker).addScore(points);
		} else if (lastAttacker instanceof Enemy) {
			((Enemy)lastAttacker).addScore(points);
			//if (points > 0) System.out.println(lastAttacker + " got " + points + " points from friendly fire!");
		}
	}
	protected void destructionTrigger() {
		awardAttacker(scoreValue);
	}
	public void addScore(long points) { scoreValue += points; } // friendly fired!
}
