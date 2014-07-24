package additionaltech;

import additionaltech.net.EFurnaceTEMessage;
import additionaltech.net.ESMButtonMessage;
import additionaltech.net.ESMTEMessage;
import additionaltech.net.GrinderTEMessage;
import additionaltech.net.HTLButtonMessage;
import additionaltech.net.InverterButtonMessage;
import additionaltech.tile.TileEFurnace;
import additionaltech.tile.TileESM;
import additionaltech.tile.TileGrinder;
import additionaltech.tile.TileHTL;
import additionaltech.tile.TilePhotobioreactor;
import additionaltech.tile.TileSolarInverter;
import additionaltech.world.OreGenerator;
import additionaltech.world.AlgaeGenerator;
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
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="additionaltech", name="Additional Tech", version="0.9.3", dependencies = "required-after:Forge@[10.12.1.1060,)")

public class AdditionalTech {
	// The instance of your mod that Forge uses.
    @Instance(value = "additionaltech")
    public static AdditionalTech instance;
    
    public boolean solarEnabled = false;
    public static boolean copperEnabled = false;
    
    public static SimpleNetworkWrapper snw; 
    
    public static CreativeTabs tabAdditionalTech = new AdditionalTechTab("tabAdditionalTech");
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		solarEnabled = config.get(config.CATEGORY_GENERAL, "Solar Power Enabled", true).getBoolean(false);
		copperEnabled = config.get(config.CATEGORY_GENERAL, "Copper Worldgen", true).getBoolean(false);	
		config.save();
	    GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
	    GameRegistry.registerWorldGenerator(new AlgaeGenerator(),0);
	    
	    MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	    
	    snw = NetworkRegistry.INSTANCE.newSimpleChannel("additionaltech"); 
	    snw.registerMessage(ESMButtonMessage.class, ESMButtonMessage.class, 0, Side.SERVER);
	    snw.registerMessage(InverterButtonMessage.class, InverterButtonMessage.class, 1, Side.SERVER);
	    snw.registerMessage(EFurnaceTEMessage.class, EFurnaceTEMessage.class, 2, Side.CLIENT);
	    snw.registerMessage(GrinderTEMessage.class, GrinderTEMessage.class, 3, Side.CLIENT);
	    snw.registerMessage(ESMTEMessage.class, ESMTEMessage.class, 4, Side.CLIENT);
	    snw.registerMessage(HTLButtonMessage.class, HTLButtonMessage.class, 5, Side.SERVER);

    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	
    	RegistryHandler.registerFluids();
    	RegistryHandler.registerBlocks();
    	RegistryHandler.registerItems();
    	RegistryHandler.registerTileEntitys();
    	
    	//register fluid and bucket combinations with the bucket handler
    	BucketHandler.INSTANCE.buckets.put(RegistryHandler.blockAlgaeSlurry, RegistryHandler.itemBucketSlurry);
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	    	
    	RecipeHandler.addRecipes();
    	GrinderRecipes.initGrinderRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}
