package additionaltech.items;

import additionaltech.AdditionalTech;
import net.minecraft.item.Item;

public class ItemDiamondGrindstone extends Item {
	public ItemDiamondGrindstone() {
		super();
		setTextureName("additionaltech:DiamondGrindstone");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setMaxDamage(1000);
		setNoRepair();
	}
}
