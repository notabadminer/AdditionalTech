package additionaltech.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import additionaltech.inventory.ContainerESM;
import additionaltech.tile.TileESM;

public class GuiESM extends GuiContainer {
    private TileESM tileEntity;
    private GuiButton inputMinusButton;
    private GuiButton inputPlusButton;
    private GuiButton outputMinusButton;
    private GuiButton outputPlusButton;

	public static final int idInputMinusButton = 0;
	public static final int idInputPlusButton = 1;
	public static final int idOutputMinusButton = 2;
	public static final int idOutputPlusButton = 3;
    
    public GuiESM(InventoryPlayer InventoryPlayer, TileESM parTileEntity) {
    	super(new ContainerESM(InventoryPlayer, parTileEntity));
        this.tileEntity = parTileEntity;
        xSize = 176;
		ySize = 200;
	}
    
    @Override
	public void initGui() {
		super.initGui();
		inputMinusButton = new GuiButton(idInputMinusButton, 74 + (width - xSize) / 2, 39 + (height - ySize) / 2, 14, 14, "-");
		inputPlusButton = new GuiButton(idInputPlusButton, 122 + (width - xSize) / 2, 39 + (height - ySize) / 2, 14, 14, "+");
		outputMinusButton = new GuiButton(idOutputMinusButton, 74 + (width - xSize) / 2, 80 + (height - ySize) / 2, 14, 14, "-");
		outputPlusButton = new GuiButton(idOutputPlusButton, 122 + (width - xSize) / 2, 80 + (height - ySize) / 2, 14, 14, "+");

		buttonList.clear();
		buttonList.add(inputMinusButton);
		buttonList.add(inputPlusButton);
		buttonList.add(outputMinusButton);
		buttonList.add(outputPlusButton);

	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	fontRendererObj.drawString("Energy Storage Module (ESM)", 6, 5, 4210752);
    	// draws "Inventory" or your regional equivalent
    	fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6,
    					ySize - 96 + 2, 4210752);
    	fontRendererObj.drawString(StatCollector.translateToLocal("Max Input"), 81, 26, 4210752);
    	fontRendererObj.drawString(StatCollector.translateToLocal("Max Output"), 78, 68, 4210752);
    	fontRendererObj.drawString(Math.round(tileEntity.maxInput) + "", 98, 43, 4210752);
    	fontRendererObj.drawString(Math.round(tileEntity.maxOutput) + "", 98, 84, 4210752);
    	
    	//draw tooltip for energy level
		int k = (this.width - this.xSize) / 2; // X asis on GUI
		int l = (this.height - this.ySize) / 2; // Y asis on GUI
		int boxX = k + 19;
		int boxY = l + 24;
		int sizeX = 15;
		int sizeY = 70;
		
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
		
		final ResourceLocation texture = new ResourceLocation("additionaltech", "textures/gui/esm.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		this.drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);
        
		int energyLevel = this.tileEntity.getEnergyLevelScaled(70);
        this.drawTexturedModalRect(guiWidth + 19, guiHeight + 94 - energyLevel, 176, 0, 15, energyLevel);
    }
    
    protected void actionPerformed(GuiButton par1GuiButton) {
		tileEntity.configurePowerHandler(par1GuiButton.id);
		tileEntity.sendPacket(par1GuiButton.id);
	}
}
