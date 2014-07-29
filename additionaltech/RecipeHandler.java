package additionaltech;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {

	public static void addRecipes(){
		
		//shaped recipes
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockSolarPanel), new Object[]{
				"GGG",
				"QQQ",
				"III",
				'G', Blocks.glass, 'Q', Items.quartz, 'I', Items.iron_ingot
			});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AdditionalTech.proxy.wireCopper, 6), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', "ingotCopper"
			}));
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemInverterCore), new Object[]{
				"WIW",
				"WIW",
				"WIW",
				'W', AdditionalTech.proxy.wireCopper, 'I', Items.iron_ingot
			});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemStageTwoCore), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', AdditionalTech.proxy.itemInverterCore
			});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemStageThreeCore), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', AdditionalTech.proxy.itemStageTwoCore
			});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockSolarInverter), new Object[]{
				"III",
				"ICI",
				"III",
				'C', AdditionalTech.proxy.itemInverterCore, 'I', Items.iron_ingot
			});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockESM), new Object[]{
				"IGI",
				"GRG",
				"IGI",
				'G', Blocks.glass, 'I', AdditionalTech.proxy.ingotCopper, 'R', Blocks.redstone_block
			});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockESM, 1, 1), new Object[]{
			"IGI",
			"GCG",
			"IGI",
			'G', Blocks.glass, 'I', Items.iron_ingot, 'C', AdditionalTech.proxy.blockESMIICore
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockESM, 1, 2), new Object[]{
			"IGI",
			"GCG",
			"IGI",
			'G', Blocks.glass, 'I', Items.gold_ingot, 'C', AdditionalTech.proxy.blockESMIIICore
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockESMIICore), new Object[]{
			"GRG",
			"RGR",
			"GRG",
			'G', AdditionalTech.proxy.dustGold, 'R', Items.redstone
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockESMIIICore), new Object[]{
			"DRD",
			"RDR",
			"DRD",
			'D', AdditionalTech.proxy.dustDiamond, 'R', Items.redstone
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemHeatingElement), new Object[]{
			"III",
			"QQQ",
			"III",
			'I', AdditionalTech.proxy.ingotCopper, 'Q', Items.quartz
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockEFurnace), new Object[]{
			"III",
			"IXI",
			"IHI",
			'I', Items.iron_ingot, 'H', AdditionalTech.proxy.itemHeatingElement
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemGrindstone), new Object[]{
			"XSX",
			"SXS",
			"XSX",
			'S', Blocks.stone
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemIronGrindstone), new Object[]{
			"XIX",
			"IXI",
			"XIX",
			'I', Items.iron_ingot
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemDiamondGrindstone), new Object[]{
			"DID",
			"IXI",
			"DID",
			'I', Items.iron_ingot, 'D', AdditionalTech.proxy.dustDiamond
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.itemMotor), new Object[]{
			"RWR",
			"WIW",
			"RWR",
			'R', Items.redstone, 'W', AdditionalTech.proxy.wireCopper, 'I', Items.iron_ingot
		});
		GameRegistry.addShapedRecipe(new ItemStack(AdditionalTech.proxy.blockGrinder), new Object[]{
			"III",
			"IXI",
			"IMI",
			'I', Items.iron_ingot, 'M', AdditionalTech.proxy.itemMotor
		});
		
		//shapeless recipes
		GameRegistry.addShapelessRecipe(new ItemStack(
				AdditionalTech.proxy.itemBucketSlurry), new ItemStack(
				Items.wheat_seeds), new ItemStack(Items.sugar),
				new ItemStack(Items.water_bucket)
		);
		
		//smelting recipes
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.oreCopper), new ItemStack(AdditionalTech.proxy.ingotCopper), 0.0F);
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustCopper), new ItemStack(AdditionalTech.proxy.ingotCopper), 0.0F);
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustDiamond), new ItemStack(Items.diamond), 0.0F);
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustGold), new ItemStack(Items.gold_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustIron), new ItemStack(Items.iron_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustObsidian), new ItemStack(Blocks.obsidian), 0.0F);
		//GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustSteel), new ItemStack(AdditionalTech.proxy.ingotSteel), 0.0F);
		//GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.dustTin), new ItemStack(AdditionalTech.proxy.ingotTin), 0.0F);

	}
}