package additionaltech.net;

import additionaltech.tile.TileESM;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class ESMButtonMessage implements IMessage, IMessageHandler<ESMButtonMessage, IMessage> {
	private int x, y, z, button;

    public ESMButtonMessage() {
    }

    public ESMButtonMessage(int x, int y, int z, int button)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.button = button;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.button = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(button);
    }

	@Override
	public IMessage onMessage(ESMButtonMessage message, MessageContext ctx) {
		World world = ctx.getServerHandler().playerEntity.worldObj;
		TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);

		if (tileEntity instanceof TileESM) {
			((TileESM) tileEntity).configurePowerHandler(message.button);
		}
		return null;
	}
}