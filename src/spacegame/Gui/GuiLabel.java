package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class GuiLabel extends Gui {
	private String labelText;
	private boolean hasToolTip = false;
	private GuiToolTip toolTip;
	private static Font f = new Font("Courier", Font.PLAIN, 12);
	private static FontMetrics fm = parentPanel.getFontMetrics(f);

	public GuiLabel(int posX, int posY, int width, int height, String value) {
		super(posX, posY, width, height);
		labelText = value;
	}

	@Override
	public void pack() {
		g.setColor(Color.black);
		g.drawRect(posX, posY, width, height);
		switch (alignment) {
		case LEFT:
			g.drawString(labelText, posX + 5, posY + height - fm.getHeight()
					/ 3);
			break;
		case CENTER:
			g.drawString(labelText,
					posX + width / 2 - fm.stringWidth(labelText) / 2, posY
							+ height - fm.getHeight() / 3);
			break;
		case RIGHT:break;
		}
	}

	public boolean hasToolTip() {
		return hasToolTip;
	}

	public GuiToolTip getToolTip() {
		return toolTip;
	}

	public GuiLabel addToolTip(String toolTipText) {
		hasToolTip = true;
		toolTip = new GuiToolTip(toolTipText, this);
		return this;
	}

	public GuiLabel align(Gui.align a) {
		alignment = a;
		return this;
	}

	public void updateText(String value) {
		this.labelText = value;
	}
}
