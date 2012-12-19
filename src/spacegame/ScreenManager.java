package spacegame;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import spacegame.SpaceGame.State;

public class ScreenManager extends JFrame {
	private static final long serialVersionUID = -3179255572389027236L;
	int vcWidth, vcHeight; // height and width of screen
	private SpaceCanvas canvas; // Jpanel canvas handle
	private InputManager input; // Input manager
	private GraphicsEnvironment e;
	private GraphicsDevice vc;
	public boolean fullScreen = true;

	private long lastWindowChange, lastBufferStratAttempt;

	private static final DisplayMode[] modes = {
			new DisplayMode(640, 480, 32, 60),
			new DisplayMode(640, 480, 32, 59),
			new DisplayMode(640, 480, 32, 75),
			new DisplayMode(720, 480, 32, 60),
			new DisplayMode(720, 480, 32, 59),
			new DisplayMode(720, 576, 32, 60),
			new DisplayMode(720, 576, 32, 75),
			new DisplayMode(720, 576, 32, 50),
			new DisplayMode(800, 600, 32, 60),
			new DisplayMode(800, 600, 32, 75),
			new DisplayMode(1024, 768, 32, 60),
			new DisplayMode(1024, 768, 32, 75),
			new DisplayMode(1152, 864, 32, 60),
			new DisplayMode(1280, 720, 32, 60),
			new DisplayMode(1280, 720, 32, 50),
			new DisplayMode(1280, 720, 32, 59),
			new DisplayMode(1280, 768, 32, 60),
			new DisplayMode(1280, 800, 32, 60),
			new DisplayMode(1280, 960, 32, 60),
			new DisplayMode(1280, 1024, 32, 60),
			new DisplayMode(1280, 1024, 32, 75),
			new DisplayMode(1360, 768, 32, 60),
			new DisplayMode(1366, 768, 32, 60),
			new DisplayMode(1440, 900, 32, 60),
			new DisplayMode(1152, 720, 32, 60),
			new DisplayMode(640, 480, 16, 60),
			new DisplayMode(720, 480, 16, 60),
			new DisplayMode(720, 576, 16, 60),
			new DisplayMode(800, 600, 16, 60),
			new DisplayMode(1024, 768, 16, 60),
			new DisplayMode(1152, 864, 16, 60),
			new DisplayMode(1280, 720, 16, 60),
			new DisplayMode(1280, 720, 16, 50),
			new DisplayMode(1280, 720, 16, 59),
			new DisplayMode(1280, 768, 16, 60),
			new DisplayMode(1280, 800, 16, 60),
			new DisplayMode(1280, 960, 16, 60),
			new DisplayMode(1280, 1024, 16, 60),
			new DisplayMode(1360, 768, 16, 60),
			new DisplayMode(1366, 768, 16, 60),
			new DisplayMode(1440, 900, 16, 60),
			new DisplayMode(1152, 720, 16, 60) };

	public ScreenManager() {
		/** Setup screen size varables **/
		e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
		vcWidth = vc.getDisplayMode().getWidth();
		vcHeight = vc.getDisplayMode().getHeight();
		/*
		 * for (int x = 0; x < vc.getDisplayModes().length; x++) { DisplayMode d
		 * = vc.getDisplayModes()[x]; System.out.println(d.getWidth() + " " +
		 * d.getHeight() + " " + d.getBitDepth() + " " + d.getRefreshRate()); }
		 */

		input = new InputManager(); // Init input manager
		canvas = new SpaceCanvas(this, input); // Init Jpanel canvas

		/** set up User interface of this Frame **/
		canvas.setPreferredSize(new Dimension(vcWidth, vcHeight));
		this.setContentPane(canvas);
		// Other UI components such as button, score board, if any.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SpaceGame");
		this.setUndecorated(true);
		this.setResizable(false);
		vc.setFullScreenWindow(this);
		this.setLocation((vcWidth / 2) - vcWidth, (vcHeight / 2) - vcHeight);
		this.pack();
		this.setVisible(true);
		this.createBufferStrategy(2);
		lastWindowChange = lastBufferStratAttempt = System.currentTimeMillis();
	}

	public void setWindowed(int x, int y) {
		input.leftPressed = false;// Game somewhat freezes here and we cannot be
									// handling a down left click..

		if (System.currentTimeMillis() - lastWindowChange < 2500L) // Limit
																	// frequency
																	// to
																	// prevent
																	// like 5
																	// calls a
																	// click,
																	// which
																	// takes the
																	// user to
																	// exception
																	// hell
			return;
		else
			lastWindowChange = System.currentTimeMillis();

		Window w = vc.getFullScreenWindow(); // Get current frame object and
												// dispose it
		if (w != null)
			w.dispose();

		this.dispose(); // dispose this JFrame

		// Find a displayMode that roughly matches the passed dimensions, adding
		// that to an array to be compared
		List<DisplayMode> tempDM = new ArrayList<DisplayMode>();
		for (int i = 0; i < modes.length; i++) {
			if (modes[i].getWidth() == x && modes[i].getHeight() == y)
				tempDM.add(modes[i]);
		}

		// Pass our list to be checked, and the display modes of the video card
		// will be matched against this, and hopefully find something taht works
		DisplayMode dm = findFirstCompatibleMode(tempDM);
		vc.setFullScreenWindow(null); // Set the full screen object to null,
										// this takes it out of fullscreen. odd
										// coders those swing guys
		vc = e.getDefaultScreenDevice(); // reset our graphics device variable

		if (vc.isDisplayChangeSupported()) // change mode if we can
			vc.setDisplayMode(dm);

		setUndecorated(false); // We want a border
		setPreferredSize(new Dimension(dm.getWidth(), dm.getHeight())); // Set
																		// dimensions
																		// of
																		// JFrame
		setResizable(false);
		setContentPane(canvas);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		canvas.setPreferredSize(null);// Will cause canvas to fill JFrame
		// Middle of screen
		setLocation(
				(int) (e.getMaximumWindowBounds().getWidth() / 2 - dm
						.getWidth() / 2),
				(int) (e.getMaximumWindowBounds().getHeight() / 2 - dm
						.getHeight() / 2));

		pack();
		setVisible(true);

		vcWidth = dm.getWidth();
		vcHeight = dm.getHeight();
		SpaceGame.getInstance().doScreenSizeChange();

		try {
			this.createBufferStrategy(2);
		} catch (IllegalStateException e) {
			System.out
					.println("Could not create Buffer Strategy, will keep attempting");
			tryBufferStrategy(0);
		}

		fullScreen = false;
	}

	public DisplayMode findFirstCompatibleMode(List<DisplayMode> m) {
		DisplayMode goodModes[] = vc.getDisplayModes();
		for (int x = 0; x < m.size(); x++) {
			for (int y = 0; y < goodModes.length; y++) {
				if (m.get(x).equals(goodModes[y]))// displayModesMatch(m.get(x),
													// goodModes[y]))
				{
					return m.get(x);
				}
			}
		}
		return null;
	}

	public boolean displayModesMatch(DisplayMode x, DisplayMode y) {
		if (x.getWidth() != y.getWidth() || x.getHeight() != y.getHeight())
			return false;
		if (x.getBitDepth() != y.getBitDepth())// DisplayMode.BIT_DEPTH_MULTI &&
												// y.getBitDepth() !=
												// DisplayMode.BIT_DEPTH_MULTI)
			return false;
		if (x.getRefreshRate() != y.getBitDepth())// DisplayMode.REFRESH_RATE_UNKNOWN
													// && y.getRefreshRate() !=
													// DisplayMode.REFRESH_RATE_UNKNOWN)
			return false;
		return true;
	}

	private void tryBufferStrategy(int tries) {
		if (tries > 60) {
			System.out
					.println("Tried over 60 times to create buffer strategy. Quitting...");
			SpaceGame.getInstance().changeState(State.DESTROYED);
		}

		if (System.currentTimeMillis() - lastBufferStratAttempt < 125) {
			try {
				Thread.sleep(125);
			} catch (Exception e) {
			} finally {
				tryBufferStrategy(tries);
			}
		} else {
			try {
				createBufferStrategy(2);
			} catch (IllegalStateException e) {
				System.out.println("Failed to create Buffer Strategy " + tries
						+ " times.");
				tryBufferStrategy(tries++);
			}
		}
	}

	public SpaceCanvas getCanvas() {
		return canvas;
	}

	public InputManager getInput() {
		return input;
	}

	@Override
	public int getWidth() {
		return vcWidth;
	}

	@Override
	public int getHeight() {
		return vcHeight;
	}
}
