package spacegame.Util;

import java.awt.Rectangle;

public interface IIntegerDynamicObject {
	public void setScale(double scale);

	public double getScale();

	public int getX();

	public int getY();

	public void setX(int x);

	public void setY(int y);

	public void setPosition(int x, int y);

	public int getWidth();

	public int getHeight();

	public void setWidth(int w);

	public void setHeight(int h);

	public void setDimensions(int w, int h);

	public Rectangle getBoundingBox();

	public double getDeltaX();

	public double getDeltaY();

	public void setDeltaX(double dx);

	public void setDeltaY(double dy);

	public void setSpeed(double dx, double dy);

	public boolean isMoving();

	public boolean isMovingX();

	public boolean isMovingY();

	public void update();

	public void updatePositionWithSpeed();
}
