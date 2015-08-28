package additionaltech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AdditionalTechTab extends CreativeTabs {

	public AdditionalTechTab(String lable) {
		super(lable);
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(AdditionalTech.proxy.blockHydrothermalliquifactor);
	}

}
