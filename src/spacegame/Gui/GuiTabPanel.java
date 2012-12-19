package spacegame.Gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GuiTabPanel extends Gui {

	private List<GuiTab> tabs = new ArrayList<GuiTab>();
	private int tabWidth, tabHeight;
	@SuppressWarnings("unused")
	private GuiScreen parentScreen;
	private int selectedIndex = 0;

	public GuiTabPanel(int posX, int posY, int width, int height,
			GuiScreen master) {
		super(posX, posY, width, height);
		parentScreen = master;
	}

	@Override
	public void pack() {
		update();
		handleInput();
		g.setColor(Color.CYAN);
		g.draw(getBoundingBox());

		for (int x = 0; x < tabs.size(); x++) {
			tabs.get(x).pack();
			tabs.get(x).setOver(false);
			if (x == selectedIndex)
				tabs.get(x).setOver(true);
		}
	}

	/**
	 * Handle mouse input on tabs, specifically to determine which elements to
	 * display
	 **/
	public void handleInput() {
		/** Determines if any tab is active **/
		if (input.leftPressed) {
			for (int x = 0; x < tabs.size(); x++) {
				if (tabs.get(x).isMouseOver(tabs.get(x).getTitleBounds())) {
					selectedIndex = x;
				}
					
			}
		}

		
	}

	public synchronized void addTab(int id, String name) {
		tabs.add(new GuiTab(this, id, name));
	}

	public void setTabWidth(int w) {
		tabWidth = w;
	}

	public int getTabWidth() {
		return tabWidth;
	}

	public void setTabHeight(int h) {
		tabHeight = h;
	}

	public int getTabHeight() {
		return tabHeight;
	}

	public void setTabDimensions(int w, int h) {
		tabWidth = w;
		tabHeight = h;
	}

	public List<GuiTab> getTabs() {
		return tabs;
	}

	public int getSelectedTab() {
		return selectedIndex;
	}
}
