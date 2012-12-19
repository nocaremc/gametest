package spacegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;

/**
 * 
 * @author Nocare Easy class that flips booleans based on input to a program
 *         TODO: add a timestamp to mouseclicks, as gui's check for the button
 *         state and the mouse location, but if a user holds the mouse down then
 *         moves the cursor over a gui element it's associated function fires
 */
public class InputManager implements KeyListener, MouseMotionListener,
		MouseListener, MouseWheelListener {
	/** keyboard type events **/
	public boolean escPressed = false;
	public boolean spacePressed = false;

	/** WASD **/
	public boolean wPressed = false;
	public boolean aPressed = false;
	public boolean sPressed = false;
	public boolean dPressed = false;

	/** actionBar **/
	public boolean onePressed = false;
	public boolean twoPressed = false;
	public boolean threePressed = false;
	public boolean fourPressed = false;
	public boolean fivePressed = false;
	public boolean sixPressed = false;
	public boolean sevenPressed = false;
	public boolean eightPressed = false;
	public boolean ninePressed = false;
	public boolean zeroPressed = false;

	/** mouse click variables **/
	public boolean leftPressed = false;
	public boolean leftReleased = false;
	public boolean rightPressed = false;
	private Point mouseLocation;

	@Override
	public void mousePressed(MouseEvent me) {
		int button = me.getButton();
		setMousePressedState(button, true);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		int button = me.getButton();
		setMousePressedState(button, false);
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		setMouseLocation(me.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		setMouseLocation(me.getPoint());
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		setKeyState(keyCode, true);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		setKeyState(keyCode, false);
	}

	public void setKeyState(int keyCode, boolean state) {
		switch (keyCode) {
		case KeyEvent.VK_SPACE:
			spacePressed = state;
			break;
		case KeyEvent.VK_ESCAPE:
			escPressed = state;
			break;
		case KeyEvent.VK_D:
			dPressed = state;
			break;
		case KeyEvent.VK_A:
			aPressed = state;
			break;
		case KeyEvent.VK_W:
			wPressed = state;
			break;
		case KeyEvent.VK_S:
			sPressed = state;
			break;

		case KeyEvent.VK_0:
			zeroPressed = state;
			break;
		case KeyEvent.VK_1:
			onePressed = state;
			break;
		case KeyEvent.VK_2:
			twoPressed = state;
			break;
		case KeyEvent.VK_3:
			threePressed = state;
			break;
		case KeyEvent.VK_4:
			fourPressed = state;
			break;
		case KeyEvent.VK_5:
			fivePressed = state;
			break;
		case KeyEvent.VK_6:
			sixPressed = state;
			break;
		case KeyEvent.VK_7:
			sevenPressed = state;
			break;
		case KeyEvent.VK_8:
			eightPressed = state;
			break;
		case KeyEvent.VK_9:
			ninePressed = state;
			break;

		default:
			// System.out.println("keyevent: "+keyCode);
			break;
		}
	}

	public void setMousePressedState(int button, boolean state) {
		switch (button) {
		case MouseEvent.BUTTON1:
			leftPressed = state;
			leftReleased = !state;
		case MouseEvent.BUTTON2:
			rightPressed = state;
		}
	}

	public boolean isAKeyDown() {
		return wPressed || aPressed || sPressed || dPressed || spacePressed;
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	public synchronized Point getMouseLocation() {
		/**
		 * If the game is in windowed mode, the border offsets the mouse
		 * position, so we have our screen manager tell us the border size so we
		 * can reverse that offset
		 */
		// if (!SpaceGame.getInstance().getScreen().fullScreen)
		// mouseLocation.translate(mouseOffset.x, mouseOffset.y);

		Point p = new Point(mouseLocation);

		/**
		 * if we don't reverse the offset when done with it, the point will
		 * cumulatively translate... regardless of the logical scope
		 **/
		// if (!SpaceGame.getInstance().getScreen().fullScreen)
		// mouseLocation.translate(-mouseOffset.x, -mouseOffset.y);

		Graphics2D g = SpaceGame.getInstance().getGraphics();
		if (g != null) {
			g.setColor(Color.yellow);
			g.drawRect(p.x, p.y, 2, 2);
		}
		return p;
	}

	public void setMouseLocation(Point mouseLocation) {
		this.mouseLocation = mouseLocation;
	}
}