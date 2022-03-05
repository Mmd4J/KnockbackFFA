package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.API.balanceAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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
            if ( e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    e.setCancelled(true);
                    Inventory cosmeticMenu = Bukkit.createInventory(null, 54, "Cosmetic Menu");
                    PlayerData.load(player);
                    List<String> cList = PlayerData.get().getStringList("owned-cosmetics");
                    if (cList != null) {
                        cList.forEach(cosmetic -> {
                                        if (CosmeticConfiguration.get().getString( cosmetic + ".name") != null) {
                                            ItemStack cosmeticItem = new ItemStack(Material.getMaterial(CosmeticConfiguration.get().getString(cosmetic + ".icon")));
                                            ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                                            cosmeticMeta.setDisplayName(CosmeticConfiguration.get().getString(cosmetic + ".name".replace("&", "§")));
                                            cosmeticMeta.setLore(CosmeticConfiguration.get().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                                            if (PlayerData.get().getString("selected-cosmetic")==null){
                                                PlayerData.get().set("selected-cosmetic", cosmetic);
                                            }
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
                        );
                    }
                    player.openInventory(cosmeticMenu);

                }
                if (kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    e.setCancelled(true);
                    Inventory shopMenu = Bukkit.createInventory(null, 54, "Shop Menu");
                    ItemStack cosmeticItem = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("ShopMenu.cosmetic.material")));
                    ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                    cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name")));
                    cosmeticMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    cosmeticItem.setItemMeta(cosmeticMeta);
                    shopMenu.setItem(ItemConfiguration.get().getInt("ShopMenu.cosmetic.slot"), cosmeticItem);
                    ItemStack kitItem = new ItemStack(Material.getMaterial(ItemConfiguration.get().getString("ShopMenu.kit.material")));
                    ItemMeta kitMeta = kits.guiItemMeta(kitItem);
                    kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name")));
                    kitMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    kitItem.setItemMeta(kitMeta);
                    shopMenu.setItem(ItemConfiguration.get().getInt("ShopMenu.kit.slot"), kitItem);
                    player.openInventory(shopMenu);
                }
                if (kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    e.setCancelled(true);
                    Inventory kitsMenu = Bukkit.createInventory(null, 54, "Kits Menu");
                    PlayerData.load(player);
                  if (PlayerData.get().getList("owned-kits").size() > 0 && PlayerData.get().getList("owned-kits")!=null) {
                    List<String> kitsList = PlayerData.get().getList("owned-kits").stream().map(s -> s.toString()).collect(Collectors.toList());
                        for (String kit : kitsList) {
                            Kits kitItems = new Kits(kit);
                                if (kitItems.get().getString("KitIcon") != null||kitItems.get().getString("KitIcon")!="AIR") {
                                    ItemStack kitItem = new ItemStack(Material.getMaterial(kitItems.get().getString("KitIcon")));
                                if (kitItem.getType()==Material.AIR){
                                    kitItem.setType(Material.BARRIER);
                                }
                                    ItemMeta kitMeta = kits.guiItemMeta(kitItem);
                                    kitMeta.setDisplayName(kitItems.get().getString("KitName").replace("&", "§"));
                                    kitMeta.setLore(kitItems.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
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
    }
    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        KnockbackFFAKit kits = new KnockbackFFAKit();
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
        e.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            Player player = (Player) e.getWhoClicked();
            if (e.getView().getTitle().equals("Cosmetic Menu")) {
                e.setCancelled(true);
                if (item != null && e.getClickedInventory() != player.getInventory()) {
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
                } else if (item != null && e.getClickedInventory() == player.getInventory()) {
                    e.setCancelled(true);
                }
            }
            if (e.getView().getTitle().equals("Shop Menu")) {
                KnockbackFFAKit kits = new KnockbackFFAKit();
                if (e.getSlot() == ItemConfiguration.get().getInt("ShopMenu.kit.slot") && e.getClickedInventory() != player.getInventory()) {
                    Inventory shopMenu = Bukkit.createInventory(null, 54, "Kit Shop");
                    List<String> cosmetics = Arrays.asList(Kits.getfolder().list());
                    PlayerData.load(player);
                    List<String> cList = PlayerData.get().getStringList("owned-kits");
                    for (String cosmetic : cosmetics) {
                        cosmetic = cosmetic.replace(".yml", "");
                            Kits kit = new Kits(cosmetic.replace(".yml",""));
                                ItemStack cosmeticItem = new ItemStack(Material.getMaterial(kit.get().getString("KitIcon")));
                            if (cosmeticItem.getType()==Material.AIR){
                                cosmeticItem.setType(Material.BARRIER);
                            }
                                ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                            cosmeticMeta.setDisplayName(kit.get().getString("KitName").replace("&", "§"));
                            List<String> lore = kit.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            lore.add("§7Cost: §a" + kit.get().getInt("Price"));
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
                if (e.getSlot() == ItemConfiguration.get().getInt("ShopMenu.cosmetic.slot") && e.getClickedInventory() != player.getInventory()) {
                    Inventory shopMenu = Bukkit.createInventory(null, 54, "Cosmetic Shop");
                    List<String> cosmetics = CosmeticConfiguration.get().getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                    PlayerData.load(player);
                    List<String> cList = PlayerData.get().getStringList("owned-cosmetics");
                    for (String cosmetic : cosmetics) {
                            ItemStack cosmeticItem = new ItemStack(Material.getMaterial(CosmeticConfiguration.get().getString( cosmetic + ".icon")));
                            ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem);
                            cosmeticMeta.setDisplayName(CosmeticConfiguration.get().getString( cosmetic + ".name").replace("&", "§"));
                            List<String> lore = CosmeticConfiguration.get().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            lore.add("§7Cost: §a" + CosmeticConfiguration.get().getInt( cosmetic + ".price"));
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
                e.setCancelled(true);
            }
            if (e.getView().getTitle().equalsIgnoreCase("Kit Shop")) {
                if (item != null && e.getClickedInventory() != player.getInventory()) {
                    PlayerData.load(player);
                    List<String> cosmetics = Arrays.asList(Arrays.stream(Kits.getfolder().list()).map(s -> s.replace(".yml","")).toArray(String[]::new));
                    float playerBal = balanceAPI.getBalance(player);
                    if (playerBal >= ItemConfiguration.get().getInt("ShopMenu." + cosmetics.get(e.getSlot()) + ".price")) {
                        List<String> ownedKits = PlayerData.get().getStringList("owned-kits");
                        if (!ownedKits.contains(cosmetics.get(e.getSlot()))) {
                            balanceAPI.removeBalance(player, ItemConfiguration.get().getInt("ShopMenu." + cosmetics.get(e.getSlot()) + ".price"));
                            ownedKits.add(cosmetics.get(e.getSlot()));
                            PlayerData.get().set("owned-kits", ownedKits);
                            PlayerData.save();
                            player.closeInventory();
                            player.sendMessage(MessageConfiguration.get().getString("purchase-success").replace("&", "§").replace("%cosmetic%", cosmetics.get(e.getSlot())));
                        } else {
                            player.sendMessage(MessageConfiguration.get().getString("already-owned").replace("&", "§").replace("%cosmetic%", cosmetics.get(e.getSlot())));
                            player.closeInventory();
                        }
                    } else {
                        player.sendMessage(MessageConfiguration.get().getString("not-enough-money").replace("&", "§"));
                        player.closeInventory();
                    }
                } else if (item != null && e.getClickedInventory() == player.getInventory()) {
                    e.setCancelled(true);
                }
            }
            if (e.getView().getTitle().equalsIgnoreCase("Cosmetic Shop")) {
                if (item != null && e.getClickedInventory() != player.getInventory()) {
                    PlayerData.load(player);
                    List<String> cosmetics = CosmeticConfiguration.get().getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                    float playerBal = balanceAPI.getBalance(player);
                    if (playerBal >= ItemConfiguration.get().getInt("ShopMenu." + cosmetics.get(e.getSlot()) + ".price")) {
                        List<String> ownedCosmetics = PlayerData.get().getStringList("owned-cosmetics");
                        if (!ownedCosmetics.contains(cosmetics.get(e.getSlot()))) {
                            balanceAPI.removeBalance(player, ItemConfiguration.get().getInt("ShopMenu." + cosmetics.get(e.getSlot()) + ".price"));
                            ownedCosmetics.add(cosmetics.get(e.getSlot()));
                            PlayerData.get().set("owned-cosmetics", ownedCosmetics);
                            PlayerData.save();
                            player.closeInventory();
                            player.sendMessage(MessageConfiguration.get().getString("purchase-success").replace("&", "§").replace("%cosmetic%", cosmetics.get(e.getSlot())));
                        } else {
                            player.sendMessage(MessageConfiguration.get().getString("already-owned").replace("&", "§").replace("%cosmetic%", cosmetics.get(e.getSlot())));
                            player.closeInventory();
                        }
                    } else {
                        player.sendMessage(MessageConfiguration.get().getString("not-enough-money").replace("&", "§"));
                        player.closeInventory();
                    }
                } else if (item != null && e.getClickedInventory() == player.getInventory()) {
                    e.setCancelled(true);
                }

            }
            if (e.getView().getTitle().equals("Kits Menu")) {
                e.setCancelled(true);
                if (item != null && e.getClickedInventory() != player.getInventory()) {
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
                } else if (item != null && e.getClickedInventory() == player.getInventory()) {
                    e.setCancelled(true);
                }
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
