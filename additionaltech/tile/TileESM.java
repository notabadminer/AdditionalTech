package additionaltech.tile;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import additionaltech.AdditionalTech;
import additionaltech.net.PacketESM;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
 
public class TileESM extends TileEntity implements IPipeConnection, IPowerEmitter, IPowerReceptor, IInventory {
	
	public PowerHandler powerHandler;
	private ItemStack[] inventoryItemStacks = new ItemStack[1];
    public int energyLevel;
    public double maxInput = 40;
    public double maxOutput = 40;
	public double maxEnergy = 20000;
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
		
		if (worldObj.isRemote) {
			return;
		}
		
		energyLevel = (int) powerHandler.getEnergyStored();
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
		if (buttonId == 0) {
			if (maxInput > 0) {
				maxInput--;
			}
		} else if (buttonId == 1) {
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
		powerHandler.configure(10, maxInput, 25, maxEnergy);
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

    public int getEnergyLevelScaled(int scale) {
        return this.energyLevel * scale / (int) maxEnergy;
    }
    
    public void sendPacket(int button) {
		PacketESM packet = new PacketESM(xCoord, yCoord,
				zCoord, button);
		AdditionalTech.packetPipeline.sendToServer(packet);
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
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagCompound tag = new NBTTagCompound();
		tagCompound.setInteger("EnergyLevel", energyLevel);
		tagCompound.setDouble("MaxInput", maxInput);
		tagCompound.setDouble("MaxOutput", maxOutput);
		tagCompound.setDouble("MaxEnergy", maxEnergy);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		try {
			powerHandler.setEnergy(tagCompound.getInteger("EnergyLevel"));
		} catch (Throwable ex2) {
			//fail quietly
		}
		try {
			maxInput = tagCompound.getDouble("MaxInput");
		} catch (Throwable ex2) {
			//fail quietly
		}
		try {
			maxOutput = tagCompound.getDouble("MaxOutput");
		} catch (Throwable ex2) {
			//fail quietly
		}
		try {
			maxEnergy = tagCompound.getDouble("MaxEnergy");
		} catch (Throwable ex2) {
			//fail quietly
		}
		powerHandler.configure(10, maxInput, 25, maxEnergy);
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
