package me.gameisntover.kbffa;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.arena.GameRules;
import me.gameisntover.kbffa.arena.ZoneConfiguration;
import me.gameisntover.kbffa.arena.regions.BlockDataManager;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.bots.BotManager;
import me.gameisntover.kbffa.command.CommandManager;
import me.gameisntover.kbffa.gui.ButtonManager;
import me.gameisntover.kbffa.hook.Expansion;
import me.gameisntover.kbffa.kit.KitManager;
import me.gameisntover.kbffa.listeners.*;
import me.gameisntover.kbffa.scoreboard.ScoreboardConfiguration;
import me.gameisntover.kbffa.util.*;
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
    private static KnockbackFFA INSTANCE;
    private final Map<UUID, Knocker> knockerHandler = new HashMap<>();
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
    BotManager botManager;

    public Knocker getKnocker(Player player) {
        if (knockerHandler.containsKey(player.getUniqueId()))
            return knockerHandler.get(player.getUniqueId());
        Knocker knocker = new Knocker(player.getUniqueId());
        knockerHandler.put(player.getUniqueId(), knocker);
        return knocker;
    }

    public Knocker getKnocker(String name) {
        Player player = Bukkit.getPlayer(name);
        return getKnocker(player);
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        INSTANCE = this;
        arenaManager = new ArenaManager();
        kitManager = new KitManager();
        blockDataManager = new BlockDataManager();
        botManager = new BotManager();
        if (!Bukkit.getOnlinePlayers().isEmpty())
            for (Player player : Bukkit.getOnlinePlayers()) {
                Knocker knocker = getKnocker(player);
                knocker.setInGame(BungeeMode());
            }
        buttonManager = new ButtonManager();
        Field f = getServer().getClass().getDeclaredField("commandMap");
        f.setAccessible(true);
        commandMap = (CommandMap) f.get(getServer());
        getLogger().info("Loading Configuration Files");
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        loadMessages();
        loadSounds();
        loadConfig();
        getLogger().info("Loading Commands");
        commandManager = new CommandManager();
        getLogger().info("Loading Java Classes");
        loadListeners();
        getLogger().info("Loading Tasks");
        loadTasks();
        getLogger().info("Enjoy using plugin :)");
        registerPlaceholders();
        for (Knocker p : getInGamePlayers())
            if (p.getPlayer().getInventory().contains(Material.BOW) && !p.getPlayer().getInventory().contains(Material.ARROW))
                p.getInventory().addItem(Items.ARROW.getItem());
    }

    @Override
    public void onDisable() {
        for (Bot bot : botManager.getBotHandler().values()) bot.remove();
    }

    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return;
        Bukkit.getPluginManager().registerEvents(this, this);
        new Expansion().register();
        getLogger().info("Successfully registered placeholders");
    }

    public List<Knocker> getInGamePlayers() {
        List<Knocker> knockers = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Knocker knocker = getKnocker(p);
            if (knocker.isInGame()) knockers.add(knocker);
        }
        return knockers;
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
        timer = getConfig().getInt("ArenaChangeTimer");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (arenaList.size() > 0) {
                    String arenaName = arenaList.get(0).getName();
                    arenaManager.setEnabledArena(arenaName);
                    zoneConfiguration.save();
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        Knocker knocker = getKnocker(p);
                        if (!knocker.isInGame()) return;
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
                    timer = getConfig().getInt("ArenaChangeTimer");
                    if (!arenaList.isEmpty()) { //checking if arenaList even has arenas
                        arenaID++;
                        if (!(arenaID <= arenaList.size())) arenaID = 1;
                        arenaManager.changeArena(arenaList.get(arenaID - 1));
                    } else if (arenaList.size() == 1) arenaManager.setEnabledArena(arenaList.get(0).getName());

                }
            }
        }.runTaskTimer(this, 0, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    Knocker knocker = getKnocker(p);
                    if (!knocker.isInGame()) return;
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
        if (!getConfig().getBoolean("ClearItems.enabled")) return;
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            Bukkit.broadcastMessage(Message.ITEM_CLEAR.toString().replace("&", "§"));
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                Knocker knocker = getKnocker(p);
                if (!knocker.isInGame()) return;
                World world = p.getWorld();
                List<Entity> entList = world.getEntities();
                for (Entity current : entList) {
                    if (!(current instanceof Item)) return;
                    current.remove();
                    p.playSound(p.getLocation(), Sounds.ITEM_REMOVED.toSound(), 1, 1);
                }
            }
        }, getConfig().getInt("ClearItems.delay"), getConfig().getInt("ClearItems.period") * 20L);
    }

    private void loadListeners() {
        Arrays.asList(new GameEventsListener(), new JoinLeaveListeners(),
                        new DeathListener(), new WandListener(), new GameRules(),
                        new GuiStuff(), new ArenaSettings())
                .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public boolean BungeeMode() {
        return getConfig().getBoolean("Bungee-Mode");
    }


}