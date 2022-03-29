package me.gameisntover.kbffa.arena;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class VoidChunkGenerator extends ChunkGenerator {
    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world
            , @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        return createChunkData(world);
    }
}
