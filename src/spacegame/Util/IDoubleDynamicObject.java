package spacegame.Util;

import java.awt.Rectangle;

public interface IDoubleDynamicObject {

	public void setScale(double scale);

	public double getScale();

	public double getX();

	public double getY();

	public void setX(double x);

	public void setY(double y);

	public void setPosition(double x, double y);

	public int getWidth();

	public int getHeight();

	public void setWidth(int w);

	public void setHeight(int h);

	public void setDimensions(int w, int h);

	public Rectangle.Double getBoundingBox();

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
