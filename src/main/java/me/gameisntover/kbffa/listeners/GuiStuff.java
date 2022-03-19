package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.api.BalanceAPI;
import me.gameisntover.kbffa.customconfig.*;
import me.gameisntover.kbffa.message.Message;
import me.gameisntover.kbffa.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiStuff implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        KnockbackFFAKit kits = new KnockbackFFAKit();
        if (e.getItem() == null) return;
            ItemStack item = e.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            Player player = e.getPlayer();
            if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    e.setCancelled(true);
                    InventoryGUI cosmeticMenu = new InventoryGUI(Bukkit.createInventory(null, 54, "Cosmetic Menu"));
                    PlayerData.load(player);
                    List<String> cList = PlayerData.get().getStringList("owned-cosmetics");
                    cList.forEach(cosmetic -> {
                                if (CosmeticConfiguration.get().getString(cosmetic + ".name") == null) {
                                    String displayName = ChatColor.translateAlternateColorCodes('&', CosmeticConfiguration.get().getString(cosmetic + ".name"));
                                    String icon = CosmeticConfiguration.get().getString(cosmetic + ".icon");
                                    List<String> lore = CosmeticConfiguration.get().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                                    assert icon != null;
                                    ItemButton cosmeticItem = ItemButton.create(new ItemBuilder(Material.getMaterial(icon)).setName(displayName), event -> {
                                        List<String> ownedCosmetics = PlayerData.get().getStringList("owned-cosmetics");
                                        String selC = ownedCosmetics.get(event.getSlot());
                                        if (CosmeticConfiguration.get().getString(selC + ".type") == "KILL_PARTICLE")
                                            PlayerData.get().set("selected-trails", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");

                                        else if (CosmeticConfiguration.get().getString(selC + ".type") == "TRAIL")
                                            PlayerData.get().set("selected-trails", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                                        PlayerData.save();
                                        player.closeInventory();
                                    });
                                    ItemMeta meta = kits.guiItemMeta(cosmeticItem.getItem());
                                    if (CosmeticConfiguration.get().getString(cosmetic + ".type") == "KILL_PARTICLE") {
                                        if (PlayerData.get().getString("selected-cosmetic") == null)
                                            PlayerData.get().set("selected-cosmetic", cosmetic);
                                        if (PlayerData.get().getString("selected-cosmetic").equals(cosmetic)) {
                                            meta.setDisplayName(meta.getDisplayName().replace("&", "§") + " §8(§aSelected§8)");
                                            meta.addEnchant(Enchantment.DURABILITY, 1, true);
                                        } else {
                                            meta.removeEnchant(Enchantment.DURABILITY);
                                            meta.setDisplayName(meta.getDisplayName().replace("&", "§"));
                                        }
                                    } else if (CosmeticConfiguration.get().getString(cosmetic + ".type").equals("TRAIL"))
                                        if (PlayerData.get().getString("selected-trails") == null)
                                            PlayerData.get().set("selected-trails", cosmetic);

                                    if (PlayerData.get().getString("selected-trails").equals(cosmetic)) {
                                        meta.setDisplayName(meta.getDisplayName().replace("&", "§") + " §8(§aSelected§8)");
                                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                                    } else {
                                        meta.removeEnchant(Enchantment.DURABILITY);
                                        meta.setDisplayName(meta.getDisplayName().replace("&", "§"));
                                    }
                                    meta.setLore(lore);
                                    cosmeticItem.getItem().setItemMeta(meta);
                                    cosmeticMenu.addButton(cosmeticItem, cList.indexOf(cosmetic));
                                } else {
                                    cList.remove(cosmetic);
                                    PlayerData.get().set("owned-cosmetics", cList);
                                    PlayerData.save();
                                }
                            }
                    );
                    cosmeticMenu.open(player);
                }
                if (kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    e.setCancelled(true);
                    InventoryGUI shopMenu = new InventoryGUI(Bukkit.createInventory(null, 54, "Shop Menu"));
                    String cIcon = ItemConfiguration.get().getString("ShopMenu.cosmetic.material");
                    String cName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name"));
                    ItemButton cosmeticItem = ItemButton.create(new ItemBuilder(Material.getMaterial(cIcon)).setName(cName), event -> {
                        InventoryGUI cosmeticShop = new InventoryGUI(Bukkit.createInventory(null, 54, "Cosmetic Shop"));
                        List<String> cosmetics = CosmeticConfiguration.get().getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                        List<String> cList = PlayerData.get().getStringList("owned-cosmetics");
                        for (String cosmetic : cosmetics) {
                            ItemButton cosmeticsItem = ItemButton.create(new ItemBuilder(Material.getMaterial(CosmeticConfiguration.get().getString(cosmetic + ".icon"))).setName(ChatColor.translateAlternateColorCodes('&', CosmeticConfiguration.get().getString(cosmetic + ".name"))), event1 -> {
                                float playerBal = BalanceAPI.getBalance(player);
                                if (playerBal >= CosmeticConfiguration.get().getInt(cosmetics.get(event1.getSlot()) + ".price")) {
                                    List<String> ownedCosmetics = PlayerData.get().getStringList("owned-cosmetics");
                                    if (!ownedCosmetics.contains(cosmetics.get(event1.getSlot()))) {
                                        BalanceAPI.removeBalance(player, ItemConfiguration.get().getInt(cosmetics.get(event1.getSlot()) + ".price"));
                                        ownedCosmetics.add(cosmetics.get(event1.getSlot()));
                                        PlayerData.get().set("owned-cosmetics", ownedCosmetics);
                                        PlayerData.save();
                                        player.sendMessage(Message.COSMETIC_PURCHASE_SUCCESS.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                    } else player.sendMessage(Message.COSMETIC_ALREADY_OWNED.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                } else player.sendMessage(Message.COSMETIC_NO_ENOUGH_MONEY.toString().replace("&", "§"));
                                player.closeInventory();
                            });
                            ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticsItem.getItem());
                            List<String> lore = CosmeticConfiguration.get().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            lore.add("§7Cost: §a" + CosmeticConfiguration.get().getInt(cosmetic + ".price"));
                            cosmeticMeta.setLore(lore);
                            if (cList.contains(cosmetic)) {
                                cosmeticMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                                cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§") + " §8(§aOwned§8)");
                            } else {
                                cosmeticMeta.removeEnchant(Enchantment.DURABILITY);
                                cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§").replace(" §8(§aOwned§8)", ""));
                            }
                            cosmeticsItem.getItem().setItemMeta(cosmeticMeta);
                            cosmeticShop.addButton(cosmeticsItem, cosmetics.indexOf(cosmetic));
                        }
                        cosmeticShop.open(player);
                    });
                    ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem.getItem());
                    cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name")));
                    cosmeticMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    cosmeticItem.getItem().setItemMeta(cosmeticMeta);
                    shopMenu.addButton(ItemConfiguration.get().getInt("ShopMenu.cosmetic.slot"), cosmeticItem);
                    String kIcon = ItemConfiguration.get().getString("ShopMenu.kit.material");
                    String kName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name"));
                    ItemButton kitItem = ItemButton.create(new ItemBuilder(Material.getMaterial(kIcon)).setName(kName), event -> {
                        InventoryGUI kitShop = new InventoryGUI(Bukkit.createInventory(null, 54, "Kit Shop"));
                        List<String> cosmetics = Arrays.asList(Kits.getfolder().list()).stream().map(s -> s.replace(".yml", "")).collect(Collectors.toList());
                        List<String> cList = PlayerData.get().getStringList("owned-kits");
                        for (String cosmetic : cosmetics) {
                            if (cosmetic == null) return;
                                Kits kit = Kits.load(cosmetic);
                                String kitIcon = kit.get().getString("KitIcon");
                                String kitName = kit.get().getString("KitName").replace("&", "§");
                                ItemButton kitsItem = ItemButton.create(new ItemBuilder(Material.getMaterial(kitIcon)).setName(kitName), event1 -> {
                                    float playerBal = BalanceAPI.getBalance(player);
                                    if (playerBal >= kit.get().getInt("Price")) {
                                        List<String> ownedKits = PlayerData.get().getStringList("owned-kits");
                                        if (!ownedKits.contains(cosmetics.get(event1.getSlot()))) {
                                            BalanceAPI.removeBalance(player, kit.get().getInt("Price"));
                                            ownedKits.add(cosmetics.get(event1.getSlot()));
                                            PlayerData.get().set("owned-kits", ownedKits);
                                            PlayerData.save();
                                            player.closeInventory();
                                            player.sendMessage(Message.COSMETIC_PURCHASE_SUCCESS.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                        } else player.sendMessage(Message.COSMETIC_ALREADY_OWNED.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                    } else player.sendMessage(Message.COSMETIC_NO_ENOUGH_MONEY.toString().replace("&", "§"));

                                    player.closeInventory();
                                });
                                if (kitsItem.getItem().getType() == Material.AIR) kitsItem.getItem().setType(Material.BARRIER);
                                ItemMeta kitsMeta = kits.guiItemMeta(kitsItem.getItem());
                                List<String> lore = kit.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                                lore.add("§7Cost: §a" + kit.get().getInt("Price"));
                                kitsMeta.setLore(lore);
                                if (cList.contains(cosmetic)) {
                                    kitsMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                                    kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§") + " §8(§aOwned§8)");
                                } else {
                                    kitsMeta.removeEnchant(Enchantment.DURABILITY);
                                    kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§").replace(" §8(§aOwned§8)", ""));
                                }
                            kitsItem.getItem().setItemMeta(kitsMeta);
                            kitShop.addButton(kitsItem, cosmetics.indexOf(cosmetic));
                            }
                        kitShop.open(player);
                    });
                    ItemMeta kitMeta = kits.guiItemMeta(kitItem.getItem());
                    kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name")));
                    kitMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    kitItem.getItem().setItemMeta(kitMeta);
                    shopMenu.addButton(ItemConfiguration.get().getInt("ShopMenu.kit.slot"), kitItem);
                    shopMenu.destroysOnClose();
                    shopMenu.open(player);
                }
                if (kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    e.setCancelled(true);
                    InventoryGUI kitsMenu = new InventoryGUI(Bukkit.createInventory(null, 54, "Kits Menu"));
                    PlayerData.load(player);
                    if (PlayerData.get().getList("owned-kits") != null && PlayerData.get().getList("owned-kits").size() > 0) {
                        if (PlayerData.get().getList("owned-kits") == null) PlayerData.get().set("owned-kits", new ArrayList<>());

                        List<String> kitsList = PlayerData.get().getList("owned-kits").stream().map(s -> s.toString()).collect(Collectors.toList());
                        for (String kit : kitsList) {
                            if (kit == null) return;
                                Kits kitItems = Kits.load(kit);
                                if (kitItems.get().getString("KitIcon") == null) return;
                                    String icon = kitItems.get().getString("KitIcon");
                                    String name = kitItems.get().getString("KitName").replace("&", "§");
                                    ItemButton kitItem = ItemButton.create(new ItemBuilder(Material.getMaterial(icon)).setName(name), event -> {
                                        String selC = kitsList.get(event.getSlot());
                                        PlayerData.get().set("selected-kit" , event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                                        PlayerData.save();
                                        player.closeInventory();
                                    });
                                    ItemMeta kitMeta = kits.guiItemMeta(kitItem.getItem());
                                    if (kitItem.getItem().getType() == Material.AIR) kitItem.getItem().setType(Material.BARRIER);

                                    kitMeta.setLore(kitItems.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                                    if (PlayerData.get().getString("selected-kit") == null) PlayerData.get().set("selected-kit", kit);

                                    if (PlayerData.get().getString("selected-kit").equalsIgnoreCase(kit)) {
                                        kitMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                                        kitMeta.setDisplayName(kitMeta.getDisplayName().replace("&", "§") + " §8(§aSelected§8)");
                                    } else {
                                        kitMeta.removeEnchant(Enchantment.DURABILITY);
                                        kitMeta.setDisplayName(kitMeta.getDisplayName().replace("&", "§").replace(" §8(§aSelected§8)", ""));
                                    }
                                    kitItem.getItem().setItemMeta(kitMeta);
                                    kitsMenu.addButton(kitItem, kitsList.indexOf(kit));
                                }
                        PlayerData.save();
                    }
                    kitsMenu.open(player);
                }
            }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        if (!e.getView().getTitle().equals("Cosmetic Menu") || !e.getView().getTitle().equals("Kits Menu")) return;
            Player player = (Player) e.getPlayer();
            PlayerUtil.playSound(player, "guiclose", 1, 1);
    }

    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent e) {
        if (!e.getView().getTitle().equals("Cosmetic Menu") || !e.getView().getTitle().equals("Kits Menu")) return;
            Player player = (Player) e.getPlayer();
            PlayerUtil.playSound(player, "guiopen", 1, 1);
    }
}
