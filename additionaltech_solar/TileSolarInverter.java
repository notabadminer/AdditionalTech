package additionaltech_solar;

import java.util.EnumSet;
import java.util.LinkedList;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;

public class TileSolarInverter extends TileEntity implements IPowerEmitter,
		IPipeConnection, IInventory {

	private ItemStack[] inv = new ItemStack[3];
	public double energy = 0;
	public double energyGenerated;
	public double maxTransfer = 40;
	public double maxEnergy = 1000;
	ForgeDirection orientation = ForgeDirection.UP;
	public ForgeDirection powerReceiverDirection = null;
	public int panelCount = 0;

	public void updateEntity() {
		super.updateEntity();
		if (panelCount == 0) {
			scanArea();
		}
		generatePower();
		 sendPower();
	}
		
	public void scanArea() {
		
		if (this.worldObj.isRemote) {
			return;
		}
		
		int xOffset = 0;
		boolean xNegResult = false;
		boolean xPosResult = false;

		// scan along x axis for solarpanels
		while (xOffset <= 20) {
			xNegResult = (checkSolarPanel(xCoord - xOffset, yCoord, zCoord));
			zScan(xCoord - xOffset, yCoord, zCoord);
			xPosResult = (checkSolarPanel(xCoord + xOffset, yCoord, zCoord));
			zScan(xCoord - xOffset, yCoord, zCoord);
			// kill the scan if we are not finding anything next to the inverter
			if (panelCount == 0 && xOffset >= 1) {
				return;
			}
			if (xOffset < 1 || xNegResult || xPosResult) {
				xOffset++;
			} else return;
		}
	}
	
	public void zScan(int xPOS, int yPOS, int zPOS) {

		int zOffset = 1;
		boolean zNegResult = false;
		boolean zPosResult = false;

		while (zOffset <= 20) {
			zNegResult = (checkSolarPanel(xPOS, yPOS, zPOS - zOffset));
			zPosResult = (checkSolarPanel(xPOS, yPOS, zPOS + zOffset));
				
			if (zNegResult || zPosResult) {
				zOffset++;
			} else return;
		}
	}
	
	public boolean checkSolarPanel(int xPOS, int yPOS, int zPOS) {
		if (worldObj.getBlock(xPOS, yPOS, zPOS) instanceof BlockSolarPanel) {
			//if the solar panel cannot see daylight, disable it
			if (!worldObj.canBlockSeeTheSky(xPOS, yPOS, zPOS)) {
				worldObj.setBlockMetadataWithNotify(xPOS, yPOS, zPOS, 1, 2);
			}
			int blockMeta = worldObj.getBlockMetadata(xPOS, yPOS, zPOS);
			if (blockMeta == 0) {
				panelCount++;
				worldObj.setBlockMetadataWithNotify(xPOS, yPOS, zPOS, 1, 2);
				return true;
			}
		}
		return false;
	}

	public void findPowerReceiver() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(
					xCoord + direction.offsetX, yCoord + direction.offsetY,
					zCoord + direction.offsetZ);
			if (isWoodenConductivePipe(tile, direction)) {
				powerReceiverDirection = direction;
			}
		}
	}
	
	public boolean isWoodenConductivePipe(TileEntity tile, ForgeDirection direction) {
		if (tile instanceof IPowerReceptor && ((IPowerReceptor) tile).getPowerReceiver(direction.getOpposite()) != null) {
			return true;
		} else
		return false;		
	}

	public void generatePower() {
		if (!worldObj.isRemote && worldObj.isDaytime() && (!worldObj.isRaining() && !worldObj.isThundering())) {		
			energyGenerated = panelCount * 0.5;
			energy += energyGenerated;
			if (energy > maxEnergy) {
				energy = maxEnergy;
			}
			else energyGenerated = 0;
		}
	}
	
	public void sendPower() {
		if (!worldObj.isRemote) { // we need to run on the server only
			if (powerReceiverDirection == null) {
				findPowerReceiver();
				return;
			}
			TileEntity tile = worldObj.getTileEntity(xCoord
					+ powerReceiverDirection.offsetX, yCoord
					+ powerReceiverDirection.offsetY, zCoord
					+ powerReceiverDirection.offsetZ);
			if (isWoodenConductivePipe(tile, powerReceiverDirection)) {
				PowerReceiver receiver = ((IPowerReceptor) tile)
						.getPowerReceiver(powerReceiverDirection.getOpposite());
				double send = Math.min(energy, maxTransfer);
				if (send > 0)
				{
					double b = receiver.receiveEnergy(Type.ENGINE,
							Math.min(send, receiver.powerRequest()),
							powerReceiverDirection.getOpposite());
					if (b > 0) {
						energy -= b;
						b = 0;
					}
				}

			}
		}
	}

	/* IPIPECONNECTION */
	@Override
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		if (type == PipeType.POWER && with != orientation) {
			return ConnectOverride.DEFAULT;
		} else
			return ConnectOverride.DISCONNECT;
	}

	/* IPOWEREMITTER */
	@Override
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory",
				Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		try {
			panelCount = tagCompound.getInteger("SolarPanels");
		} catch (Throwable ex2) {
			panelCount = 0;
		}
		try {
			energy = tagCompound.getInteger("EnergyLevel");
		} catch (Throwable ex2) {
			energy = 0;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setInteger("SolarPanels", panelCount);
		tagCompound.setDouble("EnergyLevel", energy);
	}

	// Client Server Sync
	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
		//FMLLog.info("AT: received S35 packet");
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

}