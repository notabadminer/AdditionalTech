package additionaltech.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import additionaltech.inventory.ContainerPhotobioreactor;
import additionaltech.tile.TilePhotobioreactor;
import cpw.mods.fml.common.FMLLog;

public class GuiPhotobioreactor extends GuiContainer {
    private TilePhotobioreactor tileEntity;
    
    public GuiPhotobioreactor(InventoryPlayer InventoryPlayer, TilePhotobioreactor parTileEntity) {
    	super(new ContainerPhotobioreactor(InventoryPlayer, parTileEntity));
        this.tileEntity = parTileEntity;
        xSize = 175;
		ySize = 165;
	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	fontRendererObj.drawString("Photobioreactor", 6, 5, 4210752);
    	// draws "Inventory" or your regional equivalent
    	fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 2, 4210752);
    	
    	//draw tooltip for water level
		int k = (this.width - this.xSize) / 2; // X asis on GUI
		int l = (this.height - this.ySize) / 2; // Y asis on GUI
		int boxX = k + 70;
		int boxY = l + 16;
		int sizeX = 15;
		int sizeY = 57;
		
		if (mouseX > boxX && mouseX < boxX + sizeX) 
		{
			if (mouseY > boxY && mouseY < boxY + sizeY) {
				List list = new ArrayList();
				list.add(tileEntity.waterLevel + " MB");
				this.drawHoveringText(list, (int) mouseX - k, (int) mouseY - l, fontRendererObj);
			}
			super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		}
		
		// draw tooltip for slurry level
		k = (this.width - this.xSize) / 2; // X asis on GUI
		l = (this.height - this.ySize) / 2; // Y asis on GUI
		boxX = k + 88;
		boxY = l + 16;
		sizeX = 15;
		sizeY = 57;

		if (mouseX > boxX && mouseX < boxX + sizeX) {
			if (mouseY > boxY && mouseY < boxY + sizeY) {
				List list = new ArrayList();
				list.add(tileEntity.slurryLevel + " MB");
				this.drawHoveringText(list, (int) mouseX - k, (int) mouseY - l, fontRendererObj);
			}
			super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		}
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	
    	int cookingProgress;
    	int energyLevel;
    	int batteryLevel;
		
		final ResourceLocation texture = new ResourceLocation("additionaltech", "textures/gui/photobioreactor.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		this.drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

       	int waterLevel = this.tileEntity.getWaterLevelScaled(58);
       	this.drawTexturedModalRect(guiWidth + 70, guiHeight + 74 - waterLevel, 176, 58, 16, waterLevel);
       	
       	int slurryLevel = this.tileEntity.getSlurryLevelScaled(58);
       	this.drawTexturedModalRect(guiWidth + 88, guiHeight + 74 - slurryLevel, 176, 0, 16, slurryLevel);
       	
       	//draw scale in front of everything
       	this.drawTexturedModalRect(guiWidth + 70, guiHeight + 16, 192, 0, 16, 57);
       	this.drawTexturedModalRect(guiWidth + 88, guiHeight + 16, 192, 0, 16, 57);
    }
}
