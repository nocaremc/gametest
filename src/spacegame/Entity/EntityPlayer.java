package spacegame.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import spacegame.Animation;
import spacegame.GameImages;
import spacegame.InputManager;
import spacegame.SpaceGame;
import spacegame.Gui.GuiSlot;
import spacegame.Storable.InventoryStack;

public class EntityPlayer extends EntityLiving
{
	private boolean doRenderLevelUp;

	public EntityPlayer(double posX, double posY, int width, int height,
			double maxHP, double currentHP) {
		super(posX, posY, width, height);
		facing = Facing.RIGHT;
		isEffectedByKnockBack = true;
		baseStamina = 7;
		baseWisdom = 7;
		addStats();
		stats.setRunSpeed(15.1D);
		stats.setAirRunSpeed(16D);
		stats.setJumpPower(10.1D);
		stats.setJumpHeight(150D);
		stats.setDamageApplyFrequency(2000L);

		long updateSpeed = (1000 / 6) * (long)(stats.getRunSpeed() / 6);
		//updateSpeed = (long) (0.5 * updateSpeed + updateSpeed);
		/** create animations and add to player **/
		Animation walkingLeft = new Animation();
		walkingLeft.addScene(GameImages.imgPlayer[0], updateSpeed);
		walkingLeft.addScene(GameImages.imgPlayer[1], updateSpeed);
		walkingLeft.addScene(GameImages.imgPlayer[2], updateSpeed);
		walkingLeft.addScene(GameImages.imgPlayer[2], updateSpeed);
		walkingLeft.addScene(GameImages.imgPlayer[1], updateSpeed);
		walkingLeft.addScene(GameImages.imgPlayer[0], updateSpeed);

		Animation walkingRight = new Animation();
		walkingRight.addScene(GameImages.imgPlayer[3], updateSpeed);
		walkingRight.addScene(GameImages.imgPlayer[4], updateSpeed);
		walkingRight.addScene(GameImages.imgPlayer[5], updateSpeed);
		walkingRight.addScene(GameImages.imgPlayer[5], updateSpeed);
		walkingRight.addScene(GameImages.imgPlayer[4], updateSpeed);
		walkingRight.addScene(GameImages.imgPlayer[3], updateSpeed);

		addAnimation(walkingLeft);
		addAnimation(walkingRight);
	}

	/** handle input for player here, kind of funky I know **/
	public void handleInput(boolean hasInputFocus) {
		InputManager input = SpaceGame.getInstance().getInput();
		double speedHere = stats.getSpeedHere();

		if (!hasInputFocus)
			return;

		if (input.escPressed) {
			SpaceGame.getInstance().changeState(SpaceGame.State.PAUSED);
		}

		if (input.aPressed) {
			setDeltaX(-speedHere);
			setFacing(Facing.LEFT);
		}

		if (input.dPressed) {
			setDeltaX(+speedHere);
			setFacing(Facing.RIGHT);
		}

		if (input.spacePressed && !stats.isJumping() && isOnGround()) {
			stats.setJumping(true);
		}

		/** action bar **/
		if (input.onePressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(0);
		if (input.twoPressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(1);
		if (input.threePressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(2);
		if (input.fourPressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(3);
		if (input.fivePressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(4);
		if (input.sixPressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(5);
		if (input.sevenPressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(6);
		if (input.eightPressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(7);
		if (input.ninePressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(8);
		if (input.zeroPressed)
			SpaceGame.getInstance().getMap().getHud().setActiveBarSlot(9);

		/**
		 * this is our attack button, so see if we can fire off the selected
		 * attack and do so
		 **/
		if (input.leftPressed) {
			// boolean isOverGui=false; //Don't want to handle non-gui things if
			// using a gui
			boolean isOverGui = SpaceGame.getInstance().getMap().getHud()
					.isPlayerUsing();
			/**
			 * /** get the array of slots used in our action bar
			 * java.util.List<spacegame.Gui.GuiSlot> barSlots =
			 * SpaceGame.getInstance().getMap().getHud().getActionBarSlots();
			 * java.util.List<spacegame.Gui.Gui> icoButtons =
			 * SpaceGame.getInstance().getMap().getHud().getGuiObjects(); for
			 * (int x=0;x<barSlots.size();x++) { if
			 * (barSlots.get(x).isMouseOver()) // If we are left clicking and
			 * the mouse is over this object { selectedBarAction=x+1; //Change
			 * the selected slot to the one we clicked isOverGui=true; //toggle
			 * gui flag } }
			 */

			if (canAttack() && !isOverGui) // If we can attack and aren't using
											// the gui
			{
				runBarAction();
			}
		}

		/**
		 * if (input.leftReleased) { specialDragging=false; setOnGround(false);
		 * }
		 **/

	}

	/**
	 * This function is special, in that it is only invoked directly from
	 * SpaceCanvas. It is used to allow the (human) player to click and
	 * "pick up" the character and move them on the screen This dragging effect
	 * seemed to need a higher priority in the drawing. I used it a lot while
	 * writing collision, but when I finally expanded the map width and height
	 * to that beyond the screen size this didn't account for that. It's not a
	 * hard fix, but I don't need the function anymore anyway.. However this
	 * effect may be a fun element to add to the game for other objects, so I've
	 * got the code here in case I want ti again.
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		/**
		 * if (input.leftPressed) { specialDragging=true; setOnGround(true);
		 * resetGravityTicks(); input.mouseLocation.translate(3, 22);
		 * setPosition(input.mouseLocation.x-(width/2),
		 * input.mouseLocation.y-(height/2)); input.mouseLocation.translate(-3,
		 * -22); }
		 **/
	}

	/** draw player to screen **/
	public void pack() {
		// System.out.println(stats.getMagicalDamage()+"");

		int locX = SpaceGame.getInstance().getWidth() / 2 - width / 2;
		int locY = SpaceGame.getInstance().getHeight() / 2 - height / 2;

		Graphics2D g = SpaceGame.getInstance().getGraphics();
		if (g == null)
			return;
		g.setColor(Color.CYAN);
		// g.drawRect(SpaceGame.getInstance().vcWidth/2,
		// SpaceGame.getInstance().vcHeight/2, entityPlayer.getWidth(),
		// entityPlayer.getHeight());
		if (getScale() != 1F)
			g.scale(getScale(), getScale());

		/**
		 * if moving, we'll use one of the walking animations, otherwise we use
		 * a standing image. account for direction facing as well
		 **/
		if (isMovingX()) {
			if (getAnimation() != null) {
				if (getAnimation().getImage() != null)
					g.drawImage(getAnimation().getImage(), locX, locY, null);
			} else {
				System.out.println("animation null");
			}
		} else
			g.drawImage(getImage(), locX, locY, null);

		/** need to restore scale back to default **/
		if (getScale() != 1F) {
			g.scale(Math.round((int) Math.floor(1 / getScale())),
					Math.round((int) Math.floor(1 / getScale())));
		}
		/** draw health bar **/
		// spacegame.Gui.Gui.renderHealthBar(g, this, locX+width/2,
		// locY+height/2, 60, 10);

		super.pack();

		if (doRenderLevelUp) {
			if (GameImages.LevelUpAnim.getLoopCount() > 0)
				doRenderLevelUp = false;
			else {
				if (GameImages.LevelUpAnim.getImage() != null)
					g.drawImage(GameImages.LevelUpAnim.getImage(), locX, locY,
							null);

				GameImages.LevelUpAnim.update(SpaceGame.getInstance()
						.getTimePassed());
			}
		}

		/**
		 * g.drawString(SpaceGame.getInstance().getMap().getCollisionManager().
		 * getTileTypeAt((int)getBotCenter().x, (int)getBotCenter().y)+"",
		 * SpaceGame.getInstance().getWidth()/2 - width/2,
		 * SpaceGame.getInstance().getHeight()/2 - height/2 -20); //Ouput, set
		 * by other classes as a bit of ingame debug. Crude but effective
		 * g.drawString(output, SpaceGame.getInstance().getWidth()/2 - width/2,
		 * SpaceGame.getInstance().getHeight()/2 - height/2 -20); //player
		 * position g.drawString(posXf + " " + posYf,
		 * SpaceGame.getInstance().getWidth()/2 - width/2,
		 * SpaceGame.getInstance().getHeight()/2 - height/2 -30); //display
		 * mouse location if (SpaceGame.getInstance().getInput()!=null)
		 * g.drawString
		 * (SpaceGame.getInstance().getInput().mouseLocation.toString(),
		 * SpaceGame.getInstance().getWidth()/2 - width/2,
		 * SpaceGame.getInstance().getHeight()/2 - height/2 -40); //Display
		 * camera offsets Point.double cam =
		 * SpaceGame.getInstance().getMap().getCameraOffset();
		 * g.drawString(cam.getX() + " " + cam.getY(),
		 * SpaceGame.getInstance().getWidth()/2 - width/2,
		 * SpaceGame.getInstance().getHeight()/2 - height/2 -50);
		 * 
		 * 
		 * // draw collision points for testing, This is all rather un-readable
		 * since I need to offset them so I can display properly. But I tried...
		 * g.setColor(Color.yellow); int r=2; int gameWidthHalf =
		 * SpaceGame.getInstance().getWidth()/2 - width/2; int gameHeightHalf =
		 * SpaceGame.getInstance().getHeight()/2 - height/2; int charPosX =
		 * (int)getBoundingBox().x; int charPosY = (int)getBoundingBox().y;
		 * 
		 * g.drawOval(gameWidthHalf + ((int)getBotCenter().x - charPosX),
		 * gameHeightHalf + ((int)getBotCenter().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getMidBotRight().x - charPosX),
		 * gameHeightHalf + ((int)getMidBotRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getMidBotLeft().x - charPosX),
		 * gameHeightHalf + ((int)getMidBotLeft().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getBotLeft().x - charPosX),
		 * gameHeightHalf + ((int)getBotRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getBotRight().x - charPosX),
		 * gameHeightHalf + ((int)getBotRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getTopLeft().x - charPosX),
		 * gameHeightHalf + ((int)getTopRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getTopRight().x - charPosX),
		 * gameHeightHalf + ((int)getTopRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getMidTopLeft().x - charPosX),
		 * gameHeightHalf + ((int)getMidTopRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getMidTopRight().x - charPosX),
		 * gameHeightHalf + ((int)getMidTopRight().y- charPosY), r, r);
		 * g.drawOval(gameWidthHalf + ((int)getAbsoluteXY().x - charPosX),
		 * gameHeightHalf + ((int)getAbsoluteXY().y- charPosY), r, r);
		 */
	}

	/** player will select various attacks, so need to account for that **/
	protected boolean canAttack() {
		/**
		 * This all will get reeaaally vast an complex later on, but for now,
		 * just return if we are on the first action slot..
		 **/
		int slot = SpaceGame.getInstance().getMap().getHud().getActiveBarSlot();
		if (slot == 0)
			return stats.canAttack();
		else
			return false;
	}

	/** runs the action located at the bar index **/
	private void runBarAction() {
		int slotID = -999;
		switch (SpaceGame.getInstance().getMap().getHud().getActiveBarSlot()) {
		case 0:
			slotID = 0;
			break;
		default:
			System.out.println("unkown action");
			break;
		}

		if (slotID == -999) // No action was selected, despite being here
		{
			System.out.println("Unable to cast spell, -999, ~line 300");
			return;
		}

		GuiSlot slot = SpaceGame.getInstance().getMap().getHud()
				.getActionBarSlots().get(slotID);
		InventoryStack stack = slot.getInventoryStack();
		stack.execute(this);
	}

	public void renderLevelUp() {
		doRenderLevelUp = true;
		GameImages.LevelUpAnim.start();
	}
}