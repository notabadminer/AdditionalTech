package additionaltech;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {

	public static void addRecipes(){
		
		//shaped recipes
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockSolarPanel), new Object[]{
				"GGG",
				"QQQ",
				"III",
				'G', Blocks.glass, 'Q', Items.quartz, 'I', Items.iron_ingot
			});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryHandler.wireCopper, 6), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', "ingotCopper"
			}));
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemInverterCore), new Object[]{
				"WIW",
				"WIW",
				"WIW",
				'W', RegistryHandler.wireCopper, 'I', Items.iron_ingot
			});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemStageTwoCore), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', RegistryHandler.itemInverterCore
			});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemStageThreeCore), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', RegistryHandler.itemStageTwoCore
			});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockSolarInverter), new Object[]{
				"III",
				"ICI",
				"III",
				'C', RegistryHandler.itemInverterCore, 'I', Items.iron_ingot
			});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockESM), new Object[]{
				"IGI",
				"GRG",
				"IGI",
				'G', Blocks.glass, 'I', RegistryHandler.ingotCopper, 'R', Blocks.redstone_block
			});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemHeatingElement), new Object[]{
			"III",
			"PPP",
			"III",
			'I', RegistryHandler.ingotCopper, 'R', RegistryHandler.plateQuartz
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockEFurnace), new Object[]{
			"III",
			"IXI",
			"IHI",
			'I', Items.iron_ingot, 'H', RegistryHandler.itemHeatingElement
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemGrindstone), new Object[]{
			"XSX",
			"SXS",
			"XSX",
			'S', Blocks.stone
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemIronGrindstone), new Object[]{
			"XIX",
			"IXI",
			"XIX",
			'I', Items.iron_ingot
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemGrindstone), new Object[]{
			"DID",
			"IXI",
			"DID",
			'I', Items.iron_ingot, 'D', RegistryHandler.dustDiamond
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockGrinder), new Object[]{
			"III",
			"IXI",
			"IGI",
			'I', Items.iron_ingot, 'G', RegistryHandler.itemGrindstone
		});
		
		//shapeless recipes
		GameRegistry.addShapelessRecipe(new ItemStack(
				RegistryHandler.plateQuartz), new Object[] { Items.quartz,
				Items.quartz, Items.quartz, Items.quartz });
		GameRegistry.addShapelessRecipe(new ItemStack(
				RegistryHandler.plateCopper), new Object[] {
				RegistryHandler.ingotCopper, RegistryHandler.ingotCopper,
				RegistryHandler.ingotCopper, RegistryHandler.ingotCopper, });
		
		//smelting recipes
		GameRegistry.addSmelting(new ItemStack(RegistryHandler.oreCopper), new ItemStack(RegistryHandler.ingotCopper), 0.0F);
		GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustCopper), new ItemStack(RegistryHandler.ingotCopper), 0.0F);
		GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustDiamond), new ItemStack(Items.diamond), 0.0F);
		GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustGold), new ItemStack(Items.gold_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustIron), new ItemStack(Items.iron_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustObsidian), new ItemStack(Blocks.obsidian), 0.0F);
		//GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustSteel), new ItemStack(RegistryHandler.ingotSteel), 0.0F);
		//GameRegistry.addSmelting(new ItemStack(RegistryHandler.dustTin), new ItemStack(RegistryHandler.ingotTin), 0.0F);

	}
}