package ctia.engine.core;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import ctia.engine.entity.Being;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Player;
import ctia.engine.entity.Projectile;

public class Level {
	// Boundaries
	protected int boundMinX, boundMinY, boundMaxX, boundMaxY;
	// Lists of Entities
	protected List<Entity> entities = new ArrayList<Entity>();
	protected List<Entity> entitiesToAdd = new ArrayList<Entity>();
	protected List<Entity> entitiesToRemove = new ArrayList<Entity>();

	protected boolean drawing = false;

	public Level(int width, int height) {
		boundMinX = 0;
		boundMinY = 0;
		boundMaxX = width;
		boundMaxY = height;
	}

	public void addentity(Entity entity) {
		entitiesToAdd.add(entity);
	}
	public void removeEntity(Entity entity) {
		entitiesToRemove.add(entity);
	}

	public boolean hasEntity(Entity entity) {
		return entities.contains(entity);
	}
	public Player getAPlayer() { // used for chasing
		Entity entity;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			if (entity instanceof Player) {
				return (Player)entity;
			}
		}
		return null;
	}

	public Entity getCollided(Entity collider) {
		Entity entity;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
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
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			if (entity != collider && entity != toIgnore && entity.collidesWith(collider)) {
				return entity;
			}
		}
		return null;
	}
	public Being getBeingCollided(Entity collider, boolean getBottomMost) {
		Entity entity;
		Being bottomMost = null; double maxY = -1;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
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
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
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
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			if (entity != collider && entity instanceof Projectile) {
				if (entity.collidesWith(collider)) {
					return (Projectile)entity;
				}
			}
		}
		return null;
	}

	public boolean inBounds(double xpos, double ypos) {
		return xpos >= boundMinX && xpos <= boundMaxX && ypos >= boundMinY && ypos <= boundMaxY;
	}
	public double getMinX() {
		return boundMinX;
	}
	public double getMaxX() {
		return boundMaxX;
	}
	public double getMinY() {
		return boundMinY;
	}
	public double getMaxY() {
		return boundMaxY;
	}

	public void draw(Graphics g) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw(g);
		}
	}
	public void dt() {
		updateEntities();

		Entity entity;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			entity.dt();
			if (!inBounds(entity.getPx(), entity.getPy())) {
				removeEntity(entity);
			}
		}

		updateEntities();
	}

	private void updateEntities() {
		entities.addAll(entitiesToAdd);
		entitiesToAdd.clear();
		entities.removeAll(entitiesToRemove);
		entitiesToRemove.clear();
	}
}
