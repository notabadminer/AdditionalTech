package additionaltech.tile;

import additionaltech.AdditionalTech;
import additionaltech.net.ATButtonMessage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEFurnace extends TileEntity implements IEnergyReceiver, IInventory, ISidedInventory {

	private int rfLevel = 0;
	private int rfMax = 10000;
	public int energyCost = 20;
	private ItemStack[] inventory = new ItemStack[6];
	public int furnaceTimer;
	public int furnaceCookTime = 200;
	public boolean isActive;
	public boolean lastActive;
	public int batteryLevel;
	public int batteryMax;
	public boolean currentState = false;
	public static final int slotInput = 0;
	public static final int slotOutput = 1;
	public static final int slotBattery = 2;
	public static final int slotUpgrade1 = 3;
	public static final int slotUpgrade2 = 4;
	public static final int slotUpgrade3 = 5;
	private static final int[] slots_top = new int[] { 0, 1, 2, 3, 4 };
	private static final int[] slots_bottom = new int[] { 0, 1, 2, 3, 4 };
	private static final int[] slots_sides = new int[] { 0, 1, 2, 3, 4 };

	public TileEFurnace() {
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}

		if (this.canSmelt() && rfLevel > energyCost) {
			isActive = true;
			furnaceTimer++;
			rfLevel -= energyCost;

			if (this.furnaceTimer >= this.furnaceCookTime) {
				this.furnaceTimer = 0;
				this.smeltItem();
			}
		} else {
			isActive = false;
			this.furnaceTimer = 0;
		}
		accessBattery();

		if (isActive != lastActive) {
			lastActive = isActive;
			updateBlock();
		}
	}

	private void updateBlock() {
		// we set block meta plus or minus to change status burning/idle
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (isActive) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta + 6, 2);
		} else
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta - 6, 2);
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
	 */
	private boolean canSmelt() {
		if (this.inventory[slotInput] == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[slotInput]);
			if (itemstack == null)
				return false;
			if (this.inventory[slotOutput] == null)
				return true;
			if (!this.inventory[slotOutput].isItemEqual(itemstack))
				return false;
			int result = inventory[slotOutput].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.inventory[1].getMaxStackSize();
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[slotInput]);

			if (this.inventory[slotOutput] == null) {
				this.inventory[slotOutput] = itemstack.copy();
			} else if (this.inventory[slotOutput].getItem() == itemstack.getItem()) {
				this.inventory[slotOutput].stackSize += itemstack.stackSize;
			}

			--this.inventory[slotInput].stackSize;

			if (this.inventory[slotInput].stackSize <= 0) {
				this.inventory[slotInput] = null;
			}
		}
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
				checkUpgrades();
				return itemstack;
			} else {
				itemstack = this.inventory[par1].splitStack(par2);

				if (this.inventory[par1].stackSize == 0) {
					this.inventory[par1] = null;
				}
				checkUpgrades();
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
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.inventory[par1] = par2ItemStack;
		checkUpgrades();

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "Energized Furnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Returns an integer between 0 and the passed value representing how close the current item is to being completely
	 * cooked
	 */
	public int getCookProgressScaled(int progress) {
		return furnaceTimer * progress / furnaceCookTime;
	}

	public int getEnergyLevel() {
		return rfLevel;
	}

	public void setEnergyLevel(int energy) {
		this.rfLevel = energy;
	}

	public int getEnergyLevelScaled(int scale) {
		return (int) (rfLevel * scale / rfMax);
	}

	public int getBatteryLevelScaled(int scale) {
		if (inventory[slotBattery] != null) {
			return (int) ((batteryLevel * scale) / batteryMax);
		} else
			return 0;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		Item stackItem = stack.getItem();
		if (slot == slotUpgrade1 || slot == slotUpgrade2 || slot == slotUpgrade3) {
			return stackItem == AdditionalTech.proxy.itemHeatingElement;
		} else if (slot == slotBattery) {
			return stackItem == Item.getItemFromBlock(AdditionalTech.proxy.blockESM);
		} else if (slot == slotInput) {
			return true;
		} else
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
		tagCompound.setInteger("EnergyLevel", rfLevel);
		tagCompound.setInteger("EnergyMax", rfMax);
		tagCompound.setInteger("BatteryLevel", batteryLevel);
		tagCompound.setInteger("BatteryMax", batteryMax);
		tagCompound.setBoolean("LastActive", lastActive);
		tagCompound.setInteger("FurnaceTimer", furnaceTimer);
		tagCompound.setInteger("CookTime", furnaceCookTime);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		try {
			rfLevel = tagCompound.getInteger("EnergyLevel");
		} catch (Throwable ex2) {
			rfLevel = 0;
		}
		try {
			rfMax = tagCompound.getInteger("EnergyMax");
		} catch (Throwable ex2) {
			rfMax = 10000;
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
		try {
			lastActive = tagCompound.getBoolean("LastActive");
		} catch (Throwable ex2) {
			lastActive = false;
		}
		try {
			furnaceTimer = tagCompound.getInteger("FurnaceTimer");
		} catch (Throwable ex2) {
			furnaceTimer = 0;
		}
		try {
			furnaceCookTime = tagCompound.getInteger("CookTime");
		} catch (Throwable ex2) {
			furnaceCookTime = 200;
		}
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

	public void checkUpgrades() {
		// reset to default
		energyCost = 20;
		int upgradeCount = 0;
		if (!worldObj.isRemote) {
			for (int i = 0; i < inventory.length; i++) {
				ItemStack stack = inventory[i];
				if (stack != null && stack.getItem() == AdditionalTech.proxy.itemHeatingElement) {
					upgradeCount++;
				}
			}
			if (upgradeCount > 0) {
				int energyCostModified[] = {20, 48, 72, 96};
				energyCost = energyCostModified[upgradeCount];
				furnaceCookTime = (int) (200 / Math.pow(2, upgradeCount));
			} else
				this.furnaceCookTime = 200;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		if (slot == slotInput) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		// allow pulling items from output slot only
		if (slot == slotOutput) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slots_bottom : (side == 1 ? slots_top : slots_sides);
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
}
