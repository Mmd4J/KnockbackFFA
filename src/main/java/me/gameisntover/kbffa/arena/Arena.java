package me.gameisntover.kbffa.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.event.PlayerTeleportsToArenaEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class Arena extends TempArenaManager {

    TempArenaManager tempArenaManager = new TempArenaManager();
    public File getfile() {
        return new File(tempArenaManager.getfolder(), getName() + ".yml");
    }

    public FileConfiguration get() {
        return YamlConfiguration.loadConfiguration(getfile());
    }

    public void save() {
        File file = new File(tempArenaManager.getfolder(), getName() + ".yml");
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
        List<String> arenaList = Arrays.asList(Objects.requireNonNull(tempArenaManager.getfolder().list()));
        return arenaList.contains(getName());
    }

    /**
     * checks if the arena is enabled
     *
     * @return true if the arena is the enabled arena
     */
    public boolean isEnabled() {
        return tempArenaManager.getEnabledArena().getName().equals(getName());
    }




    /**
     * teleports player to the arena
     *
     * @param @player
     */
    public void teleportPlayer(Player player) {
        if (!(getName().equalsIgnoreCase(tempArenaManager.getEnabledArena().getName()))) return;
            Arena arena = tempArenaManager.load(getName());
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            player.teleport(getSpawnLocation());
        }


    /**
     * Deletes the arena file and changes the enabled arena
     * if that arena is the enabled Arena.
     */
    public void removeArena() {
        File cfile = getfile();
        cfile.delete();
        while (tempArenaManager.getEnabledArena().equals(getName())) {
            tempArenaManager.setEnabledArena(tempArenaManager.randomArena());
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