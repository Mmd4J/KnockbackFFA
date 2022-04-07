package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.regions.DataBlock;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        e.setCancelled(true);
        if (e.getFoodLevel() < 20) e.setFoodLevel(20);
    }

    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(e.getPlayer());
        if (KnockbackFFA.getINSTANCE().BungeeMode() || knocker.isInGame())
            e.setFormat(Message.CHATFORMAT.toString().replace("%player%", player.getName()).replace("%message%", e.getMessage()));
    }
    @EventHandler
    public void onPressureButton(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (knocker.isInGame()) {
            if (e.getAction()!= Action.PHYSICAL) return;
            if (e.getClickedBlock().getType()== Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                Block block = e.getClickedBlock();
                block.getDrops().clear();
                player.setVelocity(player.getLocation().getDirection().setY(KnockbackFFA.getINSTANCE().getItems().getConfig.getInt("SpecialItems.JumpPlate.jumpLevel")));
                player.playSound(player.getLocation(), Sounds.JUMP_PLATE.toSound(), 1, 1);
            }
        }
        if (e.getAction()==Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign || e.getClickedBlock().getState() instanceof WallSign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                if (!(ChatColor.YELLOW + "[A]KnockbackFFA").equalsIgnoreCase(sign.getLine(0))) return;
                if (!(ChatColor.GREEN + "Join").equalsIgnoreCase(sign.getLine(1))) return;
                if (knocker.isInGame())
                    player.sendMessage(ChatColor.RED + "You are already in the game!");
                else player.chat("/join");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (!knocker.isInGame()) return;
        if (e.getBlockPlaced().getType() == Material.WHITE_WOOL) {
            Block block = e.getBlockPlaced();
            DataBlock db = KnockbackFFA.getINSTANCE().getBlockDataManager().getBlockData(block);
            db.setBlockType("BuildingBlock");
            String arenaName = KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName();
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName().equalsIgnoreCase(arenaName)) {
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
            runnable.runTaskTimer(KnockbackFFA.getINSTANCE(), 10L, 20L);
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(KnockbackFFA.getINSTANCE(), () -> player.getInventory().addItem(me.gameisntover.kbffa.util.Items.BUILDING_BLOCK.getItem()), 1);
        }
        if (e.getBlockPlaced().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
            Block block = e.getBlockPlaced();
            block.getDrops().clear();
            BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
            blockTimer.scheduleSyncDelayedTask(KnockbackFFA.getINSTANCE(),
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
