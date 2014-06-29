package additionaltech;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeConnection.ConnectOverride;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEFurnace extends TileEntity implements IPipeConnection, IPowerReceptor, IInventory {
	
	private PowerHandler powerHandler;
	public int energyCost = 1;
	private ItemStack[] furnaceItemStacks = new ItemStack[6];
    public int furnaceCookTime;
    public int energyLevel;
    public static final int slotInput = 0;
	public static final int slotOutput = 1;
	public static final int slotBattery = 2;
	public static final int slotUpgrade1 = 3;
	public static final int slotUpgrade2 = 4;
	public static final int slotUpgrade3 = 5;

	public TileEFurnace() {
		powerHandler = new PowerHandler(this, Type.MACHINE);
		initPowerProvider();
	}

	private void initPowerProvider() {
		powerHandler.configure(10, 150, 25, 1001);
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
	
		if (worldObj.isRemote) {
			return;
		}
		
		energyLevel = (int) powerHandler.getEnergyStored();
		
		if (this.canSmelt() && energyLevel > 0) {
			furnaceCookTime++;
			powerHandler.useEnergy(energyCost, energyCost, true);

			if (this.furnaceCookTime == 200) {
				this.furnaceCookTime = 0;
				this.smeltItem();
				flag1 = true;
			}
		} else {
			this.furnaceCookTime = 0;
		}

		if (flag1) {
            this.markDirty();
        }
	}
	
	/**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.furnaceItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[1] == null) return true;
            if (!this.furnaceItemStacks[1].isItemEqual(itemstack)) return false;
            int result = furnaceItemStacks[1].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[1] == null)
            {
                this.furnaceItemStacks[1] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[1].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[1].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0)
            {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

	@Override
	public ItemStack getStackInSlot(int par1) {
        return this.furnaceItemStacks[par1];
    }

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
        if (this.furnaceItemStacks[par1] != null){
            ItemStack itemstack;

            if (this.furnaceItemStacks[par1].stackSize <= par2) {
                itemstack = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                return itemstack;
            }
            else {
                itemstack = this.furnaceItemStacks[par1].splitStack(par2);

                if (this.furnaceItemStacks[par1].stackSize == 0) {
                    this.furnaceItemStacks[par1] = null;
                }

                return itemstack;
            }
        }
        else {
            return null;
        }
    }
	
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.furnaceItemStacks[par1] != null) {
            ItemStack itemstack = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
	public String getInventoryName()
    {
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
        return this.furnaceCookTime * progress / 200;
    }
    
    public int getEnergyLevelScaled(int scale) {
        return this.energyLevel * scale / (int) this.powerHandler.getMaxEnergyStored();
    }
    
	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		// TODO Auto-generated method stub
		return false;
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
		for (int i = 0; i < furnaceItemStacks.length; i++) {
			ItemStack stack = furnaceItemStacks[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setInteger("EnergyLevel", energyLevel);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory",
				Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < furnaceItemStacks.length) {
				furnaceItemStacks[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		try {
			energyLevel = tagCompound.getInteger("EnergyLevel");
		} catch (Throwable ex2) {
			energyLevel = 0;
		}
	}

}
