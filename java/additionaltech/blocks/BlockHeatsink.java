package additionaltech.blocks;

import additionaltech.AdditionalTech;
import additionaltech.tile.TileHeatsink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHeatsink extends BlockContainer {

	public BlockHeatsink() {
		super(new Material(MapColor.stoneColor));
		setStepSound(Block.soundTypeMetal);
		setHardness(5.0F);
		setBlockTextureName("additionaltech:copper");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setHarvestLevel("pickaxe", 1);
	}
	
	//This will tell minecraft not to render any side of our cube.
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
	   return false;
	}

	//And this tell it that you can see through this block, and neighbor blocks should be rendered.
	public boolean isOpaqueCube()
	{
	   return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileHeatsink();
	}
}
