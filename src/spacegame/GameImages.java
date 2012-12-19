package spacegame;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import spacegame.Entity.*;

public final class GameImages {
	private static final String imgPath = "D:\\Programming\\java\\game\\aGame\\img\\"; // default
																						// image
																						// path
																						// TODO:
																						// create
																						// function
																						// to
																						// get
																						// this
																						// value
																						// from
																						// current
																						// running
																						// directory;
	private static final String sprites = imgPath + "sprites\\";
	private static final String spell = sprites + "spell\\";
	private static final String gui = imgPath + "gui\\";
	private static final String player = sprites + "player\\";
	private static final String tile = imgPath + "map\\tile\\";
	private static final String map = imgPath + "map\\";

	/** ENTITY **/

	/** EntityFireBall **/
	public static final BufferedImage[] fireBall = {
			getImg(spell + "fireBall_0.png"), getImg(spell + "fireBall_2.png"),
			getImg(spell + "fireBall_4.png"), getImg(spell + "fireBall_6.png") };

	/** EntityPlayer **/
	public static final BufferedImage imgPlayer[] = {
	/** Walk Left **/
	getImg(player + "player_l0.png"), getImg(player + "player_l1.png"),
			getImg(player + "player_l2.png"),
			/** Walk Right **/
			getImg(player + "player_r0.png"), getImg(player + "player_r1.png"),
			getImg(player + "player_r2.png"),
			/** Still Left, Still Right **/
			getImg(player + "player_lStill.png"),
			getImg(player + "player_rStill.png") };

	/** EntityWorm **/
	public static final BufferedImage[] wormImg = {
			getImg(sprites + "worm\\worm_r1.png"),
			getImg(sprites + "worm\\worm_r2.png"),
			getImg(sprites + "worm\\worm_r3.png"),
			getImg(sprites + "worm\\worm_l1.png"),
			getImg(sprites + "worm\\worm_l2.png"),
			getImg(sprites + "worm\\worm_l3.png") };

	/** GUI **/
	public static final BufferedImage activeSlotImg = getImg(gui
			+ "slotSelected.png");
	public static final BufferedImage slotImg = getImg(gui + "slot.png");
	public static final BufferedImage[] damageText = getDamageTextImages();
	public static final BufferedImage pauseScreenBackground = getImg(gui
			+ "overlay.png");
	public static final Image titleScreenBackground = getImg(gui + "tileBG.png");
	public static final BufferedImage[] iconButtons = getIconButtons();

	/** GuiHud **/
	public static final BufferedImage[] statsArmorSlots = getArmorSlotsImages(); // Images
																					// that
																					// show
																					// specific
																					// armor
																					// slot
																					// "shapes"
																					// in
																					// stats
																					// gui

	/** Tile **/
	public static final BufferedImage[] tiles = get60x60Image(getImg(tile
			+ "terrain.png"));
	/*
	 * public static final BufferedImage tileGround =
	 * getImg(tile+"terrain.png").getSubimage(60, 0, 60, 60); public static
	 * final BufferedImage tileWallTop =
	 * getImg(tile+"terrain.png").getSubimage(540,0,60,60); public static final
	 * BufferedImage tileNull = getImg(tile+"terrain.png").getSubimage(660, 0,
	 * 60, 60);
	 * 
	 * public static final BufferedImage tileWallBottomLeft =
	 * getImg(tile+"terrain.png").getSubimage(180,0,60,60); public static final
	 * BufferedImage tileWallTopLeft =
	 * getImg(tile+"terrain.png").getSubimage(480, 0, 60, 60); public static
	 * final BufferedImage tileWallLeft =
	 * getImg(tile+"terrain.png").getSubimage(360,0,60,60); public static final
	 * BufferedImage tileStairLeft =
	 * getImg(tile+"terrain.png").getSubimage(120,0,60,60);
	 * 
	 * public static final BufferedImage tileWallBottomRight =
	 * getImg(tile+"terrain.png").getSubimage(240, 0, 60,
	 * 60);//getImg(tile+"terrain.png").getSubimage(300,0,60,60); public static
	 * final BufferedImage tileWallTopRight =
	 * getImg(tile+"terrain.png").getSubimage(240, 0, 60, 60); public static
	 * final BufferedImage tileWallRight =
	 * getImg(tile+"terrain.png").getSubimage(420,0,60,60); public static final
	 * BufferedImage tileStairRight =
	 * getImg(tile+"terrain.png").getSubimage(0,0,60,60);
	 */
	public static final BufferedImage[] tileDoor = getTeleportImages();

	/** MAP **/

	/** Level_One **/
	public static final BufferedImage levelOneSun = getImg(map
			+ "level_1\\sun.png");
	public static final BufferedImage levelOneBackground = getImg(map
			+ "level_1\\bg_1_1.png");

	/** EFFECTS **/

	/** Level Up **/
	public static final Animation LevelUpAnim = getLevelUpAnimation();

