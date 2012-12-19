package spacegame.Entity;

import java.util.Random;

import spacegame.AI.WalkAI;

public abstract class EntityEnemy extends EntityLiving {
	protected WalkAI walkAI;
	protected Damage touchDamage;
	/** Time stamp of when this entity spawned **/
	protected long spawnTime;
	/** Delay until entity can cause damage from spawn time **/
	protected long timeTillDamage;

	public EntityEnemy(double posX, double posY, int width, int height, Random random)
	{
		super(posX, posY, width, height);
		spawnTime = System.currentTimeMillis();
		timeTillDamage = 1500L;
	}

	@Override
	public void update()
	{
		/** Disable ability to damage until proper time has passed **/
		if (System.currentTimeMillis() - spawnTime < timeTillDamage)
			touchDamage.disable();
		else
			touchDamage.enable();
		
		super.update();
	}

	/** Check what types of AI to employ, and apply it **/
	protected void applyAI()
	{
		if (walkAI != null) {
			this.facing = walkAI.getHorizontalDirection();
		}
	}

	/** return Damage object when entity is touched **/
	public Damage getTouchDamage() {
		return touchDamage;
	}
}