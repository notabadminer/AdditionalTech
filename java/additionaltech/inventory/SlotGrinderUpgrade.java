package additionaltech.inventory;

import additionaltech.AdditionalTech;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotGrinderUpgrade extends Slot {

	public SlotGrinderUpgrade(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	   public boolean isItemValid(ItemStack itemstack) {
		Item stackItem = itemstack.getItem();
	    	if (stackItem == AdditionalTech.proxy.itemGrindstone 
	    			|| stackItem == AdditionalTech.proxy.itemIronGrindstone
	    			|| stackItem == AdditionalTech.proxy.itemDiamondGrindstone) {
	    		return true;
	    	} else return false;
	   }

	   @Override
	   public int getSlotStackLimit() {
	      return 1;
	   }
}
