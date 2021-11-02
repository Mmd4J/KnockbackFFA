package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class NoItemDrop implements Listener {
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFA.getInstance().getConfig().getBoolean("no-item-drop")) {
            e.setCancelled(true);
        }else if (KnockbackFFA.getInstance().getConfig().getBoolean("no-item-drop") == false) {
            e.setCancelled(false);
        }else {
            if (player.isOp()){
                player.sendMessage("Item drop isnt configured! please set it true or false in KnockBackFFA config files");
            }
        }
    }
}
