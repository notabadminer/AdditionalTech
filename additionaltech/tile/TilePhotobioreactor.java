package additionaltech.tile;

import cpw.mods.fml.common.FMLLog;
import additionaltech.RegistryHandler;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeConnection.ConnectOverride;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TilePhotobioreactor extends TileEntity implements IPipeConnection, IInventory, IFluidHandler {
		private ItemStack[] inventory = new ItemStack[2];
		public FluidTank watertank = new FluidTank(10000);
		public FluidTank slurrytank = new FluidTank(10000);
		public static int slotInput = 0;
		public static int slotOutput = 1;
		public int waterLevel;
		public int slurryLevel;
		public int counter;
		public int totalGrowth = 1000;

		public void TileEntityPhotobioreactor() {
			inventory = new ItemStack[2];
		}
		
		@Override
		public void updateEntity() {		
			if (worldObj.isRemote) {
				return;
			}
			waterLevel = watertank.getFluidAmount();
			slurryLevel = slurrytank.getFluidAmount();
			counter++;
			if (counter == 10) {
				growAlgae();
				counter = 0;
			}
		}
		
		public void growAlgae() {
			if (/*worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) &&*/ worldObj.isDaytime()) {
				FMLLog.info("we can grow");
				int algaeGrowth = (slurrytank.getFluidAmount() / 1000);
				FMLLog.info("growth: " + algaeGrowth);
				if (watertank.getFluidAmount() > 0 && inventory[slotInput] != null) {
					if (inventory[slotInput].getItem() == Items.sugar) {
						totalGrowth += algaeGrowth;
						watertank.drain(algaeGrowth, true);
						slurrytank.fill(FluidRegistry.getFluidStack("algaeslurry",algaeGrowth), true);
						if (totalGrowth >= 1000) {
							totalGrowth = 0;
							inventory[slotInput].stackSize--;
							if (inventory[slotInput].stackSize == 0) {
								inventory[slotInput] = null;
							}
						}
					}
				}
			}
		}
		
		
		public void processInputs(int slot){
			if (worldObj.isRemote) {
				return;
			}
			if (inventory[slot].getItem() == Items.water_bucket) {
				inventory[slotInput] = null;
				int used = watertank.fill(new FluidStack(FluidRegistry.WATER, 1000),true);
				inventory[slotOutput] = new ItemStack(Items.bucket);
			}
			if (inventory[slot].getItem() == RegistryHandler.itemBucketSlurry) {
				inventory[slotInput] = null;
				int used = slurrytank.fill(new FluidStack(FluidRegistry.getFluid("algaeslurry"), 1000),true);
				inventory[slotOutput] = new ItemStack(Items.bucket);
			}
		}

		@Override
		public int fill(ForgeDirection from, FluidStack incoming, boolean doFill) {
			int used = watertank.fill(incoming, doFill);
			incoming.amount -= used;
			return used;
		}

		@Override
		public FluidStack drain(ForgeDirection from, FluidStack resource,
				boolean doDrain) {
			return null;
		}

		@Override
		public FluidStack drain(ForgeDirection from, int maxDrain,
				boolean doDrain) {
			return null;
		}

		@Override
		public boolean canFill(ForgeDirection from, Fluid fluid) {
			return true;
		}

		@Override
		public boolean canDrain(ForgeDirection from, Fluid fluid) {
			//TODO drain algae slurry down to 1000MB
			return true;
		}

		@Override
		public FluidTankInfo[] getTankInfo(ForgeDirection from) {
			return null;
		}
		
		public int getWaterLevelScaled(int scale) {
	        return this.waterLevel * scale / watertank.getCapacity();
	    }
		
		public int getSlurryLevelScaled(int scale) {
	        return this.slurryLevel * scale / slurrytank.getCapacity();
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

	            if (this.inventory[par1].stackSize <= par2) {
	                itemstack = this.inventory[par1];
	                this.inventory[par1] = null;
	                return itemstack;
	            }
	            else {
	                itemstack = this.inventory[par1].splitStack(par2);

	                if (this.inventory[par1].stackSize == 0) {
	                    this.inventory[par1] = null;
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
		public void setInventorySlotContents(int slot, ItemStack stack)
	    {
	        this.inventory[slot] = stack;

	        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        {
	            stack.stackSize = this.getInventoryStackLimit();
	        }
	        if (slot == slotInput) {
		        processInputs(slot);
	        }
	    }

		@Override
		public String getInventoryName() {
			return "Photobioreactor";
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
			if (slot == 0 && (stack.getItem() == Items.water_bucket 
					|| stack.getItem() == RegistryHandler.itemBucketSlurry 
					|| stack.getItem() == Items.sugar)) {
				return true;
			}
			return false;
		}

		@Override
		public ConnectOverride overridePipeConnection(PipeType type,ForgeDirection with) {
			if (type == PipeType.FLUID) {
				return ConnectOverride.CONNECT;
			} else
				return ConnectOverride.DISCONNECT;
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
			tagCompound.setInteger("WaterLevel", waterLevel);
			tagCompound.setInteger("SlurryLevel", slurryLevel);
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
				watertank.fill(new FluidStack( FluidRegistry.WATER, tagCompound.getInteger("WaterLevel")), true);
			} catch (Throwable ex2) {
				waterLevel = 0;
			}
			try {
				slurrytank.fill(new FluidStack(FluidRegistry.getFluid("algaeslurry"), tagCompound.getInteger("SlurryLevel")), true);
			} catch (Throwable ex2) {
				slurryLevel = 0;
			}
		}
		
}