package additionaltech.items;

import additionaltech.AdditionalTech;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemDustSteel extends Item {

	public ItemDustSteel() {
		super();
		setTextureName("additionaltech:SteelDust");
		setCreativeTab(AdditionalTech.tabAdditionalTech);
	}

}