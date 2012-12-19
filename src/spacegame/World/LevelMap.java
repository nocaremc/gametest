package spacegame.World;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import spacegame.InputManager;
import spacegame.SpaceGame;
import spacegame.Entity.Entity;
import spacegame.Entity.EntityEnemy;
import spacegame.Entity.EntityLiving;
import spacegame.Entity.EntityPlayer;
import spacegame.Entity.EntityProjectile;
import spacegame.Entity.EntityWeapon;
import spacegame.Gui.GuiHUD;

/**
 * @author Nocare This Class contains all elements of a particular level.
 *         particularly, it will contain information regarding collidable
 *         objects, level constants, enemies to spawn, forces, and geometry and
 *         design of the level to load
 */
public abstract class LevelMap 
{
	/** Create Player **/
	protected EntityPlayer entityPlayer;
	
	/** List of entities currently active on level **/
	protected java.util.List<Entity> activeEntities = new ArrayList<Entity>();
	
	/** List of entities that can be spawned into the level **/
	protected java.util.List<Class<? extends EntityLiving>> entitiesForSpawn = new ArrayList<Class<? extends EntityLiving>>();
	
	/** Areas to spawn new entities **/
	protected java.util.List<RectangleZone> spawnZones = new ArrayList<RectangleZone>();
	
	
	private long lastSpawnTime, lastSpawnClearTime;
	protected long spawnFrequency;
	protected int creatureSpawnRangeMin, creatureSpawnRangeMax;
	protected Graphics2D g;
	protected InputManager input;
	protected CollisionManager watchDog; // Collision manager
	private Thread collisionThread;
	protected GuiHUD guiHUD; // heads up display gui

	protected boolean hasInputFocus = true; // Does this screen have input focus? Toggled during pauses,etc

	protected double gravityPower; // Power of Gravity
	protected double gravityApplyRate; // Rate gravity is applied (scales gravityPower)
	protected int gravityAppliedTicks; // # of times gravity has been applied (multiplies gravityPower)
	protected double friction; // Power of friction
	protected double airFriction; // power of air friction
	protected int tileSizeX = 60, tileSizeY = 60; // Size of tiles, in pixels
	protected int minX, minY, maxX, maxY; // Map Dimensions

	private MapStructure map; // Structure of map. Size of tiles, # of them on each axis, and what types at specific locations
	private long lastMapChange;
	private long lastUpdate;

	private boolean drawGridEnabled = false; // Whethor or not to draw a grid to represent tiles (debug)
	private boolean isInit; // is the map running?
	private boolean playerMovementLocked; // Is player input locked?

	protected double cameraOffsetX, cameraOffsetY; // Camera offset
	private Random rand = new Random();

