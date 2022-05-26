package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.arena.regions.DataBlock;
import me.gameisntover.kbffa.kit.Kit;
import me.gameisntover.kbffa.util.Items;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ArenaSettings implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        String[] arenaList = KnockbackFFA.getInstance().getArenaManager().getFolder().list();
        if (arenaList == null) return;
        for (String arenaName : arenaList) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().load(arenaName.replace(".yml", ""));
            e.setCancelled(!arena.getConfig().getBoolean("block-break"));
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        String[] arenaList = KnockbackFFA.getInstance().getArenaManager().getFolder().list();
        assert arenaList != null;
        for (String arenaName : arenaList) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().load(arenaName.replace(".yml", ""));
            e.setCancelled(!arena.getConfig().getBoolean("item-drop"));
        }
    }

    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        ReworkedKnocker knocker = ReworkedKnocker.get(player.getUniqueId());
        if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena() != null) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().load(KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getName());
            if (!arena.contains(player.getLocation())) return;
            if (knocker.getSelectedKit() == null) knocker.setSelectedKit("Default");
            knocker.getOwnedKits().add("Default");
            Kit kit = KnockbackFFA.getInstance().getKitManager().load(knocker.getSelectedKit());
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    if (Items.COSMETIC_ITEM.getItem().equals(item) || Items.SHOP_ITEM.getItem().equals(item) || Items.KIT_ITEM.getItem().equals(item)) {
                        player.getInventory().clear();
                        kit.giveKit(player);
                        break;
                    }
                }
            }
        }
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        if (knocker.getSelectedTrail() == null) return;
        String selectedTrails = knocker.getSelectedTrail();
        Block block = player.getWorld().getBlockAt(e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
        DataBlock db = KnockbackFFA.getInstance().getBlockDataManager().getBlockData(block);
        if (!db.getBlockType().equals("") || db.getBlockType() != null) return;
        if (KnockbackFFA.getInstance().getConfig().getStringList("no-trail-blocks").contains(block.getType().toString()))
            return;
        db.setPrevMaterial(block.getType());
        List<String> materialString = KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getStringList(selectedTrails + ".blocks");
        List<Material> materialList = new ArrayList<>();
        for (String material : materialString) materialList.add(Material.getMaterial(material));
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
            }.runTaskTimer(KnockbackFFA.getInstance(), KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(selectedTrails + ".speed") * 20L, 1);
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
            }.runTaskTimer(KnockbackFFA.getInstance(), KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(selectedTrails + ".speed") * 20, KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(selectedTrails + ".speed") * 20);
        }
    }
}
