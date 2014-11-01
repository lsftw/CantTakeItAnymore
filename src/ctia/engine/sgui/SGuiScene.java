package ctia.engine.sgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// Copy of SGUI that extends Scene instead of JComponent
//  In subclasses, override drawGUI(Graphics g); and dt();
@SuppressWarnings("serial")
public abstract class SGuiScene extends Scene implements BasicSGui {
	// Graphics
	public final int screenWidth, screenHeight;
	protected DrawingThread drawer = new DrawingThread(this);

	protected BufferedImage ibuff = null;
	protected Graphics gbuff = null;

	public SGuiScene(int screenWidth, int screenHeight) { this(new Dimension(screenWidth, screenHeight)); }
	public SGuiScene(Dimension screenDimensions) {
		setBackground(Color.WHITE);
		screenWidth = screenDimensions.width;
		this.screenHeight = screenDimensions.height;
		setSize(screenDimensions);
		setPreferredSize(screenDimensions); // for pack()
	}

	public void startRunning() {
		drawer.start();
	}
	public void capFps(long fpsCap) {
		drawer.setFpsCap(fpsCap); // to avoid freezes and jumps from fps change
	}

	public void redraw() {
		this.paintImmediately(0, 0, screenWidth, screenHeight);
	}
	public void paintComponent(Graphics g) {
		if (ibuff == null) {
			ibuff = (BufferedImage)(this.createImage(screenWidth, screenHeight));
			gbuff = ibuff.getGraphics();
		}

		gbuff.clearRect(0, 0, this.getWidth(), this.getHeight()); // clear buffer
		drawGui(gbuff);
		g.drawImage(ibuff, 0, 0, this); // copy buffer to current image
	}

	public BufferedImage saveScreenshot(int width, int height, String filePath) {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		this.paintComponent(g2);
		try {
			ImageIO.write(bi, "PNG", new File(filePath));
			System.out.println("Screenshot saved to " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi;
	}

	protected abstract void drawGui(Graphics g);
	public abstract void dt();
}