	protected int[][] thisMap = { // Default design of map, generic. Passed into map structure if no map exists in a level
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	private Thread mapThread;

	/**
	 * Constructor
	 * 
	 * @param gravityPower
	 * @param gravityApplyRate
	 * @param friction
	 * @param airFriction
	 **/
	public LevelMap(double gravityPower, double gravityApplyRate, double friction, double airFriction)
	{
		this.gravityPower = gravityPower;
		this.gravityApplyRate = gravityApplyRate;
		this.friction = friction;
		this.airFriction = airFriction;
		spawnFrequency = 20000L;
		input = SpaceGame.getInstance().getInput();
		setMapDimensions(0, 0, 1440, 900);
		tileSizeX = tileSizeY = 60;
		setMapStructure(1440 / tileSizeX, 900 / tileSizeY, tileSizeX, tileSizeY);
		// entityPlayer.setScale(0.5F); //Scale player
		guiHUD = new GuiHUD(0, 0, SpaceGame.getInstance().getWidth(), SpaceGame.getInstance().getHeight());
	}

	/**
	 * Init map Set up collision manager so it exists when its called for
	 * Initially spawn a bunch of entities
	 */
	public void mapInit() 
	{
		if (entityPlayer == null)
			entityPlayer = new EntityPlayer(0, 0, 60, 120, 100, 100);
		
		getEntities().add(entityPlayer); // add player to entities list
		//watchDog = new CollisionManager();
		collisionThread = new Thread("Collision Manager Thread") {
			@Override
			public void run() {
				watchDog = new CollisionManager();
			}
		};
		collisionThread.start();

		isInit = true;
		spawnAllEntities();
		lastMapChange = System.currentTimeMillis();
	}

	private void spawnAllEntities()
	{
		// Get a random value of the difference of our spawn range
		int range = (creatureSpawnRangeMax - creatureSpawnRangeMin > 0) ? creatureSpawnRangeMax - creatureSpawnRangeMin : 1;
		int randomRange = rand.nextInt(range);
		randomRange = randomRange + creatureSpawnRangeMin;

		for (int x = 0; x <= randomRange; x++)
		{
			Class<?> randEntity = getRandomEntity();
			Point randSpawnLocation = getRandomSpawnLocation(0);
			
			if (randSpawnLocation == null)
			{
				System.out.println("No Spawn Location, cannot spawn entities");
				return;
			}
			else if (randEntity == null)
			{
				System.out.println("No entities to spawn, quitting");
				return;
			}
			
			spawnEntity(randEntity, (int) randSpawnLocation.getX(), (int) randSpawnLocation.getY());
		}

		lastSpawnTime = lastSpawnClearTime = System.currentTimeMillis();
	}

	/** Returns a random entity from entitiesForSpawn list **/
	private Class<?> getRandomEntity()
	{
		if (entitiesForSpawn.size() < 1) // Nothing in list, cannot proceed
		{
			System.out.println("Unable to get random entity, list is empty");
			return null;
		}

		int randomIndex = rand.nextInt((entitiesForSpawn.size()));
		
		/** If random number is out of index, retry **/
		if (entitiesForSpawn.get(randomIndex) == null)
			return getRandomEntity();

		return entitiesForSpawn.get(randomIndex);
	}

	/** Returns a random X,Y pair from a random selection from spawnZones **/
	private Point getRandomSpawnLocation(int recursions) {
		if (spawnZones.size() < 1) // Nothing in list, cannot proceed
		{
			System.out.println("Unable to get random spawnZone, list is empty");
			return null;
		}

		int randomIndex = rand.nextInt(spawnZones.size());

		if (spawnZones.get(randomIndex) == null || spawnZones.get(randomIndex).getCount() >= spawnZones.get(randomIndex).getCap())
			if (recursions < spawnZones.size())
				return getRandomSpawnLocation(recursions + 1);

		RectangleZone randomZone = spawnZones.get(randomIndex);

		int randomX = (int) (rand.nextInt((int) (randomZone.getWidth())) + randomZone.getX()); // Random location within spawn zone
		int randomY = (int) (rand.nextInt((int) (randomZone.getHeight())) + randomZone.getY());

		randomZone.setCount(randomZone.getCount() + 1);
		return new Point(randomX, randomY);
	}

	/**
	 * Spawns an entity in the world
	 * 
	 * @param entity
	 * @param locationX
	 * @param locationY
	 *            NOTE: Entity must have only position parameters in
	 *            constructor..
	 */
	private void spawnEntity(Class<?> entity, int locationX, int locationY)
	{
		Object wm;
		try {
			/**
			 * instantiate entity, since this is a generic approach where we
			 * don't know the class name, we have this...
			 **/
			wm = (Object) entity.getConstructor(
					new Class<?>[] { Double.TYPE, Double.TYPE, Random.class })
					.newInstance(
							new Object[] { locationX, locationY,
									SpaceGame.getInstance().getRandom() });

			getEntities().add((Entity) wm);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** Spawns a random entity at a random location at every interval **/
	private void checkAndSpawnNewEntities() {
		if (System.currentTimeMillis() - lastSpawnTime < spawnFrequency) // Too
																			// early
																			// to
																			// spawn
																			// new
																			// entity
			return;
		if (getEntities().size() >= creatureSpawnRangeMax) // Too many mobs on
															// map to be
															// spawning more
			return;

		Class<?> randEntity = getRandomEntity();
		Point randSpawnLocation = getRandomSpawnLocation(0);
		spawnEntity(randEntity, (int) randSpawnLocation.getX(),
				(int) randSpawnLocation.getY());
		lastSpawnTime = System.currentTimeMillis();
	}

	/** Draw map to screen **/
	public void pack() {
		if (!isInit())
			return;
		g = SpaceGame.getInstance().getGraphics();
		cameraOffsetX = 0.0F;
		cameraOffsetY = 0.0F;
		cameraOffsetX = entityPlayer.getX()
				- (SpaceGame.getInstance().getWidth() / 2)
				+ (entityPlayer.getWidth() / 2);
		cameraOffsetY = entityPlayer.getY()
				- (SpaceGame.getInstance().getHeight() / 2)
				+ (entityPlayer.getHeight() / 2);

		/**
		 * Translate the whole graphics call based on player position, this
		 * moves the map, giving the illusion the player moves
		 **/
		g.translate(-cameraOffsetX, -cameraOffsetY);
		getMapStructure().pack(g, isDrawGridEnabled());
		for (int x = 0; x < getEntities().size(); x++) {
			if (getEntities().get(x) instanceof EntityEnemy) {
				EntityEnemy e = (EntityEnemy) getEntities().get(x);
				e.pack();
			}
			if (getEntities().get(x) instanceof EntityProjectile) {
				EntityProjectile e = (EntityProjectile) getEntities().get(x);
				e.pack();
			}
		}
		g.translate(cameraOffsetX, cameraOffsetY); // Translate back
		entityPlayer.pack(); // Draw player
		guiHUD.pack();
	}

	/**
	 * update elements of the level. player, position/movement, enemies,
	 * non-living objects, detect and handle collisions, etc
	 **/
	public void update() {
		if (!isInit())
			return;
		
		/** Sleep the thread if the last update was less than 30 milliseconds. This generates a bit of latency, but helps to
		 * create less of a difference between levels/situations where much lag is present, and very little is
		 */
		if (System.currentTimeMillis() - lastUpdate < 50)
		{
			try
			{
				Thread.sleep(50L - (System.currentTimeMillis() - lastUpdate));
			}
			catch(Exception e){e.printStackTrace();}
		}
		lastUpdate = System.currentTimeMillis();
		
		entityPlayer.update();
		if (!playerMovementLocked)
			entityPlayer.handleInput(hasInputFocus());

		for (int x = 0; x < getEntities().size(); x++) {
			if (x >= getEntities().size())
				return;
			if (getEntities().get(x) instanceof EntityEnemy) // Update monsters
			{
				EntityEnemy e = (EntityEnemy) getEntities().get(x);
				e.update();
			}

			if (x >= getEntities().size())
				return;

			if (getEntities().get(x) instanceof EntityProjectile) // update
																	// projectiles
			{
				EntityProjectile e = (EntityProjectile) getEntities().get(x);
				e.update();
			}
		}

		if (entityPlayer.isOnGround())// During knock back the player cannot
										// move, if they hit the ground we allow
										// movement again.
			setMovementLock(false);

		checkCollision();

		if (System.currentTimeMillis() - lastSpawnClearTime > 60000L) // If its
																		// been
																		// a
																		// minute,
																		// reset
																		// the
																		// entity
																		// counters
																		// on
																		// spawn
																		// regions.
		{
			for (int x = 0; x < spawnZones.size(); x++) {
				spawnZones.get(x).setCount(0);
			}
		}

		checkAndSpawnNewEntities(); // spawn new entities in zones with space
									// and timing
	}

	public void checkCollision() {
		watchDog.checkPossibleCollisions(); // Check for entity-terrain
											// collision
		watchDog.checkPlayerEnemycollisions(); // check for player-entity
												// collision

		for (int x = 0; x < getEntities().size(); x++) {
			if (getEntities().get(x) instanceof EntityWeapon)
				watchDog.checkWeaponEntityCollisions((EntityWeapon) getEntities()
						.get(x)); // check for weapon-entity collision
		}
	}

	/**
	 * set and get for input focus. Used to switch input handling around without
	 * adding/removing seperate key/mouse listeners.
	 **/
	public void setInputFocus(boolean flag) {
		hasInputFocus = flag;
	}

	/**
	 * Does this screen have input focus of the player? IE: Game is paused, we
	 * don't want to handle input on the player/map/level
	 * 
	 * @return boolean
	 */
	public boolean hasInputFocus() {
		return hasInputFocus;
	}

	/**
	 * get map SUPER bounds. This is the absolute bounding box of the level, and
	 * is used as a fail-safe in collision Entities should never be here, but we
	 * check anyhow.
	 * 
	 * @return Rectangle
	 */
	public Rectangle getBounds() {
		return new Rectangle(minX, minY, maxX, maxY);
	}

	/** set map bounding box **/
	public void setMapDimensions(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	/** get the camera offset **/
	public Point.Double getCameraOffset() {
		return new Point.Double(cameraOffsetX, cameraOffsetY);
	}

	/**
	 * Get player entity Placing this here makes the most sense, however
	 * accessing it is a bit of a pain...
	 * 
	 * @return EntityPlayer
	 */
	public EntityPlayer getPlayer() {
		return entityPlayer;
	}

	public void setPlayer(EntityPlayer pl) {
		entityPlayer = pl;
	}

	/** get or set map frictions **/
	public double getFriction() {
		return friction;
	}

	public void setFriction(double friction) {
		this.friction = friction;
	}

	public double getAirFriction() {
		return airFriction;
	}

	public void setAirFriction(double airFriction) {
		this.airFriction = airFriction;
	}

	/** get/set gravity variables **/
	public double getGravityPower() {
		return gravityPower;
	}

	public void setGravityPower(double gravityPower) {
		this.gravityPower = gravityPower;
	}

	public double getGravityApplyRate() {
		return gravityApplyRate;
	}

	public void setGravityApplyRate(double gravityApplyRate) {
		this.gravityApplyRate = gravityApplyRate;
	}

	/** enable/disable drawing tile grid **/
	public boolean isDrawGridEnabled() {
		return drawGridEnabled;
	}

	public void setDrawGridEnabled(boolean drawGridEnabled) {
		this.drawGridEnabled = drawGridEnabled;
	}

	/** get set map structure **/
	public MapStructure getMapStructure() {
		return map;
	}

	public void setMapStructure(int w, int h, int tWidth, int tHeight) {
		mapThread = new Thread("MapStructure Thread") {
			@Override
			public void run() {
				map = new MapStructure();
			}
		};
		mapThread.start();
		
		/** Wait until map thread is instanced to set its variables **/
		while (map == null)
		{
			try
			{
				Thread.sleep(SpaceGame.getInstance().getUpdatePeriod() / 1000000L);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	
		
		if (map == null)
		{
			try {
				throw new UnexpectedException("LevelMap could not Set \"map\" field. variables setting before class instantiated.");
			} catch (Exception e) {e.printStackTrace();}
		}
		
		map.setVariables(w, h, tWidth, tHeight);
		map.initMap();
	}

	/** dont allow human to move player **/
	public void setMovementLock(boolean b) {
		playerMovementLocked = b;
	}

	/** spawn an entity on the map **/
	public synchronized void spawnEntity(Entity wm) {
		activeEntities.add(wm);
		watchDog.addEntity(wm);
	}

	/**
	 * removes an entity from entities list in map, as well as in collision
	 * manager
	 **/
	public synchronized void removeEntity(Entity entity) {
		for (int x = 0; x < spawnZones.size(); x++) // Attempt to let the spawn
													// zone decrement it's
													// count, not perfect since
													// entities are rather free
													// to roam
		{
			if (spawnZones.get(x).contains(entity.getBoundingBox())
					|| spawnZones.get(x).intersects(entity.getBoundingBox()))
				spawnZones.get(x).setCount(spawnZones.get(x).getCount() - 1);
		}

		activeEntities.remove(entity);
		watchDog.removeEntity(entity);
	}

	/** return array list of entities on the level **/
	public List<Entity> getEntities() {
		return activeEntities;
	}

	/** is this class running, and has first execution passed a certain point? **/
	public boolean isInit() {
		return isInit;
	}

	public GuiHUD getHud() {
		return guiHUD;
	}

	/** Add an entity to list of spawnable entities **/
	public void addEntityForSpawn(EntityLiving entity) {
		Class<? extends EntityLiving> c = entity.getClass();
		if (c != null)
			entitiesForSpawn.add(c);
		else
			System.out.println("Cannot add entity: " + entity
					+ " to spawn list!");

	}

	/**
	 * Add a new spawn zone
	 * 
	 * @param i - max number of entities in zone
	 * @param r - Rectangle region of zone
	 **/
	public void addSpawnZone(Rectangle r, int i) {
		if (r == null) {
			System.out.println("Null spawn zone trying to be added");
		}

		RectangleZone zone = new RectangleZone(r, i);

		spawnZones.add(zone);
	}

	public CollisionManager getCollisionManager() {
		return watchDog;
	}

	/**
	 * Each map will need to handle each teleport location on its map structure.
	 * This enables some more flexibility in function of teleport tiles, without needing to create multiple
	 * teleport tile types. Simply handle them differently :]
	 * 
	 * @param t Location of a map's teleporters in Tile Units
	 */
	public abstract void handleTeleport(Point.Double t);

	protected synchronized void levelChange(LevelMap map)
	{
		if (System.currentTimeMillis() - lastMapChange < 5000) {
			return;
		}

		SpaceGame.getInstance().setMap(map);
		SpaceGame.getInstance().getMap().setPlayer(entityPlayer);
		EntityPlayer pl = SpaceGame.getInstance().getMap().getPlayer();
		// pl.setStats(stats);
		pl.setOnGround(false);
		SpaceGame.getInstance().getMap().mapInit();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}