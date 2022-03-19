package me.gameisntover.kbffa.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.api.event.PlayerTeleportsToArenaEvent;
import me.gameisntover.kbffa.customconfig.ArenaConfiguration;
import me.gameisntover.kbffa.util.Message;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class Arena {
    private static Arena enabledArena = null;
    private static File cfile;
    private static FileConfiguration config;
    private static File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator);
    private static File df = KnockbackFFA.getInstance().getDataFolder();
    private static String arenaN;
    private List<Arena> arenas = new ArrayList<>();
    public static Arena create(String arenaName,Location spawn, Location pos1, Location pos2) {
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
        Arena arena = Arena.load(arenaName);
        arena.get().set("block-break", false);
        arena.get().set("item-drop", true);
        arena.get().set("world-border", false);
        arena.get().set("block-break", false);
        arena.get().set("item-drop", false);
        arena.get().set("world-border", false);
        arena.get().set("auto-reset", false);
        arena.get().set("arena.pos1", pos1);
        arena.get().set("arena.pos2", pos2);
        arena.get().set("arena.spawn", spawn);
        arena.save();
        arenaN = arenaName;
        return new Arena();
    }

    public static Arena load(String arenaName) {
        cfile = new File(df, "ArenaData" + File.separator + arenaName + ".yml");
        config = YamlConfiguration.loadConfiguration(cfile);
        arenaN = arenaName;
        return new Arena();
    }
    /**
     * @return the enabledArena
     */
    public static Arena getEnabledArena() {
        return enabledArena;
    }


    public static File getfolder() {
        return new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData");
    }
    /**
     * returns player to the main lobby
     *
     * needs @param @player
     */

    public static void teleportToMainLobby(Player player) {
        if (ArenaConfiguration.get().getString("mainlobby.world") == null) return;
        Double x = ArenaConfiguration.get().getDouble("mainlobby.x");
        Double y = ArenaConfiguration.get().getDouble("mainlobby.y");
        Double z = ArenaConfiguration.get().getDouble("mainlobby.z");
        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("mainlobby.world"));
        if (world != null && x != null) player.teleport(new Location(world, x, y, z));
        else player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    /**
     * teleports player to the enabled arena
     *
     * @param @player
     */
    public static void teleportPlayerToArena(Player player) {
        if (!(Arena.getfolder().list().length > 0)) {
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, getEnabledArena());
            Bukkit.getPluginManager().callEvent(event);
            Location spawnLoc = getEnabledArena().getSpawnLocation();
            if (event.isCancelled()) return;
            if (spawnLoc.getWorld() != null) player.teleport(spawnLoc);
            else {
                WorldCreator wc = new WorldCreator(getEnabledArena().get().getString("arena.spawn.world"));
                wc.generateStructures(false);
                wc.generator(new VoidChunkGenerator());
                World world1 = wc.createWorld();
                world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                Bukkit.getWorlds().add(world1);
                world1.loadChunk(0, 0);
                System.out.println(world1.getName() + " successfully loaded!");
                player.teleport(new Location(world1, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()));
            }
        } else System.out.println("[KnockbackFFA] There are no arenas to teleport the player there!");

    }

    /**
     * sets the arena enabled
     *
     * @param @arenaName
     */
    public static void setEnabledArena(String arenaName) {
        enabledArena = Arena.load(arenaName);
    }

    /**
     * sets the arena enabled
     *
     * @param @arena
     */
    public static void setEnabledArena(Arena arena) {
        enabledArena = arena;
    }

    /**
     * Returns a random arena name
     *
     * @return String
     */
    public static String randomArena() {
        String[] arenas = Arena.getfolder().list();
        int random = new Random().nextInt(arenas.length);
        return arenas[random];
    }

    public static List<Arena> getArenaList() {
        List<Arena> arenas = new ArrayList<>();
        for (String arena : Arena.getfolder().list()) {
            arenas.add(Arena.load(arena.replace(".yml", "")));
        }
        return arenas;
    }

    /**
     * Changes the arena to another arena
     *
     * @param @arena
     */
    public static void changeArena(Arena arena) {
        String arenaName = arena.getName();
        setEnabledArena(arenaName);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(p)) return;
            p.getInventory().clear();
            KnockbackFFAKit kitManager = new KnockbackFFAKit();
            kitManager.lobbyItems(p);
            Arena.teleportPlayerToArena(p);
            p.playSound(p.getLocation(), Sound.valueOf(Sounds.ARENA_CHANGE.toString()), 1, 1);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message.ARENA_CHANGE.toString()).replace("%arena%", arenaName));
        }
        if (arena.get().getBoolean("auto-reset")) arena.resetArena();
    }


    public File getfile() {
        return new File(Arena.getfolder(), getName() + ".yml");
    }

    public FileConfiguration get() {
        return YamlConfiguration.loadConfiguration(getfile());
    }

    public void save() {
        File file = new File(Arena.getfolder(), getName() + ".yml");
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.save(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + file.getName() + "!");
        }
    }

    /**
     * resets arena blocks to the original state
     */
    public void resetArena() {
        Location loc1 = getPos1();
        Location loc2 = getPos2();
        assert loc2 != null;
        assert loc1 != null;
        Cuboid region = new Cuboid(loc1, loc2);
        List<String> materials = get().getStringList("blocks");
        List<Block> blocks = region.getBlocks();
        new BukkitRunnable() {
            private final int allBlocks = region.getBlocks().size();
            private int remainBlocks = region.getBlocks().size();

            @Override
            public void run() {
                int amountBlocksEachSec = KnockbackFFA.getInstance().getConfig().getInt("autoresetcheck-blocks");
                int startPoint = allBlocks - remainBlocks;
                while (startPoint < blocks.size() && amountBlocksEachSec > 0 && remainBlocks > 0) {
                    Material material = Material.getMaterial(materials.get(startPoint));
                    if (material==null && !material.equals(Material.AIR)) return;
                    Block block = blocks.get(startPoint);
                    block.setType(material);
                    amountBlocksEachSec--;
                    remainBlocks--;
                    startPoint = allBlocks - remainBlocks;
                }
                if (remainBlocks <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);
    }


    /**
     * checks if the arena is ready and its able to use
     *
     * @return true if the arena is ready
     */
    public boolean isReady() {
        List<String> arenaList = Arrays.asList(Objects.requireNonNull(Arena.getfolder().list()));
        return arenaList.contains(getName());
    }

    /**
     * checks if the arena is enabled
     *
     * @return true if the arena is the enabled arena
     */
    public boolean isEnabled() {
        return Arena.getEnabledArena().getName().equals(getName());
    }




    /**
     * teleports player to the arena
     *
     * @param @player
     */
    public void teleportPlayer(Player player) {
        if (!(getName().equalsIgnoreCase(Arena.getEnabledArena().getName()))) return;
            Arena arena = Arena.load(getName());
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            player.teleport(getSpawnLocation());
        }


    /**
     * Deletes the arena file and changes the enabled arena
     * if that arena is the enabled arena.
     */
    public void removeArena() {
        File cfile = getfile();
        cfile.delete();
        while (Arena.getEnabledArena().equals(getName())) {
            Arena.setEnabledArena(Arena.randomArena());
        }
        save();
    }

    /**
     * Returns a list of arena positions.
     * 0 Index is always the first position
     * and the 1 Index is always the second position
     *
     * @return List<Location>
     */
    public List getArenaPositions() {
        List<Location> positions = new ArrayList<>();
        positions.add(get().getLocation("arena.pos1"));
        positions.add(get().getLocation("arena.pos2"));
        return positions;
    }
    /**
     * Returns the arena name
     *
     * @return String
     */
    public String getName() {
        return arenaN;
    }

    /**
     * Checks if the player is in arena region
     *
     * @param @location
     * @return true if location is in the region
     */
    public boolean contains(Location location) {
        Cuboid cuboid = getCuboid();
        return cuboid.contains(location);
    }

    /**
     * Returns the arena spawn location
     *
     * @return Region
     */
    public Location getSpawnLocation() {
        return get().getLocation("arena.spawn");
    }

    /**
     * Returns the arenas first pos
     *
     * @return First position of the arena
     */
    public Location getPos1() {
        return get().getLocation("arena.pos1");
    }

    /**
     * Returns the arenas second pos
     *
     * @return Second position of the arena
     */
    public Location getPos2() {
        return get().getLocation("arena.pos2");
    }

    /**
     * Returns the arena cuboid region
     *
     * @return Cuboid
     */
    public Cuboid getCuboid() {
        return new Cuboid(getPos1(), getPos2());
    }
}