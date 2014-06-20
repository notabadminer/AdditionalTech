package additionaltech;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
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


public class BlockEFurnace extends BlockContainer{
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	protected IIcon blockIconFace;
	private boolean isActive;

	public BlockEFurnace() {
		super(new Material(MapColor.stoneColor));
		setStepSound(Block.soundTypeMetal);
		setHardness(5.0F);
		setBlockTextureName("additionaltech:EFurnaceFace");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("additionaltech:Casing");
		blockIconFace = par1IconRegister.registerIcon("additionaltech:"+ (this.isActive ? "EFurnaceFaceActive" : "EFurnaceFace"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		return meta == 0 && side == 3 ? blockIconFace : (side == meta ? blockIconFace : blockIcon);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {
		{
			int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

			switch (l)
			{
			case 0:
			world.setBlockMetadataWithNotify(i, j, k, 2, l);
			break;

			case 1:
			world.setBlockMetadataWithNotify(i, j, k, 5, l);
			break;

			case 2:
			world.setBlockMetadataWithNotify(i, j, k, 3, l);
			break;

			case 3:
			world.setBlockMetadataWithNotify(i, j, k, 4, l);
			break;
			}
		}
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
				return false;
		}
		tileEntity.getDescriptionPacket(); //update the tile entity on gui open
		player.openGui(AdditionalTech.instance, 1, world, x, y, z);
		return true;
    }
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEFurnace();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		super.breakBlock(world, x, y, z, block, meta);
	}
}

