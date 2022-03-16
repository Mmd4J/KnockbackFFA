package me.gameisntover.kbffa.knockbackffa.Listeners;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.Arena.Arena;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.Kits;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ArenaSettings implements Listener {

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.isInArena(player.getPlayer())) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) {
                String[] arenaList = Arena.getfolder().list();
                for (String arenaName : arenaList) {
                    Arena arena = Arena.load(arenaName.replace(".yml", ""));
                    PlayerData.load(player);
                    e.setCancelled(!arena.get().getBoolean("block-break"));
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            String[] arenaList = Arena.getfolder().list();
            assert arenaList != null;
            for (String arenaName : arenaList) {
                Arena arena = Arena.load(arenaName.replace(".yml", ""));
                PlayerData.load(player);
                e.setCancelled(KnockbackFFAAPI.isInArena(player) && !arena.get().getBoolean("item-drop"));
            }
        }
        }
    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.isInGame(player) || KnockbackFFAAPI.BungeeMode()) {
                if (Arena.getEnabledArena()!=null) {
                    Arena arena = Arena.load(Arena.getEnabledArena().getName());
                    KnockbackFFAAPI.setInGamePlayer(player, true);
                        KnockbackFFAAPI.setInArenaPlayer(player, true);
                        PlayerData.load(player);
                        if (arena.contains(player.getLocation()))
                        if (PlayerData.get().getString("selected-kit") == null) {
                            List<String> ownedKits = PlayerData.get().getStringList("owned-kits");
                            if (ownedKits.contains("Default")) {
                                ownedKits.add("Default");
                                PlayerData.get().set("owned-kits", ownedKits);
                            }
                            PlayerData.get().set("selected-kit", "Default");
                        }
                        Kits kit = Kits.load(PlayerData.get().getString("selected-kit"));
                        KnockbackFFAKit kits = new KnockbackFFAKit();
                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null) {
                                ItemMeta itemMeta = item.getItemMeta();
                                if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) || kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) || kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                                    player.getInventory().clear();
                                    kit.giveKit(player);
                                    break;
                                }
                            }
                        }
                    }    else {
                        KnockbackFFAAPI.setInArenaPlayer(player,false);
                    }
                }
        }
        }
