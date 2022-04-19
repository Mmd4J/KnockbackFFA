package me.gameisntover.kbffa.api;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

@Getter
public abstract class KnockData {
    private final File file;
    private final FileConfiguration config;

    public abstract File getFolder();

    @SneakyThrows
    KnockData(String name) {
        if (!getFolder().exists()) getFolder().mkdir();
        file = new File(KnockbackFFA.getINSTANCE().getDataFolder(), name + ".yml");
        if (!file.exists()) file.createNewFile();
        config = YamlConfiguration.loadConfiguration(file);
    }

    public Location getLocation(String path) {
        double x = getConfig().getDouble(path + ".x");
        double y = getConfig().getDouble(path + ".y");
        double z = getConfig().getDouble(path + ".z");
        World world = Bukkit.getWorld(Objects.requireNonNull(getConfig().getString(path + ".world")));
        float yaw = 0;
        float pitch = 0;
        if (getConfig().isSet(path + ".yaw")) yaw = getConfig().getInt(path + ".yaw");
        if (getConfig().isSet(path + ".pitch")) pitch = getConfig().getInt(path + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setLocation(String path, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        getConfig().set(path + ".x", x);
        getConfig().set(path + ".y", y);
        getConfig().set(path + ".z", z);
        getConfig().set(path + ".world", world);
        getConfig().set(path + ".yaw", yaw);
        getConfig().set(path + ".pitch", pitch);
    }

    @SneakyThrows
    public void saveConfig() {
        config.save(file);
    }
}
