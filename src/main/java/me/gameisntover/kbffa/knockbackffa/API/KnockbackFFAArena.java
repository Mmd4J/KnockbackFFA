package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KnockbackFFAArena {

    public static boolean isEnabled(String arenaName) {
        if (ArenaConfiguration.get().getString("EnabledArena") == arenaName) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean arenaisReady(int arenaid) {
        if (ArenaConfiguration.get().getString("arena" + arenaid + ".world") != null && ArenaConfiguration.get().getDouble("arena" + arenaid + ".x") != 0 && ArenaConfiguration.get().getDouble("arena" + arenaid + ".y") != 0 && ArenaConfiguration.get().getDouble("arena" + arenaid + ".z") != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void teleportPlayertoArena(Player player, int arenaId) {
        if (arenaisReady(arenaId)) {
            double x = ArenaConfiguration.get().getDouble("arena" + arenaId + ".x");
            double y = ArenaConfiguration.get().getDouble("arena" + arenaId + ".y");
            double z = ArenaConfiguration.get().getDouble("arena" + arenaId + ".z");
            World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena" + arenaId + ".world"));
            player.teleport(new Location(world, x, y, z));
        }
    }

    public static void teleportPlayertoArena(Player player) {
        String enabledArena = ArenaConfiguration.get().getString("EnabledArena");
        ArenaConfiguration.save();
        for (int arenaID=1; arenaID <= ArenaData.getfolder().list().length ; arenaID++) {
            if (enabledArena.equalsIgnoreCase("arena" + arenaID)) {
                teleportPlayertoArena(player, arenaID);
            }
        }
        }


    public static void leaveArena(Player player) {
        if (ArenaConfiguration.get().getString("mainlobby.world") != null) {
            Double x = ArenaConfiguration.get().getDouble("mainlobby.x");
            Double y = ArenaConfiguration.get().getDouble("mainlobby.y");
            Double z = ArenaConfiguration.get().getDouble("mainlobby.z");
            World world = Bukkit.getWorld(ArenaConfiguration.get().getString("mainlobby.world"));
            if (world != null && x != null && y != null && z != null) {
                player.teleport(new Location(world, x, y, z));
            } else {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
        }
    }

}