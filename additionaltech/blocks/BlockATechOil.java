package additionaltech.blocks;

import javax.swing.Icon;

import additionaltech.AdditionalTech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockATechOil extends BlockFluidClassic {
	
	private final Fluid fluid;

	public BlockATechOil(Fluid fluid, Material material) {
		super(fluid, material);
		this.fluid = fluid;
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		}
	
	private IIcon stillIcon;
	private IIcon flowingIcon;
    
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon("additionaltech:oil");
		flowingIcon = register.registerIcon("additionaltech:oil_flowing");
	}
    
    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.canDisplace(world, x, y, z);
    }
    
    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.displaceIfPossible(world, x, y, z);
    }

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z)
	{
	return 0x000000; // HEX color code.
	}


}
