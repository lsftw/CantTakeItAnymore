package ctia.engine.entity;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.data.Utility;
import ctia.game.entity.powerup.DamagePowerup;
import ctia.game.entity.powerup.HealthPowerup;
import ctia.game.entity.powerup.Powerup;

public abstract class Enemy extends Being {
	protected Entity lastAttacker;
	protected long scoreValue = 0;

	protected double maxSpeed = 1000000;
	protected int maxHealth;

	protected double itemDropChance = 25;
	protected int maxItemsDropped = 1;
	protected int maxHealthDropped = 1;

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

	protected void chasePlayer(double speed) {
		Player player = container.getAPlayer();
		double ppx = player.getPx();
		double ppy = player.getPy();
		double dx = ppx - px;
		double dy = ppy - py;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

		vx = dx / distance * speed;
		vy = dy / distance * speed;
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
		for (int i = 0; i < maxHealthDropped; i++) {
			if (percentChance(itemDropChance)) {
				dropHeal();
			}
		}
	}
	// powerups spread out on drop to avoid clustering at the same point: more drops = more spread
	private void dropHeal() {
		Powerup powerup = new HealthPowerup(container, px + sx / 2, py + sy / 2);
		powerup.addSpread(maxHealthDropped / 2);
		container.addEntity(powerup);
	}
	private void dropPowerup() {
		Powerup powerup = getRandomItem();
		container.addEntity(powerup);
	}
	protected Powerup getRandomItem() {
		int powerupType = rand.nextInt(1);
		Powerup powerup = null;
		double ppx = px + sx / 2;
		double ppy = py + sy / 2;
		switch (powerupType) {
		case 0:
			powerup = new DamagePowerup(container, ppx, ppy);
			break;
		default:
			Utility.warn("No powerup found for type #: " + powerupType);
			break;
		}
		if (powerup != null) {
			powerup.addSpread(maxItemsDropped / 2);
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
