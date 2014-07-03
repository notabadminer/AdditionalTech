package additionaltech.blocks;

import additionaltech.AdditionalTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;


public class BlockOreCopper extends Block {
	
	public BlockOreCopper() {
		super(new Material(MapColor.stoneColor));
		setHardness(3F);
		setResistance(5F);
		setHarvestLevel("pickaxe", 1);
		setBlockTextureName("additionaltech:CopperOre");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
	}

}
