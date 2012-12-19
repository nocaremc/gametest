package spacegame;

import java.awt.*;
import java.util.Random;

import javax.swing.*;

import spacegame.Gui.GuiGameOver;
import spacegame.Gui.GuiPause;
import spacegame.Gui.GuiSettings;
import spacegame.Gui.GuiTitleScreen;
import spacegame.World.LevelMap;
import spacegame.World.Level_One;

public class SpaceGame {
	/** GRAPHICS **/
	private Graphics2D GRAPHICS2D; // Graphics2D handle

	// 1920 x 1080
	private static final Font defaultButtonFont = new Font("Courier New",
			Font.BOLD, 16); // default button font for gui
	private boolean gameDrawing; // is a screen Paint in progress?
	private int vcWidth, vcHeight;

	/** TIMING **/
	private static final int UPDATE_RATE = 60; // Game updates a second
	private final long UPDATE_PERIOD = 1000000000L / UPDATE_RATE; // nanoseconds
	private long beginTime, timeTaken, timeLeft, cumTime, timePassed; // various
																		// timer
																		// values
	private long lastStateChangeAttempt; // Timestamp of last state change
											// attempt
	private Random random;

	/** RUN TIME **/
	private static SpaceGame instance; // Instance of this class, used by nearly
										// all classes to get instantiated
										// variables
	private boolean gameRunning; // is the game running
	private Thread gameThread; // Thread that holds this game

	public static enum State {
		INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED, SETTINGS
	} // State enumeration

	private static State state; // Current state

	/** GUI SCREENS **/
	private static GuiTitleScreen titleScreen;
	private static GuiPause pauseScreen;
	private LevelMap currentMap; // GuiIngame? haha
	private static GuiGameOver gameOverScreen;
	private static GuiSettings settingsScreen;

	private ScreenManager screen;

	public SpaceGame() {
		instance = this;
		random = new Random();

		/** init game stuff needed at start **/
		gameInit();
		screen = new ScreenManager();

		vcWidth = screen.getWidth();
		vcHeight = screen.getHeight();

		/** Init guiScreens **/
		titleScreen = new GuiTitleScreen(0, 0, vcWidth, vcHeight);
		gameOverScreen = new GuiGameOver(0, 0, vcWidth, vcHeight);
		pauseScreen = new GuiPause(0, 0, vcWidth, vcHeight);
		settingsScreen = new GuiSettings(0, 0, vcWidth, vcHeight);
		currentMap = (new Level_One());

		/** start game **/
		gameStart();
	}

	public void gameInit() {
		state = State.INITIALIZED;
		gameRunning = true;
		gameDrawing = false;
	}

	public void gameReset() {
		currentMap = new Level_One();
		currentMap.mapInit();
		state = State.PLAYING;
	}

	/**
	 * Shutdown game. Using a forced shutdown call since the process hangs and
	 * stays present indefinitely...
	 **/
	@SuppressWarnings("deprecation")
	public void gameShutdown() {
		System.out.println("Game is shutting down!");
		screen.setVisible(false);
		screen.dispose();
		gameThread.stop();
	}

	/**
	 * Create a thread for our JFrame and start it. Supposedly makes this more
	 * thread safe
	 **/
	public void gameStart() {
		gameThread = new Thread("SpaceGame Main Thread") {
			@Override
			public void run() {
				gameLoop();
			}
		};
		gameThread.start();
	}

	/** Main Game Loop **/
	private void gameLoop() {
		/** init state change stamp so first call works properly **/
		lastStateChangeAttempt = System.currentTimeMillis();
		// Game loop
		cumTime = System.nanoTime();
		currentMap.mapInit(); // Initialize map
		while (gameRunning) {
			beginTime = System.nanoTime();
			if (state == State.DESTROYED)
				break; // break the loop to finish the current play
			if (state == State.PLAYING) {
				gameUpdate();
			}

			screen.getCanvas().myPaint(screen.getBufferStrategy()); // re-paint
																	// screen
			// Delay timer to provide the necessary delay to meet the target
			// rate
			timeTaken = System.nanoTime() - beginTime;
			timePassed = System.nanoTime() - cumTime;
			cumTime += timePassed;
			timeLeft = (UPDATE_PERIOD - timeTaken) / 1000000L; // in milliseconds
			
			if (timeLeft < 1)
				timeLeft = 1; // set a minimum
			try {
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex) {
			}
		}

		/** game loop has ended, so shut down game **/
		gameShutdown();
	}

	public void gameUpdate() {
		currentMap.update();
	}

