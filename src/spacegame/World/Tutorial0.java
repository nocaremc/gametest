package spacegame.World;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import spacegame.GameImages;
import spacegame.SpaceGame;

public class Tutorial0 extends LevelMap {
	/** monsters on this map **/

	public Tutorial0()
	{
		// Gravity, Gravity Apply Rate, ground friction, air friction
		super(0.098F, 2.65F, 0.7F, 1F);
		setMapDimensions(0, 0, 2880, 900);
		setMapStructure(2880 / tileSizeX, 900 / tileSizeY, tileSizeX, tileSizeY);
		
		/** create monsters **/

		
		/** add monsters to entities list **/
		// entities.add(worm1);
		creatureSpawnRangeMin = 1;
		creatureSpawnRangeMax = 1;
		spawnFrequency = 6000L;
		mapInit();
	}

	@Override
	public void mapInit() {
		int[][] thisMap = {
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,42,7},
			{5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4}
		};

		getMapStructure().addMap(thisMap);
		super.mapInit();
		/** set player's initial position on this map **/
		entityPlayer.setOnGround(false);
		entityPlayer.setPosition(
				(minX + (entityPlayer.getWidth() / 2) + tileSizeX),
				(maxY - entityPlayer.getHeight()) - tileSizeY * 3.5);
	}

	@Override
	public void pack() {
		g = SpaceGame.getInstance().getGraphics();
		cameraOffsetX = entityPlayer.getX() - SpaceGame.getInstance().getWidth() / 2;
		cameraOffsetX *= 0.1;
		g.setBackground(Color.BLACK);
		g.fillRect(0, 0, getMapStructure().getWidth() * getMapStructure().getTileWidth(), getMapStructure().getHeight() * getMapStructure().getTileHeight());
		/**
		 * draw background as one giant tile set. Same deal with a non-tiled
		 * image but multiple images
		 **/
		BufferedImage bg = GameImages.levelOneBackground;
		g.drawImage(bg, (int) (0 - cameraOffsetX), 0, null);
		g.drawImage(bg, (int) (bg.getWidth(null) - cameraOffsetX), 0, null);
		g.drawImage(bg, (int) (bg.getWidth(null) - cameraOffsetX), bg.getHeight(null), null);
		g.drawImage(bg, (int) (0 - cameraOffsetX), bg.getHeight(null), null);
		g.drawImage(bg, (int) (0 - bg.getWidth(null) - cameraOffsetX), 0, null);
		g.drawImage(bg, (int) (0 - bg.getWidth(null) - cameraOffsetX), bg.getHeight(null), null);
		/** draw our sun **/
		// g.drawImage(GameImages.levelOneSun, 0, 0, null);
		super.pack();
	}

	@Override
	public void handleTeleport(Point.Double t) {
		Point.Double tp1 = new Point.Double(1, 27);
		if (input.wPressed) {
			if (t.equals(tp1)) {
				System.out.println("to infinitity and beyond!");
				levelChange(new Level_One());
			}
		}
	}
}