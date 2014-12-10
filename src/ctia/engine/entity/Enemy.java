package ctia.engine.entity;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.data.Utility;
import ctia.game.entity.powerup.DamagePowerup;
import ctia.game.entity.powerup.Powerup;

public abstract class Enemy extends Being {
	protected Entity lastAttacker;
	protected long scoreValue = 0;

	protected double maxSpeed = 1000000;
	protected int maxHealth;

	protected double itemDropChance = 25;
	protected int maxItemsDropped = 1;

	public Enemy(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
		resetStats();
		maxHealth = health;
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

	protected void hit(Being collided) { // collide with another entity, damaging both entities
		this.hitBy(collided, this.collisionDamage);
		collided.hitBy(this, collided.getCollisionDamage());
	}
	public void hitBy(Entity attacker, int damage) { // for giving points on destruction
		if (Settings.isEnemyFriendlyFireEnabled() || !(attacker instanceof Enemy)) {
			lastAttacker = attacker;
			super.hitBy(attacker, damage);
		}
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
		dropItems();
	}
	public void addScore(long points) { scoreValue += points; }
	protected void dropItems() {
		for (int i = 0; i < maxItemsDropped; i++) {
			if (percentChance(itemDropChance)) {
				dropPowerup();
			}
		}
	}
	private void dropPowerup() {
		Powerup powerup = getRandomItem();
		container.addEntity(powerup);
	}
	protected Powerup getRandomItem() {
		int powerupType = rand.nextInt(1);
		Powerup powerup = null;
		switch (powerupType) {
		case 0:
			powerup = new DamagePowerup(container, px + sx / 2, py + sy / 2);
			break;
		default:
			Utility.warn("No powerup found for type #: " + powerupType);
			break;
		}
		return powerup;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}
