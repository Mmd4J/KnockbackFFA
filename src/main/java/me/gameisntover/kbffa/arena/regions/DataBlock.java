package me.gameisntover.kbffa.arena.regions;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Data
public class DataBlock {
    private String blockType;
    private Block block;
    private Material prevMaterial;

    public DataBlock(Block block) {
        setBlock(block);
    }
}
