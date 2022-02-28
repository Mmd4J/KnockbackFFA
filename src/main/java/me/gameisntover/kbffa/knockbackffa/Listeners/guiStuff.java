package me.gameisntover.kbffa.knockbackffa.Listeners;

import jdk.tools.jlink.internal.plugins.StripNativeCommandsPlugin;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ItemConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class guiStuff implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e){
        KnockbackFFAKit kits = new KnockbackFFAKit();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
            e.setCancelled(true);
            Inventory cosmeticMenu = Bukkit.createInventory(null, 54, "Cosmetic Menu");
            PlayerData.load(player);
            List<String> cList = PlayerData.get().getStringList("owned-cosmetics");
            if (cList != null) {
                cList.forEach(cosmetic -> {
                    ItemStack cosmeticItem = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".material")));
                    ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                    cosmeticMeta.setDisplayName(ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".name".replace("&", "§")));
                    cosmeticMeta.setLore(ItemConfiguration.get().getStringList("CosmeticMenu." + cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    if (PlayerData.get().getString("selected-cosmetic").equals(cosmetic)) {
                        cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&","§") + " §8(§aSelected§8)");
                        cosmeticMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                        cosmeticItem.setItemMeta(cosmeticMeta);
                    }else {
                        cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§"));
                        cosmeticItem.setItemMeta(cosmeticMeta);
                    }
                    cosmeticMenu.addItem(cosmeticItem);
                });
            }
                player.openInventory(cosmeticMenu);
            }

        if (kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
            e.setCancelled(true);
        }
        if (kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
        e.setCancelled(true);

        }
    }
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent e){
        if (e.getView().getTitle().equals("Cosmetic Menu")){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            Player player = (Player) e.getWhoClicked();
            if(item!=null && e.getClickedInventory()!= player.getInventory()){
                ItemMeta meta = item.getItemMeta();
                PlayerData.load(player);
                List<String> ownedCosmetics = PlayerData.get().getStringList("owned-cosmetics");
                String selC = ownedCosmetics.get(e.getSlot());
                if (meta.hasEnchant(Enchantment.DURABILITY)) {
                    PlayerData.get().set("selected-cosmetic", "none");
                } else {
                    PlayerData.get().set("selected-cosmetic", selC);
                }
                PlayerData.save();
                player.closeInventory();
            }
            else if(item!=null && e.getClickedInventory()== player.getInventory()) {
            e.setCancelled(true);
            }
            }
    }
    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e){
        if (e.getView().getTitle().equals("Cosmetic Menu")){
            Player player = (Player) e.getPlayer();
            KnockbackFFAAPI.playSound(player,"guiclose",1,1);
        }
    }
    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent e) {
        if (e.getView().getTitle().equals("Cosmetic Menu")) {
            Player player = (Player) e.getPlayer();
            KnockbackFFAAPI.playSound(player, "guiopen", 1, 1);
        }
    }
}
