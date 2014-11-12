package ctia.engine.core;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import ctia.engine.data.Settings;
import ctia.engine.entity.Being;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Player;
import ctia.engine.entity.Projectile;

public class Level {
	// Lists of Entities
	protected List<Entity> entities = new ArrayList<Entity>();
	protected List<Entity> entitiesToAdd = new ArrayList<Entity>();
	protected List<Entity> entitiesToRemove = new ArrayList<Entity>();
	// Scrolling
	protected Entity followed;
	protected int xScroll = 0, yScroll = 0;

	protected boolean drawing = false;

	public Level() {
	}

	public void addEntity(Entity entity) {
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
				if (entity instanceof Player != toIgnore instanceof Player) { // only hit other team
					return entity;
				}
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
	public Player getPlayerCollided(Entity collider) {
		Entity entity;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			if (entity != collider && entity instanceof Player && entity.collidesWith(collider)) {
				return (Player)entity;
			}
		}
		return null;
	}

	public void draw(Graphics g) {
		drawBackground(g);
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw(g);
		}
	}
	private void drawBackground(Graphics g) {
		Image image = Settings.getBackground();
		g.drawImage(image, -xScroll, -yScroll, null);
	}

	public void dt() {
		updateEntities();

		Entity entity;
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			entity.dt();
			// TODO when to remove entity?
		}

		updateEntities();
		updateScrolling();
	}
	// Scrolling
	public void follow(Entity entity) {
		followed = entity;
	}
	private void updateScrolling() {
		if (followed != null) {
			xScroll = (int) (followed.getPx() + followed.getSx() / 2 - Settings.getWindowWidth()/2);
			yScroll = (int) (followed.getPy() + followed.getSx() / 2 - Settings.getWindowHeight()/2);
		}
	}
	public int getXscroll() { return xScroll; }
	public int getYscroll() { return yScroll; }

	private void updateEntities() {
		entities.addAll(entitiesToAdd);
		entitiesToAdd.clear();
		entities.removeAll(entitiesToRemove);
		entitiesToRemove.clear();
	}
}
