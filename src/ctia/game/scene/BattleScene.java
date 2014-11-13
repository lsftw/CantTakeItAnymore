package ctia.game.scene;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ctia.engine.core.Entity;
import ctia.engine.core.Level;
import ctia.engine.data.Settings;
import ctia.engine.entity.Player;
import ctia.engine.sgui.SGuiScene;
import ctia.game.TestBoss;
import ctia.game.entity.Hero;

@SuppressWarnings("serial")
public class BattleScene extends SGuiScene implements KeyListener, MouseListener {
	protected static final Random rand = new Random(System.currentTimeMillis());
	private Level level = new Level();
	private Player player;
	private boolean playerFiring = false;

	private BattleScene() {
		super(Settings.getWindowWidth(), Settings.getWindowHeight());
		capFps(Settings.getFps());
	}
	public void begin() {
		initGame();
		this.startRunning();
		resizeGameWithWindow();
	}
	public static BattleScene makeInstance() {
		return new BattleScene();
	}
	private void resizeGameWithWindow() {
		getFrame().addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		    	updateSize(getWidth(), getHeight());
		    }

			@Override
			public void componentHidden(ComponentEvent arg0) { }
			@Override
			public void componentMoved(ComponentEvent arg0) { }
			@Override
			public void componentShown(ComponentEvent arg0) { }
		});
	}

	protected void initGame() { // call after being added to a JFrame
		player = new Hero(level, 0, 0);
		getFrame().addKeyListener(player);
		level.addEntity(player);
		level.follow(player);
		Entity boss = new TestBoss(level, 400, 400);
		level.addEntity(boss);
		/*Entity enemy = new SpawnPoint(level, 400, 400);
		level.addEntity(enemy);*/
	}

	protected void drawGui(Graphics g) {
		level.draw(g);
	}

	public void dt() { // check lose condition and tick
		if (playerFiring) {
//			updateSize(screenWidth + 1, screenHeight + 1);
			Point p = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(p, this);
			p.x = (int) (p.x + level.getXscroll());
			p.y = (int) (p.y + level.getYscroll());
			player.tryToFire(p);
		}
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

	public void mouseClicked(MouseEvent me) { }
	public void mouseEntered(MouseEvent me) { }
	public void mouseExited(MouseEvent me) { }
	public void mousePressed(MouseEvent me) {
		playerFiring = true;
	}
	public void mouseReleased(MouseEvent me) {
		playerFiring = false;
	}

	private JFrame getFrame() { // used for adding listeners to player
		return (JFrame)(this.getTopLevelAncestor());
	}
}
