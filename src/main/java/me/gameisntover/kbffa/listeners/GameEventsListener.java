package me.gameisntover.kbffa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class GameEventsListener implements Listener {
    @EventHandler
    public void noHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e.getFoodLevel() < 20) e.setFoodLevel(20);
    }
}
