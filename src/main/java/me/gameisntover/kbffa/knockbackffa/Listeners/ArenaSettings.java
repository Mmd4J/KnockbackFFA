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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaSettings implements Listener
{
    public static Map<Player, String> playerArena = new HashMap<>();
    Integer arenaID1 = 1;

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (playerArena.get(player) != null) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                for (int i = 1; i <= ArenaData.getfolder().list().length; i++) {
                    String arenaName = ArenaConfiguration.get().getString("arena" + i + ".name");
                    PlayerData.load(player);
                    if (playerArena.get(player).equalsIgnoreCase("arena")) {
                        ArenaData.load(arenaName);
                        if (ArenaData.get().getBoolean("block-break")) {
                            e.setCancelled(true);
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
            for (int i = 1; i <= ArenaData.getfolder().list().length; i++) {
                String arenaName = ArenaConfiguration.get().getString("arena" + i + ".name");
                PlayerData.load(player);
                if (playerArena.get(player).equalsIgnoreCase("arena")) {
                    ArenaData.load(arenaName);
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
                    ArenaData.load(ArenaConfiguration.get().getString("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".name"));
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
                    ArenaData.load(ArenaConfiguration.get().getString("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".name"));
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
                    ArenaData.load(ArenaConfiguration.get().getString("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".name"));
                    Player player = (Player) e.getWhoClicked();
                    double x = player.getLocation().getX();
                    double y = player.getLocation().getY();
                    double z = player.getLocation().getZ();
                    String world = player.getWorld().getName();
                    ArenaConfiguration.get().set("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".x", x);
                    ArenaConfiguration.get().set("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".y", y);
                    ArenaConfiguration.get().set("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".z", z);
                    ArenaConfiguration.get().set("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".world", world);
                    ArenaConfiguration.save();
                    player.sendMessage(ChatColor.GREEN + "Arena Spawn Location Set!");
                }
                if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                    ArenaData.load(ArenaConfiguration.get().getString("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".name"));
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
                    ArenaData.load(ArenaConfiguration.get().getString("arena" + ArenaCommands.arenaidMap.get(e.getWhoClicked().getUniqueId()) + ".name"));
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
        Integer arenaID = 1;
        while (KnockbackFFAAPI.isInGame(player) || KnockbackFFAAPI.BungeeMode()) {
            String arenaName = ArenaConfiguration.get().getString("arena" + arenaID + ".name");
            ArenaData.load(arenaName);
            if (ArenaData.getfile().exists()) {
                BoundingBox s1box = new BoundingBox(ArenaData.get().getDouble("arena.pos1.x"), ArenaData.get().getDouble("arena.pos1.y"), ArenaData.get().getDouble("arena.pos1.z"), ArenaData.get().getDouble("arena.pos2.x"), ArenaData.get().getDouble("arena.pos2.y"), ArenaData.get().getDouble("arena.pos2.z"));
                World world = Bukkit.getWorld(ArenaData.get().getString("arena.world"));
                Location location = player.getLocation();
                if (s1box.contains(location.toVector()) && player.getWorld() == world) {
                    playerArena.put(player, "arena");
                    if (player.getInventory().isEmpty()) {
                        PlayerData.load(player);
                        if (PlayerData.get().getString("selected-kit")== null){
                            PlayerData.get().set("selected-kit","none");
                        }
                        Kits kit = new Kits(PlayerData.get().getString("selected-kit"));
                        if (PlayerData.get().getString("selected-kit").equalsIgnoreCase("none")){

                        }
                        kit.giveKit(player);

                    }
                    break;
                } else {
                    arenaID++;
                }
            } else if (!ArenaData.getfile().exists() && playerArena.get(player) != null) {
                if (playerArena.get(player).equals("arena")) {
                    playerArena.remove(player);
                    break;
                }
            } else {
                break;
            }
        }
    }
}