package additionaltech;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEFurnace extends Container {
	
		private TileEFurnace tileEntity;
		private int lastCookTime;
		private int lastEnergyLevel;
		
		public ContainerEFurnace(InventoryPlayer inventoryPlayer, TileEFurnace tEntity) {
			tileEntity = tEntity;
			// the Slot constructor takes the IInventory and the slot number in that
			// it binds to
			// and the x-y coordinates it resides on-screen
			addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotInput, 50, 58));
			addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotOutput, 109, 58));
			addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotBattery, 8, 79));
			addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotUpgrade1, 152, 42));
			addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotUpgrade2, 152, 60));
			addSlotToContainer(new Slot(tileEntity, TileEFurnace.slotUpgrade3, 152, 78));
					
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
				if (slot <= 6) {
					if (!this.mergeItemStack(stackInSlot, 7, 42, true)) {
						return null;
					}
				}
				// places it into the tileEntity is possible since its in the player
				// inventory
				else {
					boolean foundSlot = false;
					for (int i = 0; i < 7; i++){
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

	            if (this.lastCookTime != this.tileEntity.furnaceCookTime)
	            {
	                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.furnaceCookTime);
	            }
	            if (this.lastEnergyLevel != this.tileEntity.energyLevel)
	            {
	                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.energyLevel);
	            }
	        }

	        this.lastCookTime = this.tileEntity.furnaceCookTime;
	        this.lastEnergyLevel = this.tileEntity.energyLevel;
	    }
	    
	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int par1, int par2)
	    {
	        if (par1 == 0)
	        {
	            this.tileEntity.furnaceCookTime = par2;
	            FMLLog.info("Setting furnace cook time: " + par2);
	        }
	        if (par1 == 1)
	        {
	            this.tileEntity.energyLevel = par2;
	            FMLLog.info("Setting energy level: " + par2);
	        }
	    }
	}
