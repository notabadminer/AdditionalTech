package additionaltech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GrinderRecipes {
	private GrinderRecipes() {
		
	}

	/** The list of grinding results. */
	private static Map<ItemStack, ItemStack> grinderList = new HashMap();
	private static Map<ItemStack, Integer> grinderMultiplier = new HashMap();
	private static Map<ItemStack, Float> grindingExperience = new HashMap();

	private static void grinderRecipes() {
		addGrinding(new ItemStack(Blocks.stone), new ItemStack(Blocks.cobblestone), 1, 0.1F);
		addGrinding(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.gravel), 1, 0.1F);
		addGrinding(new ItemStack(Blocks.gravel), new ItemStack(Blocks.sand), 1, 0.2F);
		addGrinding(new ItemStack(RegistryHandler.oreCopper), new ItemStack(RegistryHandler.dustCopper), 2, 0.7F);
		addGrinding(new ItemStack(Blocks.iron_ore), new ItemStack(RegistryHandler.dustIron), 2, 0.7F);
		addGrinding(new ItemStack(Blocks.gold_ore), new ItemStack(RegistryHandler.dustGold), 2, 1.0F);
		addGrinding(new ItemStack(Blocks.diamond_ore), new ItemStack(Items.diamond), 3, 1.0F);
		addGrinding(new ItemStack(Items.diamond), new ItemStack(RegistryHandler.dustDiamond), 3, 1.0F);
		addGrinding(new ItemStack(Blocks.obsidian), new ItemStack(RegistryHandler.dustObsidian), 3, 1.0F);
		addGrinding(new ItemStack(Blocks.emerald_ore), new ItemStack(Items.emerald), 2, 1.0F);
		//addGrinding("emerald", new ItemStack(RegistryHandler.dustEmerald), 1, 1.0F);
		addGrinding(new ItemStack(Blocks.coal_ore), new ItemStack(Items.coal), 3, 0.1F);
		addGrinding(new ItemStack(Blocks.redstone_ore), new ItemStack(Items.redstone), 3, 0.7F);
		addGrinding(new ItemStack(Blocks.lapis_ore), new ItemStack(Items.dye, 1, 4), 3, 0.2F);
		addGrinding(new ItemStack(Blocks.quartz_ore), new ItemStack(Items.quartz), 2, 1.0F);
		addGrinding(new ItemStack(Items.bone), new ItemStack(Items.dye, 1, 15), 4, 0.3F);
		addGrinding(new ItemStack(Blocks.wool), new ItemStack(Items.string), 4, 0.3F);
	}

	/**
	 * Adds a grinding recipe.
	 */
	public static void addGrinding(ItemStack input, ItemStack output, int multiplier, float experience) {
		grinderList.put(input, output);
		grinderMultiplier.put(input, multiplier);
		grindingExperience.put(output, Float.valueOf(experience));
	}
	
	public Map getGrindingList() {
		return grinderList;
	}

	/**
	 * Returns the grinding result of an item.
	 */
	public static ItemStack getGrindingResult(ItemStack itemStack) {
		itemStack.stackSize = 1;
		return grinderList.get(itemStack);
	}
	
	public static Integer getGrindingMultiplier(ItemStack itemStack) {
		return grinderMultiplier.get(itemStack);
	}

	public static float getGrindingExperience(ItemStack itemStack) {
		return grindingExperience.containsKey(itemStack) ? ((Float) grindingExperience
				.get(itemStack).floatValue()) : 0.0F;
	}
	
	public static void initGrinderRecipes() {
		grinderRecipes();
	}
}
