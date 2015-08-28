package additionaltech.proxy;

import additionaltech.AdditionalTech;
import additionaltech.render.HeatsinkRenderer;
import additionaltech.render.ItemHeatsinkRenderer;
import additionaltech.tile.TileHeatsink;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
	
	
	@Override
	public void registerRenderers() {
		int regID;
		TileEntitySpecialRenderer render = new HeatsinkRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeatsink.class, render);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AdditionalTech.proxy.blockHeatsink), new ItemHeatsinkRenderer(render, new TileHeatsink()));
	
	}

}
