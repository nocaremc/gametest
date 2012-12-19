package spacegame.Gui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Nocare This Object (GuiIconButton) Just creates a gui object with the
 *         given dimensions, position, string, and font. Could potentially add
 *         various button styles, but perhaps it would be better to extend this
 *         class and create whole new button objects?
 */
public class GuiIconButton extends Gui {
	public int ID;
	private Gui parentGui;
	private BufferedImage icon;
	private Image icon2;

	public GuiIconButton(int ID, int posX, int posY, int width, int height,
			BufferedImage icon, GuiScreen parentGui) {
		super(posX, posY, width, height);
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.ID = ID;
		this.parentGui = parentGui;
		this.icon = icon;
	}

	public GuiIconButton(int ID, int posX, int posY, int width, int height,
			BufferedImage icon, GuiWindow parentGui) {
		super(posX, posY, width, height);
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.ID = ID;
		this.parentGui = parentGui;
		this.icon = icon;
	}

	public GuiIconButton(int ID, int posX, int posY, int width, int height,
			Image icon, GuiWindow parentGui) {
		super(posX, posY, width, height);
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.ID = ID;
		this.parentGui = parentGui;
		this.icon2 = icon;
	}

	/** draw to screen **/
	public void pack() {
		update();
		if (isMouseOver()) {
			if (parentGui instanceof GuiScreen)
				((GuiScreen) parentGui).activeGuiObject = this.ID;
			else if (parentGui instanceof GuiWindow)
				((GuiWindow) parentGui).activeGuiObject = this.ID;
			g.setPaint(new GradientPaint(posX, posY, hTransWhite, posX, posY
					+ height + 9, transparent, true));
		} else {
			g.setPaint(new GradientPaint(posX, posY + height, hTransWhite,
					posX, posY + height + 9, transparent, true));
		}

		// If passing in a scaled image, we cannot use the BufferedImage
		// variable
		if (icon == null)
			g.drawImage(icon2, posX, posY, null);
		else
			g.drawImage(icon, posX, posY, null);

		g.fillRect(posX, posY, width, height);
	}
}