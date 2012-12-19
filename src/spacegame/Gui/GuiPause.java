package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.SpaceGame.State;

public class GuiPause extends GuiScreen {
	private GuiButton guiButtons[] = new GuiButton[3];

	public GuiPause(int minX, int minY, int width, int height) {
		super(minX, minY, width, height);
		int ngX = 300;
		int ngY = 30;
		/**
		 * Define the topLeft where our button will be placed. In this case we
		 * want it in the center of the screen So we need to first center it,
		 * then subtract the CENTER of the height and width of the button object
		 **/
		int ngL = (width / 2) - (ngX / 2);
		int ngT = (height / 2) - (ngY / 2);
		/** create and add buttons to an ArrayList **/
		guiButtons[0] = new GuiButton(0, "Resume Game", ngL, ngT, ngX, ngY,
				"Courier New", 14, this);
		guiButtons[1] = new GuiButton(1, "Settings", ngL, ngT + ngY + 10, ngX,
				ngY, "Courier New", 14, this);
		guiButtons[2] = new GuiButton(2, "Exit", ngL, ngT + (ngY * 2) + 20,
				ngX, ngY, "Courier New", 14, this);
		guiObjects.add(guiButtons[0]);
		guiObjects.add(guiButtons[1]);
		guiObjects.add(guiButtons[2]);
	}

	@Override
	public void pack() {
		super.pack();
		/** tile overlay image across screen **/
		for (int x = 0; x < width; x += 16) {
			for (int y = 0; y < height; y += 16) {
				g.drawImage(GameImages.pauseScreenBackground, x, y, null);
			}
		}

		Font giantText = new Font("Courier New", Font.BOLD, 32);
		FontMetrics fm = parentPanel.getFontMetrics(giantText);
		String pauseString = "Game Paused";
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON); // Antialiase text
		g.setFont(giantText);
		g.setColor(Color.darkGray);
		int ngt = guiButtons[0].getY() - (fm.getHeight() / 2);
		g.drawString(pauseString, (width / 2)
				- (fm.stringWidth(pauseString) / 2) - 1, ngt - 1);
		g.drawString(pauseString, (width / 2)
				- (fm.stringWidth(pauseString) / 2) - 2, ngt - 2);
		g.drawString(pauseString, (width / 2)
				- (fm.stringWidth(pauseString) / 2) - 3, ngt - 3);
		g.drawString(pauseString, (width / 2)
				- (fm.stringWidth(pauseString) / 2) - 4, ngt - 4);
		// g.drawString(pauseString, (width / 2) - (fm.stringWidth(pauseString)
		// / 2)-5, ngt-5);
		g.setColor(Color.YELLOW);
		g.drawString(pauseString, (width / 2)
				- (fm.stringWidth(pauseString) / 2), ngt);

		/** pack all gui objects **/
		for (int x = 0; x < guiObjects.size(); x++) {
			Gui gui = (Gui) guiObjects.get(x);
			if (gui instanceof GuiButton) {
				((GuiButton) gui).pack();
			}
		}

	}

	/**
	 * function polls mouse, invoked in superclass's pack function. I guess pack
	 * = update? haha
	 **/
	@Override
	public void handleInput() {
		if (!getInputFocus())
			return;
		if (input.escPressed) {
			SpaceGame.getInstance().changeState(State.PLAYING);
		}
		if (input.leftPressed && activeGuiObject != -999) {
			runFunction(activeGuiObject);
		}
	}

	/** call our activeObject's (button here) function **/
	@Override
	public void runFunction(int activeGuiObject) {
		if (activeGuiObject == -999 || !input.leftPressed)
			return;
		switch (activeGuiObject) {
		case 0:
			SpaceGame.getInstance().changeState(State.PLAYING);
			activeGuiObject = -999;
			break;
		case 1:
			SpaceGame
					.getInstance()
					.getMap()
					.setDrawGridEnabled(
							!SpaceGame.getInstance().getMap()
									.isDrawGridEnabled());
			System.out.println("Opened Settings");
			activeGuiObject = -999;
			break;
		case 2:
			SpaceGame.getInstance().changeState(State.DESTROYED);
			activeGuiObject = -999;
			break;
		default:
			activeGuiObject = -999;
			break;
		}
	}

}