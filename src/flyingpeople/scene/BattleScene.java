package flyingpeople.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sgui.SGuiScene;
import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;
import flyingpeople.data.Settings;
import flyingpeople.data.level.Level;
import flyingpeople.data.level.LevelHandler;
import flyingpeople.data.level.RandomLevelGenerator;
import flyingpeople.data.level.RandomLevelGenerator.LevelStyle;
import flyingpeople.display.Art;
import flyingpeople.display.FadeMessage;
import flyingpeople.display.MessageDisplay;
import flyingpeople.display.PermanentMessage;
import flyingpeople.display.TextMessage;
import flyingpeople.flyer.Player;
import flyingpeople.flyer.enemy.Boss;
import flyingpeople.flyer.ship.DebugShip;
import flyingpeople.flyer.ship.ElavianShip;
import flyingpeople.flyer.ship.KohsairShip;
import flyingpeople.flyer.ship.MulisShip;
import flyingpeople.scene.ShipSelectScene.Ship;

@SuppressWarnings("serial")
public class BattleScene extends SGuiScene implements KeyListener, LevelHandler {
	protected static final Random rand = new Random(System.currentTimeMillis());

	public static final int HUD_WIDTH = 800; // Recommended width, used to verify settings
	public static final int HUD_HEIGHT = 100; // Mandatory minimum height of heads up display box, used to verify settings

	protected boolean paused = false;
	protected boolean lost = false, drama = false;

	protected Player player = null;
	protected Zone zone;

	// HUD Text
	protected TextMessage msgHull, msgScore, msgLevel, msgFps, msgBoss;
	protected static final int HP_UNIT = 100;
	public static final int WELCOME_DURATION = Settings.valueInt("fps") * 3;

	protected static HashMap<ShipSelectScene.Ship, String[]> TIPS = new HashMap<ShipSelectScene.Ship, String[]>(); // tips for ships
	static {
		TIPS.put(ShipSelectScene.Ship.ELAVIAN, new String[]{"Your explosions can damage yourself.", "You take less collision damage when charging."});
		TIPS.put(ShipSelectScene.Ship.KOHSAIR, new String[]{"Move while firing to deal special attacks.", "Doppler shot deals more damage than bullet rain."});
		TIPS.put(ShipSelectScene.Ship.MULIS, new String[]{"Absorbing fast projectiles is unreliable.", "Absorbing a projectile temporarily doubles damage."});
	}

	private BattleScene() {
		super(Settings.valueInt("window_width"), Settings.valueInt("window_height"));
		capFps(Settings.valueInt("fps"));

		this.setBackground(new Color(Settings.valueInt("background_red"), Settings.valueInt("background_blue"), Settings.valueInt("background_green")));
		zone = new Zone(this, 0,0, Settings.valueInt("window_width"),getViewHeight());
	}
	public void begin() {
		MessageDisplay.initializeDisplay(Art.getFont(Settings.value("font")));
		initGame();
		this.startRunning();
	}
	protected void initGame() { // call after being added to a JFrame
		Ship shipType = ShipSelectScene.Ship.values()[owner.get("ship")];

		int x = Settings.valueInt("window_width") / 2;
		int y = Flyer.getMaxY() - 50;
		if (Settings.valueBoolean("debug")) {
			player = new DebugShip(zone, x, y);
		} else {
			switch (shipType) {
			case ELAVIAN:
				player = new ElavianShip(zone, x, y);
				break;
			case KOHSAIR:
				player = new KohsairShip(zone, x, y);
				break;
			case MULIS:
				player = new MulisShip(zone, x, y);
				break;
			}
		}
		player.setPy(Flyer.getMaxY() - player.getSy());
		getFrame().setTitle("Flying People - Piloting the " + player);

		zone.addFlyer(player);
		getFrame().addKeyListener(player);
		Level startingLevel = Level.getLevel(Settings.value("startingLevel"));
		startingLevel.reset();
		zone.setLevel(startingLevel);
		displayLevelWelcome();

		initHud();
	}
	public static BattleScene makeInstance() {
		return new BattleScene();
	}

