package additionaltech.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import additionaltech.AdditionalTech;
import additionaltech.RegistryHandler;
import additionaltech.tile.TileESM;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockESM extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon[] blockIcon;

	final static String[] SUBNAMES = new String[] { "I", "II", "III" };

	public BlockESM() {
		super(new Material(MapColor.stoneColor));
		setStepSound(Block.soundTypeMetal);
		setHardness(5.0F);
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setHarvestLevel("pickaxe", 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = new IIcon[SUBNAMES.length];
		for (int i = 0; i < SUBNAMES.length; i++) {
			blockIcon[i] = par1IconRegister.registerIcon("additionaltech:ESM"
					+ SUBNAMES[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta == 2)
			return blockIcon[2];
		else if (meta == 1)
			return blockIcon[1];
		else
			return blockIcon[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list) {
		for (int i = 0; i < SUBNAMES.length; ++i) {
			list.add(new ItemStack(block, 1, i));
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			if (player.getCurrentEquippedItem() != null
					&& player.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
				IToolWrench wrench = (IToolWrench) player
						.getCurrentEquippedItem().getItem();
				if (wrench.canWrench(player, x, y, z)) {
					Random rand = new Random();
					ItemStack stack = getItemStackWithData(world, x, y, z);
					EntityItem entityItem = new EntityItem(world, x, y, z,
							stack);
					if (!world.isRemote)
						world.spawnEntityInWorld(entityItem);
					removedByPlayer(world, player, x, y, z);
					if (player.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
						((IToolWrench) player.getCurrentEquippedItem()
								.getItem()).wrenchUsed(player, x, y, z);
					}
					return true;
				}
			}
		}
		player.openGui(AdditionalTech.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileESM();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		super.breakBlock(world, x, y, z, block, meta);
	}

	public ItemStack getItemStackWithData(World world, int x, int y, int z) {
		ItemStack stack = new ItemStack(world.getBlock(x, y, z), 1,
				world.getBlockMetadata(x, y, z));
		TileEntity tentity = world.getTileEntity(x, y, z);
		if (tentity instanceof TileESM) {
			TileESM te = (TileESM) tentity;
			stack = setItemData(te.energyLevel, te.maxEnergy, te.maxInput,
					te.maxOutput);
			stack.setItemDamage(world.getBlockMetadata(x, y, z));
			return stack;
		} else
			return stack;
	}

	public static ItemStack setItemData(double energyLevel, double maxEnergy,
			double maxInput, double maxOutput) {
		ItemStack temp = new ItemStack(RegistryHandler.blockESM);
		NBTTagCompound tag = temp.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setDouble("EnergyLevel", energyLevel);
		tag.setDouble("MaxEnergy", maxEnergy);
		tag.setDouble("MaxInput", maxInput);
		tag.setDouble("MaxOutput", maxOutput);
		temp.setTagCompound(tag);
		return temp;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase player, ItemStack stack) {
		if (world.isRemote) {
			return;
		}
		// get item meta value and update maxEnergy appropriately
		int meta = stack.getItemDamage();
		int maxEnergy = meta == 0 ? 20000 : (meta == 1 ? 40000 : 60000);

		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileESM) {
			TileESM tentity = (TileESM) te;
			tentity.maxEnergy = maxEnergy;
			tentity.initPowerProvider();
			NBTTagCompound tag = stack.getTagCompound();
			if (tag == null) {
				return;
			}
			tentity.maxInput = tag.getInteger("MaxInput");
			tentity.maxOutput = tag.getInteger("MaxOutput");
			double eLevel = tag.getInteger("EnergyLevel");
			tentity.powerHandler.setEnergy(eLevel);
		}
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}
