package additionaltech.items;

import additionaltech.AdditionalTech;
import net.minecraft.item.Item;

public class ItemGrindstone extends Item {
	public ItemGrindstone() {
		super();
		setTextureName("additionaltech:Grindstone");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setMaxDamage(100);
		setNoRepair();
	}
}
