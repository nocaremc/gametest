package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;

import spacegame.GameImages;
import spacegame.SpaceGame;

/**
 * This class shows up in game, and has the special attribute of being draggable
 **/
public class GuiWindow extends Gui {
	private boolean isDragging = false;
	private Point lastDragPoint;
	private GuiIconButton closeButton;
	private Rectangle titleBar;
	protected GuiScreen parentGui;
	protected String windowTitle;

	private HashMap<Gui, Point> draggableObjects = new HashMap<Gui, Point>();

	protected int activeGuiObject;

	public GuiWindow(int posX, int posY, int width, int height,
			GuiScreen parentGui) {
		super(posX, posY, width, height);
		isVisible = false;
		this.parentGui = parentGui;
		titleBar = new Rectangle(posX + 1, posY + 1, width - 1, 15);
		/**
		 * Define close button, passing in a scaled Image instance to match
		 * title bar dimensions
		 **/
		closeButton = new GuiIconButton(0, posX + width - 15, posY + 1, 15, 15,
				GameImages.iconButtons[1].getScaledInstance(15, 15,
						Image.SCALE_SMOOTH), this);
	}

	@Override
	public void pack() {
		if (!isVisible)
			return;

		update();
		updateDrag(input.getMouseLocation(), false);
		drawFrame();
	}

	/** Draws frame of window **/
	public void drawFrame() {
		g.setColor(lightGray); // Set main window bg color
		g.fillRect(posX, posY, width, height); // draw main window background
		g.setColor(Color.black);
		g.drawRect(posX, posY, width, height); // Draw a border
		// Draw title bar
		// g.setPaint(new GradientPaint((int)titleBar.getX(),
		// (int)titleBar.getY(), lBlue, (int)titleBar.getX(),
		// (int)titleBar.getY()+(int)titleBar.getWidth(), dBlue, true));
		g.setColor(dBlue);
		g.fill(titleBar); // Draw background color
		g.setPaint(new GradientPaint((int) titleBar.getX(), (int) titleBar
				.getY(), hTransWhite, (int) titleBar.getX(), (int) titleBar
				.getY() + (int) titleBar.getHeight() + 9, transparent, true));
		g.fill(titleBar); // Draw again with translucent gradient, this adds
							// depth to it, making it look nice
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font f = new Font("Courier New", Font.ITALIC, 12); // Set up font
															// related things
		g.setFont(f);
		FontMetrics fm = parentPanel.getFontMetrics(f);
		g.setColor(Color.black);
		g.drawString(windowTitle,
				posX + (width / 2) - (fm.stringWidth(windowTitle) / 2), // Draw
																		// title
																		// string
				(int) (titleBar.getY() + (titleBar.getHeight() / 2) + fm
						.getHeight() / 3));

		closeButton.pack(); // Pack close button on top

		// Pack added objects
		for (Iterator<Gui> i = draggableObjects.keySet().iterator(); i
				.hasNext();) {
			Gui a = i.next();
			a.pack();
		}
		// In case of special items (ie tooltips), we call pack again and pack
		// those, so they keep a higher z-index
		for (Iterator<Gui> i = draggableObjects.keySet().iterator(); i
				.hasNext();) {
			Gui a = i.next();
			if (a instanceof GuiLabel)
				if (((GuiLabel) a).hasToolTip())
					((GuiLabel) a).getToolTip().pack();
		}
	}

	public void handleInput() {
		if (input.getMouseLocation() == null)
			return;

		if (input.leftPressed && isMouseOver(titleBar) && !isDragging) {
			setDragging(true);
			updateDrag(input.getMouseLocation(), true);

			// Add in a control to "close" the window, position is preserved
			// during visibility changes, so its nice
			if (closeButton.isMouseOver() && input.leftPressed)
				this.setVisible(false);
		}

		if (input.leftReleased)
			setDragging(false);

	}

	private void setDragging(boolean flag) {
		isDragging = flag;
	}

	/**
	 * Update the position of the frame, based on the last mouse location,
	 * rather than current. Will cause slight tearing on fast mouse movement,
	 * but its pretty slick in any case
	 * 
	 * @param loc
	 * @param firstRun
	 */
	private void updateDrag(Point loc, Boolean firstRun) {
		if (!isDragging)
			return;

		/**
		 * We set the drag point on first run, and nothing else. This sets our
		 * point to be 1 frame behind the actual mouse position
		 **/
		if (firstRun) {
			lastDragPoint = loc;
			// Initially set everything not visible
			for (Iterator<Gui> i = draggableObjects.keySet().iterator(); i
					.hasNext();) {
				Gui a = i.next();
				a.setVisible(false);
			}
			return;
		}

		/**
		 * get the distance travelled between mouse current location, and the
		 * last mouse location
		 **/
		double distanceX = loc.getX() - lastDragPoint.getX();
		double distanceY = loc.getY() - lastDragPoint.getY();

		/** Offset position with the distance travelled **/
		int x = (int) (posX + distanceX);
		int y = (int) (posY + distanceY);

		/**
		 * Clamp window within screen, this prevents the player from "losing"
		 * windows off screen
		 **/
		if (x < 0)
			x = 0;
		if (x + width > SpaceGame.getInstance().getWidth())
			x = SpaceGame.getInstance().getWidth() - width;

		if (y < 0)
			y = 0;
		if (y + height > SpaceGame.getInstance().getHeight())
			y = SpaceGame.getInstance().getHeight() - height;

		// Finally set position of main gui
		setPosition(x, y);
		// Update rest of elements using position + offset to find position
		titleBar.setLocation(x + 1, y + 1);
		closeButton.setPosition(x + width - 15, y + 1);

		// Update position of all objects registered within gui
		for (Iterator<Gui> i = draggableObjects.keySet().iterator(); i
				.hasNext();) {
			Gui a = i.next();
			Point offset = draggableObjects.get(a);
			a.setPosition(x + (int) offset.getX()
					+ SpaceGame.getInstance().getScreen().getInsets().left, y
					+ (int) offset.getY()
					+ +SpaceGame.getInstance().getScreen().getInsets().top);
		}

		// update position for next delay-drag
		lastDragPoint = loc;
	}

	/**
	 * Add a gui object to have its position on a Window object updated when the
	 * window is dragged.
	 * 
	 * @param gui
	 *            - Gui object to be updated
	 * @param p
	 *            - Point holding x,y offset relative to window position
	 */
	public void addForDrag(Gui gui, Point p) {
		draggableObjects.put(gui, p);
		// System.out.println(gui + " " + p.toString());
	}

	/** Sets title and returns updated instance **/
	protected GuiWindow setTitle(String title) {
		windowTitle = title;
		return this;
	}

	/** Override visible setter to include objects **/
	@Override
	public void setVisible(boolean flag) {
		for (Iterator<Gui> i = draggableObjects.keySet().iterator(); i
				.hasNext();) {
			Gui a = i.next();
			a.setVisible(flag);
		}
		super.setVisible(flag);
	}
}