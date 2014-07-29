package additionaltech.render;

import org.lwjgl.opengl.GL11;

import additionaltech.tile.TileHeatsink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class ItemHeatsinkRenderer implements IItemRenderer {

	private ModelHeatsink model;
	TileEntitySpecialRenderer render;
	private TileEntity dummytile;

	public ItemHeatsinkRenderer(ModelHeatsink model){
		this.model = model;
	}

	public ItemHeatsinkRenderer(TileEntitySpecialRenderer render, TileEntity dummy) {
		 this.render = render;
		 this.dummytile = dummy;
	}

	@Override
	public boolean handleRenderType( ItemStack itemStack, ItemRenderType type ){
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper( ItemRenderType type, ItemStack item, ItemRendererHelper helper ){
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data){
		this.render.renderTileEntityAt(this.dummytile, 0.0D, 0.0D, 0.0D, 0.0F);
	}
}
