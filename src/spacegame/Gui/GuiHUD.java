package spacegame.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.Entity.EntityPlayer;
import spacegame.SpaceGame.State;
import spacegame.Storable.InventoryStack;
import spacegame.Storable.Skill;
import spacegame.Storable.StatsManager;

public class GuiHUD extends GuiScreen {

	private java.util.List<GuiSlot> actionBarSlots = new ArrayList<GuiSlot>();
	private GuiIconButton characterInfo; // Button to open stats
	private GuiStats characterInfoGui;
	private int activeSlot = -999; // My "null" value for integers..
	private NumberFormat df = DecimalFormat.getNumberInstance();

	/** Displays many things such as health, action bars, other in-game tools **/
	public GuiHUD(int minX, int minY, int width, int height) {
		super(minX, minY, width, height);
		df.setMaximumFractionDigits(2); // Set rounding mode to 2 digits after .
	}

	/**
	 * Function that creates all the slots with appropriate positioning for an
	 * action bar
	 **/
	private void createActionBar() {
		int left = (SpaceGame.getInstance().getWidth() - 600) / 2;
		for (int x = 0; x < 10; x++) {
			actionBarSlots.add(new GuiSlot(x, left + (x * 60), SpaceGame
					.getInstance().getHeight() - 60, 60, 60));
		}
		actionBarSlots.get(0).setInventoryStack(
				new InventoryStack(1, Skill.fireBall.getName()));
		actionBarSlots.get(0).getInventoryStack()
				.setIcon(Skill.fireBall.getIcon());
	}

	@Override
	public void pack() {
		SpaceGame core = SpaceGame.getInstance();
		width = core.getWidth();
		height = core.getHeight();

		if (firstRun) {
			if (SpaceGame.getInstance().getState() != State.PLAYING)
				return;

			System.out.println("Loading HUD");

			characterInfo = new GuiIconButton(0, 0, 0, 20, 20,
					GameImages.iconButtons[0], this);
			characterInfoGui = (GuiStats) (new GuiStats(0, 0, 600, 400, this))
					.setTitle("Character Info");
			guiObjects.add(characterInfo);
			createActionBar();
			firstRun = false;
			return;
		}

		EntityPlayer pl = SpaceGame.getInstance().getMap().getPlayer();
		super.pack();

		if (needsUpdate)
			updateObjects();

		for (int x = 0; x < actionBarSlots.size(); x++) {
			actionBarSlots.get(x).pack();
			if (x == activeSlot)// Particular Slot is active/selected. So lets
								// draw something to visualize that.
				g.drawImage(GameImages.activeSlotImg, actionBarSlots.get(x)
						.getX() - core.getScreen().getInsets().left,
						actionBarSlots.get(x).getY()
								- core.getScreen().getInsets().top, null);
			else if (activeSlot == -1) // Need to handle 0 differently, since
										// the value would be -1 which x never
										// is
				g.drawImage(GameImages.activeSlotImg, actionBarSlots.get(9)
						.getX() - core.getScreen().getInsets().left,
						actionBarSlots.get(9).getY()
								- core.getScreen().getInsets().top, null);
		}

		renderXpBar(pl, actionBarSlots.get(0).getX()
				- core.getScreen().getInsets().left, height - 75
				- core.getScreen().getInsets().top, 599, 15);

		renderStatusArea(pl, 0, 0, 300, 60);

		/**
		 * Windows
		 * 
		 * We call the pack function regardless, the gui itself decides whether
		 * or not it will continue
		 **/
		characterInfoGui.pack();// Stats gui
	}

	public List<GuiSlot> getActionBarSlots() {
		if (actionBarSlots == null) {
			System.out.println("Null action bar");
			return null;
		}
		return actionBarSlots;
	}

