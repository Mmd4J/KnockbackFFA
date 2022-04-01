package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.customconfig.CosmeticConfiguration;
import me.gameisntover.kbffa.customconfig.ItemConfiguration;
import me.gameisntover.kbffa.customconfig.Kits;
import me.gameisntover.kbffa.customconfig.Knocker;
import me.gameisntover.kbffa.gui.Button;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.util.ItemBuilder;
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
import java.util.List;
import java.util.stream.Collectors;

public class GuiStuff implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        KnockbackFFAKit kits = new KnockbackFFAKit();
        ItemStack item = e.getItem();
        if (item == null || item.getItemMeta()==null) return;
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        assert itemMeta != null;
        if (!itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) return;
        if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName())) {
            e.setCancelled(true);
            GUI cosmeticMenu = new GUI("Cosmetic Menu", (short) 5);
            List<String> cList = knocker.getConfig().getStringList("owned-cosmetics");
            cList.forEach(cosmetic -> {
                        if (CosmeticConfiguration.get().getString(cosmetic + ".name") == null) {
                            String displayName = ChatColor.translateAlternateColorCodes('&', CosmeticConfiguration.get().getString(cosmetic + ".name"));
                            String icon = CosmeticConfiguration.get().getString(cosmetic + ".icon");
                            List<String> lore = CosmeticConfiguration.get().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            assert icon != null;
                            Button cosmeticItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(icon), 1, displayName, Arrays.asList("")).create(), event -> {
                                List<String> ownedCosmetics = knocker.getConfig().getStringList("owned-cosmetics");
                                String selC = ownedCosmetics.get(event.getSlot());
                                if (CosmeticConfiguration.get().getString(selC + ".type") == "KILL_PARTICLE")
                                    knocker.getConfig().set("selected-trails", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");

                                else if (CosmeticConfiguration.get().getString(selC + ".type") == "TRAIL")
                                    knocker.getConfig().set("selected-trails", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                                knocker.saveConfig();
                                player.closeInventory();
                            });
                            ItemMeta meta = kits.guiItemMeta(cosmeticItem.getItem());
                            if (CosmeticConfiguration.get().getString(cosmetic + ".type").equals("KILL_PARTICLE")) {
                                if (knocker.getConfig().getString("selected-cosmetic") == null)
                                    knocker.getConfig().set("selected-cosmetic", cosmetic);
                                cosmeticItem.setSelected(knocker.getConfig().getString("selected-cosmetic").equals(cosmetic));
                            } else if (CosmeticConfiguration.get().getString(cosmetic + ".type").equals("TRAIL"))
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
        if (kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName())) {
            e.setCancelled(true);

            GUI shopMenu = new GUI("Shop Menu", (short) 5);
            String cIcon = ItemConfiguration.get().getString("ShopMenu.cosmetic.material");
            String cName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name"));
            GUI cosmeticShop = new GUI("Cosmetic Shop", (short) 5);
            Button cosmeticItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(cIcon), 1, cName, Arrays.asList("")).create(), event -> {
                List<String> cosmetics = CosmeticConfiguration.get().getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                List<String> cList = knocker.getConfig().getStringList("owned-cosmetics");
                for (String cosmetic : cosmetics) {
                    Button cosmeticsItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(CosmeticConfiguration.get().getString(cosmetic + ".icon")), 1, ChatColor.translateAlternateColorCodes('&', CosmeticConfiguration.get().getString(cosmetic + ".name")), Arrays.asList("")).create(), event1 -> {
                        float playerBal = KnockbackFFA.getINSTANCE().getBalanceAPI().getBalance(knocker);
                        if (playerBal >= CosmeticConfiguration.get().getInt(cosmetics.get(event1.getSlot()) + ".price")) {
                            List<String> ownedCosmetics = knocker.getConfig().getStringList("owned-cosmetics");
                            if (!ownedCosmetics.contains(cosmetics.get(event1.getSlot()))) {
                                KnockbackFFA.getINSTANCE().getBalanceAPI().removeBalance(knocker, ItemConfiguration.get().getInt(cosmetics.get(event1.getSlot()) + ".price"));
                                ownedCosmetics.add(cosmetics.get(event1.getSlot()));
                                knocker.getConfig().set("owned-cosmetics", ownedCosmetics);
                                knocker.saveConfig();
                                player.sendMessage(Message.COSMETIC_PURCHASE_SUCCESS.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                            } else
                                player.sendMessage(Message.COSMETIC_ALREADY_OWNED.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                        } else player.sendMessage(Message.COSMETIC_NO_ENOUGH_MONEY.toString().replace("&", "§"));
                        player.closeInventory();
                    });
                    ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticsItem.getItem());
                    List<String> lore = CosmeticConfiguration.get().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                    lore.add("§7Cost: §a" + CosmeticConfiguration.get().getInt(cosmetic + ".price"));
                    cosmeticMeta.setLore(lore);
                    cosmeticsItem.setSelected(cList.contains(cosmetic), "§8(§aOwned§8)");
                    cosmeticsItem.getItem().setItemMeta(cosmeticMeta);
                    cosmeticShop.add(cosmeticsItem, cosmetics.indexOf(cosmetic));
                }
            });

            cosmeticShop.open(player);
            ItemMeta cosmeticMeta = kits.guiItemMeta(cosmeticItem.getItem());
            cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name")));
            cosmeticMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
            cosmeticItem.getItem().setItemMeta(cosmeticMeta);
            shopMenu.add(cosmeticItem, ItemConfiguration.get().getInt("ShopMenu.cosmetic.slot"));
            String kIcon = ItemConfiguration.get().getString("ShopMenu.kit.material");
            String kName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name"));
            Button kitItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(kIcon), 1, kName, Arrays.asList("")).create(), event -> {
                GUI kitShop = new GUI("Kit Shop", (short) 5);
                List<String> cosmetics = Arrays.asList(Kits.getFolder().list()).stream().map(s -> s.replace(".yml", "")).collect(Collectors.toList());
                List<String> cList = knocker.getConfig().getStringList("owned-kits");
                for (String cosmetic : cosmetics) {
                    if (cosmetic == null) return;
                    Kits kit = Kits.load(cosmetic);
                    String kitIcon = kit.get().getString("KitIcon");
                    String kitName = kit.get().getString("KitName").replace("&", "§");
                    Button kitsItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(kitIcon), 1, kitName, Arrays.asList("")).create(), event1 -> {
                        float playerBal = KnockbackFFA.getINSTANCE().getBalanceAPI().getBalance(knocker);
                        if (playerBal >= kit.get().getInt("Price")) {
                            List<String> ownedKits = knocker.getConfig().getStringList("owned-kits");
                            if (!ownedKits.contains(cosmetics.get(event1.getSlot()))) {
                                KnockbackFFA.getINSTANCE().getBalanceAPI().removeBalance(knocker, kit.get().getInt("Price"));
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
                    kitShop.add(kitsItem, cosmetics.indexOf(cosmetic));
                }
                kitShop.open(player);
            });
            ItemMeta kitMeta = kits.guiItemMeta(kitItem.getItem());
            kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name")));
            kitMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
            kitItem.getItem().setItemMeta(kitMeta);
            shopMenu.add(kitItem, ItemConfiguration.get().getInt("ShopMenu.kit.slot"));
            shopMenu.open(player);
        }
        if (kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
            e.setCancelled(true);
            GUI kitsMenu = new GUI("Kits Menu", (short) 5);
            if (knocker.getConfig().getList("owned-kits") != null && knocker.getConfig().getList("owned-kits").size() > 0) {
                if (knocker.getConfig().getList("owned-kits") == null)
                    knocker.getConfig().set("owned-kits", new ArrayList<>());
                List<String> kitsList = knocker.getConfig().getList("owned-kits").stream().map(s -> s.toString()).collect(Collectors.toList());
                for (String kit : kitsList) {
                    if (kit == null) return;
                    Kits kitItems = Kits.load(kit);
                    if (kitItems.get().getString("KitIcon") == null) return;
                    String icon = kitItems.get().getString("KitIcon");
                    String name = kitItems.get().getString("KitName").replace("&", "§");
                    Button kitItem = KnockbackFFA.getINSTANCE().getButtonManager().create(new ItemBuilder(Material.getMaterial(icon), 1, name, Arrays.asList("")).create(), event -> {
                        String selC = kitsList.get(event.getSlot());
                        knocker.getConfig().set("selected-kit", event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                        knocker.saveConfig();
                        player.closeInventory();
                    });
                    ItemMeta kitMeta = kits.guiItemMeta(kitItem.getItem());
                    if (kitItem.getItem().getType() == Material.AIR) kitItem.getItem().setType(Material.BARRIER);

                    kitMeta.setLore(kitItems.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
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
            kitsMenu.open(player);
        }
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) e.setCancelled(true);
    }

}
