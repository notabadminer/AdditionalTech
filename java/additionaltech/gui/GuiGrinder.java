package additionaltech.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import additionaltech.inventory.ContainerGrinder;
import additionaltech.tile.TileGrinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiGrinder extends GuiContainer {
    private TileGrinder tileEntity;
    
    public GuiGrinder(InventoryPlayer InventoryPlayer, TileGrinder parTileEntity) {
    	super(new ContainerGrinder(InventoryPlayer, parTileEntity));
        this.tileEntity = parTileEntity;
        xSize = 176;
		ySize = 200;
	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	fontRendererObj.drawString("Grinder", 6, 5, 4210752);
    	// draws "Inventory" or your regional equivalent
    	fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6,
    					ySize - 96 + 2, 4210752);
    	
    	//draw tooltip for energy level
		int k = (this.width - this.xSize) / 2; // X asis on GUI
		int l = (this.height - this.ySize) / 2; // Y asis on GUI
		int boxX = k + 7;
		int boxY = l + 18;
		int sizeX = tileEntity.batteryPresent() ? 8 : 17;
		int sizeY = 80;
		
		if (mouseX > boxX && mouseX < boxX + sizeX) 
		{
			if (mouseY > boxY && mouseY < boxY + sizeY) {
				//do something!!!!
				List list = new ArrayList();
				list.add(tileEntity.getEnergyLevel() + " RF");
				this.drawHoveringText(list, (int) mouseX - k, (int) mouseY - l, fontRendererObj);
			}
			super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		}
		
		if (tileEntity.batteryPresent()) {
		//draw tooltip for battery level
				k = (this.width - this.xSize) / 2; // X asis on GUI
				l = (this.height - this.ySize) / 2; // Y asis on GUI
				boxX = k + 16;
				boxY = l + 18;
				sizeX = 8;
				sizeY = 80;
				
				if (mouseX > boxX && mouseX < boxX + sizeX) 
				{
					if (mouseY > boxY && mouseY < boxY + sizeY) {
						//do something!!!!
						List list = new ArrayList();
						list.add("ESM: " + tileEntity.batteryLevel + " RF");
						this.drawHoveringText(list, (int) mouseX - k, (int) mouseY - l, fontRendererObj);
					}
					super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				}
		}
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	
    	int cookingProgress;
    	int energyLevel;
    	int batteryLevel;
		
		final ResourceLocation texture = new ResourceLocation("additionaltech", "textures/gui/grinder.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		this.drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        cookingProgress = this.tileEntity.getCookProgressScaled(22);
        this.drawTexturedModalRect(guiWidth + 74, guiHeight + 58, 176, 0, cookingProgress + 1, 16);
        
        if (tileEntity.batteryPresent()) {
        	this.drawTexturedModalRect(guiWidth + 15, guiHeight + 18, 192, 17, 8, 97);
        	
        	energyLevel = this.tileEntity.getEnergyLevelScaled(78);
            this.drawTexturedModalRect(guiWidth + 8, guiHeight + 97 - energyLevel, 176, 17, 6, energyLevel);
            
            batteryLevel = this.tileEntity.getBatteryLevelScaled(78);
            this.drawTexturedModalRect(guiWidth + 17, guiHeight + 97 - batteryLevel, 176, 17, 6, batteryLevel);
        } else {      
        	energyLevel = this.tileEntity.getEnergyLevelScaled(78);
        	this.drawTexturedModalRect(guiWidth + 8, guiHeight + 97 - energyLevel, 176, 17, 16, energyLevel);
        }
    }
}
