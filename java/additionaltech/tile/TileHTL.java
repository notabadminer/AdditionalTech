package additionaltech.tile;

import additionaltech.AdditionalTech;
import additionaltech.net.ATButtonMessage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileHTL extends TileEntity implements IEnergyReceiver, IInventory, IFluidHandler, ISidedInventory {
	
	private int rfLevel = 0;
	private int rfMax = 10000;
	public int energyCost = 40;
	private ItemStack[] inventory = new ItemStack[3];
	public FluidTank slurrytank = new FluidTank(8000);
	public FluidTank oiltank = new FluidTank(8000);
	public static int slotInput = 0;
	public static int slotOutput = 1;
	public static int slotBattery = 2;
	public boolean isActive;
	public boolean lastActive;
	public boolean cooldown;
	public int batteryLevel;
	public int batteryMax;
	public int slurryLevel;
	public int oilLevel;
	public int temp = 70;
	public int pressure;
	public boolean isRedstonePowered = false;

	public TileHTL() {
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}
		
		slurryLevel = slurrytank.getFluidAmount();
		oilLevel = oiltank.getFluidAmount();
		
		if (!cooldown && slurryLevel == slurrytank.getCapacity() && isRedstonePowered()) {
			isActive = true;
		}
		
		if (isActive) {
			processSlurry();
		}
				
		if (isActive != lastActive) {
			lastActive = isActive;
			updateBlock();
		}
		
		accessBattery();
	}
	
	private void updateBlock() {
		//we set block meta plus or minus to change status burning/idle
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (meta < 6) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta + 6, 2);
		} else
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta - 6, 2);
	}
	
	public boolean isRedstonePowered() {
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	public void processSlurry() {
		if (rfLevel < 40) {
			cooldown = true;
		}
		// disable button
		if (cooldown) {
			// cool down and depressurize
			if (pressure > 2) {
				//TODO play depressurize sound
				pressure-=2; } else pressure = 0;
			if (temp > 70)
				temp--;
			// set active false
			if (pressure == 0 && temp <= 70) {
				isActive = false;
				cooldown = false;
			}
		} else {
			// pressurize
			if (pressure < 2000) {
				//TODO play compressor sound
				//consume 8k MJ here
				pressure++;
				rfLevel -= energyCost;
				return;
			}
			// raise temp
			if (temp < 600) {
				//consume 2.4k MJ here
				temp++;
				rfLevel -= energyCost;
				return;
			}
			// cook
			if (slurrytank.getFluidAmount() > 0) {
				int cookRate = 1;
				//consume 4k per 1000MB here
				rfLevel -= energyCost;
				slurrytank.drain(cookRate * 3, true);
				oiltank.fill(FluidRegistry.getFluidStack("oil", cookRate), true);
			} else cooldown = true;
		}
	}

	public void processInputs(int slot) {
		if (worldObj.isRemote) {
			return;
		}
		if (inventory[slot] != null && inventory[slot].getItem() == AdditionalTech.proxy.itemBucketSlurry && inventory[slotOutput] == null) {
			inventory[slotInput] = null;
			int used = slurrytank
					.fill(new FluidStack(FluidRegistry.getFluid("algaeslurry"),
							1000), true);
			inventory[slotOutput] = new ItemStack(Items.bucket);
		}
		if (inventory[slot] != null && inventory[slot].getItem() == Items.bucket && oiltank.getFluidAmount() > 1000 
				&& inventory[slotOutput] == null) {
			inventory[slotInput].stackSize--;
			if (inventory[slotInput].stackSize == 0) {
				inventory[slotInput] = null;
			}
			oiltank.drain(1000, true);
			inventory[slotOutput] = new ItemStack(AdditionalTech.proxy.itemBucketOil);
		}
		updateTE();
	}

	@Override
	public int fill(ForgeDirection from, FluidStack incoming, boolean doFill) {
		int used;
		if (!isActive) {
			used = slurrytank.fill(incoming, doFill);
			incoming.amount -= used;
		} else used = 0;
		return used;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		FluidStack used;
		if (!isActive) {
			used = oiltank.drain(10, doDrain);
		} else used = null;
		return used;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack used;
		if (!isActive) {
			used = oiltank.drain(Math.max(10, maxDrain), doDrain);
		} else used = null;
		return used;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == FluidRegistry.getFluid("algaeSlurry");
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] info = new FluidTankInfo[1];
			info[0] = oiltank.getInfo();
			return info;
	}

	public int getSlurryLevelScaled(int scale) {
		return this.slurryLevel * scale / slurrytank.getCapacity();
	}

	public int getOilLevelScaled(int scale) {
		return this.oilLevel * scale / oiltank.getCapacity();
	}
	
	public int getEnergyLevel() {
    	return this.rfLevel;
    }
    
    public void setEnergyLevel(int energy) {
    	this.rfLevel = energy;
    }
    
    @Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		int amountReceived = Math.min(maxReceive, rfMax - rfLevel);

		if (!simulate) {
			rfLevel += amountReceived;
		}

		return amountReceived;
	}
    
    public int getEnergyLevelScaled(int scale) {
        return (this.rfLevel * scale / rfMax);
    }
    
    public int getBatteryLevelScaled(int scale) {
    	if (inventory[slotBattery] != null) {
    		return (int) ((batteryLevel * scale) / batteryMax);
    	} else return 0;
    }

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.inventory[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.inventory[par1] != null) {
			ItemStack itemstack;

			if (this.inventory[par1].stackSize <= par2) {
				itemstack = this.inventory[par1];
				this.inventory[par1] = null;
				return itemstack;
			} else {
				itemstack = this.inventory[par1].splitStack(par2);

				if (this.inventory[par1].stackSize == 0) {
					this.inventory[par1] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.inventory[par1] != null) {
			ItemStack itemstack = this.inventory[par1];
			this.inventory[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
		if (slot == slotInput) {
			processInputs(slot);
		}
	}

	@Override
	public String getInventoryName() {
		return "HydroThermalLiquifactor";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 0
				&& (stack.getItem() == AdditionalTech.proxy.itemBucketSlurry || stack
						.getItem() == Items.bucket)) {
			return true;
		}
		return false;
	}
	
	public void onButtonPressed(int button) {
		if (button == 0 && slurryLevel > 1000) {
			isActive = true;
		}
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
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setInteger("SlurryLevel", slurryLevel);
		tagCompound.setInteger("OilLevel", oilLevel);
		tagCompound.setInteger("Pressure", pressure);
		tagCompound.setInteger("Temp", temp);
		tagCompound.setBoolean("isActive", isActive);
		tagCompound.setInteger("BatteryLevel", batteryLevel);
		tagCompound.setInteger("BatteryMax", batteryMax);		
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory",
				Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		try {
			slurrytank.fill(
					new FluidStack(FluidRegistry.getFluid("algaeslurry"),
							tagCompound.getInteger("SlurryLevel")), true);
		} catch (Throwable ex2) {
			slurryLevel = 0;
		}
		try {
			oiltank.fill(new FluidStack(FluidRegistry.getFluid("oil"),
					tagCompound.getInteger("OilLevel")), true);
		} catch (Throwable ex2) {
			oilLevel = 0;
		}
		try {
			pressure = 	tagCompound.getInteger("Pressure");
		} catch (Throwable ex2) {
			pressure = 0;
		}
		try {
			temp = tagCompound.getInteger("Temp");
		} catch (Throwable ex2) {
			temp = 70;
		}
		try {
			isActive = tagCompound.getBoolean("isActive");
		} catch (Throwable ex2) {
			isActive = false;
		}
		try {
			batteryLevel = tagCompound.getInteger("BatteryLevel");
		} catch (Throwable ex2) {
			batteryLevel = 0;
		}
		try {
			batteryMax = tagCompound.getInteger("BatteryMax");
		} catch (Throwable ex2) {
			batteryMax = 0;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return null;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return false;
	}
	
	public boolean batteryPresent() {
		return inventory[slotBattery] != null;
	}
	
	public void accessBattery() {
		if (this.inventory[slotBattery] != null) {
			NBTTagCompound tag = inventory[slotBattery].getTagCompound();
			if (tag == null) {
				// ESM must be new. We'll init NBT values
				// we need to get tier first
				int esmTier = inventory[slotBattery].getItemDamage();
				tag = new NBTTagCompound();
				tag.setInteger("EnergyLevel", 0);
				tag.setInteger("MaxInput", 400);
				tag.setInteger("MaxOutput", 400);
				tag.setInteger("MaxEnergy", esmTier == 0 ? 200000 : (esmTier == 1 ? 400000 : 600000));
				inventory[slotBattery].setTagCompound(tag);
			}
			batteryLevel = tag.getInteger("EnergyLevel");
			batteryMax = tag.getInteger("MaxEnergy");
			if ((double)rfLevel / (double)rfMax < 0.05 && batteryLevel > 200) {
				tag.setInteger("EnergyLevel", batteryLevel - 200);
				rfLevel += 200;
			} else if ((double)rfLevel / (double)rfMax > 0.95 && batteryLevel < batteryMax) {
				tag.setInteger("EnergyLevel", Math.min(batteryLevel + 200, batteryMax));
				rfLevel -= 200;
			}
		}
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return rfLevel;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return rfMax;
	}

}