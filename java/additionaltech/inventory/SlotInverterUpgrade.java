package additionaltech.inventory;

import additionaltech.AdditionalTech;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotInverterUpgrade extends Slot {

	public SlotInverterUpgrade(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
		// TODO Auto-generated constructor stub
	}

	@Override
	   public boolean isItemValid(ItemStack itemstack) {
		Item stackItem = itemstack.getItem();
		return stackItem == AdditionalTech.proxy.itemInverterCore
				|| stackItem == AdditionalTech.proxy.itemStageTwoCore
				|| stackItem == AdditionalTech.proxy.itemStageThreeCore;
	   }

	   @Override
	   public int getSlotStackLimit() {
	      return 1;
	   }
}
