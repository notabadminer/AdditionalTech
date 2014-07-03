package additionaltech.net;

import additionaltech.gui.GuiSolarInverter;
import additionaltech.tile.TileESM;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketESM extends AbstractPacket {
	
	private int x, y, z, button;
	
public PacketESM() {
    	
    }

public PacketESM(int x, int y, int z, int button) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.button = button;
    }

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(button);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        button = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
	
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
		World world = player.worldObj;
		TileEntity tEntity = world.getTileEntity(x, y, z);
		
        if (tEntity instanceof TileESM) {
			((TileESM) tEntity).configurePowerHandler(button);

			NBTTagCompound data = new NBTTagCompound();
            tEntity.writeToNBT(data);
        }            
	}
}
