package me.gameisntover.kbffa.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.api.event.PlayerTeleportsToArenaEvent;
import me.gameisntover.kbffa.message.Message;
import me.gameisntover.kbffa.message.Sounds;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ArenaManagement implements ArenaManager {
    ArenaManager manager;
    public Arena enabledArena = null;
    public File cfile;
    public FileConfiguration config;
    public File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator);
    public File df = KnockbackFFA.getInstance().getDataFolder();
    private String arenaN;

    @Override
    public Arena create(String arenaName, Location position1, Location position2, Location spawnPoint) {
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
        arenaN = arenaName;
        Arena arena = manager.load(arenaName);
        arena.get().set("block-break", false);
        arena.get().set("item-drop", true);
        arena.get().set("world-border", false);
        arena.get().set("block-break", false);
        arena.get().set("item-drop", false);
        arena.get().set("world-border", false);
        arena.get().set("auto-reset", false);
        arena.get().set("arena.pos1", position1);
        arena.get().set("arena.pos2", position2);
        arena.get().set("arena.spawn", spawnPoint);
        arena.get().set("name", arenaName);
        arena.save();
        return new Arena();
    }

    @Override
    public Arena load(String arenaName) {
        cfile = new File(df, "ArenaData" + File.separator + arenaName + ".yml");
        config = YamlConfiguration.loadConfiguration(cfile);
        arenaN = arenaName;
        return new Arena();
    }
    /**
     * @return the enabledArena
     */
    @Override
    public Arena getEnabledArena() {
        return enabledArena;
    }

    @Override
    public File getfolder() {
        return new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData");
    }

    /**
     * teleports player to the enabled arena
     *
     * @param @player
     */
    @Override
    public void teleportPlayerToArena(Player player) {
        if (!(manager.getfolder().list().length > 0)) {
            PlayerTeleportsToArenaEvent event = new PlayerTeleportsToArenaEvent(player, getEnabledArena());
            Bukkit.getPluginManager().callEvent(event);
            Location spawnLoc = getEnabledArena().getSpawnLocation();
            if (event.isCancelled()) return;
            if (spawnLoc.getWorld() != null) player.teleport(spawnLoc);
            else {
                WorldCreator wc = new WorldCreator(getEnabledArena().get().getString("arena.spawn.world"));
                wc.generateStructures(false);
                wc.generator(new VoidChunkGenerator());
                World world1 = wc.createWorld();
                world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                Bukkit.getWorlds().add(world1);
                world1.loadChunk(0, 0);
                System.out.println(world1.getName() + " successfully loaded!");
                player.teleport(new Location(world1, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()));
            }
        } else System.out.println("[KnockbackFFA] There are no arenas to teleport the player there!");

    }

    /**
     * sets the arena enabled
     *
     * @param @arenaName
     */
    @Override
    public void setEnabledArena(String arenaName) {
        enabledArena = manager.load(arenaName);
    }

    /**
     * sets the arena enabled
     *
     * @param @arena
     */
    @Override
    public void setEnabledArena(Arena arena) {
        enabledArena = arena;
    }

    /**
     * Returns a random arena name
     *
     * @return String
     */
    @Override
    public String randomArena() {
        String[] arenas = manager.getfolder().list();
        int random = new Random().nextInt(arenas.length);
        return arenas[random];
    }

    @Override
    public List<Arena> getArenaList() {
        List<Arena> arenas = new ArrayList<>();
        for (String arena : manager.getfolder().list()) {
            arenas.add(manager.load(arena.replace(".yml", "")));
        }
        return arenas;
    }

    /**
     * Changes the arena to another arena
     *
     * @param @arena
     */
    @Override
    public void changeArena(Arena arena) {
        String arenaName = arena.getName();
        setEnabledArena(arenaName);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!KnockbackFFAAPI.BungeeMode() || !KnockbackFFAAPI.isInGame(p)) return;
            p.getInventory().clear();
            KnockbackFFAKit kitManager = new KnockbackFFAKit();
            kitManager.lobbyItems(p);
            manager.teleportPlayerToArena(p);
            p.playSound(p.getLocation(), Sound.valueOf(Sounds.ARENA_CHANGE.toString()), 1, 1);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message.ARENA_CHANGE.toString()).replace("%arena%", arenaName));
        }
        if (arena.get().getBoolean("auto-reset")) arena.resetArena();
    }
}
