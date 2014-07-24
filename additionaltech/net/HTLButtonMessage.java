package additionaltech.net;

import additionaltech.gui.GuiHTL;
import additionaltech.gui.GuiSolarInverter;
import additionaltech.tile.TileHTL;
import additionaltech.tile.TileSolarInverter;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class HTLButtonMessage implements IMessage, IMessageHandler<HTLButtonMessage, IMessage> {
	private int x, y, z, button;

    public HTLButtonMessage() {
    }

    public HTLButtonMessage(int x, int y, int z, int button)
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
	public IMessage onMessage(HTLButtonMessage message, MessageContext ctx) {
		World world = ctx.getServerHandler().playerEntity.worldObj;
		TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);

		if (tileEntity instanceof TileHTL) {		
        	if (button == GuiHTL.idPowerButton){
				((TileHTL) tileEntity).onPowerButtonPressed();
			}
		}
		return null;
	}
}