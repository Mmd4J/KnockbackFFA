package me.gameisntover.kbffa.Arena;

import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.api.PlayerTeleportsToArenaEvent;
import me.gameisntover.kbffa.customconfigs.ArenaConfiguration;
import me.gameisntover.kbffa.customconfigs.MessageConfiguration;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class Arena
{

    public static Arena enabledArena = null;
    public static File cfile;
    public static FileConfiguration config;
    public static File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator);
    public static File df = KnockbackFFA.getInstance().getDataFolder();
    private static String arenaN;

    public static Arena create(String arenaName, Location position1, Location position2, Location spawnPoint) {
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
        Arena arena = Arena.load(arenaName);
        arena.get().set("block-break", false);
        arena.get().set("item-drop", true);
        arena.get().set("world-border", false);
        arena.get().set("block-break", false);
        arena.get().set("item-drop", false);
        arena.get().set("world-border", false);
        arena.get().set("auto-reset", false);
        arena.get().set("arena.pos1", position1);
        arena.get().set("arena.pos2", position2);
        arena.get().set("arena.spawn", spawnPoint);
        arena.save();
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
    public void resetArena() {
        Location loc1 = getPos1();
        Location loc2 = getPos2();
        assert loc2 != null;
        assert loc1 != null;
        Cuboid region = new Cuboid(loc1, loc2);
        List<String> materials = get().getStringList("blocks");
        List<Block> blocks = region.getBlocks();
        new BukkitRunnable(){
            protected int allBlocks = region.getBlocks().size();
            protected int remainBlocks = region.getBlocks().size();
            protected int amountBlocksEachSec;
            protected int startPoint;
                    @Override
            public void run() {
                        amountBlocksEachSec = KnockbackFFA.getInstance().getConfig().getInt("autoresetcheck-blocks");
                        startPoint = allBlocks - remainBlocks;
                        while (startPoint < blocks.size() && amountBlocksEachSec > 0 && remainBlocks > 0) {
                            Material material = Material.getMaterial(materials.get(startPoint));
                            Block block = blocks.get(startPoint);
                            block.setType(material);
                            amountBlocksEachSec--;
                            remainBlocks--;
                            startPoint = allBlocks - remainBlocks;
                        }
                        if (remainBlocks <= 0) {
                            cancel();
                            System.out.println(getName() + " has been reset!");
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
        return arenaList.contains(arenaN);
    }

    /**
     * checks if the arena is enabled
     * @return true if the arena is the enabled arena
     */
    public boolean isEnabled() {
        return getEnabledArena().getName().equals(arenaN);
    }

    /**
     * teleports player to the enabled arena
     * @param @player
     */
    public static void  teleportPlayerToArena(Player player) {
        if (Arena.getfolder().list().length > 0) {
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, getEnabledArena());
            Bukkit.getPluginManager().callEvent(event);
            Location spawnLoc = getEnabledArena().getSpawnLocation();
            if(spawnLoc.getWorld()!=null) {
                if (!event.isCancelled()) {
                    player.teleport(spawnLoc);
                }
            }else {
                WorldCreator wc = new WorldCreator(getEnabledArena().get().getString("arena.spawn.world"));
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
    public static Arena getEnabledArena() {
        return enabledArena;
    }

    /**
     * teleports player to the arena
     * @param @player
     */
    public void teleportPlayer(Player player) {
        if (getEnabledArena().getName()==arenaN) {
            Arena arena = Arena.load(arenaN);
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            player.teleport(getSpawnLocation());
        }
    }

    /**
     * returns player to the main lobby
     * @param @player
     */
    public static void leaveArena(Player player) {
        if (ArenaConfiguration.get().getString("mainlobby.world") != null) {
            Double x = ArenaConfiguration.get().getDouble("mainlobby.x");
            Double y = ArenaConfiguration.get().getDouble("mainlobby.y");
            Double z = ArenaConfiguration.get().getDouble("mainlobby.z");
            World world = Bukkit.getWorld(ArenaConfiguration.get().getString("mainlobby.world"));
            if (world != null && x != null) {
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
            while (Objects.equals(getEnabledArena(), arenaN)){
                setEnabledArena(randomArena());
            }
                save();
        }

    /**
     * sets the arena enabled
     * @param @arena
     */
    public static void setEnabledArena(String arenaName){
        enabledArena = Arena.load(arenaName);
    }
    public static void setEnabledArena(Arena arena){
        enabledArena = arena;
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
    /**
     * Returns the arena name
     * @return String
     */
    public String getName(){
        return arenaN;
    }
    /**
     * Checks if the player is in arena region
     * @param @location
     * @return true if location is in the region
     */
    public boolean contains(Location location){
        Cuboid cuboid = new Cuboid(getPos1(),getPos2());
        return cuboid.contains(location);
    }
    /**
     * Returns the arena spawn location
     * @return Region
     */
    public Location getSpawnLocation(){
        return get().getLocation("arena.spawn");
    }
    /**
     * Returns the arenas first pos
     * @return First position of the arena
     */
    public Location getPos1(){
        return get().getLocation("arena.pos1");
    }
    /**
     * Returns the arenas second pos
     * @return Second position of the arena
     */
    public Location getPos2(){
        return get().getLocation("arena.pos2");
    }

    /**
     * Returns the arena cuboid region
     * @return Cuboid
     */
    public Cuboid getCuboid(){
        return new Cuboid(getPos1(),getPos2());
    }
    public static List<Arena> getArenaList(){
        List<Arena> arenas = new ArrayList<>();
        for (String arena : Arena.getfolder().list()){
            arenas.add(Arena.load(arena.replace(".yml","")));
        }
        return arenas;
    }

    /**
     * Changes the arena to another arena
     * @param @arena
     */
    public static void changeArena(Arena arena){
        String arenaName = arena.getName();
        setEnabledArena(arenaName);
        ArenaConfiguration.save();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                p.getInventory().clear();
                KnockbackFFAKit kitManager = new KnockbackFFAKit();
                kitManager.lobbyItems(p);
                Arena.teleportPlayerToArena(p);
                KnockbackFFAAPI.playSound(p, "arenachange", 1, 1);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfiguration.get().getString("arenachangemsg")).replace("%arena%", arenaName)));
            }
            if (arena.get().getBoolean("auto-reset")) {//arena.resetArena();
                 }
        }
    }
}