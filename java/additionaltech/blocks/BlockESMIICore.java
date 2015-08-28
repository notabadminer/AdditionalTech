package additionaltech.blocks;

import additionaltech.AdditionalTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;


public class BlockESMIICore extends Block {
	
	public BlockESMIICore() {
		super(new Material(MapColor.stoneColor));
		setHardness(3F);
		setResistance(5F);
		setHarvestLevel("pickaxe", 1);
		setBlockTextureName("additionaltech:ESMIICore");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
	}

}
