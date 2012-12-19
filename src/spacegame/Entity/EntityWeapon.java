package spacegame.Entity;

import spacegame.Storable.StatsManager;

/**
 * @author Nocare Stub class defining weapons, or other "non-sentient" damaging
 *         entities
 */

public class EntityWeapon extends Entity {
	protected Damage touchDamage;
	protected double speed; // Speed weapon travels
	protected Class<? extends EntityLiving> targetEntity;
	protected double targetRadius = 5F; // Radius around the target location to
										// search, useful if a projectile
										// applies a force
	protected StatsManager stats;

	public EntityWeapon(EntityLiving parent, double posX, double posY)
	{
		super(posX, posY);
		stats = parent.getStats();
		touchDamage = new Damage(parent, true, 10); // Need to pass along the
													// entity that spawns this
													// to use as damage source
	}

	/** return Damage object when entity is touched **/
	public Damage getTouchDamage() {
		return touchDamage;
	}

	public double getAttackSpeed() {
		return speed;
	}

	public StatsManager getStats() {
		return stats;
	}
}