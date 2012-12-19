package spacegame.Gui;

import java.awt.*;
import spacegame.SpaceGame;

/**
 * 
 * @author Nocare This Object (GuiButton) Just creates a gui object with the
 *         given dimensions, position, string, and font. Could potentially add
 *         various button styles, but perhaps it would be better to extend this
 *         class and create whole new button objects?
 */
public class GuiButton extends Gui {
	private String name;
	public int ID;
	private Color backgroundColor;
	private Color foregroundColor;
	/**
	 * We'll add the ability to add different font to a button, however we will
	 * otherwise use the font already in use
	 **/
	private boolean skipFont = false;
	private String font;
	private int fontSize;
	private GuiScreen parentGui;

	public GuiButton(int ID, String name, int posX, int posY, int width,
			int height, String font, int fontSize, GuiScreen parentGui) {
		super(posX, posY, width, height);
		this.name = name;
		this.ID = ID;
		this.parentGui = parentGui;
		if (font.equals("default"))
			skipFont = true;
		else {
			this.font = font;
			this.fontSize = fontSize;
		}
		backgroundColor = Color.yellow;
		foregroundColor = Color.black;
	}

	/** draw to screen **/
	public void pack() {
		g = SpaceGame.getInstance().getGraphics();

		g.setColor(backgroundColor);
		Color overYellow = new Color(255, 253, 60);
		if (isMouseOver()) {
			parentGui.activeGuiObject = this.ID;
			g.setPaint(new GradientPaint(posX, posY - 10, Color.black, posX,
					posY + height + 9, Color.yellow, true));
		} else {
			g.setPaint(new GradientPaint(posX, posY - 20, Color.black, posX,
					posY + height + 9, overYellow, true));
		}

		g.fillRect(posX, posY, width, height);

		FontMetrics fm;

		if (skipFont) {
			fm = parentPanel.getFontMetrics(SpaceGame.getInstance()
					.getDefaultFont());
		} else {
			Font f = new Font(font, Font.PLAIN, fontSize);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(f);
			fm = parentPanel.getFontMetrics(f);
		}

		g.setColor(foregroundColor);
		int x = (width / 2) - fm.stringWidth(name) / 2;
		int y = (height / 2) + fm.getHeight() / 4;
		g.drawString(name, posX + x, posY + y);
		// g.setColor(Color.WHITE);
	}
}