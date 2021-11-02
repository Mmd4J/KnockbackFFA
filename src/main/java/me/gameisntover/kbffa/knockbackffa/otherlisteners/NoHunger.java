package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class NoHunger implements Listener {
        @EventHandler
        public void onHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        }
}