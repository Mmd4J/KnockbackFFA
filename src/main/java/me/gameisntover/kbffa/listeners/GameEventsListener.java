package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.customconfig.Knocker;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GameEventsListener implements Listener {
    @EventHandler
    public void noHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e.getFoodLevel() < 20) e.setFoodLevel(20);
    }

    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(e.getPlayer());
        if (KnockbackFFA.getINSTANCE().BungeeMode() || knocker.isInGame())
            e.setFormat(Message.CHATFORMAT.toString().replace("%player%", player.getName()).replace("%message%", e.getMessage()));
    }
}
