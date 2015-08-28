package additionaltech.items;

import additionaltech.AdditionalTech;
import net.minecraft.item.Item;

public class ItemIronGrindstone extends Item {
	public ItemIronGrindstone() {
		super();
		setTextureName("additionaltech:IronGrindstone");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
		setMaxDamage(500);
		setNoRepair();
	}
}
