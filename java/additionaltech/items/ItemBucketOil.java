package additionaltech.items;

import additionaltech.AdditionalTech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBucket;


public class ItemBucketOil extends ItemBucket {
	
	public ItemBucketOil() {
		super(AdditionalTech.proxy.blockOil);
		this.setMaxStackSize(1);
        this.setCreativeTab(AdditionalTech.tabAdditionalTech);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon) {
		itemIcon = icon.registerIcon("additionaltech:BucketOil");
	}
}
