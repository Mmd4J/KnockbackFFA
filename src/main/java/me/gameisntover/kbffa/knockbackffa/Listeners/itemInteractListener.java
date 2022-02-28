package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class itemInteractListener implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e){
        KnockbackFFAKit kits = new KnockbackFFAKit();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
            e.setCancelled(true);
            Inventory cosmeticMenu = Bukkit.createInventory(null, 9, "Cosmetic Menu");
            ItemStack endKillEffect = new ItemStack(Material.ENDER_PEARL);
            ItemMeta endKillMeta = endKillEffect.getItemMeta();
            endKillMeta.setUnbreakable(true);
            endKillMeta.addItemFlags();
        }
        if (kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
            e.setCancelled(true);
            player.sendMessage("t");
        }
        if (kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
        e.setCancelled(true);

        }
    }
}
