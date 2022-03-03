package me.gameisntover.kbffa.knockbackffa.Listeners;

import jdk.tools.jlink.internal.plugins.StripNativeCommandsPlugin;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.API.balanceAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.CosmeticConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ItemConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
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
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        KnockbackFFAKit kits = new KnockbackFFAKit();
        if (e.getItem() != null) {
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
                                if (ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".type").equals("COSMETIC")) {
                                    if (ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".name") != null) {
                                        ItemStack cosmeticItem = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".material")));
                                        ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                                        cosmeticMeta.setDisplayName(ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".name".replace("&", "§")));
                                        cosmeticMeta.setLore(ItemConfiguration.get().getStringList("CosmeticMenu." + cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                                        if (PlayerData.get().getString("selected-cosmetic").equals(cosmetic)) {
                                            cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§") + " §8(§aSelected§8)");
                                            cosmeticMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                                            cosmeticItem.setItemMeta(cosmeticMeta);
                                        } else {
                                            cosmeticMeta.removeEnchant(Enchantment.DURABILITY);
                                            cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§"));
                                            cosmeticItem.setItemMeta(cosmeticMeta);
                                        }
                                        cosmeticMenu.addItem(cosmeticItem);
                                    } else {
                                        cList.remove(cosmetic);
                                        PlayerData.get().set("owned-cosmetics", cList);
                                        PlayerData.save();
                                    }
                                }
                            }
                    );
                }
                player.openInventory(cosmeticMenu);
            }
            if (kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                e.setCancelled(true);
                Inventory shopMenu = Bukkit.createInventory(null, 54, "Shop Menu");
                PlayerData.load(player);
                List<String> cosmetics = CosmeticConfiguration.get().getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                List<String> cList = PlayerData.get().getStringList("owned-cosmetics");
                for (String cosmetic : cosmetics) {
                    ItemStack cosmeticItem = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".material")));
                    ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                    cosmeticMeta.setDisplayName(ItemConfiguration.get().getString("CosmeticMenu." + cosmetic + ".name").replace("&", "§"));
                    List<String> lore = ItemConfiguration.get().getStringList("CosmeticMenu." + cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                    lore.add("§7Cost: §a" + ItemConfiguration.get().getInt("CosmeticMenu." + cosmetic + ".price"));
                    cosmeticMeta.setLore(lore);
                    if (cList.contains(cosmetic)) {
                        cosmeticMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                        cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§") + " §8(§aOwned§8)");
                        cosmeticItem.setItemMeta(cosmeticMeta);
                    } else {
                        cosmeticMeta.removeEnchant(Enchantment.DURABILITY);
                        cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§").replace(" §8(§aOwned§8)", ""));
                        cosmeticItem.setItemMeta(cosmeticMeta);
                    }
                    cosmeticItem.setItemMeta(cosmeticMeta);
                    shopMenu.addItem(cosmeticItem);
                }
                player.openInventory(shopMenu);
            }
            if (kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                e.setCancelled(true);
                Inventory kitsMenu = Bukkit.createInventory(null, 54, "Kits Menu");
                PlayerData.load(player);
                List<String> kitsList = PlayerData.get().getList("owned-kits").stream().map(s -> s.toString()).collect(Collectors.toList());
                if (kitsList != null) {
                    for(String kit : kitsList) {
                            if (ItemConfiguration.get().getString("CosmeticMenu." + kit + ".material") != null) {
                                ItemStack kitItem = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("CosmeticMenu." + kit + ".material")));
                                ItemMeta kitMeta = kits.guiItemMeta(kitItem);
                                kitMeta.setDisplayName(ItemConfiguration.get().getString("CosmeticMenu." + kit + ".name").replace("&", "§"));
                                kitMeta.setLore(ItemConfiguration.get().getStringList("CosmeticMenu." + kit + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                                if (PlayerData.get().getString("selected-kit") == null) {
                                PlayerData.get().set("selected-kit", kit);
                                PlayerData.save();
                                }
                                if (PlayerData.get().getString("selected-kit").equalsIgnoreCase(kit)) {
                                    kitMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                                    kitMeta.setDisplayName(kitMeta.getDisplayName().replace("&", "§") + " §8(§aSelected§8)");
                                } else {
                                    kitMeta.removeEnchant(Enchantment.DURABILITY);
                                    kitMeta.setDisplayName(kitMeta.getDisplayName().replace("&", "§").replace(" §8(§aSelected§8)", ""));
                                }
                                kitItem.setItemMeta(kitMeta);
                                kitsMenu.addItem(kitItem);
                            }
                        }
                    }
                    player.openInventory(kitsMenu);
                }
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
        if (e.getView().getTitle().equals("Shop Menu")){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            Player player = (Player) e.getWhoClicked();
            if(item!=null && e.getClickedInventory()!= player.getInventory()){
                PlayerData.load(player);
                List<String> cosmetics = CosmeticConfiguration.get().getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                float playerBal = balanceAPI.getBalance(player);
                if(playerBal>=ItemConfiguration.get().getInt("CosmeticMenu." + cosmetics.get(e.getSlot()) + ".price")){
                  List<String> ownedCosmetics = PlayerData.get().getStringList("owned-cosmetics");
                  List<String> ownedKits = PlayerData.get().getStringList("owned-kits");
                  if (!ownedCosmetics.contains(cosmetics.get(e.getSlot())) && !ownedKits.contains(cosmetics.get(e.getSlot()))){
                      balanceAPI.removeBalance(player, ItemConfiguration.get().getInt("CosmeticMenu." + cosmetics.get(e.getSlot()) + ".price"));
                  if (ItemConfiguration.get().getString("CosmeticMenu." + cosmetics.get(e.getSlot()) + ".type").equalsIgnoreCase("KIT")) {
                     ownedKits.add(cosmetics.get(e.getSlot()));
                      PlayerData.get().set("owned-kits",ownedKits);
                      PlayerData.save();
                  }else if (ItemConfiguration.get().getString("CosmeticMenu." + cosmetics.get(e.getSlot()) + ".type").equalsIgnoreCase("COSMETIC")) {
                      ownedCosmetics.add(cosmetics.get(e.getSlot()));
                      PlayerData.get().set("owned-cosmetics", ownedCosmetics);
                      PlayerData.save();
                  }
                      player.closeInventory();
                      player.sendMessage(MessageConfiguration.get().getString("purchase-success").replace("&", "§").replace("%cosmetic%", cosmetics.get(e.getSlot())));
                  }else {
                      player.sendMessage(MessageConfiguration.get().getString("already-owned").replace("&", "§").replace("%cosmetic%", cosmetics.get(e.getSlot())));
                    player.closeInventory();
                  }
                }else{
                    player.sendMessage(MessageConfiguration.get().getString("not-enough-money").replace("&", "§"));
                    player.closeInventory();
                }
                }           else if(item!=null && e.getClickedInventory()== player.getInventory()) {
                e.setCancelled(true);
            }
            }
        if (e.getView().getTitle().equals("Kits Menu")){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            Player player = (Player) e.getWhoClicked();
            if(item!=null && e.getClickedInventory()!= player.getInventory()){
                ItemMeta meta = item.getItemMeta();
                PlayerData.load(player);
                List<String> ownedCosmetics = PlayerData.get().getStringList("owned-kits");
                String selC = ownedCosmetics.get(e.getSlot());
                if (meta.hasEnchant(Enchantment.DURABILITY)) {
                    PlayerData.get().set("selected-kit", "none");
                } else {
                    PlayerData.get().set("selected-kit", selC);
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
        if (e.getView().getTitle().equals("Cosmetic Menu")||e.getView().getTitle().equals("Kits Menu")){
            Player player = (Player) e.getPlayer();
            KnockbackFFAAPI.playSound(player,"guiclose",1,1);
        }
    }
    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent e) {
        if (e.getView().getTitle().equals("Cosmetic Menu")||e.getView().getTitle().equals("Kits Menu")){
            Player player = (Player) e.getPlayer();
            KnockbackFFAAPI.playSound(player, "guiopen", 1, 1);
        }
    }
}
