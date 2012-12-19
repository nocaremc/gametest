package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import spacegame.SpaceGame;

/** Simple class that displays text at mouse location on a gui element **/
public class GuiToolTip extends Gui {
	String toolTipText;
	static Font f = new Font("Courier", Font.ITALIC, 10);
	static FontMetrics fm = parentPanel.getFontMetrics(f);
	Gui parentGui;

	public GuiToolTip(String toolTipText, Gui parentGui) {
		super(0, 0, (int) (fm.stringWidth(toolTipText) * 1.25), fm.getHeight());
		this.toolTipText = toolTipText;
		this.parentGui = parentGui;
	}

	@Override
	public void pack() {
		update();
		setPosition((int) input.getMouseLocation().getX() - width / 2,
				(int) (input.getMouseLocation().getY()) - height);
		if (posX < 0)
			posX = 0;
		if (posX > SpaceGame.getInstance().getWidth())
			posX = SpaceGame.getInstance().getWidth();

		if (parentGui.isMouseOver()) {
			g.setColor(lightYellow);
			g.fill(getBoundingBox());
			g.setColor(Color.black);
			g.draw(getBoundingBox());
			g.setColor(Color.black);
			g.drawString(toolTipText, posX + 5, posY + height - fm.getHeight()
					/ 3);
		}
	}

}
