package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Rectangle;

import spacegame.SpaceGame;

/** Tab object which must be child to a TabPanel **/
public class GuiTab extends Gui {
	@SuppressWarnings("unused")
	private GuiTabPanel tabPanel;
	private String tabName;
	private Rectangle tabTitle;
	@SuppressWarnings("unused")
	private int ID;
	private boolean isActiveTab = false;

	public GuiTab(GuiTabPanel tabPanel, int id, String name) {
		super(tabPanel.getX(), tabPanel.getY(), tabPanel.getWidth(), tabPanel
				.getHeight());
		this.tabPanel = tabPanel;
		tabName = name;
		ID = id;

		/**
		 * This object is the name area of the tab. If the ID is not 0 (we have
		 * multiple tabs in a given panel), we offset it based on the position
		 * of the last tab id
		 */
		if (id == 0)
			tabTitle = new Rectangle(posX + 5, posY, tabPanel.getTabWidth(),
					tabPanel.getTabHeight());
		else
			tabTitle = new Rectangle(
					(int) (tabPanel.getTabs().get(id - 1).tabTitle.getX()
							+ tabPanel.getTabWidth() + 5), posY,
					tabPanel.getTabWidth(), tabPanel.getTabHeight());
	}

	@Override
	public void pack() {
		update();
		g.setColor(Color.pink);
		g.draw(getBoundingBox());

		if (isActiveTab)
			g.setPaint(new GradientPaint(tabTitle.x, tabTitle.y, dBlue,
					tabTitle.x, tabTitle.y + tabTitle.height, lightGray, true));
		else
			g.setPaint(new GradientPaint(tabTitle.x, tabTitle.y, lightGray,
					tabTitle.x, tabTitle.y + tabTitle.height, dBlue, true));
		g.fillRoundRect(tabTitle.x, tabTitle.y, tabTitle.width,
				tabTitle.height, 35, 35);
		g.setColor(dBlue);
		g.drawRoundRect(tabTitle.x, tabTitle.y, tabTitle.width,
				tabTitle.height, 35, 35);

		g.setColor(Color.black);
		FontMetrics fm = parentPanel.getFontMetrics(SpaceGame.getInstance()
				.getDefaultFont());
		Font f = new Font("Courier New", Font.PLAIN, 14);
		g.setFont(f);
		g.drawString(tabName,
				(int) (tabTitle.getX() + tabTitle.getWidth() / 2 - fm
						.stringWidth(tabName) / 2), (int) (tabTitle.getY()
						+ tabTitle.getHeight() / 2 + fm.getHeight() / 3));
	}

	/**
	 * Returns this instance's rectangle region the "title" gui is in
	 * 
	 * @return Rectangle
	 */
	public Rectangle getTitleBounds() {
		return tabTitle;
	}

	/**
	 * Set the state of this objects panel being displayable
	 * 
	 * @param b
	 */
	public void setOver(boolean b) {
		isActiveTab = b;
	}
}
