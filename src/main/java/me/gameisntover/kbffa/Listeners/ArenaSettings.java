package me.gameisntover.kbffa.Listeners;

import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.Arena.Arena;
import me.gameisntover.kbffa.customconfigs.CosmeticConfiguration;
import me.gameisntover.kbffa.customconfigs.Kits;
import me.gameisntover.kbffa.customconfigs.PlayerData;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import redempt.redlib.blockdata.DataBlock;

import java.util.ArrayList;
import java.util.List;

public class ArenaSettings implements Listener {
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.isInArena(player)) {
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
            PlayerData.load(player);
            if (PlayerData.get().getString("selected-trails") != null) {
                String selectedTrails = PlayerData.get().getString("selected-trails");
                Block block = player.getWorld().getBlockAt(e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
                DataBlock db = KnockbackFFA.getInstance().getManager().getDataBlock(block);
                if (db.getString("block-type")==""||db.getString("block-type")==null) {
                    if (!KnockbackFFA.getInstance().getConfig().getStringList("no-trail-blocks").contains(block.getType().toString())) {
    
                        db.set("material", block.getType().name());
                        List<String> materialString = CosmeticConfiguration.get().getStringList(selectedTrails + ".blocks");
                        List<Material> materialList = new ArrayList<>();
                        for (String material : materialString) {
                            materialList.add(Material.getMaterial(material));
                        }
                        if (materialList.size() == 1) {
                            block.setType(materialList.get(0));
                            db.set("block-type", "trail");
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    block.setType(Material.getMaterial(db.getString("material")));
                                    db.set("block-type", "");
                                    cancel();
                                }
                            }.runTaskTimer(KnockbackFFA.getInstance(), CosmeticConfiguration.get().getInt(selectedTrails + ".speed") * 20, 1);
                        } else {
                            block.setType(materialList.get(0));
                            db.set("block-type", "trail");
                            new BukkitRunnable() {
                                int i = 0;

                                @Override
                                public void run() {
                                    if (i < materialList.size() - 1) {
                                        i++;
                                        String material = materialList.get(i).name();
                                        block.setType(Material.getMaterial(material));
                                    } else {
                                        block.setType(Material.getMaterial(db.getString("material")));
                                        db.set("block-type", "");
                                        cancel();
                                    }
                                }
                            }.
                                    runTaskTimer(KnockbackFFA.getInstance(), CosmeticConfiguration.get().getInt(selectedTrails + ".speed") * 20, CosmeticConfiguration.get().getInt(selectedTrails + ".speed") * 20);
                        }
                    }
                }
        }
            if (Arena.getEnabledArena()!=null) {
                Arena arena = Arena.load(Arena.getEnabledArena().getName());
                    KnockbackFFAAPI.setInGamePlayer(player, true);
                    KnockbackFFAAPI.setInArenaPlayer(player, true);
                        if (arena.contains(player.getLocation())) {
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
                        }
                    }    else {
                        KnockbackFFAAPI.setInArenaPlayer(player,false);
                    }
                }
        }
        }
