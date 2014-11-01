package ctia.game.scene;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import sgui.SGuiScene;
import ctia.engine.data.Settings;

@SuppressWarnings("serial")
public class BattleScene extends SGuiScene implements KeyListener {
	protected static final Random rand = new Random(System.currentTimeMillis());

	private BattleScene() {
		super(Settings.getWindowWidth(), Settings.getWindowHeight());
		capFps(Settings.getFps());
	}
	public void begin() {
		initGame();
		this.startRunning();
	}
	protected void initGame() { // call after being added to a JFrame
	}
	public static BattleScene makeInstance() {
		return new BattleScene();
	}

	protected void drawGui(Graphics g) {
	}

	public void dt() { // check lose condition and tick
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}
	public void keyReleased(KeyEvent ke) { }
	public void keyTyped(KeyEvent ke) { }
}
