package spacegame.Entity;

import java.awt.Point;

import spacegame.SpaceGame;

/** This class defines damage types, and corresponding reactions to damage types **/
public class Damage
{
	/** Is KnockBack applied in damage calculations? **/
	private boolean hasKnockBack;
	/** Can this effect more than one entity? **/
	private boolean hasAreaOfEffect = false;
	/** Amount of X knockback **/
	private double knockBackFactorX = 0F;
	/** Amount of Y knockback **/
	private double knockBackFactorY = 0F;
	/** Amount of raw damage this attack does **/
	private double damageFactor;
	/** Direction entity travels while under knockback effects **/
	private Point damageDirection;
	/** Entity that is doing this damage **/
	private Entity damageSource;
	/** Whether or not this damage can be applied at all **/
	private boolean enabled = true;

	public Damage(Entity damageSource, boolean knockBack, double damage) 
	{
		hasKnockBack = knockBack;
		damageFactor = damage;
		this.damageSource = damageSource;
	}

	/** set the damage value **/
	public void setDamageFactor(double damage) {
		damageFactor = damage;
	}

	/** get the damage value **/
	public double getDamageFactor() {
		return damageFactor;
	}

	/** set knock back **/
	public void setKnockBack(boolean flag) {
		hasKnockBack = flag;
	}

	/** get knock back state **/
	public boolean getKnockBack() {
		return hasKnockBack;
	}

	/** set the knock back factor, if 0 then damage factor will be used **/
	public void setKnockBackForce(double forceX, double forceY) {
		knockBackFactorX = forceX;
		knockBackFactorY = forceY;
	}

	/** applies knockBack to an entity **/
	public void applyKnockBack(Entity entity) {
		if (!enabled)
			return;
		/** damage type doesn't have knock back, or entity isn't effected by it **/
		if (!hasKnockBack || !entity.isEffectedByKnockBack)
			return;

		/** temporary value holding knock back force **/
		double forceX, forceY;

		/**
		 * if no knock back force of any kind is set, then use damageFactor for
		 * both
		 **/
		if (knockBackFactorX == 0 && knockBackFactorY == 0) {
			forceX = damageFactor;
			forceY = damageFactor;
		} else {
			forceX = knockBackFactorX;
			forceY = knockBackFactorY;
		}

		/**
		 * Apply damage vector to knock back force. If one is not set, force
		 * will always be positive x,y Multiplying by 1 or -1 here
		 **/
		if (damageDirection != null) {
			forceX *= damageDirection.getX();
			forceY *= damageDirection.getY();
		}

		entity.setSpeed(forceX, forceY);
	}

	/** set the damage direction **/
	public void setDamageDirection(int x, int y) {
		/** normalize **/
		if (x > 1)
			x = 1;
		if (x < -1)
			x = -1;
		if (y > 1)
			y = 1;
		if (y < -1)
			y = -1;
		/** set **/
		damageDirection = new Point(x, y);
	}

	/** get damage direction **/
	public Point getDamageDirection() {
		return damageDirection;
	}

	/** get knockBack force **/
	public Point.Double getKnockBackForce() {
		return new Point.Double(knockBackFactorX, knockBackFactorY);
	}

	/**
	 * apply damage to entity, any armor calculations happen in the entity
	 * itself
	 **/
	public void applyDamage(EntityLiving pl) {
		if (!enabled)
			return;
		pl.getStats().damageHealth(damageFactor, damageSource); // apply damage
																// to entity,
																// and pass
																// along damage
																// source
		pl.getStats().setDamageValueForDisplay((int) Math.round(damageFactor));
	}

	/** calculate and apply damage+-knock back between weapon and another entity **/
	public void calcDamage(EntityWeapon ew, EntityLiving el)
	{
		if (!enabled)
			return;
		if (ew.isDead()) // Weapon is dead, so return
			return;

		if (!withinTimeLimit(el)) // Verify entity can be damaged
			return;

		if (el.getBoundingBox().getX() + (el.getBoundingBox().width / 2) > ew.getBoundingBox().getX()) // if weapon collides on left side
			setDamageDirection(-1, 1);

		if (el.getBoundingBox().getY() > ew.getBoundingBox().getY())// if weapon collides from below
		{
			if (getDamageDirection() != null)
				setDamageDirection((int) getDamageDirection().getX(), -1);
		}

		setKnockBackForce((getKnockBackForce().getX() + ew.getAttackSpeed() / 300), (getKnockBackForce().getY()));
		
		if (el instanceof EntityPlayer) // In case this weapon is used by a mob
			SpaceGame.getInstance().getMap().setMovementLock(true);
		
		applyDamage(el);
		applyKnockBack(el);
		el.setOnGround(false);

		/** If Entity is a projectile and doesnt apply aoe, kill it **/
		if (ew instanceof EntityProjectile && !this.isAOE()) {
			ew.destroy();
		}
	}

	/** calculate and apply damage+-knock back between enemy and player **/
	public void calcDamage(EntityEnemy ee, EntityLiving pl)
	{
		if (!enabled)
			return;

		if (!withinTimeLimit(pl)) // Verify entity can be damaged
			return;

		if (ee.getBoundingBox().getX() + (ee.getBoundingBox().width / 2) < pl
				.getBoundingBox().getX()) // Entity is to left of player
			ee.getTouchDamage().setDamageDirection(1, 1);
		else
			// entity to right of player
			ee.getTouchDamage().setDamageDirection(-1, 1);

		/**
		 * if entity happens to fall on player, lets not launch player up in air
		 **/
		if (ee.getBoundingBox().getY() < pl.getBoundingBox().getY())
			setDamageDirection((int) getDamageDirection().getX(), 1);

		/** modify the knockback force based on player's run_speed **/
		setKnockBackForce(getKnockBackForce().getX()
				+ (pl.getStats().getRunSpeed() / 300),
				(getKnockBackForce().getY()));
		/** lock movement of player by human **/
		if (pl instanceof EntityPlayer)
			SpaceGame.getInstance().getMap().setMovementLock(true);
		applyDamage(pl);
		applyKnockBack(pl);
		pl.setOnGround(false);
	}

	/** Whether not the entity is able to be damaged at this time **/
	private boolean withinTimeLimit(EntityLiving e)
	{
		long currentTime = System.nanoTime();
		long timePassed = (currentTime - ((EntityLiving) e).getStats().getLastDamageTime()) / 1000000L;
		if (timePassed < ((EntityLiving) e).getStats().getDamageFrequency())
			return false;

		return true;
	}

	/** Returns whether or not this damage has an aoe effect **/
	private boolean isAOE() {
		return hasAreaOfEffect;
	}

	/** Enable/Disable aoe **/
	public void setAOE(boolean flag) {
		hasAreaOfEffect = flag;
	}

	/** Enable this damage **/
	public void enable() {
		enabled = true;
	}

	/** Disable this damage **/
	public void disable() {
		enabled = false;
	}
}