package ctia.engine.entity;

import ctia.engine.core.Entity;
import ctia.engine.data.Settings;

// TODO remove or change ATT_LASER
public abstract class Projectile extends Entity {
	protected Entity owner;
	// used in collision checking
	public enum TargettingType {ALL, OTHER, ENEMY, PLAYER}
	protected TargettingType targetting = TargettingType.ALL;

	protected static final short ATT_PIERCE = 1 << 0; // hits multiple targets
	protected static final short ATT_LASER = 1 << 1; // hits first target
	protected short attributes = 0; // bit set
	protected int damage;
	protected int lifespan = Settings.getFps() * 5;

	public Projectile(Entity owner, double xpos, double ypos) {
		super(owner.getZone(), xpos, ypos);
		this.owner = owner;
	}

	public void addAttribute(short attributeFlag) {
		attributes |= attributeFlag;
	}
	public void removeAttribute(short attributeFlag) {
		if (hasAttribute(attributeFlag)) attributes ^= attributeFlag;
	}
	public boolean hasAttribute(short attributeFlag) {
		return (attributes & attributeFlag) != 0;
	}

	public void dt() {
		preDt();
		px += vx;
		py += vy;
		angle = Math.atan2(vy, vx);
		checkCollision();
		postDt();
	}

	public void postDt() {
		lifespan--;
		if (lifespan <= 0) {
			container.removeEntity(this);
		}
	}

	protected Entity checkCollision() {
		Entity collided = getCollided();
		if (collided != null) hit(collided);
		return collided;
	}
	protected Entity getCollided() {
		Entity collided = null;
		switch(targetting) {
		case ALL:
			collided = container.getBeingCollided(this, hasAttribute(ATT_LASER));
			break;
		case OTHER:
			collided = container.getOtherCollided(this, owner);
			break;
		case ENEMY:
			collided = container.getEnemyCollided(this, hasAttribute(ATT_LASER));
			break;
		case PLAYER:
			collided = container.getPlayerCollided(this);
			break;
		}

		return collided;
	}
	protected void hit(Entity collided) {
		if (collided instanceof Being) {
			collided.hitBy(this.owner, damage);
			if (collided instanceof Player && ((Player)collided).health <= 0) {
				return; // let player realize the cause of their loss
			}

			if (!hasAttribute(ATT_PIERCE)) {
				container.removeEntity(this);
			}
		}
	}


	public int getDamage() { return damage; }
	public void setDamage(int damage) { this.damage = damage; }
	public void boostDmg(int boost) {
		damage += boost;
	}
	public void multDmg(double boost) {
		damage = (int)(damage * boost);
	}
}
