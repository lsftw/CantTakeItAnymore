package ctia.scene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ctia.display.Art;
import sgui.Scene;

@SuppressWarnings("serial")
public final class ShipSelectScene extends Scene implements KeyListener {
	public enum Ship {ELAVIAN, KOHSAIR, MULIS}
	public static final String[] ICONS = new String[]{"icon_elavian","icon_kohsair","icon_mulis"};
	public static final String[] DESCRIPTIONS = new String[]{"Elavian - Built using durable, military-grade materials, the Elavian destroys opposing forces with powerful rockets.",
		"Kohsair - Built from scratch using interchangeable parts, the Kohsair scouts out and eliminates individual ships using rapid-fire bullets.",
	"Mulis - Built with innovative, cutting-edge technology, the Mulis can annihilate waves with its lasers."};
	public static final int[][] STATS = new int[][]{{5,2,5,1}, {4,5,1,5}, {3,3,3,4}};

	private Ship selectedShip = Ship.values()[0];
	private JLabel lblShipText = new JLabel("Ship Name",SwingConstants.CENTER);
	private JLabel[] lblShips = new JLabel[3];
	private StatBar statHp, statSpd, statAtk, statRate;

	private ShipSelectScene() {
		initializeGUI();
	}
	private void initializeGUI() {
		this.setLayout(new BorderLayout());
		// Center: Ships Panel
		JPanel shipsPanel = new JPanel();
		shipsPanel.setLayout(new BorderLayout());
		shipsPanel.add(new JLabel("SHIP SELECT",SwingConstants.CENTER), BorderLayout.NORTH);
		//// Ship Choices Panel
		JPanel shipChoicesPanel = new JPanel();
		shipChoicesPanel.setLayout(new FlowLayout());
		for (int i = 0; i < lblShips.length; i++) {
			lblShips[i] = new JLabel(new ImageIcon(Art.getImage(ICONS[i])));
			lblShips[i].setOpaque(true); // to allow highlighting
			shipChoicesPanel.add(lblShips[i]);
		}
		shipsPanel.add(shipChoicesPanel, BorderLayout.CENTER);
		this.add(shipsPanel, BorderLayout.CENTER);
		// South: Ship Information Panel
		JPanel shipInfoPanel = new JPanel();
		shipInfoPanel.setLayout(new BorderLayout());
		shipInfoPanel.add(lblShipText, BorderLayout.NORTH);
		JPanel shipStatsPanel = new JPanel();
		shipStatsPanel.setLayout(new GridLayout(0,4));
		statHp = new StatBar("HULL"); shipStatsPanel.add(statHp);
		statSpd = new StatBar("MOTOR"); shipStatsPanel.add(statSpd);
		statAtk = new StatBar("WEAPON"); shipStatsPanel.add(statAtk);
		statRate = new StatBar("FIRERATE"); shipStatsPanel.add(statRate);
		shipInfoPanel.add(shipStatsPanel, BorderLayout.CENTER);
		this.add(shipInfoPanel, BorderLayout.SOUTH);

		selectShip(0);
	}
	public static ShipSelectScene makeInstance() {
		return new ShipSelectScene();
	}
	public void begin() {
		setWindowTitle("Cant Take It Anymore - Ship Selection");
	}
	private void setWindowTitle(String title) {
		((JFrame)(this.getTopLevelAncestor())).setTitle(title);
	}

	private void selectShip(int index) {
		unhighlight(lblShips[selectedShip.ordinal()]);

		if (index < 0) {
			index = Ship.values().length - 1;
		} else if (index >= Ship.values().length) {
			index = 0;
		}
		selectedShip = Ship.values()[index];
		lblShipText.setText(DESCRIPTIONS[index]);
		highlight(lblShips[index]);
		showStats(index);
	}
	private void showStats(int shipIndex) {
		statHp.setPoints(STATS[shipIndex][0]);
		statSpd.setPoints(STATS[shipIndex][1]);
		statAtk.setPoints(STATS[shipIndex][2]);
		statRate.setPoints(STATS[shipIndex][3]);
	}
	private static void highlight(JLabel label) {
		label.setOpaque(true);
		label.setBackground(Color.CYAN);
	}
	private static void unhighlight(JLabel label) {
		label.setOpaque(false);
		label.setBackground(Color.WHITE);
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			selectShip(selectedShip.ordinal() - 1);
			break;
		case KeyEvent.VK_RIGHT:
			selectShip(selectedShip.ordinal() + 1);
			break;
		case KeyEvent.VK_ENTER:
			owner.set("ship", selectedShip.ordinal());
			this.owner.switchScene(BattleScene.makeInstance());
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}
	public void keyReleased(KeyEvent ke) { }
	public void keyTyped(KeyEvent ke) { }
	//
	// Class StatBar
	//
	private static class StatBar extends JPanel {
		private static final int MAX_POINTS = 5;

		private static ImageIcon tickEmptyIcon;
		private static ImageIcon tickFullIcon;

		protected String name;
		protected JLabel[] statPoint = new JLabel[MAX_POINTS];

		public StatBar(String name) {
			this.name = name;
			initializeGUI();
		}
		private static void initializeTickIcons() {
			if (tickEmptyIcon == null) tickEmptyIcon = new ImageIcon(Art.getImage("tick_outline"));
			if (tickFullIcon == null) tickFullIcon = new ImageIcon(Art.getImage("tick_full"));
		}
		public void setPoints(int count) {
			initializeTickIcons();
			for (int i = 0; i < count && i < MAX_POINTS; i++) {
				statPoint[i].setIcon(tickFullIcon);
			}
			for (int i = count; i < statPoint.length; i++) {
				statPoint[i].setIcon(tickEmptyIcon);
			}
		}
		private void initializeGUI() {
			this.setLayout(new FlowLayout());
			this.add(new JLabel(name));
			for (int i = 0; i < statPoint.length; i++) {
				statPoint[i] = new JLabel();
				this.add(statPoint[i]);
			}
			setPoints(0);
		}
	}
}
