package sgui;

public interface SceneHandler {
	public void set(String key, Integer value);
	public Integer get(String key);
	public void switchScene(Scene scene); // don't forget to call scene.begin()
}
