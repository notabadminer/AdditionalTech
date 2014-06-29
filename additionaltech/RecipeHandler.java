package additionaltech;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {
	private static ItemStack oneSolarPanel = new ItemStack(RegistryHandler.blockSolarPanel);
	private static ItemStack oneCopperOre = new ItemStack(RegistryHandler.oreCopper);
	private static ItemStack oneCopperIngot = new ItemStack(RegistryHandler.ingotCopper);
	private static ItemStack sixCopperWire = new ItemStack(RegistryHandler.wireCopper, 6);
	private static ItemStack oneInverterCore = new ItemStack(RegistryHandler.itemInverterCore);
	private static ItemStack oneStageTwoCore = new ItemStack(RegistryHandler.itemStageTwoCore);
	private static ItemStack oneStageThreeCore = new ItemStack(RegistryHandler.itemStageThreeCore);
	private static ItemStack oneSolarInverter = new ItemStack(RegistryHandler.blockSolarInverter);

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
				'W', RegistryHandler.wireCopper, 'I', Items.iron_ingot
			});
		GameRegistry.addShapedRecipe(oneStageTwoCore, new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', RegistryHandler.itemInverterCore
			});
		GameRegistry.addShapedRecipe(oneStageThreeCore, new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', RegistryHandler.itemStageTwoCore
			});
		GameRegistry.addShapedRecipe(oneSolarInverter, new Object[]{
				"III",
				"ICI",
				"III",
				'C', RegistryHandler.itemInverterCore, 'I', Items.iron_ingot
			});
		GameRegistry.addShapelessRecipe(new ItemStack(
				RegistryHandler.blockEFurnace), new Object[] { Blocks.furnace,
				RegistryHandler.itemInverterCore });
		GameRegistry.addSmelting(oneCopperOre, oneCopperIngot, 0.8F);

	}
}