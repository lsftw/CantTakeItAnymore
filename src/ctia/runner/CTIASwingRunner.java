package ctia.runner;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.JFrame;

import sgui.Profiler;
import sgui.Scene;
import sgui.SceneHandler;
import ctia.data.Art;
import ctia.scene.BattleScene;

// runs the can't take it anymore game as a java swing application
@SuppressWarnings("serial")
public class CTIASwingRunner extends JFrame implements SceneHandler {
	protected HashMap<String, Integer> sceneInfo = new HashMap<String, Integer>();
	protected Scene activeScene;

	protected Profiler profiler = new Profiler();

	public CTIASwingRunner() {
		//profiler.start();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Can't Take it Anymore");
		Art.load(this);
		this.switchScene(BattleScene.makeInstance());
	}

	public static void main(String[] args) {
		CTIASwingRunner frame = new CTIASwingRunner();
		frame.setVisible(true);
	}

	public void switchScene(Scene scene) {
		// Remove old scene
		if (activeScene != null) {
			this.remove(activeScene);
			activeScene.unlink();
			if (activeScene instanceof KeyListener) {
				this.removeKeyListener((KeyListener)activeScene);
			}
			if (activeScene instanceof MouseListener) {
				this.removeMouseListener((MouseListener)activeScene);
			}
			if (activeScene instanceof MouseMotionListener) {
				this.removeMouseMotionListener((MouseMotionListener)activeScene);
			}
		}
		// Add new scene
		activeScene = scene;
		activeScene.link(this);
		this.setBackground(activeScene.getBackground());
		this.add(scene);
		this.pack();
		// Add relevant listeners
		if (scene instanceof KeyListener) {
			this.addKeyListener((KeyListener)scene);
		}
		if (scene instanceof MouseListener) {
			this.addMouseListener((MouseListener)scene);
		}
		if (scene instanceof MouseMotionListener) {
			this.addMouseMotionListener((MouseMotionListener)scene);
		}
		scene.begin();
	}
	// Scene Handler
	public Integer get(String key) {
		return sceneInfo.get(key);
	}
	public void set(String key, Integer value) {
		sceneInfo.put(key, value);
	}
}
