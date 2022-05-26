package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.arena.regions.DataBlock;
import me.gameisntover.kbffa.util.Items;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class GameEventsListener implements Listener {
    @EventHandler
    public void noHunger(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
        e.setCancelled(true);
    }

    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (ArenaManager.isInGame(player.getUniqueId()))
            e.setFormat(Message.CHATFORMAT.toString().replace("%player%", player.getName()).replace("%message%", e.getMessage()));
    }

    @EventHandler
    public void onPressureButton(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (ArenaManager.isInGame(player.getUniqueId())) {
            if (e.getAction() != Action.PHYSICAL) return;
            if (Material.LIGHT_WEIGHTED_PRESSURE_PLATE.equals(e.getClickedBlock().getType())) {
                Block block = e.getClickedBlock();
                block.getDrops().clear();
                player.setVelocity(player.getLocation().getDirection().setY(KnockbackFFA.getInstance().getItems().getConfig.getInt("SpecialItems.JumpPlate.jumpLevel")));
                player.playSound(player.getLocation(), Sounds.JUMP_PLATE.toSound(), 1, 1);
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign || e.getClickedBlock().getState() instanceof WallSign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                if (!(ChatColor.YELLOW + "[A]KnockbackFFA").equalsIgnoreCase(sign.getLine(0))) return;
                if (!(ChatColor.GREEN + "Join").equalsIgnoreCase(sign.getLine(1))) return;
                if (ArenaManager.isInGame(player.getUniqueId())) {
                    player.sendMessage(Message.ALREADY_INGAME.toString());
                }
                else player.chat("/join");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!ArenaManager.isInGame(player.getUniqueId())) return;
        if (e.getBlockPlaced().getType() == Material.WHITE_WOOL) {
            Block block = e.getBlockPlaced();
            DataBlock db = KnockbackFFA.getInstance().getBlockDataManager().getBlockData(block);
            db.setBlockType("BuildingBlock");
            String arenaName = KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getName();
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getName().equalsIgnoreCase(arenaName)) {
                        switch (block.getType()) {
                            case WHITE_WOOL:
                                block.setType(Material.YELLOW_WOOL);
                                break;
                            case YELLOW_WOOL:
                                block.setType(Material.ORANGE_WOOL);
                                break;
                            case ORANGE_WOOL:
                                block.setType(Material.RED_WOOL);
                                break;
                            case RED_WOOL:
                                block.setType(Material.AIR);
                                cancel();
                                break;
                        }
                    } else {
                        block.setType(Material.AIR);
                        db.setBlockType("");
                    }
                }
            };
            runnable.runTaskTimer(KnockbackFFA.getInstance(), 10L, 20L);
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(KnockbackFFA.getInstance(), () -> player.getInventory().addItem(Items.BUILDING_BLOCK.getItem()), 1);
        }
        if (e.getBlockPlaced().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
            Block block = e.getBlockPlaced();
            block.getDrops().clear();
            BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
            blockTimer.scheduleSyncDelayedTask(KnockbackFFA.getInstance(),
                    () -> e.getBlock().setType(Material.AIR), 100);
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if ("[KnockbackFFA]".equalsIgnoreCase(event.getLine(0)) && "Join".equalsIgnoreCase(event.getLine(1))) {
            event.setLine(0, ChatColor.YELLOW + "[A]KnockbackFFA");
            event.setLine(1, ChatColor.GREEN + "Join");
        }
    }
}
