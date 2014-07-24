package additionaltech.net;

import net.minecraft.tileentity.TileEntity;
import io.netty.buffer.ByteBuf;
import additionaltech.tile.TileGrinder;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;



public class GrinderTEMessage implements IMessage, IMessageHandler<GrinderTEMessage, IMessage> {
		public int x, y, z, batteryLevel;
		public double batteryMax;

		    public GrinderTEMessage()
		    {
		    }

		    public GrinderTEMessage(TileGrinder tileEntity)
		    {
		        this.x = tileEntity.xCoord;
		        this.y = tileEntity.yCoord;
		        this.z = tileEntity.zCoord;
		        this.batteryLevel = tileEntity.batteryLevel;
		        this.batteryMax = tileEntity.batteryMax;
		    }

		    @Override
		    public void fromBytes(ByteBuf buf)
		    {
		        this.x = buf.readInt();
		        this.y = buf.readInt();
		        this.z = buf.readInt();
		        this.batteryLevel = buf.readInt();
		        this.batteryMax = buf.readDouble();
		    }

		    @Override
		    public void toBytes(ByteBuf buf)
		    {
		        buf.writeInt(x);
		        buf.writeInt(y);
		        buf.writeInt(z);
		        buf.writeInt(batteryLevel);
		        buf.writeDouble(batteryMax);
		    }

			@Override
			public IMessage onMessage(GrinderTEMessage message, MessageContext ctx) {
				TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld
						.getTileEntity(message.x, message.y, message.z);

				if (tileEntity instanceof TileGrinder) {
					((TileGrinder) tileEntity).batteryLevel = message.batteryLevel;
					((TileGrinder) tileEntity).batteryMax = message.batteryMax;
				}
				return null;
			}
}
