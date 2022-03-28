package me.gameisntover.kbffa.manager.sub;

import lombok.Getter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.ReworkedArena;
import me.gameisntover.kbffa.manager.api.SubManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Getter
public class ArenaManager implements SubManager {

    // Arena Name ---> Arena
    private final HashMap<String, ReworkedArena> arenas = new HashMap<>();
    private final FileConfiguration arenasFile = loadFile();
    private final File arenaFolder = new File(KnockbackFFA.getInstance().getDataFolder(),"arenas");
    private FileConfiguration loadFile(){
        File file = new File("plugins/KnockbackFFA/arenas.yml"
                .replace("/", File.separator));
        if (!file.exists()) {
            try {
                if(!file.createNewFile()) return null;
            } catch (IOException ignored) {}
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onLoad() {
        assert arenasFile != null;
        ConfigurationSection arenasSection = arenasFile.getConfigurationSection("arenas");
        if(arenasSection == null) return;
        for(String sectionName : arenasSection.getKeys(false)){
            ConfigurationSection section = arenasSection.getConfigurationSection(sectionName);
            WorldCreator creator = new WorldCreator(sectionName);
            //settings to make the world a void one
            creator.generator("2;0;1");
            World world = creator.createWorld();
            Location spawn = toLocation(world, section.getString("spawn"));
            Location cornerA = toLocation(world, section.getString("first-corner"));
            Location cornerB = toLocation(world, section.getString("second-corner"));
            ReworkedArena arena = new ReworkedArena(sectionName,
                    section.getString("display-name"), spawn, cornerA, cornerB);
            arenas.put(arena.getName(), arena);
        }
    }

    private Location toLocation(World world, String string){
        String[] parts = string.split(",");
        return new Location(world, Double.parseDouble(parts[0]), Double.parseDouble(parts[1])
                , Double.parseDouble(parts[2]), Float.parseFloat(parts[3]), Float.parseFloat(parts[4]));
    }

    @Override
    public void onUnload() {

    }

}
