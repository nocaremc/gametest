package spacegame.Gui;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import spacegame.GameImages;
import spacegame.SpaceGame;
import spacegame.Storable.StatsManager;

/** Gui that Displays certain character information, including player stats **/
public class GuiStats extends GuiWindow {
	private StatsManager plStats;
	private GuiLabel labelStam, labelInt, labelStr, labelDex, labelWis,
			labelLuck; // Base stats
	private GuiLabel labelBlock, labelHitRate, labelResist, labelDodge,
			labelCrit, labelPdmg, labelMdmg;
	private GuiSlot headSlot, chestSlot, gloveSlot, feetSlot, shoulderSlot,
			leggSlot;
	private NumberFormat df = DecimalFormat.getNumberInstance();

	public GuiStats(int posX, int posY, int width, int height,
			GuiScreen parentGui) {
		super(posX, posY, width, height, parentGui);
		df.setMaximumFractionDigits(2); // Set rounding mode to 2 digits after .
		// Get player stats
		plStats = SpaceGame.getInstance().getMap().getPlayer().getStats();
		// Create Labels for Core stats
		labelStam = (new GuiLabel(posX + 5, posY + 20, 150, 20, "Stamina: "
				+ df.format(plStats.getStamina()))).addToolTip(
				"Stamina effects Health (250%) and Block (10%).").align(
				align.LEFT);
		labelStr = (new GuiLabel(posX + 5, posY + 45, 150, 20, "Strength: "
				+ df.format(plStats.getStrength())))
				.addToolTip(
						"Strength effects Physical Damage (564198/100k % small), Health (50%), and Block (1%).")
				.align(align.LEFT);
		labelInt = (new GuiLabel(posX + 5, posY + 70, 150, 20, "Intelligence: "
				+ df.format(plStats.getIntelligence())))
				.addToolTip(
						"Intelligence effects Magical Damage (564198/100k % small), Critical Rate (1%), and Mana (50%).")
				.align(align.LEFT);
		labelWis = (new GuiLabel(posX + 5, posY + 95, 150, 20, "Wisdom: "
				+ df.format(plStats.getWisdom()))).addToolTip(
				"Wisdom effects Mana (250%) and Resistance (11%).").align(
				align.LEFT);
		labelDex = (new GuiLabel(posX + 5, posY + 120, 150, 20, "Dexterity: "
				+ df.format(plStats.getDexterity())))
				.addToolTip(
						"Dexterity Effects Hit Rate (25%), Dodge (15%), and Critical Rate (1%).")
				.align(align.LEFT);
		labelLuck = (new GuiLabel(posX + 5, posY + 145, 150, 20, "Luck: "
				+ df.format(plStats.getLuck())))
				.addToolTip(
						"Luck effects Hit Rate (2%), Dodge (2%), and Critical Rate (30%).")
				.align(align.LEFT);
		// Create Labels for sub-stats
		labelBlock = (new GuiLabel(posX + 5, posY + 170, 150, 20, "Block: "
				+ df.format(plStats.getBlock())))
				.addToolTip(
						"Block is an effect that when occuring, greatly reduces physical damage recieved.")
				.align(align.LEFT);
		labelHitRate = (new GuiLabel(posX + 5, posY + 195, 150, 20,
				"Hit Rate: " + df.format(plStats.getHitRate())))
				.addToolTip(
						"Hit Rate effects the ability of an attacker to hit his/her target.")
				.align(align.LEFT);
		labelResist = (new GuiLabel(posX + 5, posY + 220, 150, 20, "Resist: "
				+ df.format(plStats.getResist())))
				.addToolTip(
						"Resist is an effect that when occuring, greatly reduces magical damage recieved.")
				.align(align.LEFT);
		labelDodge = (new GuiLabel(posX + 5, posY + 245, 150, 20, "Dodge: "
				+ df.format(plStats.getDodge()))).addToolTip(
				"Dodge effects the rate at which attack will land, if at all.")
				.align(align.LEFT);
		labelCrit = (new GuiLabel(posX + 5, posY + 270, 150, 20, "Crit Rate: "
				+ df.format(plStats.getCriticalRate())))
				.addToolTip(
						"Critical Hits do 1.5x Damage. You love this stat. Bow before its power :]")
				.align(align.LEFT);
		labelPdmg = (new GuiLabel(posX + 5, posY + 295, 150, 20,
				"Physical Damage: " + df.format(plStats.getPhysicalDamage())))
				.addToolTip(
						"The amount of Physical damage you can dish out on an unarmored opponent.")
				.align(align.LEFT);
		labelMdmg = (new GuiLabel(posX + 5, posY + 320, 150, 20,
				"Magical Damage: " + df.format(plStats.getMagicalDamage())))
				.addToolTip(
						"The amount of Magical damage you can dish out on an unarmored foe.")
				.align(align.LEFT);

		// Create slot objects
		headSlot = (new GuiSlot(0, posX + 482, posY + 70, 37, 58))
				.setCustomImages(GameImages.statsArmorSlots[2],
						GameImages.statsArmorSlots[3]);
		chestSlot = (new GuiSlot(1, posX + 485, posY + 133, 30, 61))
				.setCustomImages(GameImages.statsArmorSlots[10],
						GameImages.statsArmorSlots[11]);
		gloveSlot = (new GuiSlot(2, posX + 515, posY + 151, 10, 19))
				.setCustomImages(GameImages.statsArmorSlots[8],
						GameImages.statsArmorSlots[9]);
		shoulderSlot = (new GuiSlot(4, posX + 448, posY + 133, 26, 26))
				.setCustomImages(GameImages.statsArmorSlots[4],
						GameImages.statsArmorSlots[5]);
		leggSlot = (new GuiSlot(5, posX + 486, posY + 197, 37, 128))
				.setCustomImages(GameImages.statsArmorSlots[0],
						GameImages.statsArmorSlots[1]);
		feetSlot = (new GuiSlot(3, posX + 485, posY + 328, 31, 16))
				.setCustomImages(GameImages.statsArmorSlots[6],
						GameImages.statsArmorSlots[7]);

		addForDrag(labelStam, new Point(5, 20));
		addForDrag(labelStr, new Point(5, 45));
		addForDrag(labelInt, new Point(5, 70));
		addForDrag(labelWis, new Point(5, 95));
		addForDrag(labelDex, new Point(5, 120));
		addForDrag(labelLuck, new Point(5, 145));

		addForDrag(labelBlock, new Point(5, 170));
		addForDrag(labelResist, new Point(5, 220));
		addForDrag(labelDodge, new Point(5, 245));
		addForDrag(labelCrit, new Point(5, 270));
		addForDrag(labelPdmg, new Point(5, 295));
		addForDrag(labelMdmg, new Point(5, 320));
		addForDrag(labelHitRate, new Point(5, 195));

		addForDrag(headSlot, new Point(482, 70));
		addForDrag(chestSlot, new Point(485, 133));
		addForDrag(gloveSlot, new Point(515, 151));
		addForDrag(shoulderSlot, new Point(448, 133));
		addForDrag(leggSlot, new Point(486, 197));
		addForDrag(feetSlot, new Point(485, 328));
	}

	@Override
	public void pack() {
		super.pack();

		labelStam.updateText("Stamina: " + df.format(plStats.getStamina()));
		labelStr.updateText("Strength: " + df.format(plStats.getStrength()));
		labelInt.updateText("Intelligence: "
				+ df.format(plStats.getIntelligence()));
		labelWis.updateText("Wisdom: " + df.format(plStats.getWisdom()));
		labelDex.updateText("Dexterity: " + df.format(plStats.getDexterity()));
		labelLuck.updateText("Luck: " + df.format(plStats.getLuck()));

		labelDodge.updateText("Dodge: " + df.format(plStats.getDodge()));
		labelResist.updateText("Resist: " + df.format(plStats.getResist()));
		labelHitRate.updateText("Hit Rate: " + df.format(plStats.getHitRate()));
		labelCrit.updateText("Crit Rate: "
				+ df.format(plStats.getCriticalRate()));
		labelPdmg.updateText("Physical Damage: "
				+ df.format(plStats.getPhysicalDamage()));
		labelMdmg.updateText("Magical Damage: "
				+ df.format(plStats.getMagicalDamage()));
		labelBlock.updateText("Block: " + df.format(plStats.getBlock()));
	}
}