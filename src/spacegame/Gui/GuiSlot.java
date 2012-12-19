package spacegame.Gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import spacegame.GameImages;
import spacegame.Storable.InventoryStack;

/**
 * This class displays a "Slot" gui in the game. I plan for it to be used for
 * both attacks/actions, as well as for item slots
 * 
 * @author Nocare
 * 
 *         TODO: Need to implement some pretty vast inventory-related functions
 *         later I may compact the definition of that to something like
 *         "inventoryfordisplay", since I'll be using spells as well.
 */

public class GuiSlot extends Gui {
	private int ID;
	private InventoryStack invStack = null;
	private BufferedImage slotImage, slotImage_o;

	public GuiSlot(int ID, int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
		this.ID = ID;
	}

	@Override
	public void pack() {
		if (g == null)
			return;
		if (slotImage == null)
			g.drawImage(GameImages.slotImg, posX, posY, null); // draw slot
																// image
		else {
			if (isMouseOver()) {
				g.drawImage(slotImage_o, posX, posY, null); // draw slot image
			} else {
				g.drawImage(slotImage, posX, posY, null); // draw slot image
			}
		}

		if (invStack != null && invStack.getIcon() != null)// draw icon image
															// inside slot, if
															// any
		{
			g.scale(0.8, 0.8); // scale to 48x48, if image is 60x60
			g.drawImage(invStack.getIcon(), (int) (posX * 1.25) + 6,
					(int) (posY * 1.25) + 6, null);
			g.scale(1.25, 1.25);
		}

		if (isMouseOver() && slotImage == null) // draw slight highlight over
												// slot when mouse over
		{
			g.setColor(new Color(187, 194, 72, 50));
			g.fillRect(posX + 6, posY + 6, width - 12, height - 12);
		}
	}

	/** get's ID of current slot **/
	public int getId() {
		return ID;
	}

	/** Get/Set inventory in slot **/
	public InventoryStack getInventoryStack() {
		return invStack;
	}

	public void setInventoryStack(InventoryStack inv) {
		invStack = inv;
	}

	/** Retrieve specific slot by ID, from a collection fo slots **/
	public static GuiSlot getSlotById(int ID, java.util.List<GuiSlot> slots) {
		for (int x = 0; x < slots.size(); x++) {
			if (slots.get(x).getId() == x)
				return slots.get(x);
		}
		return null;
	}

	public GuiSlot setCustomImages(BufferedImage norm, BufferedImage over) {
		slotImage = norm;
		slotImage_o = over;
		return this;
	}
}