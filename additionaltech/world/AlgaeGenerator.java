package additionaltech.world;

import java.util.Random;

import additionaltech.RegistryHandler;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class AlgaeGenerator implements IWorldGenerator {
	
	public int spawnRate = 4;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
		if (b.biomeName.equals("swampland")) {
			for (int i = 0; i < spawnRate; ++i) {
				int xGen = chunkX + random.nextInt(16);
				int zGen = chunkZ + random.nextInt(16);
				for (int yGen = 60; yGen < 66; ++yGen) {
					if (world.getBlock(xGen, yGen, zGen) == Blocks.air
						&& RegistryHandler.blockAlgae.canBlockStay(world, xGen, yGen, zGen)) {
						world.setBlock(xGen, yGen, zGen, RegistryHandler.blockAlgae);
					}
				}
			}
		}
	}
}
