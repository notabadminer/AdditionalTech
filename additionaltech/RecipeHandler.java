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
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockESM, 1, 1), new Object[]{
			"IGI",
			"GCG",
			"IGI",
			'G', Blocks.glass, 'I', Items.iron_ingot, 'C', RegistryHandler.blockESMIICore
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockESM, 1, 2), new Object[]{
			"IGI",
			"GCG",
			"IGI",
			'G', Blocks.glass, 'I', Items.gold_ingot, 'C', RegistryHandler.blockESMIIICore
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockESMIICore), new Object[]{
			"GRG",
			"RGR",
			"GRG",
			'G', RegistryHandler.dustGold, 'R', Items.redstone
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockESMIIICore), new Object[]{
			"DRD",
			"RDR",
			"DRD",
			'D', RegistryHandler.dustDiamond, 'R', Items.redstone
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemHeatingElement), new Object[]{
			"III",
			"QQQ",
			"III",
			'I', RegistryHandler.ingotCopper, 'Q', Items.quartz
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
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemDiamondGrindstone), new Object[]{
			"DID",
			"IXI",
			"DID",
			'I', Items.iron_ingot, 'D', RegistryHandler.dustDiamond
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.itemMotor), new Object[]{
			"RWR",
			"WIW",
			"RWR",
			'R', Items.redstone, 'W', RegistryHandler.wireCopper, 'I', Items.iron_ingot
		});
		GameRegistry.addShapedRecipe(new ItemStack(RegistryHandler.blockGrinder), new Object[]{
			"III",
			"IXI",
			"IMI",
			'I', Items.iron_ingot, 'M', RegistryHandler.itemMotor
		});
		
		//shapeless recipes
		GameRegistry.addShapelessRecipe(new ItemStack(
				RegistryHandler.itemBucketSlurry), new ItemStack(
				Items.wheat_seeds), new ItemStack(Items.sugar),
				new ItemStack(Items.water_bucket)
		);
		
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