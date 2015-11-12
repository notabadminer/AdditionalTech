package additionaltech.tile;

import additionaltech.AdditionalTech;
import additionaltech.net.ATButtonMessage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileESM extends TileEntity implements IEnergyHandler, IInventory {

	private ItemStack[] inventoryItemStacks = new ItemStack[1];
	public int rfLevel;
	public int maxInput = 400;
	public int maxOutput = 400;
	public int rfMax = 200000;
	public ForgeDirection powerReceiverDirection = null;
	public boolean isRedstonePowered = false;

	public TileESM() {
	}

	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote) return;
		checkRedstonePower();
		if (!isRedstonePowered) {
			transferEnergy();
		}
	}

	public void findPowerReceiver() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
			if (tile != null && tile instanceof IEnergyReceiver) {
				IEnergyReceiver reciever = (IEnergyReceiver) tile;
				int maxEnergy = reciever.receiveEnergy(direction.getOpposite(), Math.min(this.rfLevel, maxOutput), true);
				if (maxEnergy > 0) {
					powerReceiverDirection = direction;
					return;
				}
			}
		}
	}

	public void checkRedstonePower() {
		isRedstonePowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	public double getEnergyLevelScaled(int scale) {
		return this.rfLevel * scale / rfMax;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return false;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	public void sendPacket(int button, boolean shiftPressed) {
		AdditionalTech.snw.sendToServer(new ATButtonMessage(xCoord, yCoord, zCoord, button, shiftPressed));
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	public void updateTE() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagCompound tag = new NBTTagCompound();
		tagCompound.setInteger("MaxInput", maxInput);
		tagCompound.setInteger("MaxOutput", maxOutput);
		tagCompound.setInteger("MaxEnergy", rfMax);
		tagCompound.setInteger("EnergyLevel", rfLevel);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		try {
			maxInput = tagCompound.getInteger("MaxInput");
		} catch (Throwable ex2) {
			// fail quietly
		}
		try {
			maxOutput = tagCompound.getInteger("MaxOutput");
		} catch (Throwable ex2) {
			// fail quietly
		}
		try {
			rfMax = tagCompound.getInteger("MaxEnergy");
		} catch (Throwable ex2) {
			// fail quietly
		}
		try {
			rfLevel = tagCompound.getInteger("EnergyLevel");
		} catch (Throwable ex2) {
			// fail quietly
		}
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		int amountReceived = Math.min(maxReceive, rfMax - rfLevel);

		if (!simulate) {
			rfLevel += amountReceived;
		}

		return amountReceived;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return rfLevel;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return rfMax;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		FMLLog.info("in extractEnergy");
		int amountExtracted = Math.min(maxExtract, rfLevel);

		if (!simulate) {
			rfLevel -= amountExtracted;
		}

		return amountExtracted;
	}

	protected void transferEnergy() {
		if (powerReceiverDirection == null) {
			//do nothing
		} else {
			TileEntity tile = worldObj.getTileEntity(xCoord + powerReceiverDirection.offsetX,
					yCoord + powerReceiverDirection.offsetY, zCoord + powerReceiverDirection.offsetZ);
			if (tile != null && tile instanceof IEnergyReceiver) {
				int used = ((IEnergyReceiver) tile).receiveEnergy(powerReceiverDirection.getOpposite(), Math.min(maxOutput, rfLevel), false);
				if (used > 0) {
					rfLevel -= used;
					updateTE();
				}
			} else {
				//power receiver moved. reset
				powerReceiverDirection = null;
			}
		}
	}
}
