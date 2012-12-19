package spacegame.Gui;

import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.SpaceGame.State;

public class GuiSettings extends GuiScreen {
	GuiTabPanel tabPanel;
	GuiButton btnBack;
	
	/**
	 * Buttons holding actions for specific resolutions
	 */
	private GuiButton btn1024x768, btn640x480, btn720x480, btn720x576, btn1280x768, btn1280x720, 
		btn1152x864, btn1152x720, btn800x600, btn1280x800, btn1280x960, btn1280x1024, btn1360x768, btn1440x900;

	public GuiSettings(int minX, int minY, int width, int height) {
		super(minX, minY, width, height);
		tabPanel = new GuiTabPanel(width / 2 - 400, height / 2 - 300, 800, 600,
				this);
		tabPanel.setTabDimensions(150, 30);
		tabPanel.addTab(0, "Video");
		tabPanel.addTab(1, "Controls");
		tabPanel.addTab(2, "Misc");
		btnBack = new GuiButton(0, "Back", tabPanel.getX()
				+ tabPanel.getWidth() - 100, tabPanel.getY()
				+ tabPanel.getHeight() + 30, 100, 30, "Courier New", 14, this);
		
		guiObjects.add(btnBack);
		InitVideoTab();
	}

	/**
	 * Sets up the structure of the video tab.
	 */
	private void InitVideoTab() {
		int x = tabPanel.getX() + 60;
		int y = tabPanel.getY() + 60;
		btn640x480 = new GuiButton(1, "640 x 480", x, y, 100, 30, "Courier New", 14, this);
		btn720x480 = new GuiButton(2, "720 x 480", x, y+30*1+5, 100, 30, "Courier New", 14, this);
		btn720x576 = new GuiButton(3, "720 x 576", x, y+30*2+5*2, 100, 30, "Courier New", 14, this);
		btn800x600 = new GuiButton(4, "800 x 600", x, y+30*3+5*3, 100, 30, "Courier New", 14, this);
		btn1024x768 = new GuiButton(5, "1024 x 768", x, y+30*4+5*4, 100, 30, "Courier New", 14, this);
		btn1152x720 = new GuiButton(7, "1152 x 720", x, y+30*5+5*5, 100, 30, "Courier New", 14, this);
		btn1152x864 = new GuiButton(6, "1152 x 864", x, y+30*6+5*6, 100, 30, "Courier New", 14, this);
		btn1280x720 = new GuiButton(8, "1280 x 720", x+105, y, 100, 30, "Courier New", 14, this);
		btn1280x768 = new GuiButton(9, "1280 x 768", x+105, y+30*1+5*1, 100, 30, "Courier New", 14, this);
		btn1280x800 = new GuiButton(10, "1280 x 800", x+105, y+30*2+5*2, 100, 30, "Courier New", 14, this);
		btn1280x960 = new GuiButton(11, "1280 x 960", x+105, y+30*3+5*3, 100, 30, "Courier New", 14, this);
		btn1280x1024 = new GuiButton(12, "1280 x 1024", x+105, y+30*4+5*4, 100, 30, "Courier New", 14, this);
		btn1360x768 = new GuiButton(13, "1360 x 768", x+105, y+30*5+5*5, 100, 30, "Courier New", 14, this);
		btn1440x900 = new GuiButton(14, "1440 x 900", x+105, y+30*6+5*6, 100, 30, "Courier New", 14, this);
		
		
		guiObjects.add(btn640x480);//1
		guiObjects.add(btn720x480);//2
		guiObjects.add(btn720x576);//3
		guiObjects.add(btn800x600);//4
		guiObjects.add(btn1024x768);//5
		guiObjects.add(btn1152x720);//6
		guiObjects.add(btn1152x864);//7
		guiObjects.add(btn1280x720);//8
		guiObjects.add(btn1280x768);//9
		guiObjects.add(btn1280x800);//10
		guiObjects.add(btn1280x960);//11
		guiObjects.add(btn1280x1024);//12
		guiObjects.add(btn1360x768);//13
		guiObjects.add(btn1440x900);//14
		
		
	}

	/**
	 * Switches method calls based on input and object which have focus, more or
	 * less
	 */
	@Override
	public void runFunction(int activeGuiObject) {
		
		System.out.println("case: "+activeGuiObject);
		
		switch (activeGuiObject) 
		{
			case 0:
				SpaceGame.getInstance().changeState(State.INITIALIZED);
				break;
			case 1:
				SpaceGame.getInstance().getScreen().setWindowed(640,480);
				break;
			case 2:
				SpaceGame.getInstance().getScreen().setWindowed(720,480);
				break;
			case 3:
				SpaceGame.getInstance().getScreen().setWindowed(720,576);
				break;
			case 4:
				SpaceGame.getInstance().getScreen().setWindowed(800,600);
				break;
			case 5:
				SpaceGame.getInstance().getScreen().setWindowed(1024,768);
				break;
			case 6:
				SpaceGame.getInstance().getScreen().setWindowed(1152,720);
				break;
			case 7:
				SpaceGame.getInstance().getScreen().setWindowed(1152,864);
				break;
			case 8:
				SpaceGame.getInstance().getScreen().setWindowed(1280,720);
				break;
			case 9:
				SpaceGame.getInstance().getScreen().setWindowed(1280,768);
				break;
			case 10:
				SpaceGame.getInstance().getScreen().setWindowed(1280,800);
				break;
			case 11:
				SpaceGame.getInstance().getScreen().setWindowed(1280,960);
				break;
			case 12:
				SpaceGame.getInstance().getScreen().setWindowed(1280,1024);
				break;
			case 13:
				SpaceGame.getInstance().getScreen().setWindowed(1360,768);
				break;
			case 14:
				SpaceGame.getInstance().getScreen().setWindowed(1440,900);
				break;
		}
	}

	/**
	 * Handle input for this screen
	 */
	@Override
	public void handleInput() {
		
		if (input.escPressed)
			runFunction(0);
		
		if (input.leftPressed)
		{
			if (btnBack.isMouseOver())
				runFunction(0);
			
			/** Get active object **/
			for (int x = 1; x< guiObjects.size();x++)
			{
				if (guiObjects.get(x).isMouseOver() && input.leftPressed)
				{
					runFunction(x);
				}
			}
		}
	}

	/**
	 * Draw this screen and its child elements
	 */
	@Override
	public void pack() {
		super.pack();
		g.drawImage(GameImages.titleScreenBackground, 0, 0, null);
		tabPanel.pack();
		btnBack.pack();
		switch (tabPanel.getSelectedTab())
		{
			case 0:
				packVideoTab();
				break;
		}
	}
	
	/**
	 * Draw video tab
	 */
	private void packVideoTab()
	{
		for (int x = 0; x < guiObjects.size(); x++)
		{
			if (!(guiObjects.get(x).equals(btnBack)))
				guiObjects.get(x).pack();
			
		}
	}
}
