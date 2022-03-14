package me.gameisntover.kbffa.knockbackffa.Arena;

import me.gameisntover.kbffa.knockbackffa.API.PlayerTeleportsToArenaEvent;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Arena
{


    public static File cfile;
    public static FileConfiguration config;
    public static File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator);
    public static File df = KnockbackFFA.getInstance().getDataFolder();
    private static String arenaN;
    public static Arena create(String arenaName) {
        cfile = new File(df, "ArenaData" + File.separator + arenaName + ".yml");
        if (!df.exists()) df.mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + cfile.getName() + "!");
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);
        arenaN = arenaName;
        return new Arena();
    }

    public static File getfolder() {
        return folder;
    }

    public File getfile() {
        return cfile;
    }

    public static Arena load(String arenaName) {
        cfile = new File(df, "ArenaData" + File.separator + arenaName + ".yml");
        config = YamlConfiguration.loadConfiguration(cfile);
        arenaN = arenaName;
        return new Arena();
    }
    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(cfile);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + cfile.getName() + "!");
        }
    }

    /**
     * resets arena blocks to the original state
     */
    public void resetArena(){
        Location loc1 = get().getLocation("arena.pos1");
        Location loc2 = get().getLocation("arena.pos2");
        Cuboid region = new Cuboid(loc1, loc2);
        List<String> blocks = get().getStringList("blocks");
        List<String> slocs = get().getStringList("locations");
        for (Block block : region.getBlocks()) {
            String bloc = block.getLocation().toString();
            int index = slocs.indexOf(bloc);
            if (block.getType().name() != blocks.get(index)) {
                block.setType(Material.getMaterial(blocks.get(index)));
            }
        }
    }

    /**
     * checks if the arena is ready and its able to use
     * @return true if the arena is ready
     */
    public boolean isReady() {
        List<String> arenaList = Arrays.asList(Arena.getfolder().list());
        return arenaList.contains(arenaN);
    }

    /**
     * checks if the arena is enabled
     * @return true if the arena is the enabled arena
     */
    public static boolean isEnabled() {
        return ArenaConfiguration.get().getString("EnabledArena").equalsIgnoreCase(arenaN);
    }

    /**
     * teleports player to the enabled arena
     * @param player
     */
    public static void  teleportPlayerToArena(Player player) {
        if (Arena.getfolder().list().length > 0) {
            String enabledArena = getEnabledArena();
            Arena arena = Arena.load(enabledArena.replace(".yml", ""));
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            Location spawnLoc = arena.get().getLocation("arena.spawn");
            if(spawnLoc.getWorld()!=null) {
                if (!event.isCancelled()) {
                    player.teleport(spawnLoc);
                }
            }else {
                WorldCreator wc = new WorldCreator(arena.get().getString("arena.spawn.world"));
                wc.generateStructures(false);
                wc.generator(new VoidChunkGenerator());
                World world1 = wc.createWorld();
                world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                Bukkit.getWorlds().add(world1);
                world1.loadChunk(0, 0);
                System.out.println(world1.getName() + " successfully loaded!");
                if (!event.isCancelled()) {
                    player.teleport(new Location(world1, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()));
                }
            }
        } else {
            System.out.println("[KnockbackFFA] There are no arenas to teleport the player there!");
        }
    }

    /**
     * @return the enabledArena
     */
    public static String getEnabledArena() {
        return ArenaConfiguration.get().getString("EnabledArena");
    }

    /**
     * teleports player to the arena
     * @param player
     */
    public void teleportPlayer(Player player) {
        if (getEnabledArena()==arenaN) {
            Arena arena = Arena.load(arenaN);
            Location spawnLoc = arena.get().getLocation("arena.spawn");
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            player.teleport(spawnLoc);
        }
    }

    /**
     * returns player to the main lobby
     * @param player
     */
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
    /**
     * Deletes the arena file and changes the enabled arena
     * if that arena is the enabled arena.
     */
    public void removeArena(){
        cfile.delete();
            while (getEnabledArena()==arenaN){
                get().set("EnabledArena",randomArena());
            }
                save();
        }

    /**
     * sets the arena enabled
     * @param arena
     */
    public void setEnabledArena(String arena){
        get().set("EnabledArena",arena);
        save();
    }
    /**
     * Returns a list of arena positions.
     * 0 Index is always the first position
     * and the 1 Index is always the second position
     * @return List<Location>
     *
     */
    public List getArenaPositions(){
        List<Location> positions = new ArrayList<>();
        positions.add(get().getLocation("arena.pos1"));
        positions.add(get().getLocation("arena.pos2"));
        return positions;
        }

    /**
     * Returns a random arena name
     * @return String
     */
    public static String randomArena(){
        String[] arenas = Arena.getfolder().list();
        int random = new Random().nextInt(arenas.length);
        return arenas[random];
    }
}