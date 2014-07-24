package additionaltech.gui;

import additionaltech.inventory.ContainerEFurnace;
import additionaltech.inventory.ContainerESM;
import additionaltech.inventory.ContainerGrinder;
import additionaltech.inventory.ContainerHTL;
import additionaltech.inventory.ContainerPhotobioreactor;
import additionaltech.inventory.ContainerSolarInverter;
import additionaltech.tile.TileEFurnace;
import additionaltech.tile.TileESM;
import additionaltech.tile.TileGrinder;
import additionaltech.tile.TileHTL;
import additionaltech.tile.TilePhotobioreactor;
import additionaltech.tile.TileSolarInverter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;


public class GuiHandler implements IGuiHandler {

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
        if(tileEntity instanceof TileESM){
            return new ContainerESM(player.inventory, (TileESM) tileEntity);
        }
        if(tileEntity instanceof TileGrinder){
            return new ContainerGrinder(player.inventory, (TileGrinder) tileEntity);
        }
        if(tileEntity instanceof TilePhotobioreactor){
            return new ContainerPhotobioreactor(player.inventory, (TilePhotobioreactor) tileEntity);
        }
        if(tileEntity instanceof TileHTL){
            return new ContainerHTL(player.inventory, (TileHTL) tileEntity);
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
        if(tileEntity instanceof TileESM){
            return new GuiESM(player.inventory, (TileESM) tileEntity);
        }
        if(tileEntity instanceof TileGrinder){
            return new GuiGrinder(player.inventory, (TileGrinder) tileEntity);
        }
        if(tileEntity instanceof TilePhotobioreactor){
            return new GuiPhotobioreactor(player.inventory, (TilePhotobioreactor) tileEntity);
        }
        if(tileEntity instanceof TileHTL){
            return new GuiHTL(player.inventory, (TileHTL) tileEntity);
        }
        return null;
		}
}
