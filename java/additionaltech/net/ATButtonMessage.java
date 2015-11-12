package additionaltech.net;

import additionaltech.tile.TileEFurnace;
import additionaltech.tile.TileHTL;
import additionaltech.tile.TileSolarInverter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ATButtonMessage implements IMessage, IMessageHandler<ATButtonMessage, IMessage> {
	private int x, y, z, buttonId;
	private boolean shiftPressed;

	public ATButtonMessage() {
	}

	public ATButtonMessage(int x, int y, int z, int button, boolean shift) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.buttonId = button;
		this.shiftPressed = shift;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(buttonId);
		buf.writeBoolean(shiftPressed);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.buttonId = buf.readInt();
		this.shiftPressed = buf.readBoolean();
	}

	@Override
	public IMessage onMessage(ATButtonMessage message, MessageContext ctx) {
		World world = ctx.getServerHandler().playerEntity.worldObj;

		TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
		if (tileEntity instanceof TileSolarInverter) {
			((TileSolarInverter) tileEntity).onButtonPressed(message.buttonId);
		}
		if (tileEntity instanceof TileHTL) {
			((TileHTL) tileEntity).onButtonPressed(message.buttonId);
		}
		return null;
	}
}
