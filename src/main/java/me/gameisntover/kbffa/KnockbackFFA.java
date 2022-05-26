package me.gameisntover.kbffa;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.arena.GameRules;
import me.gameisntover.kbffa.arena.ZoneConfiguration;
import me.gameisntover.kbffa.arena.regions.BlockDataManager;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.bots.BotManager;
import me.gameisntover.kbffa.command.CommandManager;
import me.gameisntover.kbffa.database.DatabaseManager;
import me.gameisntover.kbffa.gui.ButtonManager;
import me.gameisntover.kbffa.hook.Expansion;
import me.gameisntover.kbffa.kit.KitManager;
import me.gameisntover.kbffa.listeners.*;
import me.gameisntover.kbffa.scoreboard.BoardManager;
import me.gameisntover.kbffa.scoreboard.ScoreboardConfiguration;
import me.gameisntover.kbffa.util.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Getter
public final class KnockbackFFA extends JavaPlugin implements Listener {

    @Getter
    private static KnockbackFFA instance;
    BotManager botManager;
    private FileConfiguration messages;
    private FileConfiguration sounds;
    private int arenaID = 0;
    private int timer = 0;
    private ArenaManager arenaManager;
    private CommandMap commandMap;
    private KitManager kitManager;
    private ButtonManager buttonManager;
    private BlockDataManager blockDataManager;
    private CommandManager commandManager;
    private ItemConfig items;
    private ScoreboardConfiguration knockScoreboard;
    private CosmeticConfiguration cosmeticConfiguration;
    private ZoneConfiguration zoneConfiguration;
    private DatabaseManager databaseManager;
    private BoardManager scoreboardManager;

    @SneakyThrows
    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        instance = this;
        getLogger().info("Loading config files...");
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        loadConfig();
        loadMessages();
        loadSounds();
        getLogger().info("Connecting to the database...");
        databaseManager = new DatabaseManager("SQLITE");
        getLogger().info("Connecting to bStats...");
        Metrics metrics = new Metrics(this, 15224);
        //use metrics to add custom chars
        arenaManager = new ArenaManager();
        kitManager = new KitManager();
        blockDataManager = new BlockDataManager();
        botManager = new BotManager();
        scoreboardManager = new BoardManager();
        buttonManager = new ButtonManager();
        Field f = getServer().getClass().getDeclaredField("commandMap");
        f.setAccessible(true);
        commandMap = (CommandMap) f.get(getServer());
        getLogger().info("Loading Commands");
        commandManager = new CommandManager();
        getLogger().info("Loading Listeners");
        loadListeners();
        getLogger().info("Loading Tasks");
        loadTasks();
        long takenTime = (System.currentTimeMillis() - time);
        getLogger().info("Plugin loaded successfully in " + takenTime + "ms");
        registerPlaceholders();
    }

    @Override
    public void onDisable() {
        botManager.getBotHandler().values().forEach(Bot::remove);
    }

    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return;
        Bukkit.getPluginManager().registerEvents(this, this);
        new Expansion().register();
        getLogger().info("Successfully registered placeholders");
    }

    @SneakyThrows
    public void loadMessages() {
        File file = new File(getDataFolder(), "messages.yml");
        if (!file.exists()) {
            file.createNewFile();
            saveResource("messages.yml", true);
        }
        messages = YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public void loadSounds() {
        File file = new File(getDataFolder(), "sound.yml");
        if (!file.exists()) {
            file.createNewFile();
            saveResource("sound.yml", true);
        }
        sounds = YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    private void loadConfig() {
        items = new ItemConfig();
        knockScoreboard = new ScoreboardConfiguration();
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            getLogger().info("Creating DataFolder");
            dataFolder.mkdir();
            File arenaDataFolder = new File(dataFolder + File.separator + "ArenaData");
            arenaDataFolder.mkdir();
        }
        cosmeticConfiguration = new CosmeticConfiguration();
        zoneConfiguration = new ZoneConfiguration();
        File folder = new File(getDataFolder(), "Kits" + File.separator);
        if (!folder.exists()) {
            folder.mkdir();
            File file = new File(getKitManager().getFolder(), "Default.yml");
            file.createNewFile();
            Files.copy(Objects.requireNonNull(getResource("Default.yml")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            getLogger().info("Default Kit Created");
        }
        saveDefaultConfig();

    }

    private void loadTasks() {
        if (arenaManager.getFolder().listFiles() == null || arenaManager.getFolder().listFiles().length == 0)
            return;
        List<Arena> arenaList = arenaManager.getArenaList();
        arenaManager.setEnabledArena(arenaList.get(0));
        timer = getConfig().getInt("arena-switch-timer");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (arenaList.size() > 0) {
                    String arenaName = arenaList.get(0).getName();
                    arenaManager.setEnabledArena(arenaName);
                    zoneConfiguration.save();
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        boolean ingame = ArenaManager.isInGame(p.getUniqueId());
                        if (!ingame) return;
                        arenaManager.changeArena(arenaManager.load(arenaName.replace(".yml", "")));
                        cancel();
                    }
                    if (arenaList.size() > 1) arenaID++;
                }
            }
        }.runTaskTimer(this, 0, 1);
        new BukkitRunnable() {
            @Override
            public void run() {

                timer--;
                if (timer == 0) {
                    //what should happen when timer is up
                    timer = getConfig().getInt("arena-switch-timer");
                    if (!arenaList.isEmpty()) { //checking if arenaList even has arenas
                        arenaID++;
                        if (!(arenaID <= arenaList.size())) arenaID = 1;
                        arenaManager.changeArena(arenaList.get(arenaID - 1));
                    }

                }
            }
        }.runTaskTimer(this, 0, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!ArenaManager.isInGame(p.getUniqueId())) return;
                    World world = p.getWorld();
                    List<Entity> entList = world.getEntities();
                    for (Entity current : entList)
                        if (current instanceof Item)
                            if (((Item) current).getItemStack().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
                                current.remove();
                }
            }
        }.runTaskTimer(this, 0, 5);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!getConfig().getBoolean("wipe-items.enabled")) return;
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            Bukkit.broadcastMessage(Message.ITEM_CLEAR.toString());
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (!ArenaManager.isInGame(p.getUniqueId())) return;
                World world = p.getWorld();
                List<Entity> entList = world.getEntities();
                for (Entity current : entList) {
                    if (!(current instanceof Item)) return;
                    current.remove();
                    p.playSound(p.getLocation(), Sounds.ITEM_REMOVED.toSound(), 1, 1);
                }
            }
        }, 10L, getConfig().getInt("wipe-items.period") * 20L);
    }

    private void loadListeners() {
        Arrays.asList(new GameEventsListener(), new JoinLeaveListeners(),
                        new DeathListener(), new WandListener(), new GameRules(),
                        new GuiStuff(), new ArenaSettings())
                .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public boolean BungeeMode() {
        return getConfig().getBoolean("bungee");
    }


}
