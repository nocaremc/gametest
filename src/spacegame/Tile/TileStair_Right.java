package spacegame.Tile;

import java.awt.Rectangle;

import spacegame.Entity.Entity;
import spacegame.World.MapStructure;

public class TileStair_Right extends Tile {
	public TileStair_Right(int width, int height) {
		super(width, height);
	}

	public void checkCollision(int y, int x, Entity entity, MapStructure map) {
		Rectangle tile = new Rectangle(x * map.getTileWidth(), y
				* map.getTileHeight(), map.getTileWidth(), map.getTileHeight());
		/**
		 * at this point we are passing 1 tile in the player's surrounding
		 * buffer of tiles and checking if the player bounding box intersects
		 * with it
		 */
		/** Right side hitting wall **/
		if (tile.contains(entity.getBotRight())) {
			entity.setColliding(true);

			/**
			 * lets raise the player's y coordinate by an amount relative to the
			 * x % across the tile hypotenuse
			 **/
			double yOffset;

			double length = tile.width - tile.x;
			double hypotenuse = Math.sqrt(length * length + length * length);

			yOffset = (entity.getBotRight().getX() - entity.getX())
					/ hypotenuse;
			// System.out.println(yOffset);

			entity.setY(entity.getY() - entity.getY() * yOffset * 0.75);
			entity.setOnGround(true);
			entity.resetGravityTicks();
		}
	}

	public static int getID() {
		return 3;
	}
}