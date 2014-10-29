package ctia.entity;

import ctia.core.Entity;
import ctia.data.Settings;

public abstract class Projectile extends Entity {
	protected Entity owner;
	// used in collision checking
	public enum TargettingType {ALL, OTHER, ENEMY}
	protected TargettingType targetting = TargettingType.ALL;

	protected static final short ATT_PIERCE = 1 << 0; // hits multiple targets
	protected static final short ATT_LASER = 1 << 1; // hits first target
	protected short attributes = 0; // bit set
	protected int damage;

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
		checkCollision();
		postDt();
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
		}

		return collided;
	}
	protected void hit(Entity collided) {
		if (collided instanceof Being) {
			collided.hitBy(this.owner, damage);
			if (collided instanceof Player && ((Player)collided).health <= 0 && !Settings.valueBoolean("undying")) {
				return; // let player realize the cause of their loss
			}
			if (this instanceof ExplosiveProjectile) {
				((ExplosiveProjectile)this).explode();
			}

			if (!hasAttribute(ATT_PIERCE)) {
				container.removeentity(this);
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
