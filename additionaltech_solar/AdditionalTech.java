package additionaltech_solar;

import additionaltech_solar.net.PacketPipeline;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="additionaltech_solar", name="Additional Tech", version="0.1.1")

public class AdditionalTech {
	// The instance of your mod that Forge uses.
    @Instance(value = "additionaltech_solar")
    public static AdditionalTech instance;
    
    public static Block blockSolarPanel;
    public static Block blockSolarInverter;
    
 // The packet pipeline
    public static final PacketPipeline packetPipeline = new PacketPipeline();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		//autoModeEnabled = config.get(config.CATEGORY_GENERAL, "Auto Buy/Sell", true).getBoolean(false);	
		config.save();
	    packetPipeline.initalise();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	blockSolarPanel = new BlockSolarPanel().setBlockName("blockSolarPanel");
    	blockSolarInverter = new BlockSolarInverter().setBlockName("blockSolarInverter");
    	
    	GameRegistry.registerBlock(blockSolarPanel, "blockSolarPanel").getUnlocalizedName();
    	GameRegistry.registerBlock(blockSolarInverter, "blockSolarInverter").getUnlocalizedName();
    	
    	GameRegistry.registerTileEntity(TileSolarInverter.class, "TileSolarInverter");
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerSolarInverter());
    	
    	RecipeHandler.addRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}
