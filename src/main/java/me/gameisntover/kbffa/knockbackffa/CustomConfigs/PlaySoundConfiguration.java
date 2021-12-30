package me.gameisntover.kbffa.knockbackffa.CustomConfigs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlaySoundConfiguration {
    private static File file;

    private static FileConfiguration soundConfig;
    public static void setup(){
        file=new File("plugins/KnockbackFFA/sounds.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            }catch(IOException e){
            }
        }
        soundConfig= YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get(){
        return soundConfig;
    }
    public static void save(){
        try{
            soundConfig.save(file);
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
    }
    public static void reload(){
        soundConfig=YamlConfiguration.loadConfiguration(file);
    }
}
