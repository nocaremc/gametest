package spacegame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class's responsibility is to manage the game's buffer strategy and
 * re-paint the screen each tick
 **/

public class SpaceCanvas extends JPanel {
	private static final long serialVersionUID = -4343344285929867521L; // Some
																		// shit
																		// Eclipse
																		// begged
																		// to
																		// have
																		// placed
	private JFrame master; // parent JFrame
	private boolean repaintInProgress = false;
	@SuppressWarnings("unused")
	private InputManager input;

	public SpaceCanvas(JFrame master, InputManager input) {
		this.master = master;
		this.input = input;
		setFocusable(true);
		requestFocus();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		setIgnoreRepaint(true);
	}

	public synchronized void myPaint(BufferStrategy bs) {
		if (repaintInProgress)
			return;
		repaintInProgress = true;

		Graphics gr = bs.getDrawGraphics();
		if (gr instanceof Graphics2D) {
			gr.translate(master.getInsets().left, master.getInsets().top);
			SpaceGame.getInstance().gameDraw((Graphics2D) gr);

			try {
				bs.show();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			Toolkit.getDefaultToolkit().sync();
			repaintInProgress = false;
			gr.translate(-master.getInsets().left, -master.getInsets().top);
		}
	}
}