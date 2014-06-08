package additionaltech_solar.net;

import cpw.mods.fml.common.FMLLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketSolarInverter extends AbstractPacket {
	
	private int x, y, z, button;
    private boolean shiftPressed, bypass;
	
public PacketSolarInverter() {
    	
    }

public PacketSolarInverter(int x, int y, int z, int button, boolean shiftPressed, boolean bypass) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.button = button;
    this.shiftPressed = shiftPressed;
    this.bypass = bypass;
    }

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(button);
        buffer.writeBoolean(shiftPressed);
        buffer.writeBoolean(bypass);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        button = buffer.readInt();
        shiftPressed = buffer.readBoolean();
        bypass = buffer.readBoolean();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
	
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		
		TileEntity ucTileEntity = world.getTileEntity(x, y, z);
        //if (ucTileEntity instanceof UCTileEntity) {
        
			
			NBTTagCompound data = new NBTTagCompound();
            ucTileEntity.writeToNBT(data);
            
	}
}
