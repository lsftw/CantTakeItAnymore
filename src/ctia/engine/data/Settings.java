package ctia.engine.data;

import java.awt.Image;

import ctia.engine.data.art.Art;

public class Settings {
	private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static final int FPS = 50;

	private static final String IMAGE_LIST = "data/imagelist.txt";
	private static final String SPRITE_LIST = "data/spritelist.txt";

	private static final int HEALTHBAR_HEIGHT = 30;

	// gameplay
	private static final boolean ENEMY_FRIENDLY_FIRE_ENABLED = false;
	private static final int BOSS_SPAWN_TIME_SECONDS = 120;
	private static final double SPAWN_CHANCE_PER_FRAME = 5.0 / FPS; // out of 100

	public static int getWindowWidth() {
		return WINDOW_WIDTH;
	}
	public static int getWindowHeight() {
		return WINDOW_HEIGHT;
	}
	public static int getMinX() {
		return 0;
	}
	public static int getMaxX() {
		return WINDOW_WIDTH;
	}
	public static int getMinY() {
		return 0;
	}
	public static int getMaxY() {
		return WINDOW_HEIGHT;
	}
	public static int getFps() {
		return FPS;
	}

	public static String getImageListFile() {
		return IMAGE_LIST;
	}
	public static String getSpriteListFile() {
		return SPRITE_LIST;
	}

	public static int getHealthBarHeight() {
		return HEALTHBAR_HEIGHT;
	}

	public static boolean isEnemyFriendlyFireEnabled() {
		return ENEMY_FRIENDLY_FIRE_ENABLED;
	}
	public static Image getBackground() {
		return Art.getImage("background");
	}
	public static int getTimeToBossSpawn() {
		return BOSS_SPAWN_TIME_SECONDS;
	}
	public static double getSpawnChancePerFrame() {
		return SPAWN_CHANCE_PER_FRAME;
	}
}
