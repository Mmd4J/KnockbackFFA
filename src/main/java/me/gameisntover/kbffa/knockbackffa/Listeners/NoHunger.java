package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class NoHunger implements Listener
{
    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame((Player) e.getEntity())) {
            e.setCancelled(true);
        }
    }
}