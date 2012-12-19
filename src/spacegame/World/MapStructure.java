package spacegame.World;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import spacegame.GameImages;
import spacegame.Tile.*;

/**
 * @author Nocare This class handles the structure of a map. It will accept a
 *         Two Dimensional array defining all the tiles in a map, as well as
 *         size of the map. It will parse the array and decide which images to
 *         return, and which tileTypes to return for a specific tile when
 *         queried, most often by the CollisionManager
 */

public class MapStructure {
	private int width, height; // Size of map in Tile Units
	private int tileWidth, tileHeight; // Size of Tiles
	private int[][] Map; // two-dimension array of map structure. Accessed as
							// int[y][x] to keep one's sanity

	/** Define all tiles being used in our game **/
	private TileGround ground;
	private TileWallLeft wallLeft;
	private TileStair_Right stairRight;
	private TileStair_RightInner stairRightInner;
	@SuppressWarnings("unused")
	private TileNull nullTile;
	private TileDoor tileDoor;
	
	public MapStructure() {
		
	}

	public MapStructure(int width, int height, int tileWidth, int tileHeight) {
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		initMap();
	}

	/** creates a Set of default tiles AirType. and a ground **/
	public void initMap() {
		/** initialize tiles **/
		nullTile = new TileNull(tileWidth, tileHeight);
		ground = new TileGround(tileWidth, tileHeight);
		stairRight = new TileStair_Right(tileWidth, tileHeight);
		stairRightInner = new TileStair_RightInner(tileWidth, tileHeight);
		wallLeft = new TileWallLeft(tileWidth, tileHeight);
		tileDoor = new TileDoor(tileWidth, tileHeight);
	}
	
	public void setVariables(int w, int h, int tWidth, int tHeight) {
		width = w;
		height = h;
		tileWidth = tWidth;
		tileHeight = tHeight;
	}

	/** Get/Set Map[][] **/
	public void addMap(int[][] newMap) {
		Map = newMap;
	}

	public int[][] getMap() {
		return Map;
	}

	/** Get map width/height in Tile Units **/
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/** get size of tiles in pixels **/
	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	/** used when parsing a map array to return the type of tile image to return **/
	public BufferedImage getTileImg(int ID) {
		switch (ID) {
		case 1:
			return GameImages.tiles[1]; // ground
		case 2:
			return GameImages.tiles[8]; // top
		case 3:
			return GameImages.tiles[0]; // StairRight
		case 4:
			return GameImages.tiles[4]; // BotRight
		case 5:
			return GameImages.tiles[5]; // wallLeft
		case 6:
			return GameImages.tiles[3]; // BotLeft
		case 7:
			return GameImages.tiles[6]; // wallright
		case 8:
			return GameImages.tiles[7]; // topLeft
		case 9:
			return GameImages.tiles[9]; // topRight
		case 10:
			return GameImages.tiles[2]; // stairLeft
		case 42:
			return tileDoor.getImg();
		case 99:
			return GameImages.tiles[10]; // null
		default:
			return null;
		}

	}

	/** returns tile object from id **/
	public Tile getTile(int ID) {
		if (ID == 7 || ID == 8 || ID == 9 || ID == 2 || ID == 99)
			ID = 1;
		else if (ID == 10 || ID == 6)
			ID = 3;

		switch (ID) {
		case 1:
			return ground;
		case 3:
			return stairRight;
		case 4:
			return stairRightInner;
		case 5:
			return wallLeft;
		case 42:
			return tileDoor;
		default:
			return null;
		}
	}

	/** draw the map, called by LevelMap **/
	public void pack(Graphics2D g, boolean doGrid) {
		g.setColor(Color.blue); // Color used when drawing a grid representing
								// the tiles
		for (int y = 0; y < Map.length; y++) {
			for (int x = 0; x < Map[y].length; x++) {
				if (getTile(Map[y][x]) != null)
					g.drawImage(getTileImg(Map[y][x]), x * tileWidth, y
							* tileHeight, null);

				if (doGrid) { // draw grid of tiles. Good visual indicator of
								// where tiles will be placed
					g.drawRect(x * tileWidth, y * tileHeight, x * tileWidth
							+ tileWidth, y * tileHeight + tileHeight);
					g.drawString(x + " " + y, x * tileWidth + tileWidth / 2, y
							* tileHeight + tileHeight / 2);
				}
			}
		}
	}
}