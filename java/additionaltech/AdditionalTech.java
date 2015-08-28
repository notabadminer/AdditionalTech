package additionaltech;

import additionaltech.gui.GuiHandler;
import additionaltech.net.ATButtonMessage;
import additionaltech.proxy.CommonProxy;
import additionaltech.world.AlgaeGenerator;
import additionaltech.world.OreGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid=AdditionalTech.MODID, name=AdditionalTech.NAME, version=AdditionalTech.VERSION, dependencies = "required-after:Forge@[10.13.4.1448,)")

public class AdditionalTech {
    public static final String MODID = "additionaltech";
	public static final String NAME = "Additional Tech";
	public static final String VERSION = "@VERSION@";
	
	@Instance(MODID)
    public static AdditionalTech instance;
    
    public boolean solarEnabled = false;
    public static boolean copperEnabled = false;
    
    public static SimpleNetworkWrapper snw; 
    
    public static CreativeTabs tabAdditionalTech = new AdditionalTechTab("tabAdditionalTech");
    
    @SidedProxy(clientSide="additionaltech.proxy.ClientProxy", serverSide="additionaltech.proxy.CommonProxy")
	public static CommonProxy proxy;
    
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
	    snw.registerMessage(ATButtonMessage.class, ATButtonMessage.class, 0, Side.SERVER);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    	proxy.registerFluids();
    	proxy.registerBlocks();
    	proxy.registerItems();
    	proxy.registerTileEntitys();
    	proxy.registerRenderers();
    	
    	//register fluid and bucket combinations with the bucket handler
    	BucketHandler.INSTANCE.buckets.put(proxy.blockAlgaeSlurry, proxy.itemBucketSlurry);
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	    	
    	RecipeHandler.addRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}
