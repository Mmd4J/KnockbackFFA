package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.arena.Arena;
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
        Knocker knocker = KnockbackFFA.getInstance().getKnocker(player);
        if (knocker.isInArena()) return;
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
        Knocker knocker = KnockbackFFA.getInstance().getKnocker(player);
        if (!knocker.isInGame()) return;
        String[] arenaList = KnockbackFFA.getInstance().getArenaManager().getFolder().list();
        assert arenaList != null;
        for (String arenaName : arenaList) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().load(arenaName.replace(".yml", ""));
            e.setCancelled(knocker.isInArena() && !arena.getConfig().getBoolean("item-drop"));
        }
    }

    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getInstance().getKnocker(player);
        if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena() != null) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().load(KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getName());
            if (!arena.contains(player.getLocation())) return;
            if (!knocker.isInArena())
                knocker.setInArena(true);
            if (knocker.getConfig().getString("selected-kit") == null)
                knocker.getConfig().set("selected-kit", "Default");
            List<String> ownedKits = knocker.getConfig().getStringList("owned-kits");
            if (!ownedKits.contains("Default")) {
                ownedKits.add("Default");
                knocker.getConfig().set("owned-kits", ownedKits);
            }
            Kit kit = KnockbackFFA.getInstance().getKitManager().load(knocker.getConfig().getString("selected-kit"));
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    if (Items.COSMETIC_ITEM.getItem().equals(item) || Items.SHOP_ITEM.getItem().equals(item) || Items.KIT_ITEM.getItem().equals(item)) {
                        player.getInventory().clear();
                        kit.giveKit(player);
                        break;
                    }
                }
            }
        } else knocker.setInArena(false);
        if (!knocker.isInGame()) return;
        if (knocker.getConfig().getString("selected-trails") == null) return;
        String selectedTrails = knocker.getConfig().getString("selected-trails");
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
            }.
                    runTaskTimer(KnockbackFFA.getInstance(), KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(selectedTrails + ".speed") * 20, KnockbackFFA.getInstance().getCosmeticConfiguration().getConfig.getInt(selectedTrails + ".speed") * 20);
        }
    }
}
