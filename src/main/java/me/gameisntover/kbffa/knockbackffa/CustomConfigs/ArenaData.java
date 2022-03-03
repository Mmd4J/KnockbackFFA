package me.gameisntover.kbffa.knockbackffa.CustomConfigs;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ArenaData
{


    public static File cfile;
    public static FileConfiguration config;
    public static File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator);
    public static File df = KnockbackFFA.getInstance().getDataFolder();

    public static void create(String arenaName) {
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
    }

    public static File getfolder() {
        return folder;
    }

    public static File getfile() {
        return cfile;
    }

    public static void load(String arenaName) {
        cfile = new File(df, "ArenaData" + File.separator + arenaName + ".yml");
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void save() {
        try {
            config.save(cfile);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + cfile.getName() + "!");
        }
    }


}