package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {
    Double pos1x;
    Double pos1y;
    Double pos1z;
    Double pos2x;
    Double pos2y;
    Double pos2z;

    @EventHandler
    public void wandSelection1(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)&&player.isOp()&&player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.MENDING)) {
            player.sendMessage("§a Position 1 has been set!");
            pos1x = e.getBlock().getLocation().getX();
            pos1y = e.getBlock().getLocation().getY();
            pos1z = e.getBlock().getLocation().getZ();
            e.setCancelled(true);
            ArenaConfiguration.get().set("temp-pos1.x",pos1x);
            ArenaConfiguration.get().set("temp-pos1.y",pos1y);
            ArenaConfiguration.get().set("temp-pos1.z",pos1z);
            ArenaConfiguration.save();
        }
    }
    @EventHandler
    public void wandSelection2(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)&&player.isOp()&&player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.MENDING)) {
                e.setCancelled(true);
                player.sendMessage("§a Position 2 has been set!");
                pos2x = e.getClickedBlock().getLocation().getX();
                pos2y = e.getClickedBlock().getLocation().getY();
                pos2z = e.getClickedBlock().getLocation().getZ();
                ArenaConfiguration.get().set("temp-pos2.x",pos2x);
                ArenaConfiguration.get().set("temp-pos2.y",pos2y);
                ArenaConfiguration.get().set("temp-pos2.z",pos2z);
                ArenaConfiguration.save();
            }
        }
    }
}
