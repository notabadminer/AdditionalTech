package additionaltech.proxy;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import additionaltech.AdditionalTech;
import additionaltech.render.HeatsinkRenderer;
import additionaltech.render.ItemHeatsinkRenderer;
import additionaltech.render.ModelHeatsink;
import additionaltech.tile.TileHeatsink;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	
	@Override
	public void registerRenderers() {
		int regID;
		TileEntitySpecialRenderer render = new HeatsinkRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeatsink.class, render);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AdditionalTech.proxy.blockHeatsink), new ItemHeatsinkRenderer(render, new TileHeatsink()));
	
	}

}
