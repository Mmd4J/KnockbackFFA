package me.gameisntover.kbffa.knockbackffa.CustomConfigs;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Kits
{


    public static File cfile;
    public static FileConfiguration config;
    public static File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "Kits" + File.separator);
    public static File df = KnockbackFFA.getInstance().getDataFolder();
    private String kitName;
    public Kits(String kitsName) {
        this.kitName = kitsName;
        cfile = new File(df, "Kits" + File.separator + kitsName + ".yml");
        if (!df.exists()) df.mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + cfile.getName() + "!");
            }
        }else if (cfile.exists()){
            cfile = new File(df, "Kits" + File.separator + kitsName + ".yml");
            config = YamlConfiguration.loadConfiguration(cfile);
        }
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public File getfolder() {
        return folder;
    }

    public File getfile() {
        return cfile;
    }

    public FileConfiguration get() {
        return config;
    }
    public void giveKit(Player player){
        Kits kit = new Kits(kitName);
        List<ItemStack> kitContents = Arrays.asList(kit.get().getList("KitContents").toArray(new ItemStack[0]));
        player.getInventory().setContents(kitContents.toArray(new ItemStack[0]));
        for (ItemStack item : kitContents) {
            if (item.getType().toString().contains("Helmet")){
                player.getInventory().setHelmet(item);
            }   if (item.getType().toString().contains("Chestplate")){
                player.getInventory().setChestplate(item);
            }  if (item.getType().toString().contains("Leggings")){
                player.getInventory().setLeggings(item);
            } if(item.getType().toString().contains("Boots")){
                player.getInventory().setBoots(item);
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