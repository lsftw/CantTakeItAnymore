package ctia.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import ctia.data.Settings;
import ctia.data.Utility;
import ctia.display.AbilityMessage;
import ctia.display.Art;
import ctia.display.MessageDisplay;
import ctia.scene.BattleScene;

// represents a game entity with position and image
public abstract class Entity {
	private static final String MISSING_IMAGE = ".noimage";
	protected static final Random rand = new Random(System.currentTimeMillis());
	// Spatial
	protected Zone container;
	protected int sx, sy;
	protected double px, py;
	protected double vx = 0, vy = 0;
	// Graphical
	protected double angle = 0;
	protected Sprite sprite;
	protected boolean flipHorizontally = false;
	protected boolean tiledHorizontally = false, tiledVertically = false;

	public Entity(Zone container, double xpos, double ypos) {
		this.container = container;
		px = xpos;
		py = ypos;
		initialize();
	}

	// Subclasses that modify sprite initialization should override this
	protected void initialize() {
		initializeSprite();
		autoResize();
	}
	protected Sprite getSpriteToLoad() {
		return Art.getSprite(this);
	}
	private void initializeSprite() { // loads sprite based on class name
		sprite = getSpriteToLoad();
		if (sprite == null) {
			Utility.printWarning("Sprite not found for " + this + ".");
			sprite = Art.getSprite(MISSING_IMAGE);
		}
	}
	protected void autoResize() { // resize to match image size
		if (sprite == null) return;
		Image image = sprite.getFrame();
		sx = image.getWidth(null);
		sy = image.getHeight(null);
	}

	protected Image getFrameToDraw() { // override to draw different frames
		return sprite.getFrame();
	}
	public void draw(Graphics g) {
		draw((Graphics2D)g);
	}
	protected void draw(Graphics2D g2) {
		Image toDraw = getFrameToDraw();
		double tx = px + sx/2;
		double ty = py + sy/2;
		g2.translate(tx, ty);
		g2.rotate(angle);
		//if (angle!=0)System.out.println(angle);
		// if tiling, goes through and draws all images until it reaches the necessary dimensions
		if (tiledHorizontally || tiledVertically) { // TODO work when rotated/flipped
			// Calculations for tiling
			int width = toDraw.getWidth(null);
			width = width > sx ? sx : width;
			int height = toDraw.getHeight(null);
			height = height > sy ? sy : height;
			int cropWidth, cropHeight; // for cropping
			int drawWidth, drawHeight; // for cropping
			// Draw tiling
			for (int i = 0; i < sx/width + (sx%width==0 ? 0 : 1); i++) {
				for (int j = 0; j < sy/height + (sy%height==0 ? 0 : 1); j++) {
					cropWidth = width; drawWidth = width;
					cropHeight = height; drawHeight = height;
					if (i == sx/width && sx%width != 0) {
						drawWidth = sx%width; // horizontal leftover
						if (tiledHorizontally) {
							cropWidth = drawWidth;
						}
					}
					if (j == sy/height && sy%height != 0) {
						drawHeight = sy%height; // vertical leftover
						if (tiledVertically) {
							cropHeight = drawHeight;
						}
					}
					g2.drawImage(toDraw, width*(i) - sx/2, height*(j) - sy/2,
							width*(i)+drawWidth - sx/2, height*(j)+drawHeight - sy/2,
							0, 0, cropWidth, cropHeight, null);
				}
			}
		} else { // horizontal flipping for non-tiled
			g2.drawImage(toDraw, flipHorizontally?sx/2:-sx/2,
					-sy/2, (flipHorizontally?-sx:sx), sy, null);
		}
		g2.rotate(-angle);
		g2.translate(-tx, -ty);
	}

	protected void preDt() { }
	public void dt() {
		preDt();
		px += vx;
		py += vy;
		postDt();
	}
	protected void postDt() { }

	public boolean collidesWith(Entity other) { // rectangle collision check
		if (((other.px<=this.px+this.sx&&other.px+other.sx>=this.px+this.sx)||
				(other.px<=this.px&&other.px+other.sx>=this.px)||
				(other.px>=this.px&&other.px+other.sx<=this.px+this.sx))&&
				((other.py<=this.py&&other.py+other.sy>=this.py)||
						(other.py>=this.py&&other.py+other.sy<=this.py+this.sy))) {
			return true;
		}
		return false;
	}
	public void hitBy(Entity attacker, int damage) { }

	public Zone getZone() { return container; }
	public int getSx() { return sx; }
	public int getSy() { return sy; }
	public double getPx() { return px; }
	public double getPy() { return py; }
	public double getVx() { return vx; }
	public double getVy() { return vy; }

	public void setZone(Zone zone) { container = zone; }
	public void setSx(int sx) { this.sx = sx; }
	public void setSy(int sy) { this.sy = sy; }
	public void setPx(double px) { this.px = px; }
	public void setPy(double py) { this.py = py; }
	public void setVx(double vx) { this.vx = vx; }
	public void setVy(double vy) { this.vy = vy; }

	public boolean isActive() {
		return container.hasentity(this);
	}

	public static int getMinX() {
		return 0;
	}
	public static int getMinY() {
		return Settings.valueBoolean("hud_on_top") ? BattleScene.HUD_HEIGHT : 0;
	}
	public static int getMaxX() {
		return Settings.valueInt("window_width");
	}
	public static int getMaxY() {
		return Settings.valueInt("window_height") - (Settings.valueBoolean("hud_on_top") ? 0 : BattleScene.HUD_HEIGHT);
	}

	public double angleBetween(double otherX, double otherY) {
		double thisX = px + sx/2;
		double thisY = py + sy/2;
		double angle = Math.atan((otherY-thisY)/(otherX-thisX));
		if (otherX < thisX) { // to the left
			angle += Math.PI;
		}
		return angle;
	}

	public void say(int messageNum, String message, int duration) {
		sayBelow(messageNum, message, duration);
	}
	public void sayAbove(int messageNum, String message, int duration) {
		MessageDisplay.getInstance().addMessage(
				new AbilityMessage(this,-messageNum,message,duration,false));
	}
	public void sayBelow(int messageNum, String message, int duration) {
		MessageDisplay.getInstance().addMessage(
				new AbilityMessage(this,messageNum,message,duration));
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
