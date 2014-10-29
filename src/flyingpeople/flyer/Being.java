package flyingpeople.flyer;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;

// A flyer that has health, used for the player and enemies
public abstract class Being extends Flyer {
	public static final int BASE_COLLISION_DAMAGE = 25;
	protected int health = 0;
	protected int collisionDamage = BASE_COLLISION_DAMAGE;

	public Being(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}

	public void hitBy(Flyer attacker, int damage) {
		health -= damage;
	}

	public int getHealth() { return health; }
	public int getCollisionDamage() { return collisionDamage; }

	public void setHealth(int newHealth) { health = newHealth; }
	public void setCollisionDamage(int newDamage) { collisionDamage = newDamage; }
}
