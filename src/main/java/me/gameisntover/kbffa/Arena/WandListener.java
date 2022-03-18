package me.gameisntover.kbffa.Arena;

import me.gameisntover.kbffa.customconfigs.ArenaConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class WandListener implements Listener {
    public static Map<Player, Location> pos1m = new HashMap<>();
    public static Map<Player, Location> pos2m = new HashMap<>();

    @EventHandler
    public void wandSelection1(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD) && player.isOp() && player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.MENDING)) {
                player.sendMessage("§a Position 1 has been set!");
                e.setCancelled(true);
                pos1m.put(player, e.getClickedBlock().getLocation());
                ArenaConfiguration.save();
            }
        }

    }

    @EventHandler
    public void wandSelection2(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD) && player.isOp() && player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.MENDING)) {
                e.setCancelled(true);
                player.sendMessage("§a Position 2 has been set!");
                pos2m.put(player, e.getClickedBlock().getLocation());
                ArenaConfiguration.save();
            }
        }
    }
}