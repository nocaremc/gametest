package spacegame.Storable;

import spacegame.SpaceGame;
import spacegame.Entity.Entity;
import spacegame.Entity.EntityLiving;
import spacegame.Entity.EntityPlayer;
import spacegame.Entity.EntityWeapon;
import spacegame.Gui.GuiSlot;

/** This class manages the stats of an entity **/
public class StatsManager
{
	/** Entity this instance belongs to **/
	private Entity entity;
	
	/** Direct max count of mana **/
	private double mana; 
	private double currentMana;
	/** Direct max count of health **/
	private double health; 
	private double currentHealth;

	private int currentLevel;
	private double currentExperience;
	private double maxExperience;
	private double deathExperience;
	private double baseXP = 10;

	/** CORE STATS **/
	
	/** Influences health slightly, physical damage greatly, block minimally **/
	private double strength;
	
	/** Influences health greatly, block slightly **/
	private double stamina;
	
	/** Influences magical damage greatly, mana slightly, crit rate minimally **/
	private double intelligence;
	
	/** Influences mana greatly, magical resistance slightly **/
	private double wisdom;
	
	/** Influences hit rate greatly, critical rate slightly, dodge rate somewhere in between **/
	private double dexterity;
	
	/** Influences crit rate greatly, dodge and resistance slightly **/
	private double luck;

	/** SUB STATS **/
	/** calculations include level differences between two entities **/
	
	/** The rate at which an attack is block/parried so that very minimal damge is taken **/
	private double block;
	
	/** Rate an entity can hit another **/
	private double hitRate;
	
	/** Rate an entity can dodge an attack completely **/
	private double dodge;
	
	/** This is essentially a magic version of block **/
	private double resistance;
	
	/** Rate damage applied will be critical **/
	private double criticalRate;
	
	/** Maximum damage an entity can dish out (ignoring all stats of another entity) **/
	private double physicalDamage;
	
	/** Maximum Magic damage an entity can dish out (ignoring all stats of another entity) **/
	private double magicalDamage;

	private double jumpPower = 0.0005D; // how hard the player jolts upward on jump
	private double runSpeed = 5.0D; // force of player movement on ground
	private double airRunSpeed = 6.0D; // like run speed, but used for control while in the air
	@SuppressWarnings("unused")
	private double groundFriction = 2D; // friction from the ground. multiplied by the x speed each frame.
	@SuppressWarnings("unused")
	private double airFriction = 0.01D; // resistance in the air, otherwise air control enables crazy speeds

	private boolean isJumping = false;
	private int jumpTicks = 0;
	private double jumpHeight = 100D;
	private double jumpStartY = 0;
	private long lastJumpChange;

	/** timeStamps used to limit frequency of actions **/
	private long lastDamageTime;
	private long lastAttackTime;
	private long lastRegeneration;

	/** frequency of actions **/
	private long damageApplyFrequency = 1000L; // How often can damage be
												// applied to this entity?
	private long attackFrequency = 1000L; // How often can this entity attack?

	private int damageForDisplay = -999; // Damage value used for displaying
											// during pack
	private Entity lastDamageSource;

	public StatsManager(EntityLiving entity) {

	}

	public StatsManager(EntityWeapon entity) {

	}

	public StatsManager(Entity e, int level, double experience,
			double baseStrength, double baseStamina, double baseIntelligence,
			double baseWisdom, double baseDexterity, double baseLuck) {
		entity = e;
		strength = baseStrength;
		stamina = baseStamina;
		intelligence = baseIntelligence;
		wisdom = baseWisdom;
		dexterity = baseDexterity;
		luck = baseLuck;
		lastJumpChange = lastAttackTime = lastDamageTime = System.nanoTime(); //"zero" time stamps
		lastRegeneration = System.currentTimeMillis();
		currentLevel = level;
		if (currentLevel != 1) // Handle entities spawned that arent lvl 1
		{
			setLevel(currentLevel);
		} else {
			maxExperience = baseXP;
			currentExperience = 0;
		}
		calculateStats();
		currentHealth = health;
		currentMana = mana;
	}

	/** updates all components **/
	public void update() {
		if (isJumping()) // self explanatory? lawl
			updateJump();

		calculateStats();
	}

	/** Updates all sub-stats values based on main-stats TODO:balancing... **/
	private void calculateStats()
	{
		health = (stamina * 2.5D) + (strength * 0.5D);
		mana = (wisdom * 2.5D) + (intelligence * 0.5D);
		block = (stamina * 0.1D) + (strength * 0.01D);
		resistance = wisdom * 0.11D;
		hitRate = (dexterity * 0.25D) + (luck * 0.02D);
		dodge = (dexterity * 0.15D) + luck * 0.02D;
		criticalRate = (dexterity * 0.01D) + (intelligence * 0.01D)
				+ (luck * 0.3D);
		physicalDamage = strength * 1.000654985;
		magicalDamage = intelligence * 1.00000564198;

		deathExperience = ((maxExperience * 0.01) + (currentLevel * 2.5));//set the experience this entity will yield on death
		// Scale Experience gained by level difference of last damage source
		int levelDifference = 0;
		if (lastDamageSource instanceof EntityLiving
				|| lastDamageSource instanceof EntityWeapon)
			levelDifference = (((EntityLiving) lastDamageSource).getStats()
					.getLevel() - currentLevel);
		else if (lastDamageSource instanceof EntityWeapon)
			levelDifference = (((EntityWeapon) lastDamageSource).getStats()
					.getLevel() - currentLevel);

		if (levelDifference > 5 || levelDifference < -5) //Higher level, less xp
			deathExperience = deathExperience / levelDifference;
		else if (levelDifference < 0) //Lower level, more xp
			deathExperience = deathExperience / (levelDifference * 0.98756);
		else if (levelDifference > 0) //even less experience if not within 5 over
			deathExperience = deathExperience / (levelDifference * 1.009851650);
		
		if (deathExperience < 0) // Ensure experience is positive
			deathExperience *= -1;

		if (currentExperience >= maxExperience) // Level up entity is applicable
			doLevelUp();
		
		double healthRegen = (stamina * 0.15D) / 5;
		double manaRegen = 0.3 + (wisdom * 0.15D) / 5;
		
		/** Regenerate hp/mp every second **/
		if (System.currentTimeMillis() - lastRegeneration > 1000)
		{
			currentHealth += healthRegen;
			currentMana += manaRegen;
			
			if (currentHealth > health)
				currentHealth = health;
			
			if (currentMana > mana)
				currentMana = mana;
			
			lastRegeneration = System.currentTimeMillis();
		}
	}

	private void doLevelUp() {
		currentLevel += 1;
		currentExperience = currentExperience - maxExperience; // Roll-Over
																// Minutes :]
		maxExperience = maxExperience + ((currentLevel * 5) * 1.1); // Set the
																	// max
																	// experience
																	// based on
																	// current
																	// entity
																	// level and
																	// random as
																	// hell
																	// numbers
		if (entity instanceof EntityPlayer) // If player (likely always), run a
											// levelup animation on its
											// location.
			((EntityPlayer) entity).renderLevelUp();

		strength++;
		stamina += 2;
		dexterity++;
		intelligence++;
		luck++;
		wisdom += 2;
		calculateStats();
		currentHealth = health;
		currentMana = mana;
	}

	/**
	 * returns damage value, adding this base damage value to the one held in
	 * stats Any other modifications done to damage are calculated when the
	 * damage is actually applied to target entity
	 * 
	 * @param baseDamage
	 *            Damage to be added
	 * @param type
	 *            true=physical damage, false magical
	 * @return modified damage value
	 */
	public double getDamage(double baseDamage, boolean type) {
		if (type) // if P.dmg
		{
			return physicalDamage + baseDamage;
		} else
			return magicalDamage + baseDamage;
	}

	/** STATS **/

	/** Get/Set Health and Mana **/
	public double getCurrentHealth() {
		return currentHealth;
	}

	/** Set current health value **/
	public void setCurrentHealth(double i) {
		if (i > health) // If supplied health is greater than max health
			i = health;
		if (i < 0) // If health is less than 0
			i = 0;
		currentHealth = i;
	}

	public double getMaxHealth() {
		return health;
	}

	public double getCurrentMana() {
		return currentMana;
	}

	public double getMaxMana() {
		return mana;
	}

	/** Return current and max experience **/
	public double getCurrentXP() {
		return currentExperience;
	}

	public double getMaxXP() {
		return maxExperience;
	}

	/** Returns experience value this entity yeilds when it dies **/
	public double getDeathXP() {
		return deathExperience;
	}

	/** Increments entity's experience **/
	public void giveXP(double deathXP) {
		currentExperience += deathXP;
		// System.out.println(deathXP);
	}

	/** Get/Set Level **/
	public void setLevel(int i) {
		currentLevel = 1;
		maxExperience = baseXP;
		for (; currentLevel != i;) // This is done to ensure the experience
									// values are correct
		{
			doLevelUp();
		}
	}

	public int getLevel() {
		return currentLevel;
	}

	/** Get/set Jump Speed **/
	public double getJumpPower() {
		return jumpPower;
	}

	public void setJumpPower(double power) {
		jumpPower = power;
	}

	/** Get/Set run speed **/
	public double getRunSpeed() {
		return runSpeed;
	}

	public void setRunSpeed(double d) {
		runSpeed = d;
	}

	public void setAirRunSpeed(double d) {
		airRunSpeed = d;
	}

	/** Returns value of run speed at location **/
	public double getSpeedHere() {
		return (entity.isOnGround() ? runSpeed : airRunSpeed);
	}

	/** Get Stats **/
	public double getStamina() {
		return stamina;
	}

	public double getIntelligence() {
		return intelligence;
	}

	public double getStrength() {
		return strength;
	}

	public double getWisdom() {
		return wisdom;
	}

	public double getDexterity() {
		return dexterity;
	}

	public double getLuck() {
		return luck;
	}

	public double getMagicalDamage() {
		return magicalDamage;
	}

	public double getPhysicalDamage() {
		return physicalDamage;
	}

	public double getBlock() {
		return block;
	}

	public double getResist() {
		return resistance;
	}

	public double getDodge() {
		return dodge;
	}

	public double getCriticalRate() {
		return criticalRate;
	}

	public double getHitRate() {
		return hitRate;
	}

	/** decrement health of entity with damage, and account for any armor worn **/
	public void damageHealth(double damageFactor, Entity e) {
		long currentTime = System.nanoTime();
		long timePassed = (currentTime - lastDamageTime) / 1000000L; // drop
																		// down
																		// to
																		// milliseconds
		if (timePassed < damageApplyFrequency) // damage every half second,
												// return if too early
			return;
		// damagefactor *= armor? scaling based on armor is meh
		currentHealth -= damageFactor; // decr hp
		lastDamageTime = System.nanoTime(); // set last damage time
		lastDamageSource = e; // set the value for the last entity that damaged
								// this one
	}
	
	public void damageMana(double factor)
	{
		currentMana -= factor;
	}

	/** get time of last damage, used to determine knock back frequency **/
	public long getLastDamageTime() {
		return lastDamageTime;
	}

	public long getDamageFrequency() {
		return damageApplyFrequency;
	}

	/** Set/Get damage value for display **/
	public int getDamageValueForDisplay() {
		return damageForDisplay;
	}

	public void setDamageValueForDisplay(double value) {
		damageForDisplay = (int) Math.round(value);
	}

	public void setJumpHeight(double d) {
		jumpHeight = d;
	}

	/** Get/set Jump State **/
	public boolean isJumping() {
		return isJumping;
	}

	public synchronized void setJumping(boolean flag) {
		if (System.currentTimeMillis() - lastJumpChange < 250)
			entity.setGravity(!flag);
		if (flag && isJumping)
			flag = false;
		isJumping = flag;
		if (flag) {
			jumpTicks = 0;
			entity.setDeltaY(0);
			// To help consitency in jump height, we use the startingY as the
			// tile we jumped from
			jumpStartY = SpaceGame.getInstance().getMap().getCollisionManager()
					.getTileAt(entity.getBotCenter()).getY()
					* SpaceGame.getInstance().getMap().getMapStructure()
							.getTileHeight();
			updateJump();
		}
		lastJumpChange = System.currentTimeMillis();
	}

	/** Get/set Jump Ticks **/
	public int getJumpTicks() {
		return jumpTicks;
	}

	public void setJumpTicks(int jumpTicks) {
		this.jumpTicks = jumpTicks;
	}

	public void updateJump() {
		if (jumpStartY - entity.getY() > jumpHeight) {
			jumpTicks = 0;
			setJumping(false);
		} else {
			entity.setDeltaY(-jumpPower * jumpTicks);
			jumpTicks++;
			lastJumpChange = System.currentTimeMillis();

		}
		// setJumpTicks(jumpTicks + 1);
	}

	/**
	 * determine if the entity is able to attack based on its attack frequency
	 * variable
	 **/
	public boolean canAttack()
	{
		long currentTime = System.nanoTime();
		long timePass = (currentTime - lastAttackTime) / 1000000L;
		if (timePass > attackFrequency)
		{
			if (entity instanceof EntityPlayer) //Determine spell cooldown
			{
				//Get active slot id
				int slot = SpaceGame.getInstance().getMap().getHud().getActiveBarSlot();
				EntityPlayer pl = (EntityPlayer)entity;
				//Get active slot object
				GuiSlot s = GuiSlot.getSlotById(slot, SpaceGame.getInstance().getMap().getHud().getActionBarSlots());
				
				Skill skill = null;
				
				switch(s.getInventoryStack().getType())
				{
					case 1:
						skill = Skill.getSkillByName(s.getInventoryStack().getName());
						break;
				}
				
				if (skill != null)
				{
					if (timePass > skill.getCoolDown() && pl.getStats().getCurrentMana() >= skill.getManaRequired())
					{
						lastAttackTime = System.nanoTime();
						return true;
					}
					else
						return false;
				}
				return false;
			}
			return false;
		}
		else
		return false;
	}

	public void setDamageApplyFrequency(long l) {
		damageApplyFrequency = l;
	}

	/** Returns the last entity that damaged this one **/
	public Entity getLastDamageSource() {
		return lastDamageSource;
	}

	public String toString() {
		return new StringBuilder().append(this.getClass())
				.append(" mana " + mana + "  ")
				.append("cMana " + currentMana + "  ")
				.append("health " + health + "  ")
				.append("cHealth " + currentHealth + "  ")
				.append("level " + currentLevel + "  ")
				.append("experience " + currentExperience + "  ")
				.append("str " + strength + "  ")
				.append("int " + intelligence + "  ")
				.append("wis " + wisdom + "  ")
				.append("dex " + dexterity + "  ")
				.append("luck " + luck + "  ")
				.append("stamina " + stamina + " ")
				.append("run speed" + runSpeed)
				
				.toString();
	}
}