package spacegame.Tile;

import java.awt.image.BufferedImage;

import spacegame.Animation;
import spacegame.GameImages;
import spacegame.SpaceGame;

public class TileDoor extends Tile {
	Animation teleporter;

	/**
	 * This tile handles zone changes, but passes the ball to the level it is in
	 **/
	public TileDoor(int width, int height) {
		super(width, height);
		// ID 42

		teleporter = new Animation();
		for (int x = 0; x < GameImages.tileDoor.length; x++) {
			teleporter.addScene(GameImages.tileDoor[x], 130);
		}
	}

	public BufferedImage getImg() {
		teleporter.update(SpaceGame.getInstance().getTimePassed());
		return teleporter.getImage();
	}

}