	protected void drawGui(Graphics g) {
		zone.draw(g);
		drawHud(g);
		MessageDisplay.getInstance().draw(g);

		if (drama) { // make the screen grayscale, all dramatic-y
			BufferedImage bi = new BufferedImage(Settings.valueInt("window_width"), Settings.valueInt("window_height"), BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2 = bi.createGraphics();
			g2.drawImage(ibuff, 0, 0, this);
			g.drawImage(bi, 0, 0, this);
		}
	}
	protected void initHud() {
		int y = Settings.valueBoolean("hud_on_top") ? 0 : getViewHeight();

		MessageDisplay.getInstance().addMessage(new PermanentMessage("HULL", 40, y + 20));
		msgHull = new PermanentMessage("", 120, y + 20);
		MessageDisplay.getInstance().addMessage(msgHull);

		MessageDisplay.getInstance().addMessage(new PermanentMessage("SCORE", 400, y + 20));
		msgScore = new PermanentMessage("", 500, y + 20);
		MessageDisplay.getInstance().addMessage(msgScore);

		msgLevel = new PermanentMessage("", 40, y + 40);
		msgLevel.setText(zone.getLevel().toString());
		MessageDisplay.getInstance().addMessage(msgLevel);

		msgFps = new PermanentMessage("", 500, y + 40);
		if (Settings.valueBoolean("show_fps")) {
			MessageDisplay.getInstance().addMessage(new PermanentMessage("FPS", 400, y + 40));
			MessageDisplay.getInstance().addMessage(msgFps);
		}

		msgBoss = new PermanentMessage("", 40, y + 60);
		MessageDisplay.getInstance().addMessage(msgBoss);
	}
	protected void drawHud(Graphics g) {
		int y = Settings.valueBoolean("hud_on_top") ? 0 : getViewHeight();
		g.drawImage(Art.getImage("hud_border"), 0, y, Settings.valueInt("window_width"), HUD_HEIGHT, null);
		msgHull.setText(player.getHealth() + "");
		msgScore.setText(player.getScore() + "");
		Boss boss = zone.getABoss();
		if (boss != null) {
			msgBoss.setText(boss + " Hull: " + estimate(boss.getHealth()));
		} else {
			msgBoss.setText("");
		}
		msgFps.setText(drawer.calcFps() + " / " + drawer.getFpsCap());
	}

	public void dt() { // check lose condition and tick
		if (lost) return;
		if (paused) return;

		MessageDisplay.getInstance().tick();
		if (player.getHealth() <= 0 && !Settings.valueBoolean("undying")) {
			loseTheGame();
		} else if (Settings.valueBoolean("hardmode") && !Settings.valueBoolean("undying") && player.getHealth() != player.getBaseHealth()) {
			int diff = player.getBaseHealth() - player.getHealth();
			player.setHealth(-diff);
			loseTheGame();
		}
		zone.dt();
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		// Pause!
		case KeyEvent.VK_P:
			togglePause();
			break;
		case KeyEvent.VK_R:
			if (lost) this.owner.switchScene(ShipSelectScene.makeInstance());
			break;
		case KeyEvent.VK_Q:
			System.out.println("Q_Q");
			drama();
			break;
			// FPS change [debug]
		case KeyEvent.VK_1:
			System.out.println("Speed up! " + (long)(drawer.getFpsCap() * 1.1));
			capFps((long)(drawer.getFpsCap() * 1.2));
			break;
		case KeyEvent.VK_2:
			System.out.println("Slow down! " + (long)(drawer.getFpsCap() * .9));
			capFps((long)(drawer.getFpsCap() * .8));
			break;
		case KeyEvent.VK_0:
			System.out.println("Normal Speed! " + Settings.valueInt("fps"));
			capFps(Settings.valueInt("fps"));
			break;
			// Utility
		case KeyEvent.VK_F1:
			boolean unpauseAfter = false;
			if (!paused) {
				togglePause();
				unpauseAfter = true;
			}
			JOptionPane.showMessageDialog(this,
					"Arrow keys move\nZ=special, X=shoot\nP=pause, R=retry (if dead)\nF5=randomlevel, F12=screenshot",
					"Help", JOptionPane.INFORMATION_MESSAGE);
			if (paused && unpauseAfter) {
				togglePause();
			}
			break;
		case KeyEvent.VK_F5:
			System.out.println("Random Level!");
			zone.setLevel(RandomLevelGenerator.generateLevel(LevelStyle.NORMAL));
			break;
		case KeyEvent.VK_F6:
			System.out.println("Random Level Special: Saboteurs!");
			zone.setLevel(RandomLevelGenerator.generateLevel(LevelStyle.SABOTEUR));
			break;
		case KeyEvent.VK_F12:
			saveScreenshot(Settings.valueInt("window_width"), Settings.valueInt("window_height"), "fp_screenshot_" + System.currentTimeMillis() + ".png");
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}
	protected void togglePause() {
		paused = !paused;
		System.out.println((paused?"P":"Unp") + "aused!");

		String title = getFrame().getTitle();
		if (paused) {
			getFrame().setTitle(title + " # Paused");
		} else {
			getFrame().setTitle(title.substring(0, title.lastIndexOf(" # Paused")));
		}
	}
	public void keyReleased(KeyEvent ke) { }
	public void keyTyped(KeyEvent ke) { }

	public void endLevel() {
		final int MAX_HEALTH_SCORE = 10000;
		int healthScore = player.getHealth() * MAX_HEALTH_SCORE / player.getBaseHealth();
		healthScore = Math.max(healthScore, MAX_HEALTH_SCORE);
		player.addScore(healthScore);
		player.setHealth(player.getBaseHealth());

		Level nextLevel = Level.getNextLevel(zone.getLevel());
		nextLevel.reset();
		zone.setLevel(nextLevel);
		displayLevelWelcome();
		msgLevel.setText(zone.getLevel().toString());
	}
	protected void displayLevelWelcome() { // display level name and a random tip
		if (!Settings.valueBoolean("debug") && Settings.valueBoolean("show_tips")) {
			Ship shipType = ShipSelectScene.Ship.values()[owner.get("ship")];
			String tip = TIPS.get(shipType)[rand.nextInt(TIPS.get(shipType).length)];
			tip = "Tip: " + tip;
			MessageDisplay.getInstance().addMessage(new FadeMessage(tip, 20, Settings.valueInt("window_height")*2/3, WELCOME_DURATION));
		}
	}

	protected void loseTheGame() {
		MessageDisplay.getInstance().addMessage(new PermanentMessage("[R]ETRY?",
				Settings.valueInt("window_width") *2/5, Settings.valueInt("window_height") / 2 + 2*MessageDisplay.getInstance().getFontHeight()));
		lost = true;
		drama = true;
	}
	public void drama() { // also found in Zone and Projectile
		drama = true;
	}

	// - = 100 HP    + = 200 HP    # = 300 HP
	protected static String estimate(int health) {
		// exact value in debug mode
		if (Settings.valueBoolean("debug")) {
			int maxLen = (Integer.MAX_VALUE + "").length();
			int hpLen = (health + "").length();
			String str = new String(new char[maxLen]).replace('\0', ' ');
			str = str.substring(0, maxLen - hpLen) + health;
			return str;
		}
		// symbolic estimate of health
		StringBuffer temp = new StringBuffer("---------------");
		int fullHp = HP_UNIT * temp.length();
		if (health > fullHp) {
			for (int i = 0; i < temp.length() && health - fullHp >= (i+1)*HP_UNIT; i++) {
				temp.setCharAt(i, '+');
			}

			fullHp *= 2;
			if (health > fullHp) {
				for (int i = 0; i < temp.length() && health - fullHp >= (i+1)*HP_UNIT; i++) {
					temp.setCharAt(i, '#');
				}
			}
		} else {
			temp.setLength(Math.min(temp.length(), health / HP_UNIT));
		}
		return "[" + temp + "               ".substring(temp.length()) + "]";
	}

	protected int getViewHeight() { // height of the viewport, not including hud
		return Settings.valueInt("window_height") - HUD_HEIGHT; // used for hud drawing
	}
	private JFrame getFrame() { // used for adding listeners
		return (JFrame)(this.getTopLevelAncestor());
	}
}
