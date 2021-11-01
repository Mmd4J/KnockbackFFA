package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        if (KnockbackFFA.getInstance().getConfig().getBoolean("block-break")) {
            event.setCancelled(false);
        }else if(!KnockbackFFA.getInstance().getConfig().getBoolean("block-break")==false){
            event.setCancelled(true);
        }
    }
}
