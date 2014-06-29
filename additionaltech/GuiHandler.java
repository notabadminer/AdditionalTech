package additionaltech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IGuiHandler;

class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof TileSolarInverter){
                return new ContainerSolarInverter(player.inventory, (TileSolarInverter) tileEntity);
        }
        if(tileEntity instanceof TileEFurnace){
            return new ContainerEFurnace(player.inventory, (TileEFurnace) tileEntity);
    }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof TileSolarInverter){
                return new GuiSolarInverter(player.inventory, (TileSolarInverter) tileEntity);
        }
        if(tileEntity instanceof TileEFurnace){
            return new GuiEFurnace(player.inventory, (TileEFurnace) tileEntity);
    }
        return null;
	}	
}