	public synchronized void gameDraw(Graphics2D g2d) {
		GRAPHICS2D = g2d; // update our graphics handle
		if (gameDrawing)
			return;
		gameDrawing = true;

		/** Draw a screen, depending on state **/
		switch (state) {
		case INITIALIZED:
			drawTitleScreen();
			break;
		case PLAYING:
			drawMap();
			break;
		case PAUSED:
			pause();
			break;
		case GAMEOVER:
			drawGameOverScreen();
			break;
		case DESTROYED:
			break;
		case SETTINGS:
			drawSettings();
			break;
		}
		gameDrawing = false;
	}

	public synchronized boolean changeState(State s) {
		/**
		 * if 1/4 a second has not passed since the last state change, return.
		 * should prevent flickering
		 **/
		long curTime = System.currentTimeMillis();
		long timeElapsed = curTime - lastStateChangeAttempt;
		if (timeElapsed < 200L)
			return false;
		else
			lastStateChangeAttempt = System.currentTimeMillis();

		switch (s) {
		case INITIALIZED:
			state = State.INITIALIZED;
			break;
		case PLAYING:
			state = State.PLAYING;
			changeInputFocus(State.PLAYING);
			break;
		case PAUSED:
			state = State.PAUSED;
			changeInputFocus(State.PAUSED);
			break;
		case GAMEOVER:
			state = State.GAMEOVER;
			break;
		case DESTROYED:
			state = State.DESTROYED;
			break;
		case SETTINGS:
			state = State.SETTINGS;
			break;
		default:
			System.out.println("Recieved a state change that does not exist");
			break;
		}
		return true;
	}

	/** Set input focus of gui's based on state change. **/
	public synchronized void changeInputFocus(State s) {
		if (s == State.PLAYING) {
			pauseScreen.setInputFocus(false);
			titleScreen.setInputFocus(false);
			currentMap.setInputFocus(true);
		} else if (s == State.INITIALIZED) {
			pauseScreen.setInputFocus(false);
			titleScreen.setInputFocus(true);
			currentMap.setInputFocus(false);
		} else if (s == State.PAUSED) {
			pauseScreen.setInputFocus(true);
			titleScreen.setInputFocus(false);
			currentMap.setInputFocus(false);
		}
	}

	/** draw titlescreen **/
	public void drawTitleScreen() {
		if (titleScreen != null)
			titleScreen.pack();
	}

	/** Draw game over screen **/
	public void drawGameOverScreen() {
		if (gameOverScreen != null)
			gameOverScreen.pack();
	}

	/** gets current map in use and draws it **/
	public void drawMap() {
		if (currentMap != null)
			currentMap.pack();
	}

	/** pause the game, and raise the pause gui **/
	public void pause() {
		/**
		 * we want to paint the map, THEN the pause screen. This creates a nice
		 * overlay effect
		 **/
		if (currentMap != null)
			currentMap.pack();
		if (pauseScreen != null)
			pauseScreen.pack();
	}

	/** draw settings screen **/
	public void drawSettings() {
		if (settingsScreen != null)
			settingsScreen.pack();
	}

	/** Various things must be corrected when screen changes size **/
	public void doScreenSizeChange() {
		if (state == State.PLAYING)
			state = State.PAUSED;

		titleScreen.updateObjects();
		gameOverScreen.updateObjects();
		pauseScreen.updateObjects();
		currentMap.getHud().updateObjects();

		// currentMap.setWidth(getWidth());
		// currentMap.setHeight(getHeight());

	}

	/** Various Getters **/
	public static SpaceGame getInstance() { // Return object instance for this
											// class. (Core of our game)
		return instance;
	}

	public long getUpdatePeriod() { // return update period of game
		return UPDATE_PERIOD;
	}

	public SpaceCanvas getCanvas() { // return jpanel canvas handle
		return screen.getCanvas();
	}

	public InputManager getInput() { // return input manager instance
		return screen.getInput();
	}

	public Graphics2D getGraphics() { // return graphics handle, set constantly
										// in gameDraw
		return GRAPHICS2D;
	}

	public Font getDefaultFont() { // return default gui font
		return defaultButtonFont;
	}

	public long getTimePassed() { // return time passed since last game update
		return timePassed;
	}

	public LevelMap getMap() { // return map/level currently in use
		return currentMap;
	}

	public void setMap(LevelMap map) { // Set map currently in use
		currentMap = map;
	}

	public int getWidth() { // return width of screen (size of game screen on
							// the monitor)
		return screen.getWidth();
	}

	public int getHeight() { // return height of screen
		return screen.getHeight();
	}

	public ScreenManager getScreen() {
		return screen;
	}

	public Random getRandom() { // Return the baseline random generator of the
								// game, randomly resetting it
		int r = random.nextInt(60);
		for (int x = 0; x < r; x++) {
			random.nextInt();
		}
		return random;
	}

	public State getState() { // Return current game state
		return state;
	}
	
	public Thread getThread() {
		return gameThread;
	}

	public static void main(String[] args) {
		// Use the event dispatch thread to build the UI for thread-safety.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SpaceGame();
			}
		});
	}
}