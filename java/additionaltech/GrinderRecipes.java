package additionaltech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GrinderRecipes {
	
	private static final GrinderRecipes instance = new GrinderRecipes();

	public static GrinderRecipes getInstance() {
		GrinderRecipes.initGrinderRecipes();
		return instance;
	}


	private static Map<Item, ItemStack> grindingList = new HashMap();
	private static Map<Item, Float> experienceList = new HashMap();

	private static void grinderRecipes() {
		addGrinding(Item.getItemFromBlock(Blocks.stone), new ItemStack(Blocks.cobblestone, 1), 0.1F);
		addGrinding(Item.getItemFromBlock(Blocks.cobblestone), new ItemStack(Blocks.gravel, 1), 0.1F);
		addGrinding(Item.getItemFromBlock(Blocks.gravel), new ItemStack(Blocks.sand, 1), 0.2F);
		addGrinding(Item.getItemFromBlock(AdditionalTech.proxy.oreCopper),new ItemStack(AdditionalTech.proxy.dustCopper, 2), 0.7F);
		addGrinding(Item.getItemFromBlock(Blocks.iron_ore), new ItemStack(AdditionalTech.proxy.dustIron, 2), 0.7F);
		addGrinding(Item.getItemFromBlock(Blocks.gold_ore), new ItemStack(AdditionalTech.proxy.dustGold, 2), 1.0F);
		addGrinding(Item.getItemFromBlock(Blocks.diamond_ore), new ItemStack(Items.diamond, 3), 1.0F);
		addGrinding(Items.diamond,new ItemStack(AdditionalTech.proxy.dustDiamond, 1), 1.0F);
		addGrinding(Item.getItemFromBlock(Blocks.obsidian), new ItemStack(AdditionalTech.proxy.dustObsidian, 1), 1.0F);
		addGrinding(Item.getItemFromBlock(Blocks.emerald_ore), new ItemStack(Items.emerald, 2), 1.0F);
		// addGrinding("emerald", new ItemStack(RegistryHandler.dustEmerald, 1),// 1.0F);
		addGrinding(Item.getItemFromBlock(Blocks.coal_ore), new ItemStack(Items.coal, 3), 0.1F);
		addGrinding(Item.getItemFromBlock(Blocks.redstone_ore), new ItemStack(Items.redstone, 3), 0.7F);
		addGrinding(Item.getItemFromBlock(Blocks.lapis_ore), new ItemStack(Items.dye, 3, 4), 0.2F);
		addGrinding(Item.getItemFromBlock(Blocks.quartz_ore), new ItemStack(Items.quartz, 2), 1.0F);
		addGrinding(Items.bone, new ItemStack(Items.dye, 4, 15), 0.3F);
		addGrinding(Item.getItemFromBlock(Blocks.wool), new ItemStack(Items.string, 4), 0.3F);
		addGrinding(Items.quartz, new ItemStack(AdditionalTech.proxy.dustQuartz), 0.5F);
		addGrinding(Item.getItemFromBlock(Blocks.glass), new ItemStack(AdditionalTech.proxy.dustGlass), 0.5F);
		addGrinding(Items.iron_ingot, new ItemStack(AdditionalTech.proxy.dustIron, 1), 0.2F);
		addGrinding(Items.gold_ingot, new ItemStack(AdditionalTech.proxy.dustGold, 1), 0.3F);
		addGrinding(AdditionalTech.proxy.ingotCopper, new ItemStack(AdditionalTech.proxy.dustCopper, 1), 0.2F);
		addGrinding(Items.blaze_rod, new ItemStack(Items.blaze_powder, 3), 0.4F);
	}
	
	public static void initGrinderRecipes() {
		grinderRecipes();
	}

	public static void addGrinding(Item input, ItemStack output, float xp) {
		grindingList.put(input, output);
		experienceList.put(input, Float.valueOf(xp));
	}

	public ItemStack getGrindingResult(Item item) {
		return (ItemStack) grindingList.get(item);
	}

	public Map getGrindingList() {
		return this.grindingList;
	}

	public static float getExperience(Item item) {
		return experienceList.containsKey(item) ? (experienceList.get(item)) : 0.0F;
	}
}