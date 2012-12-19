package spacegame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
	private ArrayList<OneScene> scenes;
	private int sceneIndex, loopCount = 0;
	private long movieTime; // The Amount of time a given animation has been
							// running
	private long totalTime;
	public boolean running;

	public Animation() {
		scenes = new ArrayList<OneScene>();
		totalTime = 0;
	}

	/** Add a scene object(image+frequency) to this animation **/
	public synchronized void addScene(BufferedImage i, long t) {
		totalTime += t;
		scenes.add(new OneScene(i, totalTime));
	}

	/** start animation **/
	public synchronized void start() {
		movieTime = 0;
		sceneIndex = 0;
		loopCount = 0;
		running = true;
	}

	/** update animation (cycle through and loop scenes) **/
	public synchronized void update(long timePassed) {
		if (scenes.size() > 1) {
			movieTime += (timePassed / 1000000L);
			if (movieTime >= totalTime) {
				movieTime = 0;
				sceneIndex = 0;
				loopCount++;
			}
			while (movieTime > getScene(sceneIndex).getEndTime()) {
				sceneIndex++;
			}
		}
	}

	/** Return current scene image **/
	public synchronized BufferedImage getImage() {
		if (scenes.size() == 0)
			return null;
		else {
			return getScene(sceneIndex).getPic();
		}
	}

	/** internal function to get the scene from the arraylist **/
	private OneScene getScene(int x) {
		return (OneScene) scenes.get(x);
	}

	public int getSceneIndex() {
		return sceneIndex;
	}

	public int getSceneMax() {
		return scenes.size();
	}

	public int getLoopCount() {
		return loopCount;
	}
}