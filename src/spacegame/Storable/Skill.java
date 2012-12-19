package spacegame.Storable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import spacegame.GameImages;
import spacegame.Entity.Entity;

/**
 * @author Nocare This class will be used to store information on specific
 *         skills. In addition, all skills defined in the game will be here
 */
public class Skill {
	private static List<Skill> skillList = new ArrayList<Skill>();
	protected String name;
	protected boolean selfBuff; // Whether this skill is a self buff
	protected double manaConsumption; // Mana used
	protected double intensity; // BASE Power factor of skill
	private long coolDown;
	public BufferedImage displayIcon;

	public static enum enumSkill {
		FIREBALL
	};

	public enumSkill entityClass;

	public static SkillSpell fireBall;

	static {
		fireBall = new SkillSpell("Fire Ball", false, 1.159, 7, 750);
		fireBall.setIcon(GameImages.fireBall[0]);
		fireBall.setEntity(enumSkill.FIREBALL);
		skillList.add(fireBall);
	}

	public Skill(String name, boolean selfBuff, double manaConsumption,
			double intensity, long coolDown) {
		this.name = name;
		this.selfBuff = selfBuff;
		this.manaConsumption = manaConsumption;
		this.intensity = intensity;
		this.coolDown = coolDown;
	}

	public void setIcon(BufferedImage icon) {
		if (icon == null)
			return;
		displayIcon = icon;
	}

	public BufferedImage getIcon() {
		return displayIcon;
	}

	public void setEntity(enumSkill s) {
		entityClass = s;
	}

	public Class<?> getEntity() {
		switch (entityClass) {
		case FIREBALL:
			return fireBall.getClass();
		default:
			return null;
		}
	}

	public static Class<? extends Entity> getEntityByName(String name) {
		if (name.equals("Fire Ball")) {
			return spacegame.Entity.EntityFireBall.class;
		} else
			System.out.println("Unkown spell name [Skill: ~76]");
		return null;
	}
	
	public static Skill getSkillByName(String name)
	{
		if (name.equalsIgnoreCase("fire ball"))
		{
			return fireBall;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public long getCoolDown() {
		return coolDown;
	}

	public double getManaRequired() {
		return manaConsumption;
	}
}