package spacegame.Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import spacegame.Animation;
import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.AI.*;

public class EntityWorm extends EntityEnemy {
	private static Animation wormWalkRight = new Animation();
	private static Animation wormWalkLeft = new Animation();

	public EntityWorm(double posX, double posY, Random random) {
		super(posX, posY, 180, 60, random);
		facing = Facing.RIGHT;
		isEffectedByGravity = true;
		/** Ai in use **/
		walkAI = new WalkAI(this);
		walkAI.setPeriodicJump(1000);
		walkAI.seedRand(random);
		/** touch damage **/
		touchDamage = new Damage(this, true, stats.getDamage(3.56251658163,
				true));
		touchDamage.setKnockBackForce(4.5F, -4F);
		stats.setRunSpeed(4D);
		stats.setJumpPower(2.7623D);
		// stats.setLevel(10);
		long updateSpeed = (long) ((stats.getRunSpeed() * SpaceGame
				.getInstance().getUpdatePeriod()) / 1000000L) * 6;
		// updateSpeed = (long) (0.5*updateSpeed+updateSpeed);

		wormWalkRight.addScene(GameImages.wormImg[0], updateSpeed);
		wormWalkRight.addScene(GameImages.wormImg[1], updateSpeed);
		wormWalkRight.addScene(GameImages.wormImg[2], updateSpeed * 2);
		wormWalkRight.addScene(GameImages.wormImg[1], updateSpeed);
		wormWalkRight.addScene(GameImages.wormImg[0], updateSpeed);

		wormWalkLeft.addScene(GameImages.wormImg[3], updateSpeed);
		wormWalkLeft.addScene(GameImages.wormImg[4], updateSpeed);
		wormWalkLeft.addScene(GameImages.wormImg[5], updateSpeed * 2);
		wormWalkLeft.addScene(GameImages.wormImg[4], updateSpeed);
		wormWalkLeft.addScene(GameImages.wormImg[3], updateSpeed);

		addAnimation(wormWalkLeft);
		addAnimation(wormWalkRight);

	}

	public void pack() {
		Graphics2D g = SpaceGame.getInstance().getGraphics();
		if (g == null || isDead())
			return;

		/**
		 * Draw current animation scene, or draw a still image if none available
		 **/
		if (canAnimate())
			g.drawImage(getAnimation().getImage(), (int) posX, (int) posY, null);
		else
			g.drawImage(getImage(), posXi, posYi, null);

		/** Draw a health bar **/
		spacegame.Gui.Gui.renderHealthBar(g, this, (int) posX + width / 2,
				(int) posY + height / 2, width, 10);

		super.pack();
		/**
		 * //* draw entity collision points g.setColor(Color.cyan);
		 * g.draw(getBoundingBox()); int r = 2; g.drawOval((int)getBotLeft().x,
		 * (int)getBotLeft().y, r,r); g.drawOval((int)getBotCenter().x,
		 * (int)getBotCenter().y, r,r); g.drawOval((int)getBotRight().x,
		 * (int)getBotRight().y, r,r);
		 * 
		 * /** draw some status text ** g.setColor(Color.RED); Font giantText =
		 * new Font("Courier New", Font.BOLD, 16); FontMetrics fm =
		 * SpaceGame.getInstance().getFontMetrics(giantText);
		 * g.setFont(giantText);
		 * 
		 * String dX = new
		 * StringBuilder().append(deltaX).append(", ").append(deltaY
		 * ).toString(); String pos = new
		 * StringBuilder().append(posX).append(", ").append(posY).toString();
		 * String dir = new StringBuilder().append(facing).toString();
		 * 
		 * g.drawString(dX, posXi, (posYi)-fm.getHeight()); g.drawString(pos,
		 * posXi, (posYi)-fm.getHeight()*2); g.drawString(dir, posXi,
		 * (posYi)-fm.getHeight()*3);
		 */
	}

	@Override
	public void update() {
		applyAI();
		super.update();
	}

	@Override
	public Point.Double getMidBotLeft() // This point system was kind of screwy
										// with the updated collision.
	// Honestly too burned on collision to bother doing it properly right now. A
	// good couple of weeks invested in it already <.<
	{
		return getTopLeft();
	}
}