package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.kit.Kit;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.gui.Button;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.util.ItemBuilder;
import me.gameisntover.kbffa.util.Items;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiStuff implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || item.getItemMeta()==null) return;
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        assert itemMeta != null;
        if (Items.COSMETIC_ITEM.getItem().equals(item)) {
            e.setCancelled(true);
            GUI cosmeticMenu = new GUI("Cosmetic Menu", (short) 5);
            List<String> cList = knocker.getConfig().getStringList("owned-cosmetics");
            cList.forEach(cosmetic -> {
                        if (KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".name") == null) {
                            String displayName = ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".name"));
                            String icon = KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".icon");
                            List<String> lore = KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            assert icon != null;
                            Button cosmeticItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(icon), 1, displayName, Collections.singletonList("")).create(), event -> {
                                List<String> ownedCosmetics = knocker.getConfig().getStringList("owned-cosmetics");
                                String selC = ownedCosmetics.get(event.getSlot());
                                if (KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(selC + ".type") == "KILL_PARTICLE")
                                    knocker.getConfig().set("selected-trails", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");

                                else if (KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(selC + ".type") == "TRAIL")
                                    knocker.getConfig().set("selected-trails", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                                knocker.saveConfig();
                                player.closeInventory();
                            });
                            ItemMeta meta = cosmeticItem.getItem().getItemMeta();
                            if (KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".type").equals("KILL_PARTICLE")) {
                                if (knocker.getConfig().getString("selected-cosmetic") == null)
                                    knocker.getConfig().set("selected-cosmetic", cosmetic);
                                cosmeticItem.setSelected(knocker.getConfig().getString("selected-cosmetic").equals(cosmetic));
                            } else if (KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".type").equals("TRAIL"))
                                if (knocker.getConfig().getString("selected-trails") == null)
                                    knocker.getConfig().set("selected-trails", cosmetic);
                            cosmeticItem.setSelected(knocker.getConfig().getString("selected-trails").equals(cosmetic));
                            meta.setLore(lore);
                            cosmeticItem.getItem().setItemMeta(meta);
                            cosmeticMenu.add(cosmeticItem, cList.indexOf(cosmetic));
                        } else {
                            cList.remove(cosmetic);
                            knocker.getConfig().set("owned-cosmetics", cList);
                            knocker.saveConfig();
                        }
                    }
            );
            cosmeticMenu.setOpenEventConsumer(event -> {
                Player player1 = (Player) event.getPlayer();
                player1.playSound(player1.getLocation(), Sound.valueOf(Sounds.GUI_OPEN.toString()), 1, 1);
            });
            cosmeticMenu.setCloseEventConsumer(inventoryCloseEvent -> {
                Player player1 = (Player) inventoryCloseEvent.getPlayer();
                player1.playSound(player1.getLocation(), Sound.valueOf(Sounds.GUI_CLOSE.toString()), 1, 1);
            });
            cosmeticMenu.open(player);
        }
        if (Items.SHOP_ITEM.getItem().equals(item)) {
            e.setCancelled(true);

            GUI shopMenu = new GUI("Shop Menu", (short) 5);
            String cIcon = KnockbackFFA.getINSTANCE().getItems().getConfig.getString("ShopMenu.cosmetic.material");
            String cName = ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("ShopMenu.cosmetic.name"));
            GUI cosmeticShop = new GUI("Cosmetic Shop", (short) 5);
            Button cosmeticItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(cIcon), 1, cName, Arrays.asList("")).create(), event -> {
                List<String> cosmetics = KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                List<String> cList = knocker.getConfig().getStringList("owned-cosmetics");
                for (String cosmetic : cosmetics) {
                    Button cosmeticsItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".icon")), 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getString(cosmetic + ".name")), Arrays.asList("")).create(), event1 -> {
                        float playerBal = knocker.getBalance();
                        if (playerBal >= KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getInt(cosmetics.get(event1.getSlot()) + ".price")) {
                            List<String> ownedCosmetics = knocker.getConfig().getStringList("owned-cosmetics");
                            if (!ownedCosmetics.contains(cosmetics.get(event1.getSlot()))) {
                                knocker.removeBalance(KnockbackFFA.getINSTANCE().getItems().getConfig.getInt(cosmetics.get(event1.getSlot()) + ".price"));
                                ownedCosmetics.add(cosmetics.get(event1.getSlot()));
                                knocker.getConfig().set("owned-cosmetics", ownedCosmetics);
                                knocker.saveConfig();
                                player.sendMessage(Message.COSMETIC_PURCHASE_SUCCESS.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                            } else
                                player.sendMessage(Message.COSMETIC_ALREADY_OWNED.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                        } else player.sendMessage(Message.COSMETIC_NO_ENOUGH_MONEY.toString().replace("&", "§"));
                        player.closeInventory();
                    });
                    ItemMeta cosmeticMeta = cosmeticsItem.getItem().getItemMeta();
                    List<String> lore = KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                    lore.add("§7Cost: §a" + KnockbackFFA.getINSTANCE().getCosmeticConfiguration().getConfig.getInt(cosmetic + ".price"));
                    cosmeticMeta.setLore(lore);
                    cosmeticsItem.setSelected(cList.contains(cosmetic), "§8(§aOwned§8)");
                    cosmeticsItem.getItem().setItemMeta(cosmeticMeta);
                    cosmeticShop.add(cosmeticsItem, cosmetics.indexOf(cosmetic));
                }
                knocker.openGUI(cosmeticShop);
            });
            ItemMeta cosmeticMeta = cosmeticItem.getItem().getItemMeta();
            cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("ShopMenu.cosmetic.name")));
            cosmeticMeta.setLore(KnockbackFFA.getINSTANCE().getItems().getConfig.getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
            cosmeticItem.getItem().setItemMeta(cosmeticMeta);
            shopMenu.add(cosmeticItem, KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("ShopMenu.cosmetic.slot"));
            String kIcon = KnockbackFFA.getINSTANCE().getItems().getConfig.getString("ShopMenu.kit.material");
            String kName = ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("ShopMenu.kit.name"));
            Button kitItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(kIcon), 1, kName, Arrays.asList("")).create(), event -> {
                GUI kitShop = new GUI("Kit Shop", (short) 5);
                List<String> cosmetics = Arrays.asList(KnockbackFFA.getINSTANCE().getKitManager().getFolder().list()).stream().map(s -> s.replace(".yml", "")).collect(Collectors.toList());
                List<String> cList = knocker.getConfig().getStringList("owned-kits");
                for (String cosmetic : cosmetics) {
                    if (cosmetic == null) return;
                    Kit kit = KnockbackFFA.getINSTANCE().getKitManager().load(cosmetic);
                    Button kitsItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(kit.getIcon(), 1, kit.getKitName(), Arrays.asList("")).create(), event1 -> {
                        float playerBal = knocker.getBalance();
                        if (playerBal >= kit.getPrice()) {
                            List<String> ownedKits = knocker.getConfig().getStringList("owned-kits");
                            if (!ownedKits.contains(cosmetics.get(event1.getSlot()))) {
                                knocker.removeBalance(kit.getPrice());
                                ownedKits.add(cosmetics.get(event1.getSlot()));
                                knocker.getConfig().set("owned-kits", ownedKits);
                                knocker.saveConfig();
                                player.closeInventory();
                                player.sendMessage(Message.COSMETIC_PURCHASE_SUCCESS.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                            } else
                                player.sendMessage(Message.COSMETIC_ALREADY_OWNED.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                        } else player.sendMessage(Message.COSMETIC_NO_ENOUGH_MONEY.toString().replace("&", "§"));
                        player.closeInventory();
                    });
                    if (kitsItem.getItem().getType() == Material.AIR) kitsItem.getItem().setType(Material.BARRIER);
                    ItemMeta kitsMeta = kitsItem.getItem().getItemMeta();
                    List<String> lore = kit.getConfig().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                    lore.add("§7Cost: §a" + kit.getPrice());
                    kitsMeta.setLore(lore);
                    if (cList.contains(cosmetic)) {
                        kitsMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                        kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§") + " §8(§aOwned§8)");
                    } else {
                        kitsMeta.removeEnchant(Enchantment.DURABILITY);
                        kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§").replace(" §8(§aOwned§8)", ""));
                    }
                    kitsItem.getItem().setItemMeta(kitsMeta);
                    kitShop.add(kitsItem, cosmetics.indexOf(cosmetic));
                }
                knocker.openGUI(kitShop);
            });
            ItemMeta kitMeta = kitItem.getItem().getItemMeta();
            kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getINSTANCE().getItems().getConfig.getString("ShopMenu.kit.name")));
            kitMeta.setLore(KnockbackFFA.getINSTANCE().getItems().getConfig.getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
            kitItem.getItem().setItemMeta(kitMeta);
            shopMenu.add(kitItem, KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("ShopMenu.kit.slot"));
            knocker.openGUI(shopMenu);
        }
        if (Items.KIT_ITEM.getItem().equals(item)) {
            e.setCancelled(true);
            GUI kitsMenu = new GUI("Kits Menu", (short) 5);
            kitsMenu.setOpenEventConsumer(event -> {
                Player player1 = (Player) event.getPlayer();
                player1.playSound(player1.getLocation(), Sound.valueOf(Sounds.GUI_OPEN.toString()), 1, 1);
            });
            kitsMenu.setCloseEventConsumer(inventoryCloseEvent -> {
                Player player1 = (Player) inventoryCloseEvent.getPlayer();
                player1.playSound(player1.getLocation(), Sound.valueOf(Sounds.GUI_CLOSE.toString()), 1, 1);
            });
            if (knocker.getConfig().getList("owned-kits") != null && knocker.getConfig().getList("owned-kits").size() > 0) {
                if (knocker.getConfig().getList("owned-kits") == null)
                    knocker.getConfig().set("owned-kits", new ArrayList<>());
                List<String> kitsList = knocker.getConfig().getList("owned-kits").stream().map(s -> s.toString()).collect(Collectors.toList());
                for (String kit : kitsList) {
                    if (kit == null) return;
                    Kit kitItems = KnockbackFFA.getINSTANCE().getKitManager().load(kit);
                    if (kitItems.getIcon() == null) return;
                    Button kitItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(kitItems.getIcon(), 1, kitItems.getKitName(), Arrays.asList("")).create(), event -> {
                        String selC = kitsList.get(event.getSlot());
                        knocker.getConfig().set("selected-kit", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                        knocker.saveConfig();
                        player.closeInventory();
                    });
                    ItemMeta kitMeta = kitItem.getItem().getItemMeta();
                    if (kitItem.getItem().getType() == Material.AIR) kitItem.getItem().setType(Material.BARRIER);

                    kitMeta.setLore(kitItems.getConfig().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    if (knocker.getConfig().getString("selected-kit") == null)
                        knocker.getConfig().set("selected-kit", kit);
                    kitItem.setSelected(knocker.getConfig().getString("selected-kit").equalsIgnoreCase(kit));
                    kitItem.getItem().setItemMeta(kitMeta);
                    kitsMenu.add(kitItem, kitsList.indexOf(kit));
                }
                knocker.saveConfig();
                kitsMenu.setOpenEventConsumer(event -> {
                    Player player1 = (Player) event.getPlayer();
                    player1.playSound(player1.getLocation(), Sound.valueOf(Sounds.GUI_OPEN.toString()), 1, 1);
                });
                kitsMenu.setCloseEventConsumer(inventoryCloseEvent -> {
                    Player player1 = (Player) inventoryCloseEvent.getPlayer();
                    player1.playSound(player1.getLocation(), Sound.valueOf(Sounds.GUI_CLOSE.toString()), 1, 1);
                });
            }
            knocker.openGUI(kitsMenu);
        }
    }


    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) e.setCancelled(true);
    }

}
