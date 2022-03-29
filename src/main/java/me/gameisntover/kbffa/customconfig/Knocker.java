package me.gameisntover.kbffa.customconfig;

import lombok.Data;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@Data
public class Knocker {
    private File file;
    private Player player;
    private String name;
    private File folder = new File(getDf(), "players"+ File.separator);
    private FileConfiguration config;
    private final File df = KnockbackFFA.getInstance().getDataFolder();
    @SneakyThrows
    public Knocker(Player player){
        setPlayer(player);
        setFile(new File(df, "player data" + File.separator + player.getUniqueId() + ".yml"));
        if (!df.exists()) df.mkdir();
        if (!file.exists()) file.createNewFile();
        setConfig(YamlConfiguration.loadConfiguration(file));
        setName(player.getDisplayName());
    }
    @SneakyThrows
    public void save() {
            config.save(file);
    }
}