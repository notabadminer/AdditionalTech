package additionaltech;

import additionaltech.blocks.BlockATechOil;
import additionaltech.blocks.BlockAlgae;
import additionaltech.blocks.BlockAlgaeSlurry;
import additionaltech.blocks.BlockHTL;
import additionaltech.blocks.BlockOreCopper;
import additionaltech.blocks.BlockPhotobioreactor;
import additionaltech.items.ItemAlgae;
import additionaltech.items.ItemBucketOil;
import additionaltech.items.ItemBucketSlurry;
import additionaltech.items.ItemIngotCopper;
import additionaltech.items.ItemMotor;
import additionaltech.items.ItemWireCopper;
import additionaltech.tile.TileHTL;
import additionaltech.tile.TilePhotobioreactor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RegistryHandler {
    public static Block blockPhotobioreactor;
    public static Block blockHydrothermalliquifactor;
    public static Block oreCopper;
    public static Block blockAlgaeSlurry;
    public static Block blockAlgae;
    public static Block blockOil;

    public static Item ingotCopper;
    public static Item wireCopper;
    public static Item itemMotor;
    public static Item itemBucketSlurry;
    public static Item itemAlgae;
    public static Item itemBucketOil;
    
    public static Fluid algaeSlurry;
    public static Fluid oil;

	
	public static void registerBlocks(){
    	blockPhotobioreactor = new BlockPhotobioreactor().setBlockName("blockPhotobioreactor");
    	blockHydrothermalliquifactor = new BlockHTL().setBlockName("blockHydrothermalliquifactor");
    	blockAlgae = new BlockAlgae().setBlockName("blockAlgae");
    	oreCopper = new BlockOreCopper().setBlockName("oreCopper");
    	
    	GameRegistry.registerBlock(blockPhotobioreactor, blockPhotobioreactor.getUnlocalizedName());
    	GameRegistry.registerBlock(blockHydrothermalliquifactor, blockHydrothermalliquifactor.getUnlocalizedName());
    	GameRegistry.registerBlock(blockAlgae, blockAlgae.getUnlocalizedName());    	
    	GameRegistry.registerBlock(oreCopper, oreCopper.getUnlocalizedName());
    	OreDictionary.registerOre("oreCopper", oreCopper);
	}
	
	public static void registerItems(){
		ingotCopper = new ItemIngotCopper().setUnlocalizedName("ingotCopper");
    	wireCopper = new ItemWireCopper().setUnlocalizedName("wireCopper");
    	itemMotor = new ItemMotor().setUnlocalizedName("itemMotor");
    	itemAlgae = new ItemAlgae().setUnlocalizedName("itemAlgae");
    	
    	GameRegistry.registerItem(ingotCopper, ingotCopper.getUnlocalizedName());
    	OreDictionary.registerOre("ingotCopper", ingotCopper);
    	
    	
    	GameRegistry.registerItem(wireCopper, wireCopper.getUnlocalizedName());
    	OreDictionary.registerOre("wireCopper", wireCopper);
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
    	GameRegistry.registerTileEntity(TilePhotobioreactor.class, "TilePhotobioreactor");
    	GameRegistry.registerTileEntity(TileHTL.class, "TileHTL");
	}
}
