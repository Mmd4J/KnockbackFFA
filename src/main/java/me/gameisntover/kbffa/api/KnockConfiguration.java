package me.gameisntover.kbffa.api;

import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class KnockConfiguration {

    public abstract String getName();
    public abstract String getResourceName();
    public File getFile;
   public FileConfiguration getConfig;
    @SneakyThrows
    public KnockConfiguration(){
        getFile = new File(KnockbackFFA.getINSTANCE().getDataFolder(), getName()+".yml");
        if (!KnockbackFFA.getINSTANCE().getDataFolder().exists()) KnockbackFFA.getINSTANCE().getDataFolder().mkdir();
        if (!getFile.exists()) {
            getFile.createNewFile();
            if (getResourceName()!=null) KnockbackFFA.getINSTANCE().saveResource(getResourceName(), true);
        }
        getConfig = YamlConfiguration.loadConfiguration(getFile);

    }

    public void reload(){
        getConfig = YamlConfiguration.loadConfiguration(getFile);
    }
    @SneakyThrows
    public void save(){
        getConfig.save(getFile);
    }
}
