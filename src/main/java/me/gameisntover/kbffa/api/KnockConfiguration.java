package me.gameisntover.kbffa.api;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class KnockConfiguration {

    public abstract String getName();
    public abstract String getResourceName();
    public File getFile;
   @NonNull
   public FileConfiguration getConfig;
    @SneakyThrows
    public void onSetup() {
        getFile = new File(KnockbackFFA.getINSTANCE().getDataFolder(), getName()+".yml");
        if (!KnockbackFFA.getINSTANCE().getDataFolder().exists()) KnockbackFFA.getINSTANCE().getDataFolder().mkdir();
        if (!getFile.exists()) {
            getFile.createNewFile();
            if (getResourceName()!=null) KnockbackFFA.getINSTANCE().saveResource(getResourceName(), true);
        }
        getConfig = YamlConfiguration.loadConfiguration(getFile);
    }
    public KnockConfiguration(){
    onSetup();
    }

    public void reload(){
        getConfig = YamlConfiguration.loadConfiguration(getFile);
    }
    @SneakyThrows
    public void save(){
        getConfig.save(getFile);
    }
}
