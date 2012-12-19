package spacegame;

import java.awt.image.BufferedImage;

public class OneScene {
	private BufferedImage pic;
	private long endTime;

	public OneScene(BufferedImage i, long endTime) {
		this.pic = i;
		this.endTime = endTime;
	}

	public BufferedImage getPic() {
		return pic;
	}

	public long getEndTime() {
		return endTime;
	}
}