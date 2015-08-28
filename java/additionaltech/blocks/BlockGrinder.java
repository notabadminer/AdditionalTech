package additionaltech.blocks;

import java.util.Random;

import additionaltech.AdditionalTech;
import additionaltech.tile.TileGrinder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockGrinder extends BlockContainer{
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	protected IIcon blockIconFace;
	protected IIcon blockIconFaceActive;

	public BlockGrinder() {
		super(new Material(MapColor.stoneColor));
		setStepSound(Block.soundTypeMetal);
		setHardness(5.0F);
		setBlockTextureName("additionaltech:GrinderFace");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("additionaltech:Casing");
		blockIconFaceActive = par1IconRegister.registerIcon("additionaltech:GrinderFaceActive");
		blockIconFace = par1IconRegister.registerIcon("additionaltech:GrinderFace");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){

		if (meta > 5) {
			return meta == 0 && side == 6 ? blockIconFaceActive : (side + 6 == meta ? blockIconFaceActive : blockIcon);
		} else 
			return meta == 0 && side == 3 ? blockIconFace : (side == meta ? blockIconFace : blockIcon);
	}
	
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);

		return meta > 5 ? 7 : 0;
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
		player.openGui(AdditionalTech.instance, 0, world, x, y, z);
		return true;
    }
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileGrinder();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				dropStack(world, x, y, z, item, rand);
			}
		}
	}
	
	private void dropStack(World world, int x, int y, int z, ItemStack item, Random rand) {
		float rx = rand.nextFloat() * 0.8F + 0.1F;
		float ry = rand.nextFloat() * 0.8F + 0.1F;
		float rz = rand.nextFloat() * 0.8F + 0.1F;

		EntityItem entityItem = new EntityItem(world,
				x + rx, y + ry, z + rz,
				new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

		if (item.hasTagCompound()) {
			entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
		}


		float factor = 0.05F;
		entityItem.motionX = rand.nextGaussian() * factor;
		entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
		entityItem.motionZ = rand.nextGaussian() * factor;
		world.spawnEntityInWorld(entityItem);
		item.stackSize = 0;
	}
}

