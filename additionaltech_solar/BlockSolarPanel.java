package additionaltech_solar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class BlockSolarPanel extends Block {

	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	protected IIcon blockIconTop;

	public BlockSolarPanel() {
		super(new Material(MapColor.stoneColor));
		setStepSound(Block.soundTypeMetal);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		setHardness(5.0F);
		setBlockTextureName("additionaltech_solar:SolarPanelTop");
		setLightOpacity(0);
		setCreativeTab(CreativeTabs.tabMisc);
		setHarvestLevel("pickaxe", 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("additionaltech_solar:SolarPanel");
		blockIconTop = par1IconRegister.registerIcon("additionaltech_solar:SolarPanelTop");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		if (side == 1) {
			return blockIconTop;
		} else {
			return blockIcon;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isOpaqueCube() {
		return false;
	}


}
