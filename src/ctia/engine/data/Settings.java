package ctia.engine.data;

public class Settings {
	private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static final int FPS = 50;

	public static final int getWindowWidth() {
		return WINDOW_WIDTH;
	}
	public static final int getWindowHeight() {
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
	public static final int getFps() {
		return FPS;
	}
}
