package ctia.game.scene;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.entity.Enemy;
import ctia.engine.entity.Player;
import ctia.engine.sgui.SGuiScene;
import ctia.game.entity.Hero;
import ctia.game.entity.TestEnemy;

@SuppressWarnings("serial")
public class BattleScene extends SGuiScene implements KeyListener {
	protected static final Random rand = new Random(System.currentTimeMillis());
	private Level level = new Level(Settings.getMaxX(), Settings.getMaxY());
	private Player player;

	private BattleScene() {
		super(Settings.getWindowWidth(), Settings.getWindowHeight());
		capFps(Settings.getFps());
	}
	public void begin() {
		initGame();
		this.startRunning();
	}
	public static BattleScene makeInstance() {
		return new BattleScene();
	}

	protected void initGame() { // call after being added to a JFrame
		player = new Hero(level, 0, 0);
		getFrame().addKeyListener(player);
		level.addEntity(player);
		Entity enemy = new TestEnemy(level, 200, 100);
		level.addEntity(enemy);
	}

	protected void drawGui(Graphics g) {
		level.draw(g);
	}

	public void dt() { // check lose condition and tick
		level.dt();
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

	private JFrame getFrame() { // used for adding listeners to player
		return (JFrame)(this.getTopLevelAncestor());
	}
}
