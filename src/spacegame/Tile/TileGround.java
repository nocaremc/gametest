package spacegame.Tile;

import java.awt.Rectangle;
import spacegame.Entity.Entity;

public class TileGround extends Tile {
	public TileGround(int width, int height) {
		super(width, height);
	}

	public static int getID() {
		return 1;
	}

	public static void handleFallingOnto(Entity entity, Rectangle.Double r) {
		entity.setY(r.getY() - entity.getHeight());
		entity.setOnGround(true);
	}
}