package spacegame.Gui;

import java.util.ArrayList;

import spacegame.SpaceGame;

public abstract class GuiScreen extends Gui {
	protected java.util.List<Gui> guiObjects = new ArrayList<Gui>();
	protected int activeGuiObject;
	protected boolean hasInputFocus = false;
	protected long lastClickHandle;

	public GuiScreen(int minX, int minY, int width, int height) {
		super(minX, minY, width, height);
		activeGuiObject = -999;
		lastClickHandle = System.currentTimeMillis();
	}

	@Override
	public void pack() {
		update();
		handleInput();
		aalias();
	}

	/**
	 * Called during screen resizes, updates the gui width and height.
	 * GuiScreens should quite obviously extend this
	 **/
	public void updateObjects() {
		int oldWidth = width;
		int oldHeight = height;
		width = SpaceGame.getInstance().getWidth();
		height = SpaceGame.getInstance().getHeight();

		for (int i = 0; i < guiObjects.size(); i++) {
			double x = guiObjects.get(i).getX();
			double y = guiObjects.get(i).getY();
			double w = guiObjects.get(i).getWidth();
			double h = guiObjects.get(i).getHeight();

			double newX = ((x / oldWidth) * width);
			double newY = ((y / oldHeight) * height);
			double newW = ((w / oldWidth) * width);
			double newH = ((h / oldHeight) * height);

			guiObjects.get(i).setPosition((int) newX, (int) newY);
			guiObjects.get(i).setWidth((int) newW);
			guiObjects.get(i).setHeight((int) newH);
		}
	}

	public void handleInput() {
		if (!getInputFocus())
			return;
	}

	public abstract void runFunction(int activeGuiObject);

	public void setInputFocus(boolean flag) {
		hasInputFocus = flag;
	}

	public boolean getInputFocus() {
		return hasInputFocus;
	}

	public java.util.List<Gui> getGuiObjects() {
		return guiObjects;
	}
}