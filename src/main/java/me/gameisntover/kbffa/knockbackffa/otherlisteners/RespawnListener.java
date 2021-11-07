package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class RespawnListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        ItemStack kbstick = new ItemStack(Material.STICK, 1);
        ItemMeta meta = kbstick.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Knocbkack Stick");
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        kbstick.setItemMeta(meta);
        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
        ItemMeta enderpearlmeta = enderpearl.getItemMeta();
        enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
        enderpearl.setItemMeta(enderpearlmeta);
        Inventory pinventory = player.getInventory();
        pinventory.clear();
        pinventory.addItem(kbstick, enderpearl);
        player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(999999999, 255));
    }
}
