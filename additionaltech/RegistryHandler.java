package additionaltech;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import additionaltech.items.BlockOreCopper;
import additionaltech.items.ItemDustCopper;
import additionaltech.items.ItemDustDiamond;
import additionaltech.items.ItemDustGold;
import additionaltech.items.ItemDustIron;
import additionaltech.items.ItemDustObsidian;
import additionaltech.items.ItemDustSteel;
import additionaltech.items.ItemDustTin;
import additionaltech.items.ItemHeatingElement;
import additionaltech.items.ItemIngotCopper;
import additionaltech.items.ItemInverterCore;
import additionaltech.items.ItemPlateCopper;
import additionaltech.items.ItemPlateQuartz;
import additionaltech.items.ItemStageThreeCore;
import additionaltech.items.ItemStageTwoCore;
import additionaltech.items.ItemWireCopper;

public class RegistryHandler {
	public static Block blockSolarPanel;
    public static Block blockSolarInverter;
    public static Block blockEFurnace;
    public static Block oreCopper;
    public static Item dustCopper;
    public static Item dustDiamond;
    public static Item dustGold;
    public static Item dustIron;
    public static Item dustObsidian;
    public static Item dustSteel;
    public static Item dustTin;
    public static Item ingotCopper;
    public static Item plateCopper;
    public static Item wireCopper;
    public static Item plateQuartz;
    public static Item itemInverterCore;
    public static Item itemStageTwoCore;
    public static Item itemStageThreeCore;
    public static Item itemHeatingElement;
	
	public static void registerBlocks(){
		blockSolarPanel = new BlockSolarPanel().setBlockName("blockSolarPanel");
    	blockSolarInverter = new BlockSolarInverter().setBlockName("blockSolarInverter");
    	blockEFurnace = new BlockEFurnace().setBlockName("blockEFurnace");
    	oreCopper = new BlockOreCopper().setBlockName("oreCopper");
    	
    	GameRegistry.registerBlock(blockSolarPanel, blockSolarPanel.getUnlocalizedName());
    	GameRegistry.registerBlock(blockSolarInverter, blockSolarInverter.getUnlocalizedName());
    	GameRegistry.registerBlock(blockEFurnace, blockEFurnace.getUnlocalizedName());    	
    	GameRegistry.registerBlock(oreCopper, oreCopper.getUnlocalizedName());
    	OreDictionary.registerOre("oreCopper", oreCopper);
	}
	
	public static void registerItems(){
		ingotCopper = new ItemIngotCopper().setUnlocalizedName("ingotCopper");
		dustCopper = new ItemDustCopper().setUnlocalizedName("dustCopper");
		dustDiamond = new ItemDustDiamond().setUnlocalizedName("dustDiamond");
		dustGold = new ItemDustGold().setUnlocalizedName("dustGold");
		dustIron = new ItemDustIron().setUnlocalizedName("dustIron");
		dustObsidian = new ItemDustObsidian().setUnlocalizedName("dustObsidian");
		dustSteel = new ItemDustSteel().setUnlocalizedName("dustSteel");
		dustTin = new ItemDustTin().setUnlocalizedName("dustTin");
    	wireCopper = new ItemWireCopper().setUnlocalizedName("wireCopper");
    	plateCopper = new ItemPlateCopper().setUnlocalizedName("plateCopper");
    	plateQuartz = new ItemPlateQuartz().setUnlocalizedName("plateQuartz");
    	itemInverterCore = new ItemInverterCore().setUnlocalizedName("itemInverterCore");
    	itemStageTwoCore = new ItemStageTwoCore().setUnlocalizedName("itemStageTwoCore");
    	itemStageThreeCore = new ItemStageThreeCore().setUnlocalizedName("itemStageThreeCore");
    	itemHeatingElement = new ItemHeatingElement().setUnlocalizedName("itemHeatingElement");
    	
    	GameRegistry.registerItem(ingotCopper, ingotCopper.getUnlocalizedName());
    	OreDictionary.registerOre("ingotCopper", ingotCopper);
    	
    	GameRegistry.registerItem(dustCopper, dustCopper.getUnlocalizedName());
    	OreDictionary.registerOre("dustCopper", dustCopper);
    	GameRegistry.registerItem(dustDiamond, dustDiamond.getUnlocalizedName());
    	OreDictionary.registerOre("dustDiamond", dustDiamond);
    	GameRegistry.registerItem(dustGold, dustGold.getUnlocalizedName());
    	OreDictionary.registerOre("dustGold", dustGold);
    	GameRegistry.registerItem(dustIron, dustIron.getUnlocalizedName());
    	OreDictionary.registerOre("dustIron", dustIron);
    	GameRegistry.registerItem(dustSteel, dustSteel.getUnlocalizedName());
    	OreDictionary.registerOre("dustSteel", dustSteel);
    	GameRegistry.registerItem(dustTin, dustTin.getUnlocalizedName());
    	OreDictionary.registerOre("dustTin", dustTin);
    	
    	GameRegistry.registerItem(wireCopper, wireCopper.getUnlocalizedName());
    	OreDictionary.registerOre("wireCopper", wireCopper);
    	GameRegistry.registerItem(plateCopper, plateCopper.getUnlocalizedName());
    	OreDictionary.registerOre("plateCopper", plateCopper);
    	GameRegistry.registerItem(plateQuartz, plateQuartz.getUnlocalizedName());
    	OreDictionary.registerOre("plateQuartz", plateQuartz);
    	GameRegistry.registerItem(itemInverterCore, itemInverterCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemStageTwoCore, itemStageTwoCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemStageThreeCore, itemStageThreeCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemHeatingElement, itemHeatingElement.getUnlocalizedName());
	}

}
