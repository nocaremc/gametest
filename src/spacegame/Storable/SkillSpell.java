package spacegame.Storable;

import java.lang.reflect.InvocationTargetException;

import spacegame.SpaceGame;
import spacegame.Entity.EntityPlayer;
import spacegame.Entity.EntityProjectile;
import spacegame.Entity.EntityWeapon;

public class SkillSpell extends Skill 
{
	
	public SkillSpell(String name, boolean selfBuff, double manaConsumption, double intensity, long coolDown) 
	{
		super(name, selfBuff, manaConsumption, intensity, coolDown);
	}

	/**
	 * @param entityPlayer
	 * @param spellName
	 *            This method casts a spell in the game. Went through a lot of
	 *            trouble to make it as generic as possible, so that I may
	 *            easily cast spells without throwing too many major if
	 *            statements around. This will get the Entity class of a spell
	 *            and spawn it Their are a lot of hoops to jump through from the
	 *            point the GuiSlot is excecuted to here However, most of the
	 *            type enumeration is done in Skill, so hopefully I won't bulk
	 *            things up too much with different skill types
	 */
	public static void cast(EntityPlayer entityPlayer, String spellName)
	{
		Class<?> spellClass = Skill.getEntityByName(spellName);
		Skill s = Skill.getSkillByName(spellName);
		
		EntityWeapon wm = null;
		try
		{
			/**
			 * instantiate spell, since this is a generic approach where we
			 * don't know the class name, we have this...
			 **/
			wm = (EntityWeapon) spellClass.getConstructor
					(
							new Class<?>[]
							{
								spacegame.Entity.EntityLiving.class,
								Double.TYPE,
								Double.TYPE
							}
					).newInstance
					(
						new Object[]
						{
							entityPlayer,
							entityPlayer.getX(),
							entityPlayer.getY() + (entityPlayer.getHeight() / 4)
						}
					);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		if (wm != null)
		{
			if (wm instanceof EntityProjectile)// Tell the projectile where it's trying to go
			{
				((EntityProjectile) wm).setDestination(entityPlayer, SpaceGame.getInstance().getInput().getMouseLocation(), 5F);
			}
			SpaceGame.getInstance().getMap().spawnEntity(wm); // spawn fireball in world
			entityPlayer.getStats().damageMana(s.getManaRequired());
		}
	}
}