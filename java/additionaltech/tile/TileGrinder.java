package additionaltech.tile;

import additionaltech.AdditionalTech;
import additionaltech.GrinderRecipes;
import additionaltech.net.ATButtonMessage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
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

public class TileGrinder extends TileEntity implements IEnergyReceiver, IInventory, ISidedInventory {

	private int rfLevel = 0;
	private int rfMax = 10000;
	public int energyCost = 40;
	private ItemStack[] inventory = new ItemStack[7];
	public int grindTimer;
	public int grindTime = 200;
	public boolean isActive;
	public boolean lastActive;
	public int batteryLevel;
	public float playerXP;
	public Integer batteryMax;
	public boolean currentState = false;
	public static final int slotInput = 0;
	public static final int slotOutput = 1;
	public static final int slotOutput1 = 2;
	public static final int slotBattery = 3;
	public static final int slotUpgrade1 = 4;
	public static final int slotUpgrade2 = 5;
	public static final int slotUpgrade3 = 6;
	private static final int[] slots_top = new int[] { 0, 1, 2 };
	private static final int[] slots_bottom = new int[] { 0, 1, 2 };
	private static final int[] slots_sides = new int[] { 0, 1, 2 };

	public TileGrinder() {
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 6) {
			worldObj.playSound(xCoord, yCoord, zCoord, "additionaltech:grinder", 0.4F, 1.0F, true);
		}
		
		if (worldObj.isRemote) return;

		if (this.canGrind(slotOutput) || this.canGrind(slotOutput1)) {
			if (rfLevel > energyCost) {
				isActive = true;
				grindTimer++;
				rfLevel -= energyCost;

				if (this.grindTimer >= this.grindTime) {
					this.grindTimer = 0;
					this.grindItem();
				}
			}
		} else {
			isActive = false;
			this.grindTimer = 0;
		}
		if (isActive != lastActive) {
			lastActive = isActive;
			updateBlock();
		}
		accessBattery();
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
	 * Returns true if the grinder can grind an item, i.e. has a source item,
	 * destination stack isn't full, etc.
	 */
	private boolean canGrind(int slot) {
		if (this.inventory[slotInput] == null || (this.inventory[slotUpgrade1] == null
				&& this.inventory[slotUpgrade2] == null && this.inventory[slotUpgrade3] == null)) {
			return false;
		} else {
			ItemStack itemstack = GrinderRecipes.getInstance().getGrindingResult(this.inventory[slotInput].getItem());
			if (itemstack == null) {
				return false;
			}
			if (this.inventory[slot] == null)
				return true;
			if (!this.inventory[slot].isItemEqual(itemstack))
				return false;
			int result = inventory[slot].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.inventory[slot].getMaxStackSize();
		}
	}

	/**
	 * Turn one item from the grinder source stack into the appropriate
	 * itemstack from grinder recipes
	 */
	public void grindItem() {
		ItemStack itemstack = GrinderRecipes.getInstance().getGrindingResult(this.inventory[slotInput].getItem());
		playerXP += GrinderRecipes.getExperience(this.inventory[slotInput].getItem());
		// FMLLog.info("Player XP: " + playerXP);

		// find the free slot. if this fails, get out of here.
		int freeSlot;
		if (canGrind(slotOutput)) {
			freeSlot = slotOutput;
		} else if (canGrind(slotOutput1)) {
			freeSlot = slotOutput1;
		} else {
			return;
		}

		if (this.inventory[freeSlot] == null) {
			this.inventory[freeSlot] = itemstack.copy();
		} else if (this.inventory[freeSlot].getItem() == itemstack.getItem()) {
			this.inventory[freeSlot].stackSize += itemstack.stackSize;
		}

		--this.inventory[slotInput].stackSize;
		damageGrindstones();

		if (this.inventory[slotInput].stackSize <= 0) {
			this.inventory[slotInput] = null;
		}
	}

	public void damageGrindstones() {
		for (int slot = slotUpgrade1; slot <= slotUpgrade3; slot++) {
			ItemStack stack = inventory[slot];
			if (stack != null) {
				int alter = stack.getItemDamage() + 1;
				if (alter >= stack.getMaxDamage()) {
					inventory[slot] = null;
				} else {
					stack.setItemDamage(alter);
					inventory[slot] = stack;
				}
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
				// TODO output playerXP here
			} else {
				itemstack = this.inventory[par1].splitStack(par2);

				if (this.inventory[par1].stackSize == 0) {
					this.inventory[par1] = null;
				}
				checkUpgrades();
				return itemstack;
				// TODO and here
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
		return "Grinder";
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
	 * Returns an integer between 0 and the passed value representing how close
	 * the current item is to being completely destroyed
	 */
	public int getCookProgressScaled(int progress) {
		return this.grindTimer * progress / this.grindTime;
	}

	public int getEnergyLevel() {
		return (int) this.rfLevel;
	}

	public void setEnergyLevel(int energy) {
		this.rfLevel = energy;
	}

	public int getEnergyLevelScaled(int scale) {
		return (int) (this.rfLevel * scale / rfMax);
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
		return true;
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
		tagCompound.setBoolean("LastActive", lastActive);
		tagCompound.setInteger("EnergyCost", energyCost);
		tagCompound.setInteger("GrindTime", grindTime);
		tagCompound.setInteger("GrindTimer", grindTimer);
		tagCompound.setInteger("BatteryLevel", batteryLevel);
		tagCompound.setInteger("BatteryMax", batteryMax);
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
			lastActive = tagCompound.getBoolean("LastActive");
		} catch (Throwable ex2) {
			lastActive = false;
		}
		try {
			energyCost = tagCompound.getInteger("EnergyCost");
		} catch (Throwable ex2) {
			energyCost = 4;
		}
		try {
			grindTime = tagCompound.getInteger("GrindTime");
		} catch (Throwable ex2) {
			grindTime = 200;
		}
		try {
			grindTimer = tagCompound.getInteger("GrindTimer");
		} catch (Throwable ex2) {
			grindTimer = 0;
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
		energyCost = 4;
		int upgradeCount = 0;
		if (!worldObj.isRemote) {
			for (int i = 0; i < inventory.length; i++) {
				ItemStack stack = inventory[i];
				if (stack != null && (stack.getItem() == AdditionalTech.proxy.itemGrindstone
						|| stack.getItem() == AdditionalTech.proxy.itemIronGrindstone
						|| stack.getItem() == AdditionalTech.proxy.itemDiamondGrindstone)) {
					upgradeCount++;
				}
			}
			if (upgradeCount > 0) {
				energyCost = (int) Math.pow(2, upgradeCount + 0.2);
				grindTime = (int) (200 / Math.pow(2, upgradeCount));
			} else
				this.grindTime = 200;
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

	/*
	 * @Override public Packet getDescriptionPacket() { return
	 * PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityGrinder(this));
	 * }
	 */
}
