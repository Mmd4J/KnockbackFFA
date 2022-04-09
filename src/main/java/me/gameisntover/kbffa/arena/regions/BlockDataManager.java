package me.gameisntover.kbffa.arena.regions;

import lombok.Data;
import me.gameisntover.kbffa.arena.Arena;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

@Data
public class BlockDataManager {
    private Map<Block, DataBlock> blocks = new HashMap<>();

    public DataBlock getBlockData(Block block) {
        if (blocks.containsKey(block)) return blocks.get(block);
        else {
            DataBlock db = new DataBlock(block);
            blocks.put(block, db);
            return db;
        }
    }
}
