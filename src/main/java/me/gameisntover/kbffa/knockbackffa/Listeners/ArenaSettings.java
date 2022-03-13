package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.Kits;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.commands.ArenaCommands;
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
                    List <String> ownedKits = PlayerData.get().getStringList("owned-kits");
                    if (ownedKits.contains("Default")) {
                        ownedKits.add("Default");
                        PlayerData.get().set("owned-kits",ownedKits);
                    }
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
                    playerArena.put(player,"not-arena");
                }
            }

        }
        }
    }
