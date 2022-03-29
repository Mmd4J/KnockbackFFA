package me.gameisntover.kbffa.customconfig;

import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScoreboardConfiguration {
    private static File file;

    private static FileConfiguration messages;
    @SneakyThrows
    public static void setup() {
        file = new File("plugins","scoreboard.yml");
        if (!file.exists()) {
                  file.createNewFile();
                Files.copy(KnockbackFFA.getINSTANCE().getResource("scoreboard.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
