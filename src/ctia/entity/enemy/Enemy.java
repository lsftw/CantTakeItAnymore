package ctia.entity.enemy;

import java.util.ArrayList;

import ctia.core.Entity;
import ctia.core.Zone;
import ctia.data.Settings;
import ctia.entity.Being;
import ctia.entity.Player;

public abstract class Enemy extends Being {
	private ArrayList<String> attributes = new ArrayList<String>();
	protected Entity lastAttacker;
	protected long scoreValue = 0;

	protected double maxSpeed = 1000000;

	public Enemy(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
		resetStats();
		initializeWeapon();
	}
	public abstract void resetStats(); // set movespeed and health here
	protected void initializeWeapon() { }

	public boolean enteredLevel() {
		return py >= Entity.getMinY();
	}

	public void applyAttributes() {
		if (this.hasAttribute("fast")) {
			vx *= 2;
			vy *= 2;
			maxSpeed *= 2;
			scoreValue = scoreValue * 3/2;
		}
		if (this.hasAttribute("slow")) {
			vx /= 2;
			vy /= 2;
			maxSpeed /= 2;
			scoreValue = scoreValue / 2;
		}
		if (this.hasAttribute("superslow")) {
			vx /= 10;
			vy /= 10;
			maxSpeed /= 10;
			scoreValue = scoreValue / 10;
		}
		if (this.hasAttribute("weak")) {
			health /= 2;
			scoreValue = scoreValue / 2;
		}
		if (this.hasAttribute("durable")) {
			health *= 2;
			scoreValue = scoreValue * 3/2;
		}
		if (this.hasAttribute("huge")) {
			sx *= 3;
			sy *= 3;
			health *= 3;
			maxSpeed = maxSpeed > 3 ? 3 : maxSpeed;
			scoreValue = scoreValue * 5/2;
		}
		if (this.hasAttribute("spawned")) {
			scoreValue = 0;
		}
		initializeWeapon();
	}
	public Enemy morphAttributes(String ... attribute) {
		for (String att : attributes) {
			attributes.add(att);
		}
		return this;
	}
	public Enemy morphAttribute(String attribute) {
		attributes.add(attribute);
		return this;
	}
	public boolean hasAttribute(String attribute) {
		return attributes.contains(attribute);
	}

	public void dt() {
		if (Settings.valueBoolean("spinmode")) angle++;
		if (health > 0) {
			if (vx > maxSpeed) vx = maxSpeed;
			if (vy > maxSpeed) vy = maxSpeed;

			super.dt();
			Being collided = container.getBeingCollided(this, false);
			if (collided != null) hit(collided);
		} else {
			destructionTrigger();
			container.removeentity(this);
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
