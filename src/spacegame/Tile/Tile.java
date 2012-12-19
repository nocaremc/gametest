package spacegame.Tile;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D.Double;
import spacegame.Entity.Entity;

public abstract class Tile {
	protected int width, height;

	public Tile(int width, int height) {
		this.width = width;
		this.height = height;
	}

	protected boolean bottomRegion(Rectangle.Double tile, Entity entity) {
		return tile.contains(entity.getBotLeft())
				|| tile.contains(entity.getBotRight());
	}

	protected boolean topRegion(Rectangle.Double tile, Entity entity) {
		return tile.contains(entity.getTopLeft())
				|| tile.contains(entity.getTopRight())
				|| tile.contains(entity.getX() + entity.getWidth() / 2,
						entity.getY() + 1);
	}

	protected boolean leftRegion(Rectangle.Double tile, Entity entity) {
		return entity.getBotLeft().getX() < tile.getX() + tile.width;
	}

	protected boolean rightRegion(Rectangle.Double tile, Entity entity) {
		return entity.getBotRight().getX() + entity.getWidth() > tile.getX();
	}

	public Double toRealCoordinates(Rectangle.Double tile) {
		return new Rectangle.Double(tile.x * width, tile.y * height,
				tile.width, tile.height);
	}
}