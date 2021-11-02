package me.gameisntover.kbffa.knockbackffa.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class kbffakits implements Listener {
    @EventHandler
    public void guiClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Kits")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.STICK)) {
                ItemStack kbstick = new ItemStack(Material.STICK, 1);
                ItemStack fireball = new ItemStack(Material.FIRE_CHARGE, 3);
                ItemMeta fireballmeta = fireball.getItemMeta();
                fireballmeta.addEnchant(Enchantment.MENDING, 1, true);
                fireballmeta.setDisplayName(ChatColor.YELLOW + "FireBall");
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
                player.setFoodLevel(100);
                player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(999999999, 255));
                player.closeInventory();
            }
        }
    }
}

