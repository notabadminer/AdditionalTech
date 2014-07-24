package additionaltech.net;

import net.minecraft.tileentity.TileEntity;
import io.netty.buffer.ByteBuf;
import additionaltech.tile.TileESM;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;



public class ESMTEMessage implements IMessage, IMessageHandler<ESMTEMessage, IMessage> {
		public int x, y, z, energyLevel, maxEnergy;

		    public ESMTEMessage()
		    {
		    }

		    public ESMTEMessage(TileESM tileEntity)
		    {
		        this.x = tileEntity.xCoord;
		        this.y = tileEntity.yCoord;
		        this.z = tileEntity.zCoord;
		        this.energyLevel = tileEntity.energyLevel;
		        this.maxEnergy = tileEntity.maxEnergy;
		    }

		    @Override
		    public void fromBytes(ByteBuf buf)
		    {
		        this.x = buf.readInt();
		        this.y = buf.readInt();
		        this.z = buf.readInt();
		        this.energyLevel = buf.readInt();
		        this.maxEnergy = buf.readInt();
		    }

		    @Override
		    public void toBytes(ByteBuf buf)
		    {
		        buf.writeInt(x);
		        buf.writeInt(y);
		        buf.writeInt(z);
		        buf.writeInt(energyLevel);
		        buf.writeInt(maxEnergy);
		    }

			@Override
			public IMessage onMessage(ESMTEMessage message, MessageContext ctx) {
				TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld
						.getTileEntity(message.x, message.y, message.z);

				if (tileEntity instanceof TileESM) {
					((TileESM) tileEntity).energyLevel = message.energyLevel;
					((TileESM) tileEntity).maxEnergy = message.maxEnergy;
				}
				return null;
			}
}
