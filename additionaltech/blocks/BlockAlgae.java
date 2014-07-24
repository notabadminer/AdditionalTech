package additionaltech.blocks;

import java.util.Random;

import additionaltech.AdditionalTech;
import additionaltech.RegistryHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockAlgae extends BlockBush {
	private static final int GROWTH_RATE = 4;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;

	public BlockAlgae() {
		super();
		//setCreativeTab(AdditionalTech.tabAdditionalTech);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.001F, 1.0F);
		setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		return blockIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("additionaltech:algae");
	}

	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockLightValue(x, y + 1, z) < 15) {
			return;
		}

		int rInt;
		int blockMeta = world.getBlockMetadata(x, y, z);
		if (blockMeta >= GROWTH_RATE) {
			rInt = random.nextInt(4);
			switch (rInt) {
			case 1:
				if (world.getBlock(x + 1, y - 1, z) == Blocks.water
						&& world.getBlock(x + 1, y, z) == Blocks.air 
						&& world.getBlock(x + 1, y - 2, z) != Blocks.water) {
					world.setBlock(x + 1, y, z, RegistryHandler.blockAlgae, 0, 3);
					world.setBlockMetadataWithNotify(x, y, z, 0, 2);
					return;
				}
			case 2:
				if (world.getBlock(x - 1, y - 1, z) == Blocks.water
						&& world.getBlock(x - 1, y, z) == Blocks.air
						&& world.getBlock(x - 1, y - 2, z) != Blocks.water) {
					world.setBlock(x - 1, y, z, RegistryHandler.blockAlgae, 0, 3);
					world.setBlockMetadataWithNotify(x, y, z, 0, 2);
					return;
				}
			case 3:
				if (world.getBlock(x, y - 1, z + 1) == Blocks.water
						&& world.getBlock(x, y, z + 1) == Blocks.air
						&& world.getBlock(x, y - 2, z + 1) != Blocks.water) {
					world.setBlock(x, y, z + 1, RegistryHandler.blockAlgae, 0, 3);
					world.setBlockMetadataWithNotify(x, y, z, 0, 2);
					return;
				}
			case 4:
				if (world.getBlock(x, y - 1, z - 1) == Blocks.water
						&& world.getBlock(x, y, z - 1) == Blocks.air
						&& world.getBlock(x + 1, y - 2, z - 1) != Blocks.water) {
					world.setBlock(x, y, z - 1, RegistryHandler.blockAlgae, 0, 3);
					world.setBlockMetadataWithNotify(x, y, z, 0, 2);
					return;
				}
			default:
				return;
			}
		}
		else
			world.setBlockMetadataWithNotify(x, y, z, blockMeta + 1, 2);
	}
	
	@Override
	 protected boolean canPlaceBlockOn(Block parBlock)
	    {
	        return parBlock == Blocks.water;
	    }
	
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
		if (par1World.getBlock(par2, par3-1, par4) == Blocks.water) {
			return true;
		}
		return false;
    }
	
	@Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Water;
    }
	
	public int getRenderType() {
        return 0; // Magic number.
    }
	
	@Override
	 public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	    {
	        return RegistryHandler.itemAlgae;
	    }

}
