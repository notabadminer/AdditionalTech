package additionaltech.tile;

import additionaltech.AdditionalTech;
import additionaltech.blocks.BlockSolarPanel;
import additionaltech.net.ATButtonMessage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSolarInverter extends TileEntity implements IEnergyProvider, IInventory {

	private ItemStack[] inventory;
	private final int invSize = 3;
	public static final int upgradeSlot0 = 0;
	public static final int upgradeSlot1 = 1;
	public static final int upgradeSlot2 = 2;

	public int rfLevel = 0;
	public int rfGenerated = 0;
	public int lastGenerated = 0;
	public int maxTransfer = 500;
	public int maxEnergy = 10000;
	public ForgeDirection powerReceiverDirection = null;
	public int panelCount = 0;
	public int panelMax = 9;

	public TileSolarInverter() {
		super();
		inventory = new ItemStack[invSize];
	}

	public void updateEntity() {
		generatePower();
		sendPower();

		if (worldObj.isRemote && rfGenerated > 0) {
			worldObj.playSound(xCoord, yCoord, zCoord, "additionaltech:inverterHum", 1.0F, 1.0F, true);
		}
	}

	public void onButtonPressed(int button) {
		if (button == 0) {
			scanArea();
			resetPowerDirection();
		}
	}

	public void scanArea() {

		if (this.worldObj.isRemote) {
			return;
		}

		int xOffset = 0;
		boolean xNegResult = false;
		boolean xPosResult = false;

		// scan along x axis for solarpanels
		while (xOffset <= 20) {
			int startCount = panelCount;
			xNegResult = checkSolarPanel(xCoord - xOffset, yCoord, zCoord);
			// FMLLog.info("Scanning xOffset: " + xOffset);
			zScan(xCoord - xOffset, yCoord, zCoord);
			xPosResult = checkSolarPanel(xCoord + xOffset, yCoord, zCoord);
			zScan(xCoord + xOffset, yCoord, zCoord);
			// kill the scan if we are not finding anything next to the inverter
			if (panelCount == 0 && xOffset > 0) {
				return;
			}
			xOffset++;
		}
	}

	public void zScan(int xPOS, int yPOS, int zPOS) {

		int zOffset = 1;
		boolean zNegResult = false;
		boolean zPosResult = false;

		while (zOffset <= 20) {
			// FMLLog.info("Scanning zOffset: " + zOffset);
			zNegResult = (checkSolarPanel(xPOS, yPOS, zPOS - zOffset));
			zPosResult = (checkSolarPanel(xPOS, yPOS, zPOS + zOffset));

			if (zNegResult || zPosResult) {
				zOffset++;
			} else
				return;
		}
	}

	public boolean checkSolarPanel(int xPOS, int yPOS, int zPOS) {
		if (worldObj.getBlock(xPOS, yPOS, zPOS) instanceof BlockSolarPanel) {
			// if the solar panel cannot see daylight, disable it
			if (!worldObj.canBlockSeeTheSky(xPOS, yPOS, zPOS)) {
				worldObj.setBlockMetadataWithNotify(xPOS, yPOS, zPOS, 1, 2);
			}
			int blockMeta = worldObj.getBlockMetadata(xPOS, yPOS, zPOS);
			if (blockMeta == 0 && panelCount < panelMax) {
				panelCount++;
				// FMLLog.info("Found another solar panel. Count: " +
				// panelCount);
				worldObj.setBlockMetadataWithNotify(xPOS, yPOS, zPOS, 1, 2);
			}
			return true;
		}
		return false;
	}

	public void checkUpgrades() {
		// reset panalMax to default
		panelMax = 9;
		if (!worldObj.isRemote) {
			for (int i = 0; i < inventory.length; i++) {
				ItemStack stack = inventory[i];
				if (stack != null) {
					if (stack.getItem() == AdditionalTech.proxy.itemInverterCore) {
						panelMax += 9;
					} else if (stack.getItem() == AdditionalTech.proxy.itemStageTwoCore) {
						panelMax += 25;
					} else if (stack.getItem() == AdditionalTech.proxy.itemStageThreeCore) {
						panelMax += 52;
					}
				}
			}
		}
	}

	public void generatePower() {
		if (worldObj.isRemote)
			return;
		if (worldObj.isDaytime() && (!worldObj.isRaining() && !worldObj.isThundering())) {
			rfGenerated = panelCount * 3;
			if (rfGenerated > 0) {
				if (rfLevel + rfGenerated > maxEnergy) {
					rfGenerated = maxEnergy - rfLevel;
					rfLevel += rfGenerated;
				} else
					rfLevel += rfGenerated;
			}
		} else {
			rfGenerated = 0;
			updateTE();
		}
		if (lastGenerated != rfGenerated) {
			lastGenerated = rfGenerated;
			updateTE();
		}
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	public void sendPacket(int button, boolean shiftPressed) {
		AdditionalTech.snw.sendToServer(new ATButtonMessage(xCoord, yCoord, zCoord, button, shiftPressed));
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	public void updateTE() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		try {
			panelCount = tagCompound.getInteger("SolarPanels");
		} catch (Throwable ex2) {
			panelCount = 0;
		}
		try {
			panelMax = tagCompound.getInteger("MaxSolarPanels");
		} catch (Throwable ex2) {
			panelMax = 9;
		}
		try {
			rfLevel = tagCompound.getInteger("rfLevel");
		} catch (Throwable ex2) {
			rfLevel = 0;
		}
		try {
			rfGenerated = tagCompound.getInteger("rfGenerated");
		} catch (Throwable ex2) {
			rfGenerated = 0;
		}
		try {
			powerReceiverDirection = ForgeDirection.getOrientation(tagCompound.getInteger("powerReceiverDirection"));
		} catch (Throwable ex2) {
			powerReceiverDirection = null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setInteger("SolarPanels", panelCount);
		tagCompound.setInteger("MaxSolarPanels", panelMax);
		tagCompound.setInteger("rfLevel", rfLevel);
		tagCompound.setInteger("rfGenerated", rfGenerated);
		if (powerReceiverDirection != null) {
			tagCompound.setInteger("powerReceiverDirection", powerReceiverDirection.ordinal());
		} else {
			tagCompound.setInteger("powerReceiverDirection", -1);
		}
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			checkUpgrades();
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
		// we need to adjust the panel max since the upgrade slot inventory has
		// changed
		checkUpgrades();
	}

	@Override
	public String getInventoryName() {
		return "Solar Inverter";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (!simulate) {
			rfLevel -= maxExtract;
		}
		return Math.min(rfLevel, 400);
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return rfLevel;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return maxEnergy;
	}

	protected void sendPower() {
		if (powerReceiverDirection == null) {
			return;
		}
		TileEntity tile = worldObj.getTileEntity(xCoord + powerReceiverDirection.offsetX,
				yCoord + powerReceiverDirection.offsetY, zCoord + powerReceiverDirection.offsetZ);
		if (tile != null && tile instanceof IEnergyReceiver) {
			IEnergyReceiver handler = (IEnergyReceiver) tile;
			int maxRF = handler.receiveEnergy(powerReceiverDirection.getOpposite(), Math.min(maxTransfer, rfLevel),
					true);
			rfLevel -= handler.receiveEnergy(powerReceiverDirection.getOpposite(), maxRF, false);

		} else {
			powerReceiverDirection = null;
		}
	}

	public void resetPowerDirection() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY,
					zCoord + direction.offsetZ);
			if (tile instanceof IEnergyReceiver) {
				powerReceiverDirection = direction;
			}
		}
	}
}