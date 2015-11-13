package additionaltech.blocks;

import additionaltech.AdditionalTech;
import additionaltech.tile.TilePhotobioreactor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPhotobioreactorController extends BlockContainer {

	public BlockPhotobioreactorController() {
		super(new Material(MapColor.stoneColor));
		this.setHardness(2F);
		this.setResistance(15F);
		setBlockTextureName("additional_tech:photobioreactor");
		this.setCreativeTab(AdditionalTech.tabAdditionalTech);
	}

	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon, ioIcon, blockFace;

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("additionaltech:photobioreactor");
		ioIcon = par1IconRegister.registerIcon("additionaltech:photobioreactor_io");
		blockFace = par1IconRegister.registerIcon("additionaltech:photobioreactor_face");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta == 2 || meta == 3) {
			if (side == 0 || side == 1) { // top
				return blockIcon;
			} else if (side == meta) { // front
				return blockFace;
			} else if (side == 4 || side == 5) {
				return ioIcon;
			} else {
				return blockIcon;
			}
		} else if (side == 0 || side == 1) { // top
			return blockIcon;
		} else if (side == meta) { // front
			return blockFace;
		} else if (side == 2 || side == 3) {
			return ioIcon;
		} else {
			return blockIcon;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {
		{
			int l = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

			switch (l) {
			case 0:
				world.setBlockMetadataWithNotify(i, j, k, 2, l);

			case 1:
				world.setBlockMetadataWithNotify(i, j, k, 5, l);

			case 2:
				world.setBlockMetadataWithNotify(i, j, k, 3, l);

			case 3:
				world.setBlockMetadataWithNotify(i, j, k, 4, l);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(AdditionalTech.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TilePhotobioreactor();
	}

}
