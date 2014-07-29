package additionaltech.inventory;

import additionaltech.tile.TileESM;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerESM extends Container {
	
		private TileESM tileEntity;
		private int lastEnergyLevel;
		private int lastMaxEnergy;
		private int lastMaxInput;
		private int lastMaxOutput;
		
		
		public ContainerESM(InventoryPlayer inventoryPlayer, TileESM tEntity) {
			tileEntity = tEntity;
			// the Slot constructor takes the IInventory and the slot number in that
			// it binds to
			// and the x-y coordinates it resides on-screen
			//addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotInput, 50, 58));
					
			// commonly used vanilla code that adds the player's inventory
			bindPlayerInventory(inventoryPlayer);
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer entityplayer) {
			return tileEntity.isUseableByPlayer(entityplayer);
		}
		
		void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 9; j++) {
					addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
							8 + j * 18, 119 + i * 18));
				}
			}
			
			for (int i = 0; i < 9; i++) {
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 177));
			}
		}
		
		@Override
		public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
			ItemStack stack = null;
			Slot slotObject = (Slot) inventorySlots.get(slot);
			// null checks and checks if the item can be stacked (maxStackSize > 1)
			if (slotObject != null && slotObject.getHasStack()) {
				ItemStack stackInSlot = slotObject.getStack();
				stack = stackInSlot.copy();
				
				// merges the item into player inventory since its in the tileEntity
				if (slot < 1) {
					if (!this.mergeItemStack(stackInSlot, 1, 36, true)) {
						return null;
					}
				}
				// places it into the tileEntity is possible since its in the player
				// inventory
				else {
					boolean foundSlot = false;
					for (int i = 0; i < 1; i++){
						if (((Slot)inventorySlots.get(i)).isItemValid(stackInSlot) && this.mergeItemStack(stackInSlot, i, i + 1, false)) {
							foundSlot = true;
							break;
						}
					}
					if (!foundSlot){
						return null;
					}
				}
				
				if (stackInSlot.stackSize == 0) {
					slotObject.putStack(null);
				}
				else {
					slotObject.onSlotChanged();
				}
				
				if (stackInSlot.stackSize == stack.stackSize) {
					return null;
				}
				slotObject.onPickupFromSlot(player, stackInSlot);
			}
			
			return stack;
		}
		
		@Override
		protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards)
		{
			boolean flag1 = false;
			int k = (backwards ? end - 1 : start);
			Slot slot;
			ItemStack itemstack1;

			if (stack.isStackable())
			{
				while (stack.stackSize > 0 && (!backwards && k < end || backwards && k >= start))
				{
					slot = (Slot) inventorySlots.get(k);
					itemstack1 = slot.getStack();

					if (!slot.isItemValid(stack)) {
						continue;
					}

					if (itemstack1 != null && itemstack1.getItem() == stack.getItem() &&
							(!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) &&
							ItemStack.areItemStackTagsEqual(stack, itemstack1))
					{
						int l = itemstack1.stackSize + stack.stackSize;

						if (l <= stack.getMaxStackSize() && l <= slot.getSlotStackLimit()) {
							stack.stackSize = 0;
							itemstack1.stackSize = l;
							tileEntity.markDirty();
							flag1 = true;
						} else if (itemstack1.stackSize < stack.getMaxStackSize() && l < slot.getSlotStackLimit()) {
							stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
							itemstack1.stackSize = stack.getMaxStackSize();
							tileEntity.markDirty();
							flag1 = true;
						}
					}

					k += (backwards ? -1 : 1);
				}
			}

			if (stack.stackSize > 0)
			{
				k = (backwards ? end - 1 : start);

				while (!backwards && k < end || backwards && k >= start) {
					slot = (Slot) inventorySlots.get(k);
					itemstack1 = slot.getStack();

					if (!slot.isItemValid(stack)) {
						continue;
					}

					if (itemstack1 == null) {
						int l = stack.stackSize;

						if (l <= slot.getSlotStackLimit()) {
							slot.putStack(stack.copy());
							stack.stackSize = 0;
							tileEntity.markDirty();
							flag1 = true;
							break;
						} else {
							putStackInSlot(k, new ItemStack(stack.getItem(), slot.getSlotStackLimit(), stack.getItemDamage()));
							stack.stackSize -= slot.getSlotStackLimit();
							tileEntity.markDirty();
							flag1 = true;
						}
					}

					k += (backwards ? -1 : 1);
				}
			}

			return flag1;
		}
		
		/**
	     * Looks for changes made in the container, sends them to every listener.
	     */
	    public void detectAndSendChanges()
	    {
	        super.detectAndSendChanges();

	        for (int i = 0; i < this.crafters.size(); ++i)
	        {
	            ICrafting icrafting = (ICrafting)this.crafters.get(i);

	            if (this.lastEnergyLevel != this.tileEntity.energyLevel)
	            {
	                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.energyLevel);
	            }
	            if (this.lastMaxEnergy != this.tileEntity.maxEnergy)
	            {
	                icrafting.sendProgressBarUpdate(this, 1, (int) this.tileEntity.maxEnergy);
	            }
	            if (this.lastMaxInput != this.tileEntity.maxInput)
	            {
	                icrafting.sendProgressBarUpdate(this, 2, (int) this.tileEntity.maxInput);
	            }
	            if (this.lastMaxOutput != this.tileEntity.maxOutput)
	            {
	                icrafting.sendProgressBarUpdate(this, 3, (int) this.tileEntity.maxOutput);
	            }
	        }

	        this.lastEnergyLevel = this.tileEntity.energyLevel;
	    }
	    
	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int par1, int par2)
	    {
	        if (par1 == 0) {
	            this.tileEntity.energyLevel = par2;
	        }
	        if (par1 == 1) {
	            this.tileEntity.maxEnergy = par2;
	        }
	        if (par1 == 2) {
	            this.tileEntity.maxInput = par2;
	        }
	        if (par1 == 3) {
	            this.tileEntity.maxOutput = par2;
	        }
	    }
}
