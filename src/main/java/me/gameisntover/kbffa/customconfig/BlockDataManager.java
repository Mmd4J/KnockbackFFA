package me.gameisntover.kbffa.customconfig;

import lombok.Data;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

@Data
public class BlockDataManager {
    private Map<Block,DataBlock> blocks = new HashMap<>();
    public DataBlock getBlockData(Block block){
        if (blocks.containsKey(block)) return blocks.get(block);
        else{
            DataBlock db = new DataBlock(block);
            blocks.put(block,db);
            return db;
        }
    }
}
