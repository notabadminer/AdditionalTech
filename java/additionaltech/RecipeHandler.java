package additionaltech;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {

	public static void addRecipes(){
		
		//shaped recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AdditionalTech.proxy.wireCopper, 6), new Object[]{
				"CCC",
				"XXX",
				"XXX",
				'C', "ingotCopper"
			}));
		
		
		//shapeless recipes
		GameRegistry.addShapelessRecipe(new ItemStack(
				AdditionalTech.proxy.itemBucketSlurry), new ItemStack(
				Items.wheat_seeds), new ItemStack(Items.sugar),
				new ItemStack(Items.water_bucket)
		);
		
		//smelting recipes
		GameRegistry.addSmelting(new ItemStack(AdditionalTech.proxy.oreCopper), new ItemStack(AdditionalTech.proxy.ingotCopper), 0.0F);
	}
}