package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import spacegame.InputManager;
import spacegame.SpaceCanvas;
import spacegame.SpaceGame;
import spacegame.Entity.EntityLiving;
import spacegame.Util.IIntegerDynamicObject;

public abstract class Gui implements IIntegerDynamicObject {
	protected int posX;
	protected int posY;
	protected int width;
	protected int height;
	protected double scale, deltaX, deltaY;
	protected static InputManager input;
	protected static SpaceCanvas parentPanel;
	protected Graphics2D g;

	protected boolean isVisible = true;
	protected boolean firstRun = true;
	protected boolean needsUpdate = false; // If screen size has changed, but a
											// gui can not yet be fully
											// initialized, this is true

	public static Color transLightGray = new Color(196, 196, 196, 100);
	public static Color hTransLightYellow = new Color(255, 221, 85, 127);
	public static Color lightYellow = new Color(255, 221, 85);
	public static Color lightGray = new Color(196, 196, 196);
	public static Color dRed = new Color(175, 85, 0);
	public static Color lBlue = new Color(85, 85, 255);
	public static Color dBlue = new Color(75, 75, 224);
	public static Color hTransWhite = new Color(255, 255, 255, 127); // Half
																		// transparent
																		// white
	public static Color transparent = new Color(255, 255, 255, 0);

	public static enum align {
		LEFT, RIGHT, CENTER
	};

	protected align alignment;

	public Gui(int posX, int posY, int width, int height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		input = SpaceGame.getInstance().getInput();
		parentPanel = SpaceGame.getInstance().getCanvas();
		g = SpaceGame.getInstance().getGraphics();
		alignment = align.CENTER;
		deltaX = 0;
		deltaY = 0;
	}

	public abstract void pack();

	protected void aalias() {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public boolean isMouseOver() {
		if (input.getMouseLocation() == null)
			return false;
		/**
		 * could potentially do this directly in InputManager, but I don't yet
		 * know if this will be a problem in fullscreen/cross-platform
		 **/
		/**
		 * mouse position seems slightly offset on the screen, so we translate
		 * it's point before comparing it
		 **/
		// input.mouseLocation.translate(3, 22);
		boolean flag = this.getBoundingBox().contains(input.getMouseLocation());
		/** Necessary to translate the point back **/
		// input.mouseLocation.translate(-3, -22);
		return flag && isVisible;
	}

	public boolean isMouseOver(Rectangle r) {
		if (input.getMouseLocation() == null)
			return false;

		return r.getBounds().contains(input.getMouseLocation()) && isVisible;
	}

	/**
	 * create rectangle object based on position and dimensions... used for easy
	 * Graphics.draw(Rectangle) calls
	 **/
	@Override
	public Rectangle getBoundingBox() {
		return (new Rectangle(posX, posY, width, height));
	}

	/** Position/Dimension gets **/
	@Override
	public int getX() {
		return posX;
	}

	@Override
	public void setX(int x) {
		posX = x;
	}

	@Override
	public int getY() {
		return posY;
	}

	@Override
	public void setY(int y) {
		posY = y;
	}

	@Override
	public void setPosition(int x, int y) {
		posX = x;
		posY = y;
	}

	// Width
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int w) {
		width = w;
	}

	// Height
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int h) {
		height = h;
	}

	@Override
	public void setDimensions(int w, int h) {
		width = w;
		height = h;
	}

	@Override
	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public double getDeltaX() {
		return deltaX;
	}

	@Override
	public double getDeltaY() {
		return deltaY;
	}

	@Override
	public void setDeltaX(double dx) {
		deltaX = dx;
	}

	@Override
	public void setDeltaY(double dy) {
		deltaY = dy;
	}

	@Override
	public void setSpeed(double dx, double dy) {
		deltaX = dx;
		deltaY = dy;
	}

	@Override
	public boolean isMovingX() {
		return deltaX != 0;
	}

	@Override
	public boolean isMovingY() {
		return deltaY != 0;
	}

	@Override
	public boolean isMoving() {
		return deltaX != 0 || deltaY != 0;
	}

	@Override
	public void update() {
		g = SpaceGame.getInstance().getGraphics();
		input = SpaceGame.getInstance().getInput();
		updatePositionWithSpeed();
	}

	@Override
	public void updatePositionWithSpeed() {
		posX = (int) Math.round(posX + deltaX);
		posY = (int) Math.round(posY + deltaY);
	}

	public void setVisible(boolean flag) {
		isVisible = flag;
	}

	public boolean isVisible() {
		return isVisible;
	}

	/** STATIC **/

	public static void renderHealthBar(Graphics2D g2, EntityLiving e, int x,
			int y, int width, int height) {
		g2.setColor(Color.gray);
		g2.drawRect(x - (e.getWidth() / 2), y - 15 - (e.getHeight() / 2),
				width, height);
		g2.setPaint(new GradientPaint(x - (e.getWidth() / 2), y - 15
				- (e.getHeight() / 2), Color.RED, x - (e.getWidth() / 2), y + 5
				- (e.getHeight() / 2), Color.black, true));

		double hpPerc = e.getStats().getCurrentHealth()
				/ e.getStats().getMaxHealth();
		int hp = (int) (hpPerc * width);
		if (hp < 0)
			hp = 0;
		g2.fillRect(x - (e.getWidth() / 2), y - 15 - (e.getHeight() / 2), hp,
				height);

		int cHp = (int) Math.round(e.getStats().getCurrentHealth());
		if (cHp < 0)
			cHp = 0;

		String health = cHp + " / " + Math.round(e.getStats().getMaxHealth());
		Font font = new Font("Courier New", Font.PLAIN, 11);
		FontMetrics fm = parentPanel.getFontMetrics(font);
		g2.setFont(font);
		g2.setColor(Color.white);
		g2.drawString(health, x - (fm.stringWidth(health) / 2),
				y - (e.getHeight() / 2) - fm.getHeight() / 2 + 1);
	}

	/** Series of functions to simply return a string from a numerical value **/
	static String str(double d) {
		return new StringBuilder().append(d).toString();
	}

}