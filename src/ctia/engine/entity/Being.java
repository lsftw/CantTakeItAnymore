package ctia.engine.entity;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;

// A entity that has health, used for the player and enemies
public abstract class Being extends Entity {
	public static final int BASE_COLLISION_DAMAGE = 25;
	protected int health = 0;
	protected int collisionDamage = BASE_COLLISION_DAMAGE;

	public Being(Level container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	public void hitBy(Entity attacker, int damage) {
		health -= damage;
	}

	public int getHealth() { return health; }
	public int getCollisionDamage() { return collisionDamage; }

	public void setHealth(int newHealth) { health = newHealth; }
	public void setCollisionDamage(int newDamage) { collisionDamage = newDamage; }
}
