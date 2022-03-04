package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.Kits;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.arenas.ArenaCommands;
import me.gameisntover.kbffa.knockbackffa.arenas.WandListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class ArenaSettings implements Listener {
    public static Map<Player, String> playerArena = new HashMap<>();

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (playerArena.get(player) == "arena") {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                List<String> arenaList = Arrays.asList(ArenaData.getfolder().list());
                for (String arenaName : arenaList) {
                    ArenaData.load(arenaName.replace(".yml", ""));
                    PlayerData.load(player);
                    if (playerArena.get(player).equalsIgnoreCase("arena")) {
                        if (!ArenaData.get().getBoolean("block-break")) {
                            e.setCancelled(true);
                        }else{
                            e.setCancelled(false);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            List<String> arenaList = Arrays.asList(ArenaData.getfolder().list());
            for (String arenaName : arenaList) {
                ArenaData.load(arenaName.replace(".yml", ""));
                PlayerData.load(player);
                if (playerArena.get(player).equalsIgnoreCase("arena")) {
                    if (ArenaData.get().getBoolean("item-drop")) {
                        e.setCancelled(false);
                    } else if (!ArenaData.get().getBoolean("item-drop")) {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onArenaGUI(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("Arena Editor")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            } else {
                if (e.getCurrentItem().getType().equals(Material.DIAMOND_PICKAXE)) {
                    ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                    if (!ArenaData.get().getBoolean("block-break")) {
                        ArenaData.get().set("block-break", true);
                        ArenaData.save();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                        lore.add(ChatColor.GREEN + "Currently Block Breaking is " + ArenaData.get().getBoolean("block-break"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    } else {
                        ArenaData.get().set("block-break", false);
                        ArenaData.save();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                        lore.add(ChatColor.GREEN + "Currently Block Breaking is " + ArenaData.get().getBoolean("item-drop"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    }
                }
                if (e.getCurrentItem().getType().equals(Material.DIAMOND)) {
                    ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                    if (!ArenaData.get().getBoolean("item-drop")) {
                        ArenaData.get().set("item-drop", true);
                        ArenaData.save();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                        lore.add(ChatColor.GREEN + "Currently Item Dropping is " + ArenaData.get().getBoolean("item-drop"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    } else {
                        ArenaData.get().set("item-drop", false);
                        ArenaData.save();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                        lore.add(ChatColor.GREEN + "Currently Item Dropping is " + ArenaData.get().getBoolean("item-drop"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    }
                }
                if (e.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
                    ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                    Player player = (Player) e.getWhoClicked();
                    double x = player.getLocation().getX();
                    double y = player.getLocation().getY();
                    double z = player.getLocation().getZ();
                    String world = player.getWorld().getName();
                    ArenaData.get().set("arena.x", x);
                    ArenaData.get().set("arena.y", y);
                    ArenaData.get().set("arena.z", z);
                    ArenaData.get().set("arena.world", world);
                    ArenaConfiguration.save();
                    player.sendMessage(ChatColor.GREEN + "Arena Spawn Location Set!");
                }
                if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                    ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                    if (WandListener.pos1m.get(e.getWhoClicked()) != null && WandListener.pos2m.get(e.getWhoClicked()) != null) {
                        Location loc1 = WandListener.pos1m.get(e.getWhoClicked());
                        Location loc2 = WandListener.pos2m.get(e.getWhoClicked());
                        BoundingBox box = new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
                        e.getWhoClicked().getWorld().getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                        e.getWhoClicked().getWorld().getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                        ArenaData.get().set("arena.pos1.x", loc1.getX());
                        ArenaData.get().set("arena.pos1.y", loc1.getY());
                        ArenaData.get().set("arena.pos1.z", loc1.getZ());
                        ArenaData.get().set("arena.pos2.x", loc2.getX());
                        ArenaData.get().set("arena.pos2.y", loc2.getY());
                        ArenaData.get().set("arena.pos2.z", loc2.getZ());
                        String world = e.getWhoClicked().getWorld().getName();
                        ArenaData.get().set("arena.world", world);
                        ArenaData.save();
                        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Arena Positions Set!");
                    }
                }
                if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
                    ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                    if (ArenaData.get().getBoolean("world-border") == false) {
                        ArenaData.get().set("world-border", true);
                        ArenaData.save();
                        Double x1 = ArenaData.get().getDouble("arena.pos1.x");
                        Double y1 = ArenaData.get().getDouble("arena.pos1.y");
                        Double z1 = ArenaData.get().getDouble("arena.pos1.z");
                        Double x2 = ArenaData.get().getDouble("arena.pos2.x");
                        Double y2 = ArenaData.get().getDouble("arena.pos2.y");
                        Double z2 = ArenaData.get().getDouble("arena.pos2.z");
                        BoundingBox box = new BoundingBox(x1, y1, z1, x2, y2, z2);
                        Bukkit.getWorld(ArenaData.get().getString("arena.world")).getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                        Bukkit.getWorld(ArenaData.get().getString("arena.world")).getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                        lore.add(ChatColor.GREEN + "Currently the world border is " + ArenaData.get().getBoolean("world-border"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    } else {
                        ArenaData.get().set("world-border", false);
                        ArenaData.save();
                        WorldBorder worldBorder = Bukkit.getWorld(ArenaData.get().getString("arena.world")).getWorldBorder();
                        worldBorder.reset();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                        lore.add(ChatColor.GREEN + "Currently the world border is " + ArenaData.get().getBoolean("world-border"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.isInGame(player) || KnockbackFFAAPI.BungeeMode()) {
            List<String> arenaList = Arrays.asList(Arrays.stream(ArenaData.getfolder().list()).map(s -> {
                return s.replace(".yml", "");
            }).toArray(String[]::new));
            Location loc1 = null;
            Location loc2 = null;
            for (String arena : arenaList) {
                ArenaData.load(arena);
                BoundingBox s1box = new BoundingBox(ArenaData.get().getDouble("arena.pos1.x"), ArenaData.get().getDouble("arena.pos1.y"), ArenaData.get().getDouble("arena.pos1.z"), ArenaData.get().getDouble("arena.pos2.x"), ArenaData.get().getDouble("arena.pos2.y"), ArenaData.get().getDouble("arena.pos2.z"));
                World world = Bukkit.getWorld(ArenaData.get().getString("arena.world"));
                Location location = player.getLocation();
                if (s1box.contains(location.toVector()) && player.getWorld() == world) {
                    loc1 = new Location(world, ArenaData.get().getDouble("arena.pos1.x"), ArenaData.get().getDouble("arena.pos1.y"), ArenaData.get().getDouble("arena.pos1.z"));
                    loc2 = new Location(world, ArenaData.get().getDouble("arena.pos2.x"), ArenaData.get().getDouble("arena.pos2.y"), ArenaData.get().getDouble("arena.pos2.z"));
                    }
                }
            if (loc1 != null && loc2 != null) {
                playerArena.put(player, "arena");
                PlayerData.load(player);
                if (PlayerData.get().getString("selected-kit") == null) {
                    PlayerData.get().set("selected-kit", "Default");
                }
                Kits kit = new Kits(PlayerData.get().getString("selected-kit"));
                KnockbackFFAKit kits = new KnockbackFFAKit();
                for (ItemStack item : player.getInventory().getContents()){
                    if (item != null){
                        ItemMeta itemMeta = item.getItemMeta();
                        if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)||kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)||kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)){
                            player.getInventory().clear();
                            kit.giveKit(player);
                            break;
                        }
                    }
                }
            }else {
                if (playerArena.get(player)==null || playerArena.get(player).equalsIgnoreCase("arena")) {
                    //problem! it always sets the player arena to not arena
                    playerArena.put(player,"not-arena");
                }
            }

        }
        }
    }
