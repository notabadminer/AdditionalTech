package additionaltech;

import additionaltech.items.BlockOreCopper;
import additionaltech.items.ItemIngotCopper;
import additionaltech.items.ItemWireCopper;
import additionaltech.items.ItemInverterCore;
import additionaltech.items.ItemStageThreeCore;
import additionaltech.items.ItemStageTwoCore;
import additionaltech.net.PacketPipeline;
import additionaltech.world.OreGenerator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="additionaltech", name="Additional Tech", version="0.1.1", dependencies = "required-after:Forge@[10.12.1.1060,)")

public class AdditionalTech {
	// The instance of your mod that Forge uses.
    @Instance(value = "additionaltech")
    public static AdditionalTech instance;
    
    public static Block blockSolarPanel;
    public static Block blockSolarInverter;
    public static Block blockEFurnace;
    public static Block oreCopper;
    public static Item ingotCopper;
    public static Item wireCopper;
    public static Item itemInverterCore;
    public static Item itemStageTwoCore;
    public static Item itemStageThreeCore;
    
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
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	blockSolarPanel = new BlockSolarPanel().setBlockName("blockSolarPanel");
    	blockSolarInverter = new BlockSolarInverter().setBlockName("blockSolarInverter");
    	blockEFurnace = new BlockEFurnace().setBlockName("blockEFurnace");
    	oreCopper = new BlockOreCopper().setBlockName("oreCopper");
    	
    	GameRegistry.registerBlock(blockSolarPanel, blockSolarPanel.getUnlocalizedName());
    	GameRegistry.registerBlock(blockSolarInverter, blockSolarInverter.getUnlocalizedName());
    	GameRegistry.registerBlock(blockEFurnace, blockEFurnace.getUnlocalizedName());    	
    	GameRegistry.registerBlock(oreCopper, oreCopper.getUnlocalizedName());
    	
    	ingotCopper = new ItemIngotCopper().setUnlocalizedName("ingotCopper");
    	wireCopper = new ItemWireCopper().setUnlocalizedName("wireCopper");
    	itemInverterCore = new ItemInverterCore().setUnlocalizedName("itemInverterCore");
    	itemStageTwoCore = new ItemStageTwoCore().setUnlocalizedName("itemStageTwoCore");
    	itemStageThreeCore = new ItemStageThreeCore().setUnlocalizedName("itemStageThreeCore");
    	
    	GameRegistry.registerItem(ingotCopper, ingotCopper.getUnlocalizedName());
    	OreDictionary.registerOre("ingotCopper", ingotCopper);
    	GameRegistry.registerItem(wireCopper, wireCopper.getUnlocalizedName());
    	OreDictionary.registerOre("wireCopper", wireCopper);
    	GameRegistry.registerItem(itemInverterCore, itemInverterCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemStageTwoCore, itemStageTwoCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemStageThreeCore, itemStageThreeCore.getUnlocalizedName());
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	
    	GameRegistry.registerTileEntity(TileSolarInverter.class, "TileSolarInverter");
    	GameRegistry.registerTileEntity(TileEFurnace.class, "TileEFurnace");
    	
    	RecipeHandler.addRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}
