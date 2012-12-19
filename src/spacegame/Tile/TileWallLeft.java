package spacegame.Tile;

import java.awt.Rectangle;

import spacegame.SpaceGame;
import spacegame.Entity.Entity;
import spacegame.World.MapStructure;

public class TileWallLeft extends Tile {
	public TileWallLeft(int width, int height) {
		super(width, height);
	}

	/** With wall-left, we'll always just shoot the player to the right of it **/
	public void checkCollision(Rectangle.Double tile, Entity entity,
			MapStructure map) {
		int tileBelow = SpaceGame.getInstance().getMap().getCollisionManager()
				.getTileTypeAt(entity.getBotRight());
		System.out.println(" a" + tileBelow);
		tile = toRealCoordinates(tile);
		if (entity.getBoundingBox().intersects(tile)) {
			entity.setX(tile.getX() + tile.getWidth() - entity.getWidth() / 4);

		}

	}

	public static int getID() {
		return 5;
	}
}