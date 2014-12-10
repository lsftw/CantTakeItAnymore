package ctia.game.scene;

import java.awt.Color;
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
import ctia.game.entity.Hero;
import ctia.game.entity.enemy.Boss;
import ctia.game.entity.enemy.SpawnPoint;

@SuppressWarnings("serial")
public class BattleScene extends SGuiScene implements KeyListener, MouseListener {
	protected static final Random rand = new Random(System.currentTimeMillis());
	private Level level = new Level();
	private Player player;
	private boolean playerFiring = false;

	private double timeUntilBossSeconds = Settings.getTimeToBossSpawn();
	private boolean bossSpawned = false;

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
		// player
		player = new Hero(level, 0, 0);
		getFrame().addKeyListener(player);
		level.addEntity(player);
		level.follow(player);
		// enemy
		level.dt(); // level actually adds player entity
		spawnSpawner();
	}

	protected void drawGui(Graphics g) {
		level.draw(g);
		drawHud(g);
	}

	protected void drawHud(Graphics g) {
		drawBossCountdown(g);
		drawPlayerHealth(g);
		drawBossHealth(g);
	}
	private void drawBossCountdown(Graphics g) {
		Boss boss = level.getABoss();
		if (boss == null) {
			// TODO genericize to drawBar() alongside healthbar?
			int countDownBarHeight = Settings.getHealthBarHeight();
			int maxHealthWidth = Settings.getWindowWidth();
			double health = timeUntilBossSeconds;
			int maxHealth = Settings.getTimeToBossSpawn();
			double healthPercentage = health / maxHealth;
			int healthWidth = (int) (maxHealthWidth * healthPercentage);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, maxHealthWidth, countDownBarHeight);
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, healthWidth, countDownBarHeight);
			g.setColor(Color.WHITE);
			g.drawString("Boss spawns in " + Math.round(health) + " seconds ", Settings.getWindowWidth() / 2, 0 + countDownBarHeight / 2);
		}
	}
	private void drawPlayerHealth(Graphics g) {
		Player player = level.getAPlayer();
		int healthBarHeight = Settings.getHealthBarHeight();
		if (player != null) {
			int health = player.getHealth();
			int maxHealth = player.getBaseHealth();
			drawHealth(g, "Player", health, maxHealth, 0, Settings.getWindowHeight() - healthBarHeight);
		}
	}
	private void drawBossHealth(Graphics g) {
		Boss boss = level.getABoss();
		if (boss != null) {
			int health = boss.getHealth();
			int maxHealth = boss.getMaxHealth();
			drawHealth(g, "Boss", health, maxHealth, 0, 0);
		}
	}
	private void drawHealth(Graphics g, String name, int health, int maxHealth, int x, int y) {
		double healthPercentage = health * 1.0 / maxHealth;
		int maxHealthWidth = Settings.getWindowWidth();
		int healthWidth = (int) (maxHealthWidth * healthPercentage);
		int healthBarHeight = Settings.getHealthBarHeight();
		g.setColor(Color.RED);
		g.fillRect(x, y, maxHealthWidth, healthBarHeight);
		g.setColor(Color.GREEN);
		g.fillRect(x, y, healthWidth, healthBarHeight);
		g.setColor(Color.WHITE);
		g.drawString(name + ": " + health + " / " + maxHealth, Settings.getWindowWidth() / 2, y + healthBarHeight / 2);
	}

	public void dt() { // check lose condition and tick
		handlePlayerFire();
		countDown();
		level.dt();
	}
	private void handlePlayerFire() {
		if (playerFiring) {
//			updateSize(screenWidth + 1, screenHeight + 1);
			Point p = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(p, this);
			p.x = (int) (p.x + level.getXscroll());
			p.y = (int) (p.y + level.getYscroll());
			player.tryToFire(p);
		}
	}
	private void countDown() {
		if (timeUntilBossSeconds > 0) {
			double secondsPerFrame = 1.0 / Settings.getFps();
			timeUntilBossSeconds -= secondsPerFrame;
			trySpawnEnemies();
		} else {
			timeUntilBossSeconds = 0;
			if (!bossSpawned) {
				Point location = getRandomPointNearPlayer();
				int px = location.x;
				int py = location.y;
				Entity boss = new Boss(level, px, py);
				level.addEntity(boss);
				bossSpawned = true;
			}
		}
	}
	private void trySpawnEnemies() {
		double spawnChance = Settings.getSpawnChancePerFrame();
		if (rand.nextDouble() * 100 < spawnChance) {
			spawnSpawner();
		}
	}
	private void spawnSpawner() {
		Point location = getRandomPointNearPlayer();
		if (location != null) {
			int px = location.x;
			int py = location.y;
			Entity enemy = new SpawnPoint(level, px, py);
			level.addEntity(enemy);
		}
	}
	private Point getRandomPointNearPlayer() {
		Player player = level.getAPlayer();
		if (player != null) {
			double playerCenterX = player.getPx() + player.getSx() / 2;
			double playerCenterY = player.getPy() + player.getSy() / 2;
			double randAngle = rand.nextDouble() * (2 * Math.PI);
			double distanceFromPlayer = Math.max(Settings.getWindowWidth(), Settings.getWindowHeight()); // pick offscreen point
			int px = (int) (playerCenterX + Math.cos(randAngle) * distanceFromPlayer);
			int py = (int) (playerCenterY + Math.sin(randAngle) * distanceFromPlayer);
			return new Point(px, py);
		}
		return null;
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
