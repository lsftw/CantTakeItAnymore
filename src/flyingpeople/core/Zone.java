package flyingpeople.core;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import flyingpeople.data.level.Level;
import flyingpeople.data.level.LevelHandler;
import flyingpeople.flyer.Being;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.Projectile;
import flyingpeople.flyer.enemy.Boss;
import flyingpeople.flyer.enemy.Enemy;

public class Zone {
	public static final int DESTROY_MIN_X=-200, DESTROY_MIN_Y=-200, DESTROY_MAX_X=Flyer.getMaxX(), DESTROY_MAX_Y=Flyer.getMaxY();
	// Boundaries
	protected int boundMinX, boundMinY, boundMaxX, boundMaxY;
	// Lists of Fliers
	protected List<Flyer> fliers = new ArrayList<Flyer>();
	protected List<Flyer> fliersToAdd = new ArrayList<Flyer>();
	protected List<Flyer> fliersToRemove = new ArrayList<Flyer>();

	protected LevelHandler levelHandler;
	protected Level currentLevel;
	protected boolean drawing = false;

	public Zone(LevelHandler levelHandler, int xmin, int ymin, int xmax, int ymax) {
		this.levelHandler = levelHandler;
		boundMinX = xmin; boundMinY = ymin;
		boundMaxX = xmax; boundMaxY = ymax;
	}

	public void addFlyer(Flyer flyer) {
		fliersToAdd.add(flyer);
	}
	public void removeFlyer(Flyer flyer) {
		fliersToRemove.add(flyer);
	}

	public boolean hasFlyer(Flyer flyer) {
		return fliers.contains(flyer);
	}
	public Enemy getAnEnemy() { // TODO remove?
		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer instanceof Enemy) {
				return (Enemy)flyer;
			}
		}
		return null;
	}
	public Boss getABoss() { // used for display boss stats (one boss at a time)
		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer instanceof Boss) {
				return (Boss)flyer;
			}
		}
		return null;
	}
	public Player getAPlayer() { // used for chasing
		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer instanceof Player) {
				return (Player)flyer;
			}
		}
		return null;
	}

	public Flyer getCollided(Flyer collider) {
		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer != collider) {
				if (flyer.collidesWith(collider)) {
					return flyer;
				}
			}
		}
		return null;
	}
	public Flyer getOtherCollided(Flyer collider, Flyer toIgnore) {
		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer != collider && flyer != toIgnore && flyer.collidesWith(collider)) {
				return flyer;
			}
		}
		return null;
	}
	public Being getBeingCollided(Flyer collider, boolean getBottomMost) {
		Flyer flyer;
		Being bottomMost = null; double maxY = -1;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer != collider && flyer instanceof Being && flyer.collidesWith(collider)) {
				if (getBottomMost) {
					if (flyer.py > maxY) {
						maxY = flyer.py;
						bottomMost = (Being)flyer;
					}
				} else {
					return (Being)flyer;
				}
			}
		}
		return bottomMost;
	}
	public Enemy getEnemyCollided(Flyer collider, boolean getBottomMost) {
		Flyer flyer;
		Enemy bottomMost = null; double maxY = -1;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer != collider && flyer instanceof Enemy && flyer.collidesWith(collider)) {
				if (getBottomMost) {
					if (flyer.py > maxY) {
						maxY = flyer.py;
						bottomMost = (Enemy)flyer;
					}
				} else {
					return (Enemy)flyer;
				}
			}
		}
		return bottomMost;
	}
	public Projectile getProjectileCollided(Flyer collider) {
		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			if (flyer != collider && flyer instanceof Projectile) {
				if (flyer.collidesWith(collider)) {
					return (Projectile)flyer;
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

		Flyer flyer;
		for (int i = 0; i < fliers.size(); i++) {
			flyer = fliers.get(i);
			flyer.dt();
			if (flyer.px < DESTROY_MIN_X || flyer.px > DESTROY_MAX_X || flyer.py < DESTROY_MIN_Y || flyer.py > DESTROY_MAX_Y) {
				removeFlyer(flyer);
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
