package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;

import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.SpaceGame.State;

public class GuiGameOver extends GuiScreen {
	private GuiButton guiButtons[] = new GuiButton[2];

	public GuiGameOver(int minX, int minY, int width, int height) {
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
		guiButtons[1] = new GuiButton(1, "Exit", ngL, ngT + (ngY) + 10, ngX,
				ngY, "Courier New", 14, this);
		/** create and add buttons to an ArrayList **/
		guiObjects.add(guiButtons[0]);
		guiObjects.add(guiButtons[1]);
	}

	@Override
	public void pack() {
		super.pack();
		/** Draw the background of our panel **/
		g.drawImage(GameImages.titleScreenBackground, 0, 0, null);
		Font monsterFont = new Font("Courier New", Font.BOLD, 32);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.yellow);
		g.setFont(monsterFont);

		FontMetrics fm = parentPanel.getFontMetrics(monsterFont);

		g.drawString("Game Over!", width / 2
				- (fm.stringWidth("Game Over!") / 2),
				(height / 2) - fm.getHeight());

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
		case 0: // Need to resetMap
			SpaceGame.getInstance().gameReset();
			break;
		case 1:
			SpaceGame.getInstance().changeState(State.DESTROYED);
			break;
		}
		activeGuiObject = -999;
	}
}