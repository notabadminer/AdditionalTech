package additionaltech.tile;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import additionaltech.GrinderRecipes;
import additionaltech.RegistryHandler;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeConnection.ConnectOverride;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

public class TileGrinder extends TileEntity implements IPipeConnection, IPowerReceptor, IInventory, ISidedInventory  {
	
	private PowerHandler powerHandler;
	public int energyCost = 4;
	private ItemStack[] inventory = new ItemStack[7];
    public int grindTimer;
    public int grindTime = 200;
    public boolean isActive;
    public boolean lastActive;
    public int energyLevel;
    public int maxEnergy = 2000;
    public int batteryLevel;
    public float playerXP;
    public double batteryMax;
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
		powerHandler = new PowerHandler(this, Type.MACHINE);
		initPowerProvider();
	}

	private void initPowerProvider() {
		powerHandler.configure(10, 40, 25, maxEnergy);
		powerHandler.configurePowerPerdition(0, 0);
	}

	/* IPIPECONNECTION */
	@Override
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		if (type == PipeType.POWER) {
			return ConnectOverride.DEFAULT;
		} else
			return ConnectOverride.DISCONNECT;
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return powerHandler.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {
	}
	
	@Override
	public void updateEntity() {

		boolean flag1 = false;
		
		if (worldObj.isRemote && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 6) {
			worldObj.playSound(xCoord, yCoord, zCoord, "additionaltech:grinder", 0.4F, 1.0F, true);
		}
	
		if (worldObj.isRemote) {
			return;
		}
		
		energyLevel = (int) powerHandler.getEnergyStored();
		
		if (this.canGrind(slotOutput) || this.canGrind(slotOutput1)) {
			if (energyLevel > energyCost) {
				isActive = true;
				grindTimer++;
				powerHandler.useEnergy(energyCost, energyCost, true);

				if (this.grindTimer >= this.grindTime) {
					this.grindTimer = 0;
					this.grindItem();
					flag1 = true;
				}
			}
		} else {
			isActive = false;
			this.grindTimer = 0;
		}

		if (flag1) {
            this.markDirty();
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
		if (isActive) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta + 6, 2);
		} else
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta - 6, 2);
	}
	
	/**
     * Returns true if the grinder can grind an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGrind(int slot)
    {
        if (this.inventory[slotInput] == null)
        {
            return false;
        }
        else
        {
        	ItemStack itemInStack = this.inventory[slotInput];
        	Item itemForStack = itemInStack.getItem();
        	ItemStack itemstack = GrinderRecipes.getGrindingResult(itemForStack);
            if (itemstack == null) return false;
            if (this.inventory[slot] == null) return true;
            if (!this.inventory[slot].isItemEqual(itemstack)) return false;
            int result = inventory[slot].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.inventory[slot].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the grinder source stack into the appropriate itemstack from grinder recipes
     */
	public void grindItem() {
		ItemStack itemInStack = this.inventory[slotInput];
		Item itemForStack = itemInStack.getItem();
		ItemStack itemstack = GrinderRecipes.getGrindingResult(itemForStack);
		playerXP += GrinderRecipes.getExperience(itemForStack);
		//FMLLog.info("Player XP: " + playerXP);

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
				if(alter >= stack.getMaxDamage()) {
					FMLLog.info("Max damage reached. Grindstone " + slot + " should be destroyed");
					this.inventory[slot].stackSize = 0;
					this.inventory[slot] = null;
				}
				stack.setItemDamage(alter);
				this.inventory[slot] = null;
				this.inventory[slot] = stack;
			}
		}
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public int getSizeInventory()
    {
        return this.inventory.length;
    }

	@Override
	public ItemStack getStackInSlot(int par1) {
        return this.inventory[par1];
    }

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
        if (this.inventory[par1] != null){
            ItemStack itemstack;
            checkUpgrades();

            if (this.inventory[par1].stackSize <= par2) {
                itemstack = this.inventory[par1];
                this.inventory[par1] = null;
                return itemstack;
                //TODO output playerXP here
            }
            else {
                itemstack = this.inventory[par1].splitStack(par2);

                if (this.inventory[par1].stackSize == 0) {
                    this.inventory[par1] = null;
                }

                return itemstack;
                //TODO and here
            }
        }
        else {
            return null;
        }
    }
	
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.inventory[par1] != null) {
            ItemStack itemstack = this.inventory[par1];
            this.inventory[par1] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inventory[par1] = par2ItemStack;
        checkUpgrades();

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
	public String getInventoryName()
    {
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
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * destroyed
     */
    public int getCookProgressScaled(int progress) {
        return this.grindTimer * progress / this.grindTime;
    }
    
    public int getEnergyLevelScaled(int scale) {
        return this.energyLevel * scale / (int) this.powerHandler.getMaxEnergyStored();
    }
    
    public int getBatteryLevelScaled(int scale) {
    	if (inventory[slotBattery] != null) {
    		return (int) ((batteryLevel * scale) / batteryMax);
    	} else return 0;
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
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
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
		tagCompound.setInteger("EnergyLevel", energyLevel);
		tagCompound.setBoolean("LastActive", lastActive);
		tagCompound.setInteger("EnergyCost", energyCost);
		tagCompound.setInteger("GrindTime", grindTime);
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
			energyLevel = tagCompound.getInteger("EnergyLevel");
		} catch (Throwable ex2) {
			energyLevel = 0;
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
	}
	
	public boolean batteryPresent() {
		return inventory[slotBattery] != null;
	}
	
	public void accessBattery() {
		if (this.inventory[slotBattery] != null) {
			NBTTagCompound tag = inventory[slotBattery].getTagCompound();
			if (tag == null) {
				//ESM must be new. We'll init NBT values
				//we need to get tier first
				int esmTier = inventory[slotBattery].getItemDamage();
				tag = new NBTTagCompound();
				tag.setInteger("EnergyLevel", 0);
				tag.setDouble("MaxInput", 40);
				tag.setDouble("MaxOutput", 40);
				tag.setDouble("MaxEnergy", esmTier == 0 ? 20000 : (esmTier == 1 ? 40000 : 60000));
				inventory[slotBattery].setTagCompound(tag);
			}
			batteryLevel = tag.getInteger("EnergyLevel");
			batteryMax = tag.getDouble("MaxEnergy");
			if ((double)energyLevel / (double)maxEnergy < 0.05 && batteryLevel > 40) {
				tag.setInteger("EnergyLevel", batteryLevel - 40);
				powerHandler.setEnergy(energyLevel + 40);
			} else if ((double)energyLevel / (double)maxEnergy > 0.95
					&& batteryLevel < batteryMax) {
				tag.setInteger("EnergyLevel", batteryLevel + 40);
				powerHandler.useEnergy(40, 40, true);
			}
		}
	}
	
	public void checkUpgrades() {
		//reset to default
		energyCost = 4;
		int upgradeCount = 0;
		if (!worldObj.isRemote) {
			for (int i = 0; i < inventory.length; i++) {
				ItemStack stack = inventory[i];
				if (stack != null && (stack.getItem() == RegistryHandler.itemGrindstone 
						|| stack.getItem() == RegistryHandler.itemIronGrindstone 
						|| stack.getItem() == RegistryHandler.itemDiamondGrindstone)) {
					upgradeCount++;
				}
			}
			if (upgradeCount > 0) {
				energyCost = (int) Math.pow(2,upgradeCount + 0.2);
				grindTime = (int) (200 / Math.pow(2,upgradeCount));
			} else  this.grindTime = 200;
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
		//allow pulling items from output slot only
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
}
