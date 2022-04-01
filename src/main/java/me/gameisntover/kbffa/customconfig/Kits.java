package me.gameisntover.kbffa.customconfig;

import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Kits {


    public static File cfile;
    public static FileConfiguration config;
    public static File folder = new File(KnockbackFFA.getINSTANCE().getDataFolder(), "Kits" + File.separator);
    public static File df = KnockbackFFA.getINSTANCE().getDataFolder();
    private static String kitName;

    public static Kits load(String kitsName) {
        kitName = kitsName;
        cfile = new File(df, "Kits" + File.separator + kitsName + ".yml");
        config = YamlConfiguration.loadConfiguration(cfile);
        return new Kits();
    }

    public static Kits create(String kitsName) {
        kitName = kitsName;
        cfile = new File(df, "Kits" + File.separator + kitsName + ".yml");
        if (!df.exists()) df.mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
                config = YamlConfiguration.loadConfiguration(cfile);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + cfile.getName() + "!");
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);
        return new Kits();
    }

    public static File getFolder() {
        return folder;
    }

    public static File getfile() {
        return cfile;
    }

    public FileConfiguration get() {
        return config;
    }

    public void giveKit(Player player) {
        if (cfile.exists()) {
            Kits kit = Kits.load(kitName);
            if (kit.get().getList("KitContents") != null) {
                List<ItemStack> kitContents = Arrays.asList(kit.get().getList("KitContents").toArray(new ItemStack[0]));
                player.getInventory().setContents(kitContents.toArray(new ItemStack[0]));
                for (ItemStack item : kitContents) {
                    if (item.getType().toString().contains("Helmet")) {
                        player.getInventory().setHelmet(item);
                    }
                    if (item.getType().toString().contains("Chestplate")) {
                        player.getInventory().setChestplate(item);
                    }
                    if (item.getType().toString().contains("Leggings")) {
                        player.getInventory().setLeggings(item);
                    }
                    if (item.getType().toString().contains("Boots")) {
                        player.getInventory().setBoots(item);
                    }
                }
            } else {
                Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
                knocker.getConfig().set("owned-kits", knocker.getConfig().getStringList("owned-kits").stream().filter(s -> s.contains(kitName)).collect(Collectors.toList()));
                knocker.saveConfig();
            }
        }
    }

    public void save() {
        try {
            config.save(cfile);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + cfile.getName() + "!");
        }
    }


}