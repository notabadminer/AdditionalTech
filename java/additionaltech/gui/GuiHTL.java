package additionaltech.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import additionaltech.inventory.ContainerHTL;
import additionaltech.tile.TileHTL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiHTL extends GuiContainer {
	private TileHTL tileEntity;
	
	private GuiButton powerButton;
	public static final int idPowerButton = 0;

	public GuiHTL(InventoryPlayer InventoryPlayer, TileHTL parTileEntity) {
		super(new ContainerHTL(InventoryPlayer, parTileEntity));
		this.tileEntity = parTileEntity;
		xSize = 176;
		ySize = 200;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		powerButton = new GuiButton(idPowerButton, 56 + (width - xSize) / 2, 80 + (height - ySize) / 2, 36, 14, "Power");
		buttonList.clear();
		buttonList.add(powerButton);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString("HydroThermalLiquifactor", 6, 5, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(
				StatCollector.translateToLocal("container.inventory"), 6,
				ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int mouseX,
			int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int cookingProgress;
		int energyLevel;
		int batteryLevel;
		
		//disable power button if machine is active
		powerButton.enabled = !tileEntity.isActive;

		final ResourceLocation texture = new ResourceLocation("additionaltech",
				"textures/gui/htl.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		this.drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);
		
		int tempScaled = this.tileEntity.temp * 39 / 800;
		this.drawTexturedModalRect(guiWidth + 62,
				guiHeight + 63 - tempScaled, 176, 79, 8, tempScaled);
		
		int pressureScaled = this.tileEntity.pressure * 39 / 2800;
		this.drawTexturedModalRect(guiWidth + 75,
				guiHeight + 63 - pressureScaled, 176, 79, 8, pressureScaled);

		int slurryLevel = this.tileEntity.getSlurryLevelScaled(70);
		this.drawTexturedModalRect(guiWidth + 101,
				guiHeight + 94 - slurryLevel, 200, 0, 8, slurryLevel);

		int oilLevel = this.tileEntity.getOilLevelScaled(70);
		this.drawTexturedModalRect(guiWidth + 115, guiHeight + 94 - oilLevel,
				208, 0, 8, oilLevel);

		if (tileEntity.batteryPresent()) {
			this.drawTexturedModalRect(guiWidth + 17, guiHeight + 19, 192, 0,
					8, 99);

			energyLevel = this.tileEntity.getEnergyLevelScaled(79);
			this.drawTexturedModalRect(guiWidth + 10, guiHeight + 99
					- energyLevel, 176, 0, 6, energyLevel);

			batteryLevel = this.tileEntity.getBatteryLevelScaled(79);
			this.drawTexturedModalRect(guiWidth + 19, guiHeight + 99
					- batteryLevel, 182, 0, 6, batteryLevel);
		} else {
			energyLevel = this.tileEntity.getEnergyLevelScaled(79);
			this.drawTexturedModalRect(guiWidth + 10, guiHeight + 99
					- energyLevel, 176, 0, 15, energyLevel);
		}
		
		// draw tooltip for energy level
				int boxX = guiWidth + 8;
				int boxY = guiHeight + 20;
				int sizeX = tileEntity.batteryPresent() ? 8 : 17;
				int sizeY = 80;

				if (mouseX > boxX && mouseX < boxX + sizeX) {
					if (mouseY > boxY && mouseY < boxY + sizeY) {
						// do something!!!!
						List list = new ArrayList();
						list.add(tileEntity.getEnergyLevel() + " RF");
						this.drawHoveringText(list, (int) mouseX, (int) mouseY,
								fontRendererObj);
					}
					super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				}

				if (tileEntity.batteryPresent()) {
					// draw tooltip for battery level
					boxX = guiWidth + 18;
					boxY = guiHeight + 20;
					sizeX = 8;
					sizeY = 80;

					if (mouseX > boxX && mouseX < boxX + sizeX) {
						if (mouseY > boxY && mouseY < boxY + sizeY) {
							// do something!!!!
							List list = new ArrayList();
							list.add("ESM: " + tileEntity.batteryLevel + " RF");
							this.drawHoveringText(list, (int) mouseX, (int) mouseY, fontRendererObj);
						}
						super.drawGuiContainerForegroundLayer(mouseX, mouseY);
					}
				}
				
				// draw tooltip for temp
				boxX = guiWidth + 62;
				boxY = guiHeight + 24;
				sizeX = 8;
				sizeY = 39;

				if (mouseX > boxX && mouseX < boxX + sizeX) {
					if (mouseY > boxY && mouseY < boxY + sizeY) {
						// do something!!!!
						List list = new ArrayList();
						list.add("Temp");
						list.add(tileEntity.temp + " F");
						this.drawHoveringText(list, (int) mouseX, (int) mouseY,
								fontRendererObj);
					}
					super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				}

				// draw tooltip for pressure
				boxX = guiWidth + 75;
				boxY = guiHeight + 24;
				sizeX = 8;
				sizeY = 39;

				if (mouseX > boxX && mouseX < boxX + sizeX) {
					if (mouseY > boxY && mouseY < boxY + sizeY) {
						// do something!!!!
						List list = new ArrayList();
						list.add("Pressure");
						list.add(tileEntity.pressure + " PSI");
						this.drawHoveringText(list, (int) mouseX, (int) mouseY,
								fontRendererObj);
					}
					super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				}

				// draw tooltip for slurry level
				boxX = guiWidth + 100;
				boxY = guiHeight + 24;
				sizeX = 7;
				sizeY = 70;

				if (mouseX > boxX && mouseX < boxX + sizeX) {
					if (mouseY > boxY && mouseY < boxY + sizeY) {
						// do something!!!!
						List list = new ArrayList();
						list.add("Algae");
						list.add(tileEntity.slurryLevel + " MB");
						this.drawHoveringText(list, (int) mouseX, (int) mouseY,
								fontRendererObj);
					}
					super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				}

				// draw tooltip for oil level
				boxX = guiWidth + 115;
				boxY = guiHeight + 24;
				sizeX = 7;
				sizeY = 70;

				if (mouseX > boxX && mouseX < boxX + sizeX) {
					if (mouseY > boxY && mouseY < boxY + sizeY) {
						// do something!!!!
						List list = new ArrayList();
						list.add("Oil");
						list.add(tileEntity.oilLevel + " MB");
						this.drawHoveringText(list, (int) mouseX, (int) mouseY,
								fontRendererObj);
					}
					super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				}
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		tileEntity.sendPacket(par1GuiButton.id, false);
	}
}
