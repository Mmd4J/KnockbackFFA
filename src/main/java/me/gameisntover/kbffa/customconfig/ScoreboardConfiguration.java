package me.gameisntover.kbffa.customconfig;

import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScoreboardConfiguration {
    private static File file;

    private static FileConfiguration messages;

    public static void setup() {
        file = new File("plugins/KnockbackFFA/scoreboard.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.copy(KnockbackFFA.getInstance().getResource("scoreboard.yml"), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {

            }
        }
        messages = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return messages;
    }

    public static void save() {
        try {
            messages.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        messages = YamlConfiguration.loadConfiguration(file);
    }
}
