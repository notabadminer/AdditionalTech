package additionaltech.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import additionaltech.inventory.ContainerSolarInverter;
import additionaltech.tile.TileSolarInverter;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiSolarInverter extends GuiContainer {
	
	private TileSolarInverter tileEntity;
	private GuiButton resetButton;
	public static final int idResetButton = 0;
		
	public GuiSolarInverter(InventoryPlayer inventoryPlayer,
			TileSolarInverter parTileEntity) {
		super(new ContainerSolarInverter(inventoryPlayer, parTileEntity));
		tileEntity = parTileEntity;
		xSize = 176;
		ySize = 200;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		resetButton = new GuiButton(idResetButton, 135 + (width - xSize) / 2, 104 + (height - ySize) / 2, 35, 11, "Reset");
		buttonList.clear();
		buttonList.add(resetButton);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString("Solar Inverter", 6, 5, 4210752);
		fontRendererObj.drawString("Panels Connected: " + tileEntity.panelCount, 18, 26, 4210752);
		fontRendererObj.drawString("Max Panels:  " + tileEntity.panelMax, 18, 39, 4210752);
		fontRendererObj.drawString("Generating: " + tileEntity.energyGenerated + " MJ", 18, 52, 4210752);
		fontRendererObj.drawString("Stored: " + Math.round(tileEntity.energy) + " MJ", 18, 65, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(
				StatCollector.translateToLocal("container.inventory"), 6,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		final ResourceLocation texture = new ResourceLocation("additionaltech", "textures/gui/solarinverter.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		this.drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);		
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		/*if (par1GuiButton.id == idResetButton) {
			tileEntity.onResetButtonPressed();
		}*/
		tileEntity.sendPacket(par1GuiButton.id);
	}
}
