package ctia.scene;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ctia.data.Utility;
import ctia.display.Art;
import sgui.Scene;

@SuppressWarnings("serial")
public final class MainMenuScene extends Scene implements KeyListener, MouseListener {
	private JLabel button_start, button_quit; // sshhh they act like buttons
	private JLabel selectedButton = null; // for key selection

	private MainMenuScene() {
		initializeGUI();
	}
	private void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(new JLabel(new ImageIcon(Art.getImage("titlescreen_graphic"))));
		button_start = new JLabel(new ImageIcon(Art.getImage("titlescreen_startbutton")));
		button_start.setBackground(Color.WHITE);
		button_start.setOpaque(true);
		button_start.addMouseListener(this);
		this.add(button_start);
		button_quit = new JLabel(new ImageIcon(Art.getImage("titlescreen_quitbutton")));
		button_quit.setBackground(Color.WHITE);
		button_quit.setOpaque(true);
		button_quit.addMouseListener(this);
		this.add(button_quit);
	}
	public static MainMenuScene makeInstance() {
		return new MainMenuScene();
	}
	public void begin() {
		setWindowTitle("Cant Take It Anymore - Main Menu");
	}
	private void setWindowTitle(String title) {
		((JFrame)(this.getTopLevelAncestor())).setTitle(title);
	}

	public void mouseClicked(MouseEvent me) {
		Object source = me.getSource();
		if (source == button_start) {
			startGame();
		} else if (source == button_quit) {
			System.exit(0);
		}
	}
	public void mouseEntered(MouseEvent me) {
		Object source = me.getSource();
		if (source instanceof JLabel) {
			selectButton((JLabel)source);
		}
	}
	public void mouseExited(MouseEvent me) {
		Object source = me.getSource();
		if (source instanceof JLabel) {
			selectButton(null);
		}
	}
	public void mousePressed(MouseEvent me) { }
	public void mouseReleased(MouseEvent me) { }

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (selectedButton == null) {
				selectButton(button_quit);
				break; // button already selected, no need to fall through
			}
		case KeyEvent.VK_DOWN:
			if (selectedButton == null) {
				selectButton(button_start);
				break;
			}
			selectButton(selectedButton == button_start ? button_quit : button_start);
			break;
		case KeyEvent.VK_ENTER:
			if (selectedButton != null) {
				if (selectedButton == button_start) {
					startGame();
				} else if (selectedButton == button_quit) {
					System.exit(0);
				}
			}
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}
	public void keyReleased(KeyEvent ke) { }
	public void keyTyped(KeyEvent ke) { }

	private void startGame() {
		if (this.owner != null) {
			this.owner.switchScene(ShipSelectScene.makeInstance());
		} else {
			Utility.printError("Can't start game, this MainMenuScene is not linked to a SceneHandler!");
		}
	}
	private void selectButton(JLabel label) {
		if (selectedButton != null) unhighlight(selectedButton);
		selectedButton = label;
		if (label != null) highlight(label);
	}
	private static void highlight(JLabel label) {
		label.setBackground(Color.CYAN);
	}
	private static void unhighlight(JLabel label) {
		label.setBackground(Color.WHITE);
	}
}
