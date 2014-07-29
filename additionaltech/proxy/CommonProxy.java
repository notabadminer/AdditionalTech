package additionaltech.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import additionaltech.blocks.BlockATechOil;
import additionaltech.blocks.BlockAlgae;
import additionaltech.blocks.BlockAlgaeSlurry;
import additionaltech.blocks.BlockEFurnace;
import additionaltech.blocks.BlockESM;
import additionaltech.blocks.BlockESMIICore;
import additionaltech.blocks.BlockESMIIICore;
import additionaltech.blocks.BlockGrinder;
import additionaltech.blocks.BlockHTL;
import additionaltech.blocks.BlockHeatsink;
import additionaltech.blocks.BlockOreCopper;
import additionaltech.blocks.BlockPhotobioreactor;
import additionaltech.blocks.BlockSolarInverter;
import additionaltech.blocks.BlockSolarPanel;
import additionaltech.items.ItemAlgae;
import additionaltech.items.ItemBlockESM;
import additionaltech.items.ItemBucketOil;
import additionaltech.items.ItemBucketSlurry;
import additionaltech.items.ItemDiamondGrindstone;
import additionaltech.items.ItemDustCopper;
import additionaltech.items.ItemDustDiamond;
import additionaltech.items.ItemDustGlass;
import additionaltech.items.ItemDustGold;
import additionaltech.items.ItemDustIron;
import additionaltech.items.ItemDustObsidian;
import additionaltech.items.ItemDustQuartz;
import additionaltech.items.ItemDustSteel;
import additionaltech.items.ItemDustTin;
import additionaltech.items.ItemGrindstone;
import additionaltech.items.ItemHeatingElement;
import additionaltech.items.ItemIngotCopper;
import additionaltech.items.ItemInverterCore;
import additionaltech.items.ItemIronGrindstone;
import additionaltech.items.ItemMotor;
import additionaltech.items.ItemStageThreeCore;
import additionaltech.items.ItemStageTwoCore;
import additionaltech.items.ItemWireCopper;
import additionaltech.tile.TileEFurnace;
import additionaltech.tile.TileESM;
import additionaltech.tile.TileGrinder;
import additionaltech.tile.TileHTL;
import additionaltech.tile.TileHeatsink;
import additionaltech.tile.TilePhotobioreactor;
import additionaltech.tile.TileSolarInverter;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
	public static Block blockSolarPanel;
    public static Block blockSolarInverter;
    public static Block blockEFurnace;
    public static Block blockESM;
    public static Block blockGrinder;
    public static Block blockESMIICore;
    public static Block blockESMIIICore;
    public static Block blockPhotobioreactor;
    public static Block blockHydrothermalliquifactor;
    public static Block oreCopper;
    public static Block blockAlgaeSlurry;
    public static Block blockAlgae;
    public static Block blockOil;
    public static Block blockHeatsink;

    public static Item dustCopper;
    public static Item dustDiamond;
    public static Item dustGold;
    public static Item dustIron;
    public static Item dustObsidian;
    public static Item dustSteel;
    public static Item dustTin;
    public static Item dustQuartz;
    public static Item dustGlass;
    public static Item ingotCopper;
    public static Item wireCopper;
    public static Item itemInverterCore;
    public static Item itemStageTwoCore;
    public static Item itemStageThreeCore;
    public static Item itemHeatingElement;
    public static Item itemBlockESM;
    public static Item itemGrindstone;
    public static Item itemIronGrindstone;
    public static Item itemDiamondGrindstone;
    public static Item itemMotor;
    public static Item itemBucketSlurry;
    public static Item itemAlgae;
    public static Item itemBucketOil;
    
    public static Fluid algaeSlurry;
    public static Fluid oil;

	
	public static void registerBlocks(){
		blockSolarPanel = new BlockSolarPanel().setBlockName("blockSolarPanel");
    	blockSolarInverter = new BlockSolarInverter().setBlockName("blockSolarInverter");
    	blockEFurnace = new BlockEFurnace().setBlockName("blockEFurnace");
    	blockESM = new BlockESM().setBlockName("blockESM");
    	blockESMIICore = new BlockESMIICore().setBlockName("blockESMIICore");
    	blockESMIIICore = new BlockESMIIICore().setBlockName("blockESMIIICore");
    	blockGrinder = new BlockGrinder().setBlockName("blockGrinder");
    	blockPhotobioreactor = new BlockPhotobioreactor().setBlockName("blockPhotobioreactor");
    	blockHydrothermalliquifactor = new BlockHTL().setBlockName("blockHydrothermalliquifactor");
    	blockAlgae = new BlockAlgae().setBlockName("blockAlgae");
    	blockHeatsink = new BlockHeatsink().setBlockName("blockHeatsink");
    	oreCopper = new BlockOreCopper().setBlockName("oreCopper");
    	
    	GameRegistry.registerBlock(blockSolarPanel, blockSolarPanel.getUnlocalizedName());
    	GameRegistry.registerBlock(blockSolarInverter, blockSolarInverter.getUnlocalizedName());
    	GameRegistry.registerBlock(blockEFurnace, blockEFurnace.getUnlocalizedName());
    	GameRegistry.registerBlock(blockESM, ItemBlockESM.class, blockESM.getUnlocalizedName());
    	GameRegistry.registerBlock(blockESMIICore, blockESMIICore.getUnlocalizedName());
    	GameRegistry.registerBlock(blockESMIIICore, blockESMIIICore.getUnlocalizedName());
    	GameRegistry.registerBlock(blockGrinder, blockGrinder.getUnlocalizedName());
    	GameRegistry.registerBlock(blockPhotobioreactor, blockPhotobioreactor.getUnlocalizedName());
    	GameRegistry.registerBlock(blockHydrothermalliquifactor, blockHydrothermalliquifactor.getUnlocalizedName());
    	GameRegistry.registerBlock(blockAlgae, blockAlgae.getUnlocalizedName());
    	GameRegistry.registerBlock(blockHeatsink, blockHeatsink.getUnlocalizedName());
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
		dustQuartz = new ItemDustQuartz().setUnlocalizedName("dustQuartz");
		dustGlass = new ItemDustGlass().setUnlocalizedName("dustGlass");

    	wireCopper = new ItemWireCopper().setUnlocalizedName("wireCopper");
    	itemInverterCore = new ItemInverterCore().setUnlocalizedName("itemInverterCore");
    	itemStageTwoCore = new ItemStageTwoCore().setUnlocalizedName("itemStageTwoCore");
    	itemStageThreeCore = new ItemStageThreeCore().setUnlocalizedName("itemStageThreeCore");
    	itemHeatingElement = new ItemHeatingElement().setUnlocalizedName("itemHeatingElement");
    	itemGrindstone = new ItemGrindstone().setUnlocalizedName("itemGrindstone");
    	itemIronGrindstone = new ItemIronGrindstone().setUnlocalizedName("itemIronGrindstone");
    	itemDiamondGrindstone = new ItemDiamondGrindstone().setUnlocalizedName("itemDiamondGrindstone");
    	itemMotor = new ItemMotor().setUnlocalizedName("itemMotor");
    	itemAlgae = new ItemAlgae().setUnlocalizedName("itemAlgae");
    	
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
    	GameRegistry.registerItem(dustQuartz, dustQuartz.getUnlocalizedName());
    	OreDictionary.registerOre("dustQuartz", dustQuartz);
    	GameRegistry.registerItem(dustGlass, dustGlass.getUnlocalizedName());
    	OreDictionary.registerOre("dustGlass", dustGlass);
    	
    	GameRegistry.registerItem(wireCopper, wireCopper.getUnlocalizedName());
    	OreDictionary.registerOre("wireCopper", wireCopper);
    	GameRegistry.registerItem(itemInverterCore, itemInverterCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemStageTwoCore, itemStageTwoCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemStageThreeCore, itemStageThreeCore.getUnlocalizedName());
    	GameRegistry.registerItem(itemHeatingElement, itemHeatingElement.getUnlocalizedName());
    	GameRegistry.registerItem(itemGrindstone, itemGrindstone.getUnlocalizedName());
    	GameRegistry.registerItem(itemIronGrindstone, itemIronGrindstone.getUnlocalizedName());
    	GameRegistry.registerItem(itemDiamondGrindstone, itemDiamondGrindstone.getUnlocalizedName());
    	GameRegistry.registerItem(itemMotor, itemMotor.getUnlocalizedName());
    	GameRegistry.registerItem(itemAlgae, itemAlgae.getUnlocalizedName());
	}
	
	public static void registerFluids() {
		Fluid algaeslurry = new Fluid("algaeslurry").setUnlocalizedName("agaeslurry");
		FluidRegistry.registerFluid(algaeslurry);
		blockAlgaeSlurry = new BlockAlgaeSlurry(algaeslurry, Material.water).setBlockName("blockAlgaeSlurry");
		GameRegistry.registerBlock(blockAlgaeSlurry, blockAlgaeSlurry.getUnlocalizedName());
		algaeslurry.setUnlocalizedName(blockAlgaeSlurry.getUnlocalizedName());
		
		Fluid oil = new Fluid("oil").setUnlocalizedName("oil");
		FluidRegistry.registerFluid(oil);
		blockOil = new BlockATechOil(oil, Material.water).setBlockName("blockATechOil");
		GameRegistry.registerBlock(blockOil, blockOil.getUnlocalizedName());
		oil.setUnlocalizedName(blockOil.getUnlocalizedName());
		
		itemBucketSlurry = new ItemBucketSlurry().setUnlocalizedName("itemBucketSlurry");
		itemBucketSlurry.setContainerItem(Items.bucket);
		GameRegistry.registerItem(itemBucketSlurry, itemBucketSlurry.getUnlocalizedName());
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(algaeslurry.getName(), FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketSlurry), new ItemStack(Items.bucket));
		
		itemBucketOil = new ItemBucketOil().setUnlocalizedName("itemBucketOil");
		itemBucketOil.setContainerItem(Items.bucket);
		GameRegistry.registerItem(itemBucketOil, itemBucketOil.getUnlocalizedName());
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(oil.getName(), FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketOil), new ItemStack(Items.bucket));
	}
	
	public static void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileSolarInverter.class, "TileSolarInverter");
    	GameRegistry.registerTileEntity(TileEFurnace.class, "TileEFurnace");
    	GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinder");
    	GameRegistry.registerTileEntity(TileESM.class, "TileESM");
    	GameRegistry.registerTileEntity(TilePhotobioreactor.class, "TilePhotobioreactor");
    	GameRegistry.registerTileEntity(TileHTL.class, "TileHTL");
    	GameRegistry.registerTileEntity(TileHeatsink.class, "TileHeatsink");
	}
	
	public void registerRenderers() {
	}

}
