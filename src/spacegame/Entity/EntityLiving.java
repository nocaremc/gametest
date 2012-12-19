package spacegame.Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.util.Random;

import spacegame.Animation;
import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.SpaceGame.State;
import spacegame.Storable.StatsManager;
import spacegame.Util.Vector2d;
import spacegame.Entity.EntityPlayer;

@SuppressWarnings("unused")
public class EntityLiving extends Entity {
	/** Base Stats **/
	protected double baseStrength = 1;
	protected double baseStamina = 5;
	protected double baseIntelligence = 1;
	protected double baseWisdom = 5;
	protected double baseDexterity = 5;
	protected double baseLuck = 5;
	protected StatsManager stats;
	
	/** Is damage value being displayed? **/
	private boolean animatingDamage=false;
	private Vector2d dmgAnimLast;
	private Vector2d dmgAnimSpeed;
	private Vector2d dmgAnimTarget;
	private long dmgAnimStartTime;
	private int dmgAnimTicks=0;

	/** Create a enumeration for direction entity is facing **/
	public static enum Facing { LEFT, RIGHT }

	protected Facing facing = Facing.LEFT; // Direction entity is facing

	public EntityLiving(double posX, double posY, int width, int height)
	{
		super(posX, posY, width, height);
		onGround = false;
		isEffectedByGravity = true;
		addStats(); // create a stat manager and add our base stats
	}

	/** Get/set Direction being faced **/
	public Facing getFacing() {
		return facing;
	}

	public void setFacing(Facing facing) {
		/** verify it is an acceptable value **/
		switch (facing) {
		case LEFT:
			this.facing = Facing.LEFT;
			break;
		case RIGHT:
			this.facing = Facing.RIGHT;
			break;
		}
	}

	/** UPDATE **/
	@Override
	public void update()
	{	
		if (stats.getCurrentHealth() < 0) // is dead
		{
			/** this entity is dead, and killed by player **/
			if (getStats().getLastDamageSource() instanceof EntityPlayer)
			{
				EntityPlayer pl = (EntityPlayer) getStats().getLastDamageSource();
				pl.getStats().giveXP(stats.getDeathXP());
			}
			else
				System.out.println(getStats().getLastDamageSource());

			/** If the player is the entity dying, raise a game over state **/
			if (this instanceof EntityPlayer)
			{
				SpaceGame.getInstance().changeState(State.GAMEOVER);
			}
			
			destroy();//Remove entity from game
			return;
		}

		/** no animation, so update our supers and exit function **/
		if (animations.size() < 1) {
			super.update();
			return;
		}
		/**
		 * set the animation in use based on the character's facing. This
		 * defaults to 0 and 1 in the animation index, so be sure to add the
		 * walking animations in correct order
		 **/
		switch (facing)
		{
			case LEFT:
				setAnimationInUse(0);
				break;
			case RIGHT:
				setAnimationInUse(1);
				break;
		}
		super.update();
		stats.update();
	}
	
	/** player will select various attacks, so need to account for that **/
	protected boolean canAttack() {
		return true;
	}

	/** over-ridden to modify living entities run speed an jump power **/
	@Override
	public void setScale(double factor) {
		super.setScale(factor);
		stats.setRunSpeed(stats.getRunSpeed() * scale);
		stats.setJumpPower(stats.getJumpPower() * scale);
	}

	/** Get stat manager **/
	public StatsManager getStats() {
		return stats;
	}

	public void setStats(StatsManager stats) {
		this.stats = stats;
	}

	public void pack() {
		Graphics2D g = SpaceGame.getInstance().getGraphics();
		if (g == null)
			return;
		
		/** Check for recent damage and and animate it **/
		if (stats.getDamageValueForDisplay() > 0 && !animatingDamage)
		{
			animatingDamage = true;
			renderDamageValue(g, true);
		}
		else if (stats.getDamageValueForDisplay() > 0 && animatingDamage)
		{
			renderDamageValue(g, false);
		}
	}

	/** adds this entity to spawn list **/
	public void addToSpawnList() {
		SpaceGame.getInstance().getMap().addEntityForSpawn(this);
	}

	/** creates a stat manager and adds base stats to it **/
	public void addStats() {
		stats = new spacegame.Storable.StatsManager(this, 1, 0, baseStrength,
				baseStamina, baseIntelligence, baseWisdom, baseDexterity,
				baseLuck);
	}
	
	
	/**
	 * If we have a damage applied, we will render it in this function.
	 * @param g - Graphics object
	 * @param firstRun - Controls whether this function will update or start the animation 
	 */
	private void renderDamageValue(Graphics2D g, boolean firstRun)
	{
		/** Set up number animation **/
		if (firstRun)
		{
			dmgAnimStartTime = System.currentTimeMillis();
			dmgAnimLast = new Vector2d(posX, posY-50);
			Random rand = new Random(SpaceGame.getInstance().getRandom().nextLong());
			int randX = rand.nextInt(10);
			int randY = rand.nextInt(10);
			int dx = (!rand.nextBoolean()) ? 1 : -1;//Get travel X
			
			
			/** Set target a number of ticks away, will change later when we animate in an elliptical fashion **/
			dmgAnimTarget = new Vector2d(posX + (randX * 10), posY + (randY *10));
			dmgAnimSpeed = dmgAnimLast.normalise(dmgAnimTarget);
			
			dmgAnimSpeed = Vector2d.add(dmgAnimSpeed, new Vector2d(10 * dx,-10), null);
		}
		
		/** Calculate position this run **/
		if (System.currentTimeMillis() - dmgAnimStartTime > 50)
		{
			dmgAnimLast = Vector2d.add(dmgAnimLast, dmgAnimSpeed, null);
			dmgAnimStartTime = System.currentTimeMillis();
			dmgAnimTicks++;
		}
		
		/** Get String value of damage **/
		String dmg = String.valueOf(stats.getDamageValueForDisplay());
		int[] a = new int[dmg.length()];
		
		/** Convert String damage into array of integers using substring **/
		for (int x = 0; x < dmg.length(); x++)
		{
			if (x+1 <= dmg.length())
				a[x] = Integer.valueOf(dmg.substring(x, x+1));
		}
		
		/** Display each element of array as corresponding image**/
		int count = 0;
		for (int x = 0; x < a.length; x++)
		{
			g.drawImage(GameImages.damageText[a[x]].getScaledInstance(20, 25, BufferedImage.SCALE_SMOOTH), (int)dmgAnimLast.getX()+(40*count), (int)dmgAnimLast.getY(), null);
			count++;
		}
	}
}