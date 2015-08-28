package additionaltech.blocks;

import additionaltech.AdditionalTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;


public class BlockESMIIICore extends Block {
	
	public BlockESMIIICore() {
		super(new Material(MapColor.stoneColor));
		setHardness(3F);
		setResistance(5F);
		setHarvestLevel("pickaxe", 1);
		setBlockTextureName("additionaltech:ESMIIICore");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
	}

}
