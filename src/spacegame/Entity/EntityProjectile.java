package spacegame.Entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import spacegame.SpaceGame;
import spacegame.Util.Vector2d;

public class EntityProjectile extends EntityWeapon {

	protected long displayLife = 1000L; // How long will this object remain
										// visible and collidable? TODO: Good
										// range limiter
	private long creationTime; // Time this entity was created
	private boolean flagArrived = false; // Whether or not the entity has
											// reached its target
	private double theta = 0; // angle projectile is traveling
	private Vector2d target = null; // Target of the projectile

	public EntityProjectile(EntityLiving parent, double posX, double posY)
	{
		super(parent, posX, posY);
		creationTime = System.nanoTime(); // Create timestamp for entity
											// instance
		isFrictionLess = true; // Don't want to apply friction or gravity
		isEffectedByGravity = false; // Covered by above boolean, but so its
										// clear...
		onGround = false; // If this entity touches the ground, it dies, so set
							// this to false
	}

	@Override
	public void update()
	{
		long curTime = System.nanoTime();
		long timePassed = (curTime - creationTime) / 1000000L; // Update time and check if its life has been too long
		if (timePassed > displayLife || isDead || isColliding || onGround)
		{
			super.destroy(); // delete this entity
			return;
		}

		calculateVelocity(); // Determine the speed to set this entity traveling
		super.update();
	}

	/**
	 * Calculates and sets the DeltaX and DeltaY of entity, based on it's target
	 * location
	 **/
	private void calculateVelocity() {
		if (target == null) // No target, no continue
			return;

		Vector2d position = new Vector2d(posX, posY); // Current position
		/**
		 * Get distance distance between origin and target location, and reverse
		 * the sign since it seems backwards
		 **/
		double x = (position.getX() - target.getX()) * -1;
		double y = (position.getY() - target.getY()) * -1;
		theta = Math.atan2(y, x); // Find angle between x and y TODO: replace
									// with dot function at some point. uses
									// less math
		// create vector with destination (in Cartesian coordinates)
		Vector2d velocity = new Vector2d(Math.cos(theta), Math.sin(theta));
		velocity.normalise(null); // Normalize vector so its 1-length, and
									// usable for simple speed scaling;

		/**
		 * If projectile has arrived at mouse location, lets preserve last
		 * speed, otherwise, keep buggering on
		 **/
		if (!(x < targetRadius && y < targetRadius && x > -targetRadius && y > -targetRadius)
				&& !flagArrived)
			setSpeed(velocity.getX() * speed, velocity.getY() * speed);
		else {
			setSpeed(deltaX, deltaY);
			flagArrived = true;
		}
	}

	/**
	 * set the destination of the entity's path. Will be used to keep movement
	 * speed on track at current speed
	 **/
	public void setDestination(Entity e, Point dest, double targetRadius) {
		this.targetRadius = targetRadius;
		/**
		 * offset the target (mouse location) by the player's world position
		 * Since player is locked to center of screen, subtract the center
		 * finally, account for height of projectile, in order to place it in
		 * the center of mouseY
		 */
		target = new Vector2d((e.getX() + dest.getX() - SpaceGame.getInstance()
				.getWidth() / 2), (e.getY() + dest.getY() - SpaceGame
				.getInstance().getHeight() / 2) + height / 2);

		/**
		 * Final check if the player projecting this, on the direction he/she is
		 * facing
		 **/
		if (e instanceof EntityLiving) {
			EntityLiving el = (EntityLiving) e;
			if (target.getX() < el.getX()
					&& el.getFacing() == EntityLiving.Facing.RIGHT)
				el.setFacing(EntityLiving.Facing.LEFT);
			else if (target.getX() > el.getX()
					&& el.getFacing() == EntityLiving.Facing.LEFT)
				el.setFacing(EntityLiving.Facing.RIGHT);
		}
	}

	/** draw to screen **/
	public void pack() {
		Graphics2D g = SpaceGame.getInstance().getGraphics();
		if (getAnimation() == null || getAnimation().getImage() == null)
			return;
		g.drawImage(getAnimation().getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH), posXi, posYi, null);
		/**
		 * DEBUG g.setColor(Color.magenta); g.draw(getBoundingBox());
		 * g.drawString(deltaX+" "+deltaY, posX, posY-10); int r = 6;
		 * g.setColor(Color.cyan); g.fillOval((int)getTopLeft().getX(),
		 * (int)getTopLeft().getY(), r, r);
		 **/
	}
}