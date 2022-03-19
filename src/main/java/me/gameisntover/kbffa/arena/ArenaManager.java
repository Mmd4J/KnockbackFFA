package me.gameisntover.kbffa.arena;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public interface ArenaManager {
    public Arena create(String arenaName, Location position1, Location position2, Location spawnPoint);
    public File getfolder();
    public Arena load(String arenaName);
    public void teleportPlayerToArena(Player player);
    public Arena getEnabledArena();
    public void teleportToMainLobby(Player player);
    public void setEnabledArena(String arenaName);
    public void setEnabledArena(Arena arena);
    public String randomArena();
     void changeArena(Arena arena);
    public List<Arena> getArenaList();
}
