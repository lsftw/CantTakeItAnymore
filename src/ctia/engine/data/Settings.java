package ctia.engine.data;

public class Settings {
	private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static final int FPS = 50;

	private static final String IMAGE_LIST = "data/imagelist.txt";
	private static final String SPRITE_LIST = "data/spritelist.txt";

	private static final boolean ENEMY_FRIENDLY_FIRE_ENABLED = false;

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

	public static boolean isEnemyFriendlyFireEnabled() {
		return ENEMY_FRIENDLY_FIRE_ENABLED;
	}
}
