package me.gameisntover.kbffa.arena;

import lombok.Data;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.event.PlayerTeleportsToArenaEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class Arena {
    private String name;
    private Cuboid region;
    private File file;
    private FileConfiguration config;
    private File arenaFolder = new File(KnockbackFFA.getINSTANCE().getDataFolder(), "arenas");

    public Arena(String arenaName) {
        setName(arenaName);
        setFile(new File(arenaFolder, arenaName + ".yml"));
        setConfig(YamlConfiguration.loadConfiguration(file));
        setRegion(new Cuboid(getPos1(), getPos2()));
    }

    @SneakyThrows
    public void save() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.save(file);
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
                    if (material == null && !material.equals(Material.AIR)) return;
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
        }.runTaskTimer(KnockbackFFA.INSTANCE, 0, 20);
    }


    /**
     * checks if the arena is ready and its able to use
     *
     * @return true if the arena is ready
     */
    public boolean isReady() {
        List<String> arenaList = Arrays.asList(Objects.requireNonNull(getArenaFolder().list()));
        return arenaList.contains(getName());
    }

    /**
     * checks if the arena is enabled
     *
     * @return true if the arena is the enabled arena
     */
    public boolean isEnabled() {
        return KnockbackFFA.getINSTANCE().getTempArenaManager().getEnabledArena().getName().equals(getName());
    }

    /**
     * teleports player to the arena
     *
     * @param @player
     */
    public void teleportPlayer(Player player) {
        if (!(getName().equalsIgnoreCase(KnockbackFFA.getINSTANCE().getTempArenaManager().getEnabledArena().getName())))
            return;
        Arena arena = KnockbackFFA.getINSTANCE().getTempArenaManager().load(getName());
        PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, arena);
        Bukkit.getPluginManager().callEvent(event);
        player.teleport(getSpawnLocation());
    }


    /**
     * Deletes the arena file and changes the enabled arena
     * if that arena is the enabled Arena.
     */
    public void removeArena() {
        File cfile = getFile();
        cfile.delete();
        while (KnockbackFFA.getINSTANCE().getTempArenaManager().getEnabledArena().equals(getName())) {
            KnockbackFFA.getINSTANCE().getTempArenaManager().setEnabledArena(KnockbackFFA.getINSTANCE().getTempArenaManager().randomArena());
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
        positions.add(getConfig().getLocation("arena.pos1"));
        positions.add(getConfig().getLocation("arena.pos2"));
        return positions;
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
}