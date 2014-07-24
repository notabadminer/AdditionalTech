package additionaltech.tile;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import additionaltech.AdditionalTech;
import additionaltech.net.ESMButtonMessage;
import additionaltech.net.ESMTEMessage;
import additionaltech.net.GrinderTEMessage;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeConnection.ConnectOverride;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
 
public class TileESM extends TileEntity implements IPipeConnection, IPowerEmitter, IPowerReceptor, IInventory {
	
	public PowerHandler powerHandler;
	private ItemStack[] inventoryItemStacks = new ItemStack[1];
    public int energyLevel;
    public int maxInput = 40;
    public int maxOutput = 40;
	public int maxEnergy = 20000;
	public ForgeDirection powerReceiverDirection = null;
	public boolean isRedstonePowered = false;

	public TileESM() {
		powerHandler = new PowerHandler(this, Type.STORAGE);
		initPowerProvider();
	}

	public void initPowerProvider() {
		powerHandler.configure(10, maxInput, 25, maxEnergy);
		powerHandler.configurePowerPerdition(0, 0);
	}
	
	public void updateEntity() {
		super.updateEntity();
		checkRedstonePower();
		if (!isRedstonePowered) {
			sendPower();
		}
		if (!worldObj.isRemote) energyLevel = (int) powerHandler.getEnergyStored();
	}

	/* IPIPECONNECTION */
	@Override
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		if (type == PipeType.POWER) {
			return ConnectOverride.DEFAULT;
		} else
			return ConnectOverride.DISCONNECT;
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
			if (isPowerReceptor(tile, powerReceiverDirection)) {
				PowerReceiver receiver = ((IPowerReceptor) tile)
						.getPowerReceiver(powerReceiverDirection.getOpposite());
				double send = Math.min(receiver.powerRequest(),maxOutput);
				send = powerHandler.useEnergy(send, send, true);
				if (send > 0) {
					double b = receiver.receiveEnergy(Type.STORAGE, send, powerReceiverDirection.getOpposite());
				}				
			}
		}
	}

	public void findPowerReceiver() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(
					xCoord + direction.offsetX, yCoord + direction.offsetY,
					zCoord + direction.offsetZ);
			if (isPowerReceptor(tile, direction)) {
				powerReceiverDirection = direction;
			}
		}
	}
	
	public boolean isPowerReceptor(TileEntity tile, ForgeDirection direction) {
		if (tile instanceof IPowerReceptor && ((IPowerReceptor) tile).getPowerReceiver(direction.getOpposite()) != null) {
			return true;
		} else
		return false;		
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return powerHandler.getPowerReceiver();
	}
	
	public void configurePowerHandler(int buttonId) {
		if (worldObj.isRemote) return;
		if (buttonId == 0) {
			FMLLog.info("button 0 pressed");
			if (maxInput > 0) {
				maxInput--;
			}
		} else if (buttonId == 1) {
			FMLLog.info("button 1 pressed");
			if (maxInput < 40) {
				maxInput++;
			}
		} else if (buttonId == 2) {
			if (maxOutput > 0) {
				maxOutput--;
			}
		} else if (buttonId == 3) {
			if (maxOutput < 40) {
				maxOutput++;
			}
		} 
		powerHandler.configure(Math.min(10, maxInput), maxInput, 25, maxEnergy);
	}
	
	public void checkRedstonePower() {
		isRedstonePowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	@Override
	public void doWork(PowerHandler workProvider) {
	}
	
	@Override
	public World getWorld() {
		return worldObj;
	}

    public double getEnergyLevelScaled(int scale) {
        return this.energyLevel * scale / maxEnergy;
    }
    
	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return false;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}
	
	public void sendPacket(int button) {
			AdditionalTech.snw.sendToServer(new ESMButtonMessage(xCoord, yCoord, zCoord, button));
	}
	
	@Override
    public Packet getDescriptionPacket() {
        return AdditionalTech.snw.getPacketFrom(new ESMTEMessage(this));
    }
	
	public void updateTE() {
		 worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagCompound tag = new NBTTagCompound();
		tagCompound.setInteger("MaxInput", maxInput);
		tagCompound.setInteger("MaxOutput", maxOutput);
		tagCompound.setInteger("MaxEnergy", maxEnergy);
		tagCompound.setInteger("EnergyLevel", energyLevel);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		try {
			maxInput = tagCompound.getInteger("MaxInput");
		} catch (Throwable ex2) {
			//fail quietly
		}
		try {
			maxOutput = tagCompound.getInteger("MaxOutput");
		} catch (Throwable ex2) {
			//fail quietly
		}
		try {
			maxEnergy = tagCompound.getInteger("MaxEnergy");
		} catch (Throwable ex2) {
			//fail quietly
		}
		//configure powerhandler before setting energy level
		powerHandler.configure(10, maxInput, 25, maxEnergy);
		try {
			energyLevel = tagCompound.getInteger("EnergyLevel");
			powerHandler.setEnergy(energyLevel);
		} catch (Throwable ex2) {
			//fail quietly
		}
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
	}

}
