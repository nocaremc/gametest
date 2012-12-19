package spacegame.Tile;

import java.awt.Rectangle;

import spacegame.Entity.Entity;
import spacegame.World.MapStructure;

public class TileStair_RightInner extends Tile {
	public TileStair_RightInner(int width, int height) {
		super(width, height);
	}

	public void checkCollision(int y, int x, Entity entity, MapStructure map) {
		Rectangle tile = new Rectangle(x * map.getTileWidth(), y
				* map.getTileHeight(), map.getTileWidth(), map.getTileHeight());
		if (tile.contains(entity.getBotRight())) {
			entity.setColliding(true);
			entity.setY(entity.getY() - tile.height * 0.45);
			entity.setOnGround(true);
			entity.resetGravityTicks();
		}
	}

	public static int getID() {
		return 4;
	}
}