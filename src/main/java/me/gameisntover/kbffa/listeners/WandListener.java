package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.util.Items;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class WandListener implements Listener {
    public static Map<Player, Location> pos1m = new HashMap<>();
    public static Map<Player, Location> pos2m = new HashMap<>();

    @EventHandler
    public void wandSelectionEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.getInventory().getItemInMainHand().equals(Items.POSITION_SELECTOR_WAND.getItem())) return;
        if (e.getClickedBlock() == null) return;
        Location clickedLocation = e.getClickedBlock().getLocation();
        switch (e.getAction()) {
            case LEFT_CLICK_BLOCK:
                e.setCancelled(true);
                if (clickedLocation.equals(pos1m.get(player)) || clickedLocation.equals(pos2m.get(player))) return;
                player.sendMessage("§6 Position 1 has been set to " + clickedLocation.getX() + "," + clickedLocation.getY() + "," + clickedLocation.getZ());
                pos1m.put(player, clickedLocation);
                break;
            case RIGHT_CLICK_BLOCK:
                e.setCancelled(true);
                if (clickedLocation.equals(pos1m.get(player)) || clickedLocation.equals(pos2m.get(player))) return;
                player.sendMessage("§6 Position 2 has been set to " + clickedLocation.getX() + "," + clickedLocation.getY() + "," + clickedLocation.getZ());
                pos2m.put(player, clickedLocation);
                break;
        }
    }

}

