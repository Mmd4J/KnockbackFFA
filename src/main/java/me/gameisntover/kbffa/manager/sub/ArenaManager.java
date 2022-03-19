package me.gameisntover.kbffa.manager.sub;

import lombok.Getter;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.manager.api.SubManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Getter
public class ArenaManager implements SubManager {

    // Arena Name ---> Arena
    private final HashMap<String, Arena> arenas = new HashMap<>();
    private final FileConfiguration arenasFile = loadFile();

    private FileConfiguration loadFile(){
        File file = new File("plugins/KnockbackFFA/zones.yml");
        if (!file.exists()) {
            try {
                if(!file.createNewFile()) return null;
            } catch (IOException ignored) {}
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }

}
