package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.gui.Button;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.kit.Kit;
import me.gameisntover.kbffa.util.ItemBuilder;
import me.gameisntover.kbffa.util.Items;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuiStuff implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || item.getItemMeta() == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ReworkedKnocker knocker = ReworkedKnocker.get(player.getUniqueId());
        assert itemMeta != null;
        if (Items.COSMETIC_ITEM.getItem().equals(item)) {
            e.setCancelled(true);
            GUI cosmeticMenu = new GUI("Cosmetic Menu", (short) 5);
            List<String> cList = knocker.getOwnedCosmetics();
            cList.forEach(cosmetic -> {
                        if (KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".name") != null) {
                            String displayName = ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".name"));
                            String icon = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".icon");
                            List<String> lore = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            assert icon != null;
                            Button cosmeticItem = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.getMaterial(icon), 1, displayName, Collections.singletonList("")).create(), event -> {
                                List<String> ownedCosmetics = knocker.getOwnedCosmetics();
                                String selC = ownedCosmetics.get(event.getSlot());
                                assert event.getCurrentItem() != null;
                                assert event.getCurrentItem().getItemMeta() != null;
                                if (KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(selC + ".type").equals("KILL_PARTICLE"))
                                    knocker.setSelectedTrail(event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                                else if (KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(selC + ".type").equals("TRAIL"))
                                    knocker.setSelectedCosmetic(event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) ? selC : "");
                                player.closeInventory();
                            });
                            ItemMeta meta = cosmeticItem.getItem().getItemMeta();
                            if (KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".type").equals("KILL_PARTICLE")) {
                                if (knocker.getSelectedCosmetic() == null)
                                        knocker.setSelectedCosmetic(cosmetic);
                                cosmeticItem.setSelected(knocker.getSelectedCosmetic().equals(cosmetic));
                            } else if (KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".type").equals("TRAIL"))
                                if (knocker.getSelectedTrail() == null)
                                    knocker.setSelectedTrail(cosmetic);
                            cosmeticItem.setSelected(knocker.getSelectedTrail().equals(cosmetic));
                            meta.setLore(lore);
                            cosmeticItem.getItem().setItemMeta(meta);
                            cosmeticMenu.add(cosmeticItem, cList.indexOf(cosmetic));
                        } else {
                            cList.remove(cosmetic);
                            knocker.getOwnedCosmetics().removeIf(s -> Objects.equals(s, s));
                            knocker.getOwnedCosmetics().addAll(cList);
                        }
                    }
            );
            cosmeticMenu.setOpenEventConsumer(event -> {
                Player player1 = (Player) event.getPlayer();
                player1.playSound(player1.getLocation(), Sounds.GUI_OPEN.toSound(), 1, 1);
            });
            cosmeticMenu.setCloseEventConsumer(inventoryCloseEvent -> {
                Player player1 = (Player) inventoryCloseEvent.getPlayer();
                player1.playSound(player1.getLocation(), Sounds.GUI_CLOSE.toSound(), 1, 1);
            });
            cosmeticMenu.open(player);
        }
        if (Items.SHOP_ITEM.getItem().equals(item)) {
            e.setCancelled(true);

            GUI shopMenu = new GUI("Shop Menu", (short) 5);
            String cIcon = KnockbackFFA.getInstance().getItems().getConfig.getString("ShopMenu.cosmetic.material");
            String cName = ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getInstance().getItems().getConfig.getString("ShopMenu.cosmetic.name"));
            GUI cosmeticShop = new GUI("Cosmetic Shop", (short) 5);
            Button cosmeticItem = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.getMaterial(cIcon), 1, cName, Arrays.asList("")).create(), event -> {
                List<String> cosmetics = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getList("registered-cosmetics").stream().map(s -> s.toString()).collect(Collectors.toList());
                List<String> cList = knocker.getOwnedCosmetics();
                for (String cosmetic : cosmetics) {
                    Button cosmeticsItem = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.getMaterial(KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".icon")), 1, ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getString(cosmetic + ".name")), Collections.singletonList("")).create(), event1 -> {
                        float playerBal = knocker.getBalance();
                        if (playerBal >= KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(cosmetics.get(event1.getSlot()) + ".price")) {
                            List<String> ownedCosmetics = knocker.getOwnedCosmetics();
                            if (!ownedCosmetics.contains(cosmetics.get(event1.getSlot()))) {
                                knocker.removeBalance(KnockbackFFA.getInstance().getItems().getConfig.getInt(cosmetics.get(event1.getSlot()) + ".price"));
                                ownedCosmetics.add(cosmetics.get(event1.getSlot()));
                                knocker.getOwnedCosmetics().addAll(ownedCosmetics);
                                player.sendMessage(Message.COSMETIC_PURCHASE_SUCCESS.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                            } else
                                player.sendMessage(Message.COSMETIC_ALREADY_OWNED.toString().replace("&", "§").replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                        } else player.sendMessage(Message.COSMETIC_NO_ENOUGH_MONEY.toString().replace("&", "§"));
                        player.closeInventory();
                    });
                    ItemMeta cosmeticMeta = cosmeticsItem.getItem().getItemMeta();
                    List<String> lore = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                    lore.add("§7Cost: §a" + KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(cosmetic + ".price"));
                    cosmeticMeta.setLore(lore);
                    cosmeticsItem.setSelected(cList.contains(cosmetic), "§8(§aOwned§8)");
                    cosmeticsItem.getItem().setItemMeta(cosmeticMeta);
                    cosmeticShop.add(cosmeticsItem, cosmetics.indexOf(cosmetic));
                }
                knocker.getPlayer().openInventory(cosmeticShop.getInventory());
            });
            ItemMeta cosmeticMeta = cosmeticItem.getItem().getItemMeta();
            cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getInstance().getItems().getConfig.getString("ShopMenu.cosmetic.name")));
            cosmeticMeta.setLore(KnockbackFFA.getInstance().getItems().getConfig.getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
            cosmeticItem.getItem().setItemMeta(cosmeticMeta);
            shopMenu.add(cosmeticItem, KnockbackFFA.getInstance().getItems().getConfig.getInt("ShopMenu.cosmetic.slot"));
            String kIcon = KnockbackFFA.getInstance().getItems().getConfig.getString("ShopMenu.kit.material");
            String kName = ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getInstance().getItems().getConfig.getString("ShopMenu.kit.name"));
            Button kitItem = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.getMaterial(kIcon), 1, kName, Arrays.asList("")).create(), event -> {
                GUI kitShop = new GUI("Kit Shop", (short) 5);
                List<String> cosmetics = Arrays.asList(KnockbackFFA.getInstance().getKitManager().getFolder().list()).stream().map(s -> s.replace(".yml", "")).collect(Collectors.toList());
                List<String> cList = knocker.getOwnedKits();
                for (String cosmetic : cosmetics) {
                    if (cosmetic == null) return;
                    Kit kit = KnockbackFFA.getInstance().getKitManager().load(cosmetic);
                    Button kitsItem = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(kit.getIcon(), 1, kit.getKitName(), Collections.singletonList("")).create(), event1 -> {
                        float playerBal = knocker.getBalance();
                        if (playerBal >= kit.getPrice()) {
                            List<String> ownedKits = knocker.getOwnedKits();
                            if (!ownedKits.contains(cosmetics.get(event1.getSlot()))) {
                                knocker.removeBalance((int) kit.getPrice());
                                ownedKits.add(cosmetics.get(event1.getSlot()));
                                knocker.getOwnedKits().addAll(ownedKits);
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
                knocker.getPlayer().openInventory(kitShop.getInventory());
            });
            ItemMeta kitMeta = kitItem.getItem().getItemMeta();
            kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', KnockbackFFA.getInstance().getItems().getConfig.getString("ShopMenu.kit.name")));
            kitMeta.setLore(KnockbackFFA.getInstance().getItems().getConfig.getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
            kitItem.getItem().setItemMeta(kitMeta);
            shopMenu.add(kitItem, KnockbackFFA.getInstance().getItems().getConfig.getInt("ShopMenu.kit.slot"));
            knocker.getPlayer().openInventory(shopMenu.getInventory());
        }
        if (Items.KIT_ITEM.getItem().equals(item)) {
            e.setCancelled(true);
            GUI kitsMenu = new GUI("Kits Menu", (short) 5);
            kitsMenu.setOpenEventConsumer(event -> {
                Player player1 = (Player) event.getPlayer();
                player1.playSound(player1.getLocation(), Sounds.GUI_OPEN.toSound(), 1, 1);
            });
            kitsMenu.setCloseEventConsumer(inventoryCloseEvent -> {
                Player player1 = (Player) inventoryCloseEvent.getPlayer();
                player1.playSound(player1.getLocation(), Sounds.GUI_CLOSE.toSound(), 1, 1);
            });
            if (!knocker.getOwnedKits().isEmpty()) {
                knocker.getOwnedKits().add("Default");
                List<String> kitsList = knocker.getOwnedKits().stream().map(Object::toString).collect(Collectors.toList());
                for (String kit : kitsList) {
                    if (kit == null) return;
                    Kit kitItems = KnockbackFFA.getInstance().getKitManager().load(kit);
                    if (kitItems.getIcon() == null) return;
                    Button kitItem = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(kitItems.getIcon(), 1, kitItems.getKitName(), Collections.singletonList("")).create(), event -> {
                        String selC = kitsList.get(event.getSlot());
                        assert event.getCurrentItem() != null;
                        assert event.getCurrentItem().getItemMeta() != null;
                        if (!event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                            knocker.setSelectedKit(selC);
                            player.sendMessage(Message.SUCCESSFULLY_SELECTED_COSMETIC.toString().replace("%cosmetic%", selC));
                        } else {
                            knocker.setSelectedKit("");
                            player.sendMessage(Message.SUCCESSFULLY_DESELECTED_COSMETIC.toString().replace("%cosmetic%", selC));
                        }
                        player.closeInventory();
                    });
                    kitItem.setSelected(knocker.getSelectedKit().equalsIgnoreCase(kit));
                    ItemMeta kitMeta = kitItem.getItem().getItemMeta();
                    if (kitItem.getItem().getType() == Material.AIR) kitItem.getItem().setType(Material.BARRIER);

                    assert kitMeta != null;
                    kitMeta.setLore(kitItems.getConfig().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));


                    kitItem.getItem().setItemMeta(kitMeta);
                    kitsMenu.add(kitItem, kitsList.indexOf(kit));
                }
                kitsMenu.setOpenEventConsumer(event -> {
                    Player player1 = (Player) event.getPlayer();
                    player1.playSound(player1.getLocation(), Sounds.GUI_OPEN.toSound(), 1, 1);
                });
                kitsMenu.setCloseEventConsumer(inventoryCloseEvent -> {
                    Player player1 = (Player) inventoryCloseEvent.getPlayer();
                    player1.playSound(player1.getLocation(), Sounds.GUI_CLOSE.toSound(), 1, 1);
                });
            }
            knocker.getPlayer().openInventory(kitsMenu.getInventory());
        }
    }


    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) e.setCancelled(true);

    }


}