	private static BufferedImage[] getTeleportImages() {
		BufferedImage base = getImg(tile + "zoneTeleport.png");
		BufferedImage[] a = new BufferedImage[12];
		int i = 0;
		for (int y = 0; y < base.getHeight(); y += 120) {
			for (int x = 0; x < base.getWidth(); x += 60) {
				a[i++] = base.getSubimage(x, y, 60, 120);
			}
		}
		return a;
	}

	/**
	 * Returns armor slot images. This particular image is wonky, hence the code
	 * below
	 **/
	private static BufferedImage[] getArmorSlotsImages() {
		BufferedImage base = getImg(gui + "statsArmorSlots.png");
		BufferedImage[] array = new BufferedImage[12];

		// leg
		array[0] = base.getSubimage(0, 0, 37, 128);
		array[1] = base.getSubimage(111, 0, 37, 128);
		// head
		array[2] = base.getSubimage(37, 0, 37, 58);
		array[3] = base.getSubimage(148, 0, 37, 58);
		// shoulder
		array[4] = base.getSubimage(37, 58, 26, 26);
		array[5] = base.getSubimage(148, 58, 26, 26);
		// feet
		array[6] = base.getSubimage(37, 85, 31, 16);
		array[7] = base.getSubimage(148, 85, 31, 16);
		// glove
		array[8] = base.getSubimage(37, 106, 10, 19);
		array[9] = base.getSubimage(148, 106, 10, 19);
		// chest
		array[10] = base.getSubimage(74, 0, 30, 61);
		array[11] = base.getSubimage(186, 0, 30, 61);

		return array;

	}

	/** Returns all Icons for IconButtons **/
	private static BufferedImage[] getIconButtons() {
		BufferedImage baseImg = getImg(gui + "\\icoButtons.png");
		BufferedImage imgArray[] = new BufferedImage[400]; // I probably made
															// this image too
															// large...
		int count = 0;
		for (int y = 0; y < baseImg.getHeight(); y += 20) {
			for (int x = 0; x < baseImg.getWidth(); x += 20) {
				imgArray[count] = baseImg.getSubimage(x, y, 20, 20);
				count++;
			}
		}

		return imgArray;
	}

	private static Animation getLevelUpAnimation() {
		BufferedImage baseImg = getImg(player + "\\levelUp.png");
		BufferedImage imgArray[] = new BufferedImage[24];
		int count = 0;
		for (int y = 0; y < baseImg.getHeight(); y += 120) {
			for (int x = 0; x < baseImg.getWidth(); x += 60) {
				imgArray[count] = baseImg.getSubimage(x, y, 60, 120);
				count++;
			}
		}

		Animation a = new Animation();

		for (int x = 0; x < imgArray.length; x++) {
			a.addScene(imgArray[x], 42);
		}
		for (int x = imgArray.length - 1; x > -1; x--) {
			a.addScene(imgArray[x], 42);
		}

		return a;
	}

	private static BufferedImage[] get60x60Image(BufferedImage base) {
		BufferedImage ar[] = new BufferedImage[400];
		int i = 0;
		for (int y = 0; y < base.getHeight(); y += 60) {
			for (int x = 0; x < base.getWidth(); x += 60) {
				ar[i++] = base.getSubimage(x, y, 60, 60);
			}
		}

		return ar;
	}

	/**
	 * This function cuts up an image and returns its parts.
	 * @return BufferedImage[]
	 */
	private static BufferedImage[] getDamageTextImages() {
		BufferedImage numbers = getImg(gui + "damageFont.png");
		BufferedImage[] bi = new BufferedImage[10];
		int i = 0;
		for (int y = 0; y < numbers.getHeight(); y+=50)
		{
			for (int x = 0; x < numbers.getWidth(); x+=40)
			{
				bi[i++] = numbers.getSubimage(x, y, 40, 50);
			}
		}
		
		return bi;
	}

	/** Easy image getter **/
	private static BufferedImage getImg(String filename) {
		try {
			return ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("The image: \"" + filename
					+ "\" was not loaded.");
			return null;
		}
	}

	/**
	 * Helper function for entities to return an image when an animation is not
	 * present, based on entity and direction faced
	 **/
	public static BufferedImage getEntityImage(Entity e) {
		boolean flag = false;
		if (e instanceof EntityLiving)
			flag = ((EntityLiving) e).getFacing() == EntityLiving.Facing.LEFT;

		if (e instanceof EntityPlayer)
			return (flag) ? imgPlayer[6] : imgPlayer[7];
		if (e instanceof EntityWorm)
			return (flag) ? wormImg[3] : wormImg[0];
		if (e instanceof EntityFireBall)
			return fireBall[0];

		return null;
	}

	/**
	 * ICON BUTTONS KEY 0 = StatsIcon in GuiHud 1 = CloseIcon in GuiWindow
	 */
}