package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class NoHunger implements Listener {
    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame((Player) e.getEntity())) return;
            if (e.getEntity().getFoodLevel() != 20) e.getEntity().setFoodLevel(20);
            e.setCancelled(true);
        }
    }