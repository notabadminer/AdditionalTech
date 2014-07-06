package additionaltech;

import additionaltech.net.PacketPipeline;
import additionaltech.tile.TileEFurnace;
import additionaltech.tile.TileESM;
import additionaltech.tile.TileGrinder;
import additionaltech.tile.TilePhotobioreactor;
import additionaltech.tile.TileSolarInverter;
import additionaltech.world.OreGenerator;
import additionaltech.gui.GuiHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="additionaltech", name="Additional Tech", version="0.9.0", dependencies = "required-after:Forge@[10.12.1.1060,)")

public class AdditionalTech {
	// The instance of your mod that Forge uses.
    @Instance(value = "additionaltech")
    public static AdditionalTech instance;
    
    public boolean solarEnabled = false;
    public static boolean copperEnabled = false;
    
 // The packet pipeline
    public static final PacketPipeline packetPipeline = new PacketPipeline();
    
    public static CreativeTabs tabAdditionalTech = new AdditionalTechTab("tabAdditionalTech");
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		solarEnabled = config.get(config.CATEGORY_GENERAL, "Solar Power Enabled", true).getBoolean(false);
		copperEnabled = config.get(config.CATEGORY_GENERAL, "Copper Worldgen", true).getBoolean(false);	
		config.save();
	    packetPipeline.initalise();
	    GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
	    
	    MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	
    	RegistryHandler.registerFluids();
    	RegistryHandler.registerBlocks();
    	RegistryHandler.registerItems();
    	
    	//register fluid and bucket combinations with the bucket handler
    	BucketHandler.INSTANCE.buckets.put(RegistryHandler.blockAlgaeSlurry, RegistryHandler.itemBucketSlurry);
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	
    	GameRegistry.registerTileEntity(TileSolarInverter.class, "TileSolarInverter");
    	GameRegistry.registerTileEntity(TileEFurnace.class, "TileEFurnace");
    	GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinder");
    	GameRegistry.registerTileEntity(TileESM.class, "TileESM");
    	GameRegistry.registerTileEntity(TilePhotobioreactor.class, "TilePhotobioreactor");
    	
    	RecipeHandler.addRecipes();
    	GrinderRecipes.initGrinderRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}
