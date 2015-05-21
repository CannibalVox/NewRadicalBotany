package net.technicpack.newradicalbotany.coremodsupport;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.technicpack.newradicalbotany.NewRadicalBotany;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;

public class BiomeCleanseSupport {
    public static void cleanseBiomeAroundTile(TileEntity tile) {
        if (!tile.getWorldObj().isRemote && tile.getWorldObj().rand.nextInt(35) == 0) {
            int x = tile.getWorldObj().rand.nextInt(5) - 2;
            int z = tile.getWorldObj().rand.nextInt(5) - 2;

            int tileBiome = tile.getWorldObj().getBiomeGenForCoords(x + tile.xCoord, z + tile.zCoord).biomeID;
            if (tileBiome == Config.biomeTaintID || tileBiome == Config.biomeEerieID || tileBiome == Config.biomeMagicalForestID) {
                BiomeGenBase[] biomesForGeneration = null;
                biomesForGeneration = tile.getWorldObj().getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x + tile.xCoord, z + tile.zCoord, 1, 1);
                if (biomesForGeneration != null && biomesForGeneration.length > 0 && biomesForGeneration[0] != null) {
                    BiomeGenBase biome = biomesForGeneration[0];
                    Utils.setBiomeAt(tile.getWorldObj(), x + tile.xCoord, z + tile.zCoord, biome);
                }
            }
        }
    }
}
