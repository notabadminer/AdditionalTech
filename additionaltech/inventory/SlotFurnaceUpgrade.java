package additionaltech.inventory;

import additionaltech.RegistryHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotFurnaceUpgrade extends Slot {

	public SlotFurnaceUpgrade(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
		// TODO Auto-generated constructor stub
	}

	@Override
	   public boolean isItemValid(ItemStack itemstack) {
		Item stackItem = itemstack.getItem();
	      return stackItem == RegistryHandler.itemHeatingElement;
	   }

	   @Override
	   public int getSlotStackLimit() {
	      return 1;
	   }
}