	/** Renders an experience bar with static gradient design and colors **/
	public void renderXpBar(EntityPlayer pl, int x, int y, int width, int height) {
		Color lb = new Color(155, 224, 236, 255); // Create a new fully opaque,
													// light blue color obj
		g.setColor(Color.gray); // set color to grey
		g.fillRect(x, y, width, height); // Fill a rectangle, this is our
											// background for the bar
		g.setPaint(new GradientPaint(x, y, lb, x, y + height, Color.CYAN, true)); // set
																					// the
																					// painting
																					// mode
																					// to
																					// a
																					// gradient
		double xpPerc = pl.getStats().getCurrentXP() / pl.getStats().getMaxXP(); // Get
																					// the
																					// player's
																					// current
																					// %
																					// across
																					// the
																					// level
		int fillXp = (int) (xpPerc * width); // Scale width by value of % across
												// level
		g.fillRect(x + 1, y + 1, fillXp - 1, height - 1); // Fill a rectangle,
															// with the width
															// being our scaled
															// width.

		Font xp = new Font("Courier New", Font.PLAIN, 11); // Create a font for
															// the text over the
															// xp
		FontMetrics fm = parentPanel.getFontMetrics(xp); // Get more information
															// about size, etc
															// of font
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON); // ensure anti-aliasing is
													// on
		g.setFont(xp); // finally, set the font
		String xpText = df.format(pl.getStats().getCurrentXP()) + " / "
				+ df.format(pl.getStats().getMaxXP()); // get our roughly real
														// xp values
		int left = fm.stringWidth(xpText) / 2; // We'll shift the text left so
												// its always centered
		int top = fm.getHeight() / 3; // And up, I always find 4 to work better
										// than 2
		// Draw our text witht he gradiant, this adds a little more readability
		// when the xp bar does not extend into the text
		// g-color is still gradient..
		g.drawString(xpText, x + (width / 2) - left - 1, y + (height) - top - 1);
		g.setColor(Color.black);
		g.drawString(xpText, x + (width / 2) - left, y + (height) - top); // Draw
																			// our
																			// black
																			// text
	}

	/**
	 * Renders a status area on the screen. This area has the health, mana, etc.
	 * Typical in mmo's and some rpgs.
	 **/
	public void renderStatusArea(EntityPlayer pl, int x, int y, int width,
			int height) {
		// super.pack();

		StatsManager stats = pl.getStats();
		Font text = new Font("Courier New", Font.BOLD, 12);
		FontMetrics textMetrics = parentPanel.getFontMetrics(text);

		/** BackDrop **/
		g.setColor(transLightGray);
		g.fillRect(x, y, width, height);

		/** Health Bar **/
		g.setPaint(new GradientPaint(x, y, Color.LIGHT_GRAY, x, y
				+ (height / 3) - 1, transLightGray, true));
		g.fillRect(x, y, width - 1, height / 3 - 1); // grey backdrop
		g.setColor(Color.black);
		g.drawRect(x, y, width - 1, height / 3 - 1); // Black border

		g.setPaint(new GradientPaint(x, y, Color.red, x, y + (height / 3),
				dRed, true));
		int hpPerc = (int) Math.round((stats.getCurrentHealth() / stats
				.getMaxHealth()) * (width - 2));
		g.fillRect(x + 1, y + 1, hpPerc, height / 3 - 2); // hp gradient-ed bar
		String health = df.format(stats.getCurrentHealth()) + " / "
				+ df.format(stats.getMaxHealth());
		g.setColor(Color.black);
		g.drawString(health, x + (width / 2)
				- (textMetrics.stringWidth(health) / 2), y + (height / 3)
				- textMetrics.getHeight() / 2);

		y--;

		/** Mana Bar **/
		g.setPaint(new GradientPaint(x, y, Color.LIGHT_GRAY, x, y
				+ ((height / 3) * 2) - 1, transLightGray, true));
		g.fillRect(x, y + (height / 3), width - 1, height / 3 - 1); // grey
																	// backdrop
		g.setColor(Color.black);
		g.drawRect(x, y + (height / 3), width - 1, height / 3 - 1); // Black
																	// border

		g.setPaint(new GradientPaint(x, y + (height / 3), lBlue, x, y
				+ ((height / 3) * 2), dBlue, true));
		int manaPerc = (int) Math.round((stats.getCurrentMana() / stats
				.getMaxMana()) * (width - 2));
		g.fillRect(x + 1, y + (height / 3) + 1, manaPerc, height / 3 - 2); // hp
																			// gradient-ed
																			// bar
		String mana = df.format(stats.getCurrentMana()) + " / "
				+ df.format(stats.getMaxMana());
		g.setColor(Color.black);
		g.drawString(mana, x + (width / 2)
				- (textMetrics.stringWidth(mana) / 2), y + ((height / 3) * 2)
				- textMetrics.getHeight() / 2);

		/** Level Display **/
		String lvl = "lv." + stats.getLevel();
		g.drawRect(x, y + (height / 3) * 3 - (height / 3) - 1,
				textMetrics.stringWidth(lvl) + 10, height / 3 + 1);
		g.setPaint(new GradientPaint(x, y, Color.LIGHT_GRAY, x, y
				+ ((height / 3) * 2) - 1, transLightGray, true));
		g.fillRect(x, y + (height / 3) * 3 - (height / 3),
				textMetrics.stringWidth(lvl) + 10, height / 3);
		g.setColor(Color.gray);
		g.drawString(lvl, x + 5 - 1, y + (height - textMetrics.getHeight() / 2)
				+ 1);
		g.setColor(Color.black);
		g.drawString(lvl, x + 5, y + (height - textMetrics.getHeight() / 2));

		/** Icon Buttons **/
		characterInfo.setPosition(x + textMetrics.stringWidth(lvl) + 10, y
				+ (height / 3) * 2);
		characterInfo.pack();
	}

	@Override
	public void handleInput() {
		super.handleInput();
		if (input.escPressed) // We'll set all the gui windows non-visible
		{
			closeWindows();
		}

		if (input.leftPressed && activeGuiObject != -999) {
			runFunction(activeGuiObject);
		}
		characterInfoGui.handleInput();
	}

	@Override
	public void runFunction(int activeGuiObject) {
		if (System.currentTimeMillis() - lastClickHandle < 200)
			return;

		lastClickHandle = System.currentTimeMillis();

		boolean flag = true; // Holds whether an acceptable button was used
		boolean flagVisible;
		switch (activeGuiObject) {
		case 0:
			if (!guiObjects.get(0).isMouseOver())
				return;
			flagVisible = (characterInfoGui.isVisible()) ? false : true;
			characterInfoGui.setVisible(flagVisible);
			break;
		default:
			flag = false;
			break;
		}
		if (flag)
			activeGuiObject = -999;
	}

	public void closeWindows() {
		characterInfoGui.setVisible(false);
	}

	public boolean isPlayerUsing() {
		update();
		if (input.leftPressed) {
			for (int x = 0; x < actionBarSlots.size(); x++) {
				if (actionBarSlots.get(x).isMouseOver()) // If we are left
															// clicking and the
															// mouse is over
															// this object
				{
					activeSlot = x; // Change the selected slot to the one we
									// clicked
					return true;
				}
			}

			if (characterInfoGui != null && characterInfoGui.isMouseOver())
				return true;
			if (characterInfo != null && characterInfo.isMouseOver())
				return true;
		}

		return false;
	}

	public void setActiveBarSlot(int i) {
		activeSlot = i;
	}

	public int getActiveBarSlot() {
		return activeSlot;
	}

	@Override
	public void updateObjects() {
		SpaceGame core = SpaceGame.getInstance();
		if (core.getState() != State.PLAYING) {
			needsUpdate = true;
			return;
		}

		@SuppressWarnings("unused")
		int oldWidth = width;
		@SuppressWarnings("unused")
		int oldHeight = height;

		// super.updateObjects();

		needsUpdate = false;
	}
}