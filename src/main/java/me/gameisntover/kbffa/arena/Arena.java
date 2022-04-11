package me.gameisntover.kbffa.arena;

import lombok.Data;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.event.PlayerTeleportsToArenaEvent;
import me.gameisntover.kbffa.arena.regions.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Arena {
    private String name;
    private Cuboid region;
    private File file, arenaFolder = new File(KnockbackFFA.getINSTANCE().getDataFolder(), "ArenaData");
    private FileConfiguration config;
    private boolean ready;

    public Arena(String arenaName, Location pos1, Location pos2) {
        setName(arenaName);
        setFile(new File(arenaFolder, arenaName + ".yml"));
        setConfig(YamlConfiguration.loadConfiguration(file));
        setRegion(new Cuboid(pos1, pos2));
        setReady(true);

    }

    public Arena(String arenaName) {
        setName(arenaName);
        setFile(new File(arenaFolder, arenaName + ".yml"));
        if (getFile().exists()) {
            setConfig(YamlConfiguration.loadConfiguration(file));
            setRegion(new Cuboid(getPos1(), getPos2()));
            setReady(true);
        }
    }

    @SneakyThrows
    public void save() {
        config.save(file);
    }

    /**
     * resets arena blocks to the original state
     */
    public void resetArena() {
        List<String> materials = getConfig().getStringList("blocks");
        List<Block> blocks = region.getBlocks();
        new BukkitRunnable() {
            private final int allBlocks = region.getBlocks().size();
            private int remainBlocks = region.getBlocks().size();

            @Override
            public void run() {
                int amountBlocksEachSec = KnockbackFFA.getINSTANCE().getConfig().getInt("autoresetcheck-blocks");
                int startPoint = allBlocks - remainBlocks;
                while (startPoint < blocks.size() && amountBlocksEachSec > 0 && remainBlocks > 0) {
                    Material material = Material.getMaterial(materials.get(startPoint));
                    if (material == null || !material.equals(Material.AIR)) return;
                    Block block = blocks.get(startPoint);
                    block.setType(material);
                    amountBlocksEachSec--;
                    remainBlocks--;
                    startPoint = allBlocks - remainBlocks;
                }
                if (remainBlocks <= 0) cancel();
            }
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 0, 20);
    }

    /**
     * checks if the arena is enabled
     *
     * @return true if the arena is the enabled arena
     */
    public boolean isEnabled() {
        return KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName().equals(getName());
    }

    /**
     * teleports player to the arena
     *
     * @param player player
     */
    public void teleportPlayer(Player player) {
        if (!(getName().equalsIgnoreCase(KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName())))
            return;
        Arena arena = KnockbackFFA.getINSTANCE().getArenaManager().load(getName());
        PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
        Bukkit.getPluginManager().callEvent(event);
        player.teleport(getSpawnLocation());
    }


    /**
     * Deletes the arena file and changes the enabled arena
     * if that arena is the enabled Arena.
     */
    public void removeArena() {
        getFile().delete();
        while (KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getName().equals(getName()))
            KnockbackFFA.getINSTANCE().getArenaManager().setEnabledArena(KnockbackFFA.getINSTANCE().getArenaManager().randomArena());
        save();
    }

    /**
     * Checks if the player is in arena region
     *
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
        return getConfig().getLocation("arena.spawn");
    }

    /**
     * Returns the arenas first pos
     *
     * @return First position of the arena
     */
    public Location getPos1() {
        return getConfig().getLocation("arena.pos1");
    }

    /**
     * Returns the arenas second pos
     *
     * @return Second position of the arena
     */
    public Location getPos2() {
        return getConfig().getLocation("arena.pos2");
    }

    /**
     * Returns the arena cuboid region
     *
     * @return Cuboid
     */
    public Cuboid getCuboid() {
        return new Cuboid(getPos1(), getPos2());
    }

    public void createAIPath(Location location) {
        List<String> aiPaths = getConfig().getStringList("AIPaths");
        aiPaths.add(location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ());
        getConfig().set("AIPaths", aiPaths);
        save();
    }

    public Location getNearestAIPath(Location location) {
        List<String> aiPaths = getConfig().getStringList("AIPaths");
        Map<Double, Location> distanceMap = new HashMap<>();
        Double lowest = null;
        for (String path : aiPaths) {
            String[] stringLoc = path.split(",");
            Location loc = new Location(Bukkit.getWorld(stringLoc[0]), Double.parseDouble(stringLoc[1]), Double.parseDouble(stringLoc[2]), Double.parseDouble(stringLoc[3]));
            double distance = location.distance(loc);
            distanceMap.put(distance, loc);
            if (lowest == null) lowest = distance;
            if (distance < lowest) lowest = distance;
        }
        return distanceMap.get(lowest);
    }
}