package spacegame.AI;

import java.awt.Point;
import java.util.Random;

import spacegame.SpaceGame;
import spacegame.Entity.*;
import spacegame.Entity.EntityLiving.Facing;
import spacegame.World.MapStructure;

/**
 * @author Nocare This Class allows an enemy to determine where they will walk
 */
public class WalkAI extends EnemyAI {
	private Point.Double lastPosition;
	private int stuckCounter = 0; // Counter used to determine if this entity
									// has been stuck

	private long lastLookTime;
	private boolean doJump;
	private long jumpIntervalMin, lastJump;
	private Random rand;

	public WalkAI(EntityLiving el) {
		this.el = el;
		lastPosition = el.getBotCenter();
		lastLookTime = lastJump = System.currentTimeMillis();
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
	}

	public void seedRand(Random rand) {
		this.rand = new Random(rand.nextLong());
	}

	/**
	 * Looks for walls and reverses direction if needed. Uses Facing of
	 * EntityLiving
	 **/
	public EntityLiving.Facing getHorizontalDirection() {
		double x = lastPosition.getX() - el.getBotCenter().getX();
		@SuppressWarnings("unused")
		double y = lastPosition.getY() - el.getBotCenter().getY();

		if (!(x > 2 || x < -2)) // entity did not move
			stuckCounter++;
		else
			stuckCounter--;

		if (stuckCounter > 300) // Entity has been stuck at least 5 seconds
		{
			SpaceGame.getInstance().getMap().removeEntity(el);
		}

		lastPosition = el.getBotCenter();

		// Try to jump if enabled
		if (doJump && System.currentTimeMillis() - lastJump > jumpIntervalMin) {
			int i = rand.nextInt() / 100000;
			if (i < jumpIntervalMin && i > 0 && el.isOnGround()) {
				el.setDeltaY(-el.getStats().getJumpPower());
			}
			lastJump = System.currentTimeMillis();
		}

		// Check for a wall and reverse direction if needed
		boolean isAWall = findWallAtDirection(el.getFacing());
		if (isAWall) // wall in the way, so reverse
		{
			if (el.getFacing() == EntityLiving.Facing.RIGHT) {
				el.setDeltaX(el.getStats().getRunSpeed() * -1F);
				return EntityLiving.Facing.LEFT;
			} else {
				el.setDeltaX(el.getStats().getRunSpeed());
				return EntityLiving.Facing.RIGHT;
			}
		} else // No walls, so lets check for a floor and reverse if neccissary
		{
			boolean isFloor = findFloor(el.getFacing());
			if (!isFloor) // no floor, so reverse
			{
				if (el.getFacing() == EntityLiving.Facing.RIGHT) {
					el.setDeltaX(el.getStats().getRunSpeed() * -1F);
					return EntityLiving.Facing.LEFT;
				} else {
					el.setDeltaX(el.getStats().getRunSpeed());
					return EntityLiving.Facing.RIGHT;
				}
			} else {
				switch (el.getFacing()) {
				case RIGHT:
					el.setDeltaX(el.getStats().getRunSpeed());
					break;
				case LEFT:
					el.setDeltaX(el.getStats().getRunSpeed() * -1F);
					break;
				}
				return el.getFacing();
			}
		}
	}

	/** checks 1 tile in front of the direction the entity is facing for a wall **/
	private boolean findWallAtDirection(Facing facing) {
		/** x area to match to a tile **/
		double lookX;
		/** tile we are positioned at **/
		int currentTileX;
		int currentTileY;
		/** need to compensate for entities width if facing right **/
		if (el.getFacing() == EntityLiving.Facing.RIGHT) {
			lookX = el.getX() + el.getWidth();
		} else {
			lookX = el.getX();
		}

		currentTileX = (int) Math.round(lookX
				/ SpaceGame.getInstance().getMap().getMapStructure()
						.getTileWidth());
		currentTileY = (int) Math.round(el.getY()
				/ SpaceGame.getInstance().getMap().getMapStructure()
						.getTileHeight() - 1);

		/**
		 * Search out of ranges if (el.getFacing() == EntityLiving.Facing.RIGHT)
		 * { searchTile = currentTileX; if (searchTile >
		 * SpaceGame.getInstance().getMap().getMapStructure().getWidth())
		 * System.out.println("Searching too far right"); } else { searchTile =
		 * currentTileX; if (searchTile < 0)
		 * System.out.println("Searching too far left"); }
		 **/

		int[][] map = SpaceGame.getInstance().getMap().getMapStructure()
				.getMap();
		MapStructure ms = SpaceGame.getInstance().getMap().getMapStructure();
		/** if index out of bounds, return null **/
		if (ms.getHeight() - 1 < currentTileY || currentTileY < 0)
			return false;
		else if (ms.getWidth() - 1 < currentTileX || currentTileX < 0)
			return false;

		/** return true if not air **/
		return map[currentTileY][currentTileX] == 5
				|| map[currentTileY][currentTileX] == 7;
	}

	/** looks for a floor below the entity **/
	@SuppressWarnings("unused")
	public boolean findFloor(EntityLiving.Facing dir) {
		if (System.currentTimeMillis() - lastLookTime < 5000)
			return true;

		lastLookTime = System.currentTimeMillis();

		/** x area to match to a tile **/
		Point.Double look;
		/** tile we are positioned at **/
		int currentTileX;
		int currentTileY;
		/** need to compensate for entities width if facing right **/
		if (el.getFacing() == EntityLiving.Facing.RIGHT) {
			look = new Point.Double(el.getX() + el.getWidth(), el.getY()
					+ el.getHeight() + 60);
		} else {
			look = new Point.Double(el.getX(), el.getY() + el.getHeight() + 60);
		}

		return SpaceGame.getInstance().getMap().getCollisionManager()
				.getTileTypeAt(look.x, look.y) != 0;

		/**
		 * Searching out of map bounds if (el.getFacing() ==
		 * EntityLiving.Facing.RIGHT) { searchTile = currentTileX; if
		 * (searchTile >
		 * SpaceGame.getInstance().getMap().getMapStructure().getWidth())
		 * System.out.println("Searching too far right"); } else { searchTile =
		 * currentTileX; if (searchTile < 0)
		 * System.out.println("Searching too far left"); }
		 **/

		/** return true if not air **/
		// return true;
	}

	/** Sets jumping occasionally to true, and the minimum interval **/
	public void setPeriodicJump(long i) {
		doJump = true;
		jumpIntervalMin = i;
	}

}