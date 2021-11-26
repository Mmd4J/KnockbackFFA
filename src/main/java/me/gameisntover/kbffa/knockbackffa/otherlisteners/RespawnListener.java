package me.gameisntover.kbffa.knockbackffa.otherlisteners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class RespawnListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        KnockbackFFAKit.Kits(player);
    }
}
