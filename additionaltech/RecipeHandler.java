package additionaltech;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {
	private static ItemStack oneSolarPanel = new ItemStack(AdditionalTech.blockSolarPanel);
	private static ItemStack oneCopperOre = new ItemStack(AdditionalTech.oreCopper);
	private static ItemStack oneCopperIngot = new ItemStack(AdditionalTech.ingotCopper);
	private static ItemStack sixCopperWire = new ItemStack(AdditionalTech.wireCopper, 6);
	private static ItemStack oneInverterCore = new ItemStack(AdditionalTech.itemInverterCore);
	private static ItemStack oneStageTwoCore = new ItemStack(AdditionalTech.itemStageTwoCore);
	private static ItemStack oneStageThreeCore = new ItemStack(AdditionalTech.itemStageThreeCore);
	private static ItemStack oneSolarInverter = new ItemStack(AdditionalTech.blockSolarInverter);

	public static void addRecipes(){
		GameRegistry.addShapedRecipe(oneSolarPanel, new Object[]{
				"GGG",
				"QQQ",
				"III",
				'G', Blocks.glass, 'Q', Items.quartz, 'I', Items.iron_ingot
			});
		GameRegistry.addRecipe(new ShapedOreRecipe(sixCopperWire, new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', "ingotCopper"
			}));
		GameRegistry.addShapedRecipe(oneInverterCore, new Object[]{
				"WIW",
				"WIW",
				"WIW",
				'W', AdditionalTech.wireCopper, 'I', Items.iron_ingot
			});
		GameRegistry.addShapedRecipe(oneStageTwoCore, new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', AdditionalTech.itemInverterCore
			});
		GameRegistry.addShapedRecipe(oneStageThreeCore, new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', AdditionalTech.itemStageTwoCore
			});
		GameRegistry.addShapedRecipe(oneSolarInverter, new Object[]{
				"III",
				"ICI",
				"III",
				'C', AdditionalTech.itemInverterCore, 'I', Items.iron_ingot
			});
		GameRegistry.addShapelessRecipe(new ItemStack(
				AdditionalTech.blockEFurnace), new Object[] { Blocks.furnace,
				AdditionalTech.itemInverterCore });
		GameRegistry.addSmelting(oneCopperOre, oneCopperIngot, 0.8F);

	}
}