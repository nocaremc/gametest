package spacegame.World;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;

import spacegame.SpaceGame;
import spacegame.Entity.Entity;
import spacegame.Entity.EntityEnemy;
import spacegame.Entity.EntityLiving;
import spacegame.Entity.EntityPlayer;
import spacegame.Entity.EntityWeapon;

/**
 * @author Nocare Separates Collision related code from Maps/entities At least
 *         to some degree. All collision is handled here, though I will be
 *         throwing specific tile collisions in their respective tile classes at
 *         some point.
 */
public class CollisionManager
{
	/**
	 * Map structure currently loaded by the game. It defines the geometry of
	 * our scan areas
	 **/
	private MapStructure map;

	/**
	 * Entities placed into this list are ONLY those currently "alive" in the
	 * game
	 **/
	private List<Entity> entities;

	/**
	 * Last time (in milliseconds) their was an attempt to initialize this class
	 **/
	private long lastInitAttempt;

	public CollisionManager()
	{
		lastInitAttempt = System.currentTimeMillis();
		initCollisionManager();
	}

	/** Need to ensure some variables are assigned, or everything falls apart, so we ensure that with recursion **/
	public void initCollisionManager()
	{
		long curTime = System.currentTimeMillis();
		long timePassed = lastInitAttempt - curTime;

		lastInitAttempt = System.currentTimeMillis();
		if (timePassed < 250) {
			try {
				Thread.sleep(251);
				map = SpaceGame.getInstance().getMap().getMapStructure();
				entities = SpaceGame.getInstance().getMap().getEntities();
			} catch (Exception e) {
				// System.out.println("Unable to sleep init collisionManager");
			}
		} else if (SpaceGame.getInstance().getMap() == null)
			initCollisionManager();

		else if (SpaceGame.getInstance().getMap().getMapStructure() == null)
			initCollisionManager();

		else if (SpaceGame.getInstance().getMap().getEntities() == null)
			initCollisionManager();

		else
		{
			map = SpaceGame.getInstance().getMap().getMapStructure();
			entities = SpaceGame.getInstance().getMap().getEntities();
		}
	}

	/**
	 * This Method's purpose is to check, and force and entity to stay within
	 * the map boundaries. It does not take into account the tile sizes when
	 * checking, but instead the maximum and minimum dimensions. Tile sizes are
	 * taken into account when moving a violating entity however, to help make
	 * sure and entity is not inside bounding Tiles
	 * 
	 * @param entity
	 */
	private void ensureInBounds(Entity entity)
	{
		Rectangle bounds = SpaceGame.getInstance().getMap().getBounds();
		if (!bounds.contains(entity.getBotCenter())) // if within bounds, skip
		{
			entity.setColliding(true);
			if (entity.getBotLeft().x < bounds.x)// Too far left
				entity.setX(bounds.x + (entity.getWidth() / 4)
						+ map.getTileWidth());

			if (entity.getBotRight().x > bounds.width)// Too far right
				entity.setX(bounds.width + entity.getX()
						- entity.getBotRight().x);

			if (entity.getY() < bounds.y) // Too high
			{
				entity.setY(bounds.y);
				if (entity instanceof EntityLiving) // If a living entity, reset
													// the value of its jump and
													// Y speed
				{
					@SuppressWarnings("unused")
					EntityLiving pl = (EntityLiving) entity;
					entity.setOnGround(false);
				}
			}

			if (entity.getY() + entity.getHeight() > bounds.height) // Too low
			{
				entity.setY(bounds.height - entity.getHeight()
						- map.getTileHeight());
				entity.setOnGround(true);
			}
		}
	}

	/** Check all entities for Terrain Collision **/
	public void checkPossibleCollisions() {
		if (entities.size() < 1) // no entities to check for collision, exit.
			return;

		/**
		 * For all the entities, check for possible collisions, but first ensure
		 * its within map bounds
		 **/
		for (int x = 0; x < entities.size(); x++) {
			ensureInBounds(entities.get(x));
			if (entities.get(x) instanceof Entity)
				checkPossibleTerrainCollisions((Entity) entities.get(x));
		}
	}

	/**
	 * Check an entity for collision against current level terrain, and handle.
	 * TODO: Move collision handling into respective tile files
	 * 
	 * @param entity
	 */
	public void checkPossibleTerrainCollisions(Entity entity)
	{
		/** Get a Bounding box based on position, and range of motion **/
		Rectangle.Double AABB = new Rectangle.Double(entity.getX(),
				entity.getY(), entity.getWidth(), entity.getHeight());

		/** get max and min ranges **/
		Point tileRangeMin = getTileAt(AABB.getX(), AABB.getY());
		Point tileRangeMax = getTileAt(AABB.getX() + AABB.getWidth(),
				AABB.getY() + AABB.getHeight());

		/**
		 * determine which point is our high and low end, since we use a
		 * Cartesian coordinate system.
		 **/
		double minX = (tileRangeMax.getX() > tileRangeMin.getX()) ? tileRangeMin.getX() : tileRangeMax.getX();
		double maxX = (tileRangeMin.getX() < tileRangeMax.getX()) ? tileRangeMax.getX() : tileRangeMin.getX();
		double minY = (tileRangeMax.getY() > tileRangeMin.getY()) ? tileRangeMin.getY() : tileRangeMax.getY();
		double maxY = (tileRangeMin.getY() < tileRangeMax.getY()) ? tileRangeMax.getY() : tileRangeMin.getY();

		entity.setOnGround(false);

		/** Scan for non-air tiles and add to list **/
		for (double x = minX; x <= maxX; x++) {
			for (double y = minY; y <= maxY; y++) {
				int type = getTileTypeAt(x * map.getTileWidth(),
						y * map.getTileHeight());
				double tx = x * map.getTileWidth();
				double ty = y * map.getTileHeight();
				switch (type)
				{
					case 1: // ground
						if (entity.getBotCenter().getY() >= ty)
						{
							entity.setOnGround(true);
							entity.setY(ty - entity.getHeight());// + (entity.getY()+entity.getHeight() - entity.getBotCenter().getY()));
						}
						else if (entity.getTopLeft().getY() <= ty + map.getTileHeight() && entity.getMidBotLeft().getY() >= ty + map.getTileHeight())
						{
							entity.setY(ty + map.getTileHeight() + 1);
							entity.setDeltaY(1);
						}
						entity.setOnGround(true);
						break;
	
					case 3: // stair left ->
						if (entity.getBotCenter().getX() >= tx && entity.getBotCenter().getX() <= tx + map.getTileWidth())
						{
							entity.setOnGround(true);
							double max = tx + map.getTileWidth();
							double entX = max - entity.getBotCenter().getX();
							entity.setY(ty - entity.getHeight() + entX);
						}
						break;
	
					case 4: // stair left inner
						entity.setOnGround(true);
						entity.setY(entity.getY() - map.getTileHeight() / 20);
						break;
	
					case 5: // wall-left
						if (entity.getBotLeft().getX() <= tx + map.getTileWidth()) {
							entity.setX(tx + map.getTileWidth() - entity.getWidth()
									/ 4);
						}
						break;
	
					case 7: // wall-right
						if (entity.getBotRight().getX() >= tx) {
							entity.setX(tx - (entity.getWidth() / 4) * 3);
						}
						break;
	
					case 10: // stair right <-
						if (entity.getBotCenter().getX() >= tx
								&& entity.getBotCenter().getX() <= tx
										+ map.getTileWidth()) {
							entity.setOnGround(true);
							double max = tx;
							double entX = entity.getBotCenter().getX() - max;
							entity.setY(ty - entity.getHeight() + entX);
						}
					case 6: // stair left inner
						entity.setOnGround(true);
						entity.setY(entity.getY() - map.getTileHeight() / 19);
						break;
	
					case 42: // On a teleport tile, have the level handle it
						if (entity instanceof EntityPlayer)
							SpaceGame.getInstance().getMap().handleTeleport(new Point.Double(x, y));
	
					default:
						entity.setOnGround(false);
				}
			}
		}
	}

	/** Check for player to enemy collision **/
	public void checkPlayerEnemycollisions() {
		if (entities.size() < 1) // no entities to check for collision, exit.
			return;

		for (int x = 0; x < entities.size(); x++) {
			if (entities.get(x) instanceof EntityEnemy)
				checkForEntityCollision((Entity) entities.get(x));
		}
	}

	/**
	 * Check a given Weapon instance against entities to detect possible
	 * collisions Weapon's Damage instance handles the collision
	 * 
	 * @param e
	 */
	public void checkWeaponEntityCollisions(EntityWeapon e) {
		if (entities.size() < 1) // no entities to check for collision, exit.
			return;

		for (int x = 0; x < entities.size(); x++) {
			if (entities.get(x) instanceof EntityPlayer)
				continue;

			if (e != entities.get(x) && entities.get(x) instanceof EntityLiving)
				if (e.getBoundingBox().intersects(
						entities.get(x).getBoundingBox()))
					if (spritesCollide(e, entities.get(x)))
						e.getTouchDamage().calcDamage(e,
								(EntityLiving) entities.get(x));
		}
	}

	/**
	 * Check if a given Entity intersects with the player, and apply the
	 * entity's "touch damage" if applicable.
	 * 
	 * @param entity
	 */
	private void checkForEntityCollision(Entity entity) {
		/** if enemy and player bounding boxes intersect **/
		EntityPlayer pl = SpaceGame.getInstance().getMap().getPlayer();
		EntityEnemy ee = (EntityEnemy) entity;
		if (ee.getBoundingBox().intersects(pl.getBoundingBox())) {
			/**
			 * bounding boxes make contact, but that is not refined at all, and
			 * we hate ghost collisions! Now we'll do a second check to see if
			 * the sprite images overlap
			 **/
			if (spritesCollide(ee, pl))
				ee.getTouchDamage().calcDamage(ee, pl); // Okay, pass into
														// Damage and calculate
														// damage
		}
	}

	/**
	 * This function compares two images to see if they overlap. I've got
	 * Trolliolli of CodeCall forums to thank for shareing the code I learned
	 * this from The original implementation uses a String hash set, so the
	 * value will still be obtainable I found that to be slow...
	 * 
	 * @param ee
	 * @param pl
	 */
	private boolean spritesCollide(Entity ee, Entity pl) {
		HashSet<Integer> Hee = getMask(ee);
		HashSet<Integer> Hpl = getMask(pl);

		/**
		 * if either are null, we can't proceed without producing a NPE, so exit
		 * this method
		 **/
		if (Hee == null || Hpl == null || ee.isDead() || pl.isDead())
			return false;

		/**
		 * Empty the HashSet, keeping only duplicate pixel locations between the
		 * two entities
		 **/
		Hee.retainAll(Hpl);
		if (Hee.size() > 0) // if true, we've got a collision
			return true;
		else
			return false;

	}

	/**
	 * Trolliolli of CodeCall forums wrote the original version of this code.
	 * Over time with a lot of testing I did modify it quite a bit. Originally
	 * it returned a string, which could in turn be used to find the actual
	 * point of collision.
	 * 
	 * With over 10 entities on the screen I found that to be brutally intensive
	 * on the processor, and so I played around with multiple data types, and
	 * ended with using an Integer. I use both the x,y of image location +
	 * entity location, and create a hash using that data. It's not particularly
	 * a complex method, but I find the likelyhood of matching hashes, without
	 * matching pixels to be unlikely, and it brings the hardware usage on this
	 * function to be really quite low.
	 * 
	 * @param e
	 * @return HashSet
	 */
	private HashSet<Integer> getMask(Entity e) {
		HashSet<Integer> mask = new HashSet<Integer>();
		BufferedImage im = null;

		/** get image entity is currently using **/
		if (e.getAnimation() != null && e.getAnimation().getClass() != null)
			im = (BufferedImage) e.getAnimation().getImage();
		else {
			return null;
		}

		/** iterate over image and collect pixel data **/
		int pixel, alpha;
		for (int a = 0; a < im.getWidth(); a++) {
			for (int b = 0; b < im.getHeight(); b++) {
				pixel = im.getRGB(a, b);
				alpha = (pixel >> 24) & 0xFF; // Shift pixel rgb to get the
												// alpha
				if (alpha != 0) {
					/**
					 * add any pixels that are not transparent (alpha 0) to a
					 * hashset, modifying it with its location in the world
					 **/
					// mask.add((e.getX()+a)+","+(e.getY()+b));
					int tmp[] = { (int) (e.getX() + a + e.getY() + b) };
					mask.add(java.util.Arrays.hashCode(tmp));
				}
			}
		}
		return mask;
	}

	/**
	 * Add an entity to this
	 * 
	 * @param wm
	 */
	public void addEntity(Entity wm) {
		entities.add(wm);
	}

	/**
	 * Remove an entity from this
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Integer Tile Type at given X,Y
	 */
	public int getTileTypeAt(double x, double y) {
		x /= map.getTileWidth();
		y /= map.getTileHeight();

		int xa = (int) Math.floor(x);
		int ya = (int) Math.floor(y);

		if (ya < map.getMap().length && ya > -1)
			if (xa < map.getMap()[ya].length && xa > -1)
				return map.getMap()[ya][xa];

		return 0;
	}

	/**
	 * 
	 * @param g
	 * @return Integer Tile Type at given Point
	 */
	public int getTileTypeAt(Point.Double g) {
		double x = g.getX() / map.getTileWidth();
		double y = g.getY() / map.getTileHeight();

		int xa = (int) Math.floor(x);
		int ya = (int) Math.floor(y);

		if (ya < map.getMap().length && ya > -1)
			if (xa < map.getMap()[ya].length && xa > -1)
				return map.getMap()[ya][xa];

		return 0;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Point containing TileUnit coordinates that correspond with a
	 *         given x,y
	 */
	public Point getTileAt(double x, double y) {
		x /= map.getTileWidth();
		y /= map.getTileHeight();

		x = (int) Math.floor(x);
		y = (int) Math.floor(y);

		return new Point((int) x, (int) y);
	}

	/**
	 * 
	 * @param g
	 * @return Point containing TileUnit coordinates that correspond with a
	 *         given point
	 */
	public Point getTileAt(Point.Double g) {
		double x = g.getX() / map.getTileWidth();
		double y = g.getY() / map.getTileHeight();

		x = (int) Math.floor(x);
		y = (int) Math.floor(y);

		return new Point((int) x, (int) y);
	}
}