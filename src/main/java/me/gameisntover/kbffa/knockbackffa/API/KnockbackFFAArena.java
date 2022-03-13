package me.gameisntover.kbffa.knockbackffa.API;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.arenas.VoidChunkGenerator;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class KnockbackFFAArena
{


    public static boolean isEnabled(String arenaName) {
            return ArenaConfiguration.get().getString("EnabledArena").equalsIgnoreCase(arenaName);
        }

    public static boolean arenaisReady(int arenaid) {
        List<String> arenaList = Arrays.asList(ArenaData.getfolder().list());
        return arenaid -- <= arenaList.size() && arenaid -- >= 0;
    }

    public static void teleportPlayertoArena(Player player, String arenaName) {
        if (getEnabledArena()==arenaName) {
            ArenaData.load(arenaName);
            double x = ArenaData.get().getDouble("arena.x");
            double y = ArenaData.get().getDouble("arena.y");
            double z = ArenaData.get().getDouble("arena.z");
            World world = Bukkit.getWorld(ArenaData.get().getString("arena.world"));
            player.teleport(new Location(world, x, y, z));
        }
    }

    public static void  teleportPlayertoArena(Player player) {
        if (ArenaData.getfolder().list().length > 0) {
            String enabledArena = getEnabledArena();
            ArenaData.load(enabledArena.replace(".yml", ""));
            double x = ArenaData.get().getDouble("arena.x");
            double y = ArenaData.get().getDouble("arena.y");
            double z = ArenaData.get().getDouble("arena.z");
            if(Bukkit.getWorld(ArenaData.get().getString("arena.world"))!=null) {
                World world = Bukkit.getWorld(ArenaData.get().getString("arena.world"));
                player.teleport(new Location(world, x, y, z));
            }else{
                    WorldCreator wc = new WorldCreator(ArenaData.get().getString("arena.world"));
                    wc.generateStructures(false);
                    wc.generator(new VoidChunkGenerator());
                    World world1 = wc.createWorld();
                    world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE , false);
                    Bukkit.getWorlds().add(world1);
                    world1.loadChunk(0,0);
                System.out.println(world1.getName() + " successfully loaded!");
                player.teleport(new Location(world1, x, y, z));

            }
        } else {
            System.out.println("[KnockbackFFA] There are no arenas to teleport the player there!");
        }
    }

    public static String getEnabledArena() {
        return ArenaConfiguration.get().getString("EnabledArena");
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