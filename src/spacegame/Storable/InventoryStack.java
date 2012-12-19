package spacegame.Storable;

import java.awt.image.BufferedImage;

import spacegame.Entity.EntityPlayer;

public class InventoryStack {
	private BufferedImage iconImg;
	/** Determines the type of item stored. Spell, item, action or otherwise **/
	private int contentsType = 0;
	/** name of item in inventory **/
	private String name;

	public InventoryStack(int type, String name) {
		contentsType = type;
		this.name = name;
	}

	public BufferedImage getIcon() {
		return iconImg;
	}

	public void setIcon(BufferedImage icon) {
		iconImg = icon;
	}

	/** sets the type of item in the slot **/
	public void setType(int type) {
		contentsType = type;
	}

	/** perform execute function of whatever this is **/
	public void execute(EntityPlayer entityPlayer) {
		switch (contentsType) {
		case 1:
			SkillSpell.cast(entityPlayer, name);
		}
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return contentsType;
	}
}