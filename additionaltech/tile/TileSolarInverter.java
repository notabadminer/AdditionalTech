package additionaltech.tile;

import java.util.EnumSet;
import java.util.LinkedList;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import additionaltech.AdditionalTech;
import additionaltech.RegistryHandler;
import additionaltech.blocks.BlockSolarPanel;
import additionaltech.net.PacketSolarInverter;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;

public class TileSolarInverter extends TileEntity implements IPowerEmitter, IPipeConnection, IInventory {

	private ItemStack[] inventory;
	private final int invSize = 3;
	public static final int upgradeSlot0 = 0;
	public static final int upgradeSlot1 = 1;
	public static final int upgradeSlot2 = 2;

	public double energy = 0;
	public double energyGenerated;
	public double maxTransfer = 40;
	public double maxEnergy = 1000;
	ForgeDirection orientation = ForgeDirection.UP;
	public ForgeDirection powerReceiverDirection = null;
	public int panelCount = 0;
	public int panelMax = 9;
	
	public TileSolarInverter() {
		super();
		inventory = new ItemStack[invSize];
	}

	public void updateEntity() {
		super.updateEntity();
		generatePower();
		sendPower();
		//if panel count is greater than zero, assume power is generated and hum
		if (panelCount > 0 && worldObj.isRemote) {
			worldObj.playSound(xCoord, yCoord, zCoord, "additionaltech:inverterHum", 0.7F, 1.0F, true);
		}
	}
	
	public void onResetButtonPressed() {
		scanArea();
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
			//FMLLog.info("Scanning xOffset: " + xOffset);
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
			//FMLLog.info("Scanning zOffset: " + zOffset);
			zNegResult = (checkSolarPanel(xPOS, yPOS, zPOS - zOffset));
			zPosResult = (checkSolarPanel(xPOS, yPOS, zPOS + zOffset));
				
			if (zNegResult || zPosResult) {
				zOffset++;
			} else return;
		}
	}
	
	public boolean checkSolarPanel(int xPOS, int yPOS, int zPOS) {
		if (worldObj.getBlock(xPOS, yPOS, zPOS) instanceof BlockSolarPanel) {
			//if the solar panel cannot see daylight, disable it
			if (!worldObj.canBlockSeeTheSky(xPOS, yPOS, zPOS)) {
				worldObj.setBlockMetadataWithNotify(xPOS, yPOS, zPOS, 1, 2);
			}
			int blockMeta = worldObj.getBlockMetadata(xPOS, yPOS, zPOS);
			if (blockMeta == 0 && panelCount < panelMax) {
				panelCount++;
				//FMLLog.info("Found another solar panel. Count: " + panelCount);
				worldObj.setBlockMetadataWithNotify(xPOS, yPOS, zPOS, 1, 2);
			}
			return true;
		}
		return false;
	}

	public void findPowerReceiver() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(
					xCoord + direction.offsetX, yCoord + direction.offsetY,
					zCoord + direction.offsetZ);
			if (isWoodenConductivePipe(tile, direction)) {
				powerReceiverDirection = direction;
			}
		}
	}
	
	public void checkUpgrades() {
		//reset panalMax to default
		panelMax = 9;
		if (!worldObj.isRemote) {
			for (int i = 0; i < inventory.length; i++) {
				ItemStack stack = inventory[i];
				if (stack != null) {
					if (stack.getItem() == RegistryHandler.itemInverterCore) {
						panelMax += 9;
					} else if (stack.getItem() == RegistryHandler.itemStageTwoCore) {
						panelMax += 24;
					} else if (stack.getItem() == RegistryHandler.itemStageThreeCore) {
						panelMax += 50;
					}
				}
			}
		}
	}
	
	public boolean isWoodenConductivePipe(TileEntity tile, ForgeDirection direction) {
		if (tile instanceof IPowerReceptor && ((IPowerReceptor) tile).getPowerReceiver(direction.getOpposite()) != null) {
			return true;
		} else
		return false;		
	}

	public void generatePower() {
		if (!worldObj.isRemote && worldObj.isDaytime() && (!worldObj.isRaining() && !worldObj.isThundering())) {
			energyGenerated = panelCount * 0.25;
			if (energy + energyGenerated > maxEnergy) {
				energyGenerated = maxEnergy - energy;
				energy += energyGenerated;
			}
			else energy += energyGenerated;
		}
	}
	
	public void sendPower() {
		if (!worldObj.isRemote) { // we need to run on the server only
			if (powerReceiverDirection == null) {
				findPowerReceiver();
				return;
			}
			TileEntity tile = worldObj.getTileEntity(xCoord
					+ powerReceiverDirection.offsetX, yCoord
					+ powerReceiverDirection.offsetY, zCoord
					+ powerReceiverDirection.offsetZ);
			if (isWoodenConductivePipe(tile, powerReceiverDirection)) {
				PowerReceiver receiver = ((IPowerReceptor) tile)
						.getPowerReceiver(powerReceiverDirection.getOpposite());
				double send = Math.min(energy, maxTransfer);
				if (send > 0) {
					double b = receiver.receiveEnergy(Type.ENGINE,
							Math.min(send, receiver.powerRequest()),
							powerReceiverDirection.getOpposite());
					if (b > 0) {
						energy -= b;
						b = 0;
					}
				}				
			}
		}
	}

	/* IPIPECONNECTION */
	@Override
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		if (type == PipeType.POWER && with != orientation) {
			return ConnectOverride.DEFAULT;
		} else
			return ConnectOverride.DISCONNECT;
	}

	/* IPOWEREMITTER */
	@Override
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory",
				Constants.NBT.TAG_COMPOUND);
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
			energy = tagCompound.getInteger("EnergyLevel");
		} catch (Throwable ex2) {
			energy = 0;
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
		tagCompound.setDouble("EnergyLevel", energy);
	}
	
	public void sendPacket(int button) {
		PacketSolarInverter packet = new PacketSolarInverter(xCoord, yCoord,
				zCoord, button);
		AdditionalTech.packetPipeline.sendToServer(packet);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
		//we need to adjust the panel max since the upgrade slot inventory has changed
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		Item stackItem = stack.getItem();
		if (slot == upgradeSlot0 || slot == upgradeSlot1 || slot == upgradeSlot2) {
			return stackItem == RegistryHandler.itemInverterCore
					|| stackItem == RegistryHandler.itemStageTwoCore
					|| stackItem == RegistryHandler.itemStageThreeCore;
		} else return false;
	}

}