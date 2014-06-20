package additionaltech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiEFurnace extends GuiContainer {
    private TileEFurnace tileEntity;
    
    public GuiEFurnace(InventoryPlayer InventoryPlayer, TileEFurnace parTileEntity) {
    	super(new ContainerEFurnace(InventoryPlayer, parTileEntity));
        this.tileEntity = parTileEntity;
        xSize = 176;
		ySize = 200;
	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	fontRendererObj.drawString("Energized Furnace", 6, 5, 4210752);
    	// draws "Inventory" or your regional equivalent
    	fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6,
    					ySize - 96 + 2, 4210752);
    	
    	//draw tooltip for energy level
		int k = (this.width - this.xSize) / 2; // X asis on GUI
		int l = (this.height - this.ySize) / 2; // Y asis on GUI
		int boxX = k + 8;
		int boxY = l + 25;
		int sizeX = 17;
		int sizeY = 51;
		
		if (mouseX > boxX && mouseX < boxX + sizeX) 
		{
			if (mouseY > boxY && mouseY < boxY + sizeY) {
				//do something!!!!
				List list = new ArrayList();
				list.add(tileEntity.energyLevel + " MJ");
				this.drawHoveringText(list, (int) mouseX - k, (int) mouseY - l, fontRendererObj);
			}
			super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		}
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	
    	int cookingProgress;
    	int energyLevel;
		
		final ResourceLocation texture = new ResourceLocation("additionaltech", "textures/gui/efurnace.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		this.drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        cookingProgress = this.tileEntity.getCookProgressScaled(24);
        this.drawTexturedModalRect(guiWidth + 74, guiHeight + 58, 176, 0, cookingProgress + 1, 16);
        
        energyLevel = this.tileEntity.getEnergyLevelScaled(50);
        this.drawTexturedModalRect(guiWidth + 8, guiHeight + 75 - energyLevel, 176, 17, 17, energyLevel + 1);
    }
}
