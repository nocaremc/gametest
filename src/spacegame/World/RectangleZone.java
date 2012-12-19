package spacegame.World;

import java.awt.Rectangle;

public class RectangleZone extends Rectangle {
	private static final long serialVersionUID = -2248418874330609153L;
	private int entityCap, entityCount;

	public RectangleZone(Rectangle r, int i) {
		super(r);
		entityCap = i;
		entityCount = 0;
	}

	public int getCap() {
		return entityCap;
	}

	public void setCap(int i) {
		entityCap = i;
	}

	public int getCount() {
		return entityCount;
	}

	public void setCount(int i) {
		entityCount = i;
	}
}