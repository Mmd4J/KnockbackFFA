package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.TempArenaManager;
import me.gameisntover.kbffa.customconfig.CosmeticConfiguration;
import me.gameisntover.kbffa.customconfig.DataBlock;
import me.gameisntover.kbffa.customconfig.Kits;
import me.gameisntover.kbffa.customconfig.Knocker;
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

import java.util.ArrayList;
import java.util.List;

public class ArenaSettings implements Listener {
    private final TempArenaManager tempArenaManager = new TempArenaManager();

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (knocker.isInArena()) return;
        String[] arenaList = tempArenaManager.getfolder().list();
        if(arenaList == null) return;
        for (String arenaName : arenaList) {
            Arena arena = tempArenaManager.load(arenaName.replace(".yml", ""));
            e.setCancelled(!arena.getConfig().getBoolean("block-break"));
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (!knocker.isInGame()) return;
        String[] arenaList = tempArenaManager.getfolder().list();
        assert arenaList != null;
        for (String arenaName : arenaList) {
            Arena arena = tempArenaManager.load(arenaName.replace(".yml", ""));
            e.setCancelled(knocker.isInArena() && !arena.getConfig().getBoolean("item-drop"));
        }
    }

    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (!knocker.isInGame()) return;
        if (knocker.getConfig().getString("selected-trails") != null) {
            String selectedTrails = knocker.getConfig().getString("selected-trails");
            Block block = player.getWorld().getBlockAt(e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
            DataBlock db = KnockbackFFA.getINSTANCE().getBlockDataManager().getBlockData(block);
            if (!db.getBlockType().equals("") || db.getBlockType() != null) return;
            if (KnockbackFFA.getINSTANCE().getConfig().getStringList("no-trail-blocks").contains(block.getType().toString()))
                return;
            db.setPrevMaterial(block.getType());
            List<String> materialString = CosmeticConfiguration.get().getStringList(selectedTrails + ".blocks");
            List<Material> materialList = new ArrayList<>();
            for (String material : materialString) {
                materialList.add(Material.getMaterial(material));
            }
            if (materialList.size() == 1) {
                block.setType(materialList.get(0));
                db.setBlockType("trail");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(db.getPrevMaterial());
                        db.setBlockType("");
                        cancel();
                    }
                }.runTaskTimer(KnockbackFFA.getINSTANCE(), CosmeticConfiguration.get().getInt(selectedTrails + ".speed") * 20, 1);
            } else {
                block.setType(materialList.get(0));
                db.setBlockType("trail");
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        if (i < materialList.size() - 1) {
                            i++;
                            String material = materialList.get(i).name();
                            block.setType(Material.getMaterial(material));
                        } else {
                            block.setType(db.getPrevMaterial());
                            db.setBlockType("");
                            cancel();
                        }
                    }
                }.
                        runTaskTimer(KnockbackFFA.getINSTANCE(), CosmeticConfiguration.get().getInt(selectedTrails + ".speed") * 20, CosmeticConfiguration.get().getInt(selectedTrails + ".speed") * 20);
            }
        }
        if (tempArenaManager.getEnabledArena() != null) {
            Arena arena = tempArenaManager.load(tempArenaManager.getEnabledArena().getName());
            knocker.setInGame(true);
            knocker.setInArena(true);
            if (!arena.contains(player.getLocation())) return;
            if (knocker.getConfig().getString("selected-kit") == null) return;
            List<String> ownedKits = knocker.getConfig().getStringList("owned-kits");
            if (ownedKits.contains("Default")) {
                ownedKits.add("Default");
                knocker.getConfig().set("owned-kits", ownedKits);
            }
            knocker.getConfig().set("selected-kit", "Default");
            Kits kit = Kits.load(knocker.getConfig().getString("selected-kit"));
            KnockbackFFAKit kits = new KnockbackFFAKit();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) return;
                ItemMeta itemMeta = item.getItemMeta();
                if (kits.cosmeticMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) || kits.shopMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) || kits.kitsMeta().getDisplayName().contains(itemMeta.getDisplayName()) && itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    player.getInventory().clear();
                    kit.giveKit(player);
                    break;
                }
            }
        } else knocker.setInArena(false);
    }
}
