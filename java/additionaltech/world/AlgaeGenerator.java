package additionaltech.world;

import java.util.Random;

import additionaltech.AdditionalTech;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class AlgaeGenerator implements IWorldGenerator {
	
	public int spawnRate = 4;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int x = chunkX * 16;
		int z = chunkZ * 16;
		String biomeName = world.getBiomeGenForCoords(x, z).biomeName;
		if(biomeName.contentEquals("Swampland")) {
			for (int i = 0; i < spawnRate; ++i) {
				int xGen = x + random.nextInt(16);
				int zGen = z + random.nextInt(16);
					if (world.getBlock(xGen, 63, zGen) == Blocks.air
						&& AdditionalTech.proxy.blockAlgae.canBlockStay(world, xGen, 63, zGen)) {
						world.setBlock(xGen, 63, zGen, AdditionalTech.proxy.blockAlgae);
					}
			}
		}
	}
}
