package additionaltech_solar;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipeHandler {
	private static ItemStack oneSolarPanel = new ItemStack(AdditionalTech.blockSolarPanel);

	public static void addRecipes(){
		GameRegistry.addShapedRecipe(oneSolarPanel, new Object[]{
				"GGG",
				"QQQ",
				"III",
				'G', Blocks.glass, 'Q', Items.quartz, 'I', Items.iron_ingot
			});

	}
}