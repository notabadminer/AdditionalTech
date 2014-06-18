package additionaltech.net;

import additionaltech.TileSolarInverter;
import cpw.mods.fml.common.FMLLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketSolarInverterTE extends AbstractPacket{
	
	private int x, y, z, panelCount, panelMax;
	private double energy, energyOutput;
	
public PacketSolarInverterTE() {
    	
    }

public PacketSolarInverterTE(int x, int y, int z, int panelCount, int panelMax, double energy, double energyOutput) {
	this.x = x;
    this.y = y;
    this.z = z;
    this.panelCount = panelCount;
    this.panelMax = panelMax;
    this.energy = energy;
    this.energyOutput = energyOutput;
}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(panelCount);
        buffer.writeInt(panelMax);
        buffer.writeDouble(energy);
        buffer.writeDouble(energyOutput);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        panelCount = buffer.readInt();
        panelMax = buffer.readInt();
        energy = buffer.readDouble();
        energyOutput = buffer.readDouble();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		World world = player.worldObj;
		//FMLLog.info("UC: Client received PacketCoinSum");
		TileEntity tEntity = world.getTileEntity(x, y, z);
        if (tEntity instanceof TileSolarInverter) {
        	((TileSolarInverter) tEntity).panelCount = panelCount;
        	((TileSolarInverter) tEntity).panelMax = panelMax;
        	((TileSolarInverter) tEntity).energy = energy;
        	((TileSolarInverter) tEntity).energyGenerated = energyOutput;
        }
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		
		TileEntity tEntity = world.getTileEntity(x, y, z);
        if (tEntity instanceof TileSolarInverter) {
        	energy = ((TileSolarInverter) tEntity).energy;
        	panelCount = ((TileSolarInverter) tEntity).panelCount;
        	panelMax = ((TileSolarInverter) tEntity).panelMax;
        	energyOutput = ((TileSolarInverter) tEntity).energyGenerated;
        }
	}

}
