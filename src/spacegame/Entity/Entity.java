package spacegame.Entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import spacegame.Animation;
import spacegame.SpaceGame;
import spacegame.Util.IDoubleDynamicObject;

public class Entity implements IDoubleDynamicObject {

	protected double posX, posY, deltaX, deltaY; // Position and Speed
	protected int posXi, posYi, width, height; // Dimensions
	private String entityName;

	/** List that holds animations belonging to this entity **/
	protected java.util.List<Animation> animations = new ArrayList<Animation>();
	
	/** Index of currently being used animation **/
	protected int animationInUse = -1;

	protected boolean isEffectedByGravity = false; // Do we apply gravity?
	protected boolean isFrictionLess = false; // Do we apply friction?
	protected boolean onGround = true; // On the ground?
	protected boolean specialDragging = false; // Is a mouse-dragging function
												// in effect by player?
	protected boolean isEffectedByKnockBack = false; // Does knockBack apply to
														// damage calculations?
	protected boolean isVisible = true; // Is this entity displayable?
	protected boolean isColliding = false; // is entity touching any map-object
											// other than air?
	protected boolean isDead = false; // Is this entity available to access?

	protected int gravityTicks = 0; // How many ticks has gravity been applied
									// since the last time on ground
	protected double scale = 1F; // Scale of entity

	/** Construct entity without speed **/
	public Entity(double posX, double posY, int width, int height) {
		this(posX, posY, 0, 0, width, height);
	}

	/** Construct with speed **/
	public Entity(double posX, double posY, double deltaX, double deltaY,
			int width, int height) {
		this.posX = posX;
		this.posY = posY;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.width = width;
		this.height = height;
		entityName = this.getClass().toString();
	}
	
	/** Construct without speed or size **/
	public Entity(double posX, double posY)
	{
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * DIMENSIONS
	 */

	/** Set both height and width of entity **/
	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/** set width of entity **/
	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	/** set height of entity **/
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	/** get width of entity **/
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	/** scale entity's dimensions **/
	@Override
	public void setScale(double factor) {
		scale = factor;
		setDimensions((int) Math.round(width * factor),
				(int) Math.round(height * factor));
		setPosition((posX * scale), (posY * scale));
	}

	/** get scale of entity **/
	@Override
	public double getScale() {
		return scale;
	}

	/**
	 * POSITION
	 */
	@Override
	public void setPosition(double x, double y) {
		posX = x;
		posY = y;
	}

	@Override
	public void setX(double x) {
		posX = x;
	}

	@Override
	public void setY(double y) {
		posY = y;
	}

	@Override
	public double getX() {
		return posX;
	}

	@Override
	public double getY() {
		return posY;
	}

	/** get the absolute position (center of entity) as a vector2f **/
	public Point.Double getAbsoluteXY() {
		return new Point.Double(posX + (width / 2), posY + (height / 2));
	}

	public void setOnGround(boolean flag) {
		onGround = flag;
	}

	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * SPEED
	 */

	/** set speed of x and y **/
	@Override
	public void setSpeed(double dx, double dy) {
		deltaX = dx;
		deltaY = dy;
	}

	/** set speed of x **/
	@Override
	public void setDeltaX(double dx) {
		deltaX = dx;
	}

	/** set speed of y **/
	@Override
	public void setDeltaY(double dy) {
		deltaY = dy;
	}

	/** get speed of X **/
	@Override
	public double getDeltaX() {
		return deltaX;
	}

	/** get speed of y **/
	@Override
	public double getDeltaY() {
		return deltaY;
	}

	/** is entity moving **/
	@Override
	public boolean isMovingX() {
		return deltaX > 0.01 || deltaX < -0.01;
	}

	@Override
	public boolean isMovingY() {
		return deltaY > 0.01 || deltaY < -0.01;
	}

	@Override
	public boolean isMoving() {
		return deltaX > 0.01 || deltaX < -0.01 || deltaY > 0.01 || deltaY < -0.01;
	}

	public void resetGravityTicks() {
		gravityTicks = 0;
	}

	/**
	 * ANIMATIONS
	 */

	/** Are Images available for an animation? **/
	public boolean canAnimate() {
		return getAnimation() != null && getAnimation().getImage() != null;
	}

	/** add an animation object to entity **/
	public void addAnimation(Animation a) {
		this.animations.add(a);
	}

	/** add an animation object to entity at specified index **/
	public void addAnimationAt(Animation a, int index) {
		if (animations.get(index) != null)
			animations.set(index, a);
		else
			System.out.println("Cannot add animation at index: " + index
					+ " on sprite: " + this);
	}

	/** remove animation object from list, specifying object index **/
	public void removeAnimation(int index) {
		if (animations.get(index) != null)
			animations.remove(index);
		else
			System.out.println("Cannot remove animation at index: " + index
					+ " on sprite: " + this);
	}

	/** set the animation being used currently by index **/
	public void setAnimationInUse(int index) {
		if (animations.get(index) != null)
			animationInUse = index;
		else
			System.out.println("Animation(" + index
					+ ") attempting to be used does not exist on sprite: "
					+ this);
	}

	/** get animation being used **/
	public Animation getAnimation() {
		if (animations.size() < 1 || this.isDead || animationInUse < 0)
			return null;

		if (animations.get(animationInUse) != null)
			return (Animation) animations.get(animationInUse);
		else
			return null;
	}

	/*
	 * MISC
	 */
	public boolean isEffectedByGravity() {
		return isEffectedByGravity;
	}

	public void setGravity(boolean flag) {
		isEffectedByGravity = flag;
	}

	public void setColliding(boolean flag) {
		isColliding = flag;
	}

	public boolean getColliding() {
		return isColliding;
	}

	/*
	 * COLLISION POINTS I use these points in determining specify spots to check
	 * for collision on an entity. Modifying points requires passing Point
	 * objects to the setter, pass null to keep a point unchanged
	 */

	/** return a bounding box and entities current position and dimensions **/
	@Override
	public Rectangle.Double getBoundingBox() {
		return (new Rectangle.Double(posX, posY, width, height));
	}

	public Point.Double getBotCenter() {
		return new Point.Double(posX + (width / 2), posY + height - 5);
	}

	public Point.Double getTopLeft() {
		return new Point.Double(posX + (width / 4F), posY);
	}

	public Point.Double getBotLeft() {
		return new Point.Double(posX + (width / 4F), posY + height - 5);
	}

	public Point.Double getMidBotLeft() {
		return new Point.Double(posX + (width / 4F), posY + height
				- (height / 3F));
	}

	public Point.Double getMidTopLeft() {
		return new Point.Double(posX + (width / 4F), posY + (height / 3F));
	}

	public Point.Double getBotRight() {
		return new Point.Double(posX + width - (width / 4), posY + height - 5);
	}

	public Point.Double getMidBotRight() {
		return new Point.Double(posX + width - (width / 4), posY + height
				- (height / 3F));
	}

	public Point.Double getMidTopRight() {
		return new Point.Double(posX + width - (width / 4), posY
				+ (height / 3F));
	}

	public Point.Double getTopRight() {
		return new Point.Double(posX + width - (width / 4F), posY);
	}

	/*
	 * UPDATES
	 */
	@Override
	public void update() // Main update call of all entities
	{
		if (specialDragging)
			return;
		updateAnimation();
		updatePositionWithSpeed();
	}

	/** update animation **/
	public void updateAnimation() {
		if (animations.size() > 0 && animationInUse != -1) {
			Animation a = (Animation) animations.get(animationInUse);
			a.update(SpaceGame.getInstance().getTimePassed());
		}
	}

	/** update position **/
	@Override
	public void updatePositionWithSpeed() {
		if (onGround)
			resetGravityTicks();

		if (SpaceGame.getInstance().getMap() == null) // Need our map object for
														// level constants, so
														// quit if we don't have
														// it
		{
			System.out.println("Unable to get map object! " + this
					+ " failed to continue update");
			return;
		}

		/** Entity is effected by gravity, in the air, and effected by friction **/
		if (isEffectedByGravity && !isOnGround() && !isFrictionLess) {
			deltaY += applyGravity();
			deltaX = deltaX * SpaceGame.getInstance().getMap().getAirFriction();
			gravityTicks++;
		} else if (onGround && !isFrictionLess) /**
		 * Entity is on the ground, and
		 * effected by friction
		 **/
		{
			deltaX = deltaX * SpaceGame.getInstance().getMap().getFriction();
		}

		/**
		 * clamp speed to prevent passing points of collision, and to keep
		 * things sane...
		 **/
		double clamp = 31F;
		if (deltaY > clamp)
			deltaY = clamp;
		else if (deltaY < -clamp)
			deltaY = -clamp;

		if (deltaX > clamp)
			deltaX = clamp;
		else if (deltaX < -clamp)
			deltaX = -clamp;

		/** finally update position with our speed changes **/
		posX = deltaX + posX;
		posY = deltaY + posY;

		posXi = (int) posX;
		posYi = (int) posY;
	}

	/** Apply gravity to Y speed of entity **/
	public double applyGravity() {
		/** exit if entity not effected by gravity or variables are null **/
		if (!isEffectedByGravity() || SpaceGame.getInstance().getMap() == null
				|| onGround)
			return 0F;
		double gravityPower = SpaceGame.getInstance().getMap()
				.getGravityPower();
		double gravityApplyRate = SpaceGame.getInstance().getMap()
				.getGravityApplyRate();
		return gravityPower * gravityApplyRate * gravityTicks;
	}

	public boolean isDead() {
		return isDead;
	}

	/** remove an entity from being displayable and collidable **/
	public synchronized void destroy() {
		isVisible = false;
		isDead = true;
		SpaceGame.getInstance().getMap().removeEntity(this);
	}

	public BufferedImage getImage() {
		return spacegame.GameImages.getEntityImage(this);
	}

	public int getXi() {
		return posXi;
	}

	public int getYi() {
		return posYi;
	}

	public void setGravityTicks(int i) {
		gravityTicks = i;
	}

	/** debug **/
	public String toString() {
		return entityName + " [" + posX + ", " + posY + ", " + deltaX + ", "
				+ deltaY + ", " + width + ", " + height + "]";
	}
}