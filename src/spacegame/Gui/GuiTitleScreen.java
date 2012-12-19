package spacegame.Gui;

import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.SpaceGame.State;

public class GuiTitleScreen extends GuiScreen {
	private GuiButton guiButtons[] = new GuiButton[4];

	public GuiTitleScreen(int minX, int minY, int width, int height) {
		super(minX, minY, width, height);
		/** create a new button object and place it on the screen **/
		/** round button dimensions based on how I see them at 1440x900 **/
		int ngX = 300;
		int ngY = 30;
		/**
		 * Define the topLeft where our button will be placed. In this case we
		 * want it in the center of the screen So we need to first center it,
		 * then subtract the CENTER of the height and width of the button object
		 **/
		int ngL = (width / 2) - (ngX / 2);
		int ngT = (height / 2) - (ngY / 2);
		guiButtons[0] = new GuiButton(0, "New Game", ngL, ngT, ngX, ngY,
				"Courier New", 14, this);
		guiButtons[1] = new GuiButton(1, "Load Game", ngL, ngT + ngY + 10, ngX,
				ngY, "Courier New", 14, this);
		guiButtons[2] = new GuiButton(2, "Settings", ngL, ngT + (ngY * 2) + 20,
				ngX, ngY, "Courier New", 14, this);
		guiButtons[3] = new GuiButton(3, "Exit", ngL, ngT + (ngY * 3) + 30,
				ngX, ngY, "Courier New", 14, this);
		/** create and add buttons to an ArrayList **/
		guiObjects.add(guiButtons[0]);
		guiObjects.add(guiButtons[1]);
		guiObjects.add(guiButtons[2]);
		guiObjects.add(guiButtons[3]);
	}

	@Override
	public void pack() {
		super.pack();
		/** Draw the background of our panel **/
		g.drawImage(GameImages.titleScreenBackground, posX, posY, null);
		/**
		 * call pack functions of our gui objects, this calls their specific
		 * render functions
		 **/
		for (int x = 0; x < guiObjects.size(); x++) {
			Gui gui = (Gui) guiObjects.get(x);
			if (gui instanceof GuiButton) {
				((GuiButton) gui).pack();
			}
		}
	}

	@Override
	public void handleInput() {
		if (input.escPressed) {
			// SpaceGame.getInstance().changeState(State.PLAYING);
			SpaceGame.getInstance().changeState(State.DESTROYED);
		}
		if (input.leftPressed && activeGuiObject != -999) {
			runFunction(activeGuiObject);
		}
		super.handleInput();
	}

	/** call our activeObject's function **/
	@Override
	public void runFunction(int activeGuiObject) {
		if (activeGuiObject == -999)
			return;
		switch (activeGuiObject) {
		case 0:
			SpaceGame.getInstance().changeState(State.PLAYING);
			break;
		case 1:
			System.out.println("continue game");
			SpaceGame.getInstance().getScreen().setWindowed(1024, 768);
			break;
		case 2:
			SpaceGame.getInstance().changeState(State.SETTINGS);
		case 3:
			SpaceGame.getInstance().changeState(State.DESTROYED);
			break;
		}
		activeGuiObject = -999;
	}
	/*
	 * @Override public void updateObjects() { super.updateObjects(); int ngX =
	 * 300; int ngY = 30; /** Define the topLeft where our button will be
	 * placed. In this case we want it in the center of the screen* So we need
	 * to first center it, then subtract the CENTER of the height and width of
	 * the button object ** int ngL = (getWidth() / 2) - (ngX / 2); int ngT =
	 * (getHeight() / 2)- (ngY / 2);
	 * 
	 * guiObjects.get(0).setPosition(ngL, ngT);
	 * guiObjects.get(1).setPosition(ngL, ngT+ngY+10);
	 * guiObjects.get(2).setPosition(ngL, ngT+ngY*2+20);
	 * 
	 * }
	 */
}