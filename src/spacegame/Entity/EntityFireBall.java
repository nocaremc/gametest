package spacegame.Entity;

import spacegame.Animation;
import spacegame.SpaceGame;
import spacegame.GameImages;

public class EntityFireBall extends EntityProjectile
{
	public EntityFireBall(EntityLiving parent, double posX, double posY)
	{
		/**
		 * offset Y position to center of its point of origion (where it was
		 * spawned)
		 **/
		super(parent, posX, posY - (SpaceGame.getInstance().getMap().getMapStructure().getTileHeight()/2));
		width = 30;
		height = 30;
		Animation fireBallAnim = new Animation();
		long updatePeriod = (1000L / 4); // all images in one second
		/** Add images **/
		fireBallAnim.addScene(GameImages.fireBall[0], updatePeriod);
		fireBallAnim.addScene(GameImages.fireBall[1], updatePeriod);
		fireBallAnim.addScene(GameImages.fireBall[2], updatePeriod);
		fireBallAnim.addScene(GameImages.fireBall[3], updatePeriod);
		animations.add(fireBallAnim);
		setAnimationInUse(0);
		displayLife = 2000L;
		speed = 20F;
		/** touch damage **/
		touchDamage = new Damage(parent, true, stats.getDamage(2.2D, false));
		touchDamage.setKnockBackForce(4.5F, -4F);
	}
}