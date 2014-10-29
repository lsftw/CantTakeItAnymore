package ctia.core;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import ctia.data.level.Level;
import ctia.data.level.LevelHandler;
import ctia.entity.Being;
import ctia.entity.Player;
import ctia.entity.Projectile;
import ctia.entity.enemy.Enemy;

public class Zone {
	public static final int DESTROY_MIN_X=-200, DESTROY_MIN_Y=-200, DESTROY_MAX_X=Entity.getMaxX(), DESTROY_MAX_Y=Entity.getMaxY();
	// Boundaries
	protected int boundMinX, boundMinY, boundMaxX, boundMaxY;
	// Lists of Fliers
	protected List<Entity> fliers = new ArrayList<Entity>();
	protected List<Entity> fliersToAdd = new ArrayList<Entity>();
	protected List<Entity> fliersToRemove = new ArrayList<Entity>();

	protected LevelHandler levelHandler;
	protected Level currentLevel;
	protected boolean drawing = false;

	public Zone(LevelHandler levelHandler, int xmin, int ymin, int xmax, int ymax) {
		this.levelHandler = levelHandler;
		boundMinX = xmin; boundMinY = ymin;
		boundMaxX = xmax; boundMaxY = ymax;
	}

	public void addentity(Entity entity) {
		fliersToAdd.add(entity);
	}
	public void removeentity(Entity entity) {
		fliersToRemove.add(entity);
	}

	public boolean hasentity(Entity entity) {
		return fliers.contains(entity);
	}
	public Enemy getAnEnemy() { // TODO remove?
		Entity entity;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity instanceof Enemy) {
				return (Enemy)entity;
			}
		}
		return null;
	}
	public Player getAPlayer() { // used for chasing
		Entity entity;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity instanceof Player) {
				return (Player)entity;
			}
		}
		return null;
	}

	public Entity getCollided(Entity collider) {
		Entity entity;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity != collider) {
				if (entity.collidesWith(collider)) {
					return entity;
				}
			}
		}
		return null;
	}
	public Entity getOtherCollided(Entity collider, Entity toIgnore) {
		Entity entity;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity != collider && entity != toIgnore && entity.collidesWith(collider)) {
				return entity;
			}
		}
		return null;
	}
	public Being getBeingCollided(Entity collider, boolean getBottomMost) {
		Entity entity;
		Being bottomMost = null; double maxY = -1;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity != collider && entity instanceof Being && entity.collidesWith(collider)) {
				if (getBottomMost) {
					if (entity.py > maxY) {
						maxY = entity.py;
						bottomMost = (Being)entity;
					}
				} else {
					return (Being)entity;
				}
			}
		}
		return bottomMost;
	}
	public Enemy getEnemyCollided(Entity collider, boolean getBottomMost) {
		Entity entity;
		Enemy bottomMost = null; double maxY = -1;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity != collider && entity instanceof Enemy && entity.collidesWith(collider)) {
				if (getBottomMost) {
					if (entity.py > maxY) {
						maxY = entity.py;
						bottomMost = (Enemy)entity;
					}
				} else {
					return (Enemy)entity;
				}
			}
		}
		return bottomMost;
	}
	public Projectile getProjectileCollided(Entity collider) {
		Entity entity;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			if (entity != collider && entity instanceof Projectile) {
				if (entity.collidesWith(collider)) {
					return (Projectile)entity;
				}
			}
		}
		return null;
	}
	public boolean inBounds(int xpos, int ypos) {
		return xpos >= boundMinX && xpos <= boundMaxX && ypos >= boundMinY && ypos <= boundMaxY;
	}

	public void draw(Graphics g) {
		for (int i = 0; i < fliers.size(); i++) {
			fliers.get(i).draw(g);
		}
	}
	public void dt() {
		updateFliers();

		Entity entity;
		for (int i = 0; i < fliers.size(); i++) {
			entity = fliers.get(i);
			entity.dt();
			if (entity.px < DESTROY_MIN_X || entity.px > DESTROY_MAX_X || entity.py < DESTROY_MIN_Y || entity.py > DESTROY_MAX_Y) {
				removeentity(entity);
			}
		}

		if (currentLevel != null) currentLevel.tick();
		updateFliers();
	}
	public void endLevel() {
		levelHandler.endLevel();
	}

	private void updateFliers() {
		fliers.addAll(fliersToAdd);
		fliersToAdd.clear();
		fliers.removeAll(fliersToRemove);
		fliersToRemove.clear();
	}
	public void setLevel(Level level) {
		if (currentLevel != null) level.unlink();
		this.currentLevel = level;
		level.link(this);
	}
	public Level getLevel() {
		return currentLevel;
	}
}
