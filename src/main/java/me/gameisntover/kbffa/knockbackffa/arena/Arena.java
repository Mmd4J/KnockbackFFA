package me.gameisntover.kbffa.knockbackffa.arena;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.Config;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Arena
{
    private static final KnockbackFFA plugin = KnockbackFFA.getInstance();
    public static final Map<String, Arena> ARENA_CACHE = new HashMap<>();
    public static final File ARENA_FOLDER = new File(plugin.getDataFolder(), "ArenaData" + File.separator);

    private String name;
    private boolean blockBreak;
    private boolean itemDrop;
    private boolean worldBorder;
    private Location pos1;
    private Location pos2;
    private World world;

    private Config config;

    private Arena(String name) {
        this.name = name;
        this.blockBreak = false;
        this.itemDrop = false;
        this.worldBorder = false;
        this.pos1 = null;
        this.pos2 = null;
        this.world = null;

        ARENA_CACHE.put(name, this);

        File file = new File(ARENA_FOLDER, name + ".yml");
        boolean save = false;
        if (!file.exists()) {
            save = true;
            config = new Config(ARENA_FOLDER, name + ".yml");
        }
        if (save)
            save();
        config = new Config(ARENA_FOLDER, name + ".yml");
        load();
    }

    private void load() {
        this.name = config.getString("name");
        this.blockBreak = config.getBoolean("blockBreak");
        this.itemDrop = config.getBoolean("itemDrop");
        this.worldBorder = config.getBoolean("worldBorder");
        this.pos1 = config.getLocation("pos1");
        this.pos2 = config.getLocation("pos2");

        String world = config.getString("world");
        assert world != null;
        this.world = Bukkit.getWorld(world);
    }

    private void save() {
        config.set("name", name);
        config.set("blockBreak", blockBreak);
        config.set("itemDrop", itemDrop);
        config.set("worldBorder", worldBorder);
        config.set("pos1", pos1);
        config.set("pos2", pos2);
        config.set("world", world);
        config.save();
    }
}
