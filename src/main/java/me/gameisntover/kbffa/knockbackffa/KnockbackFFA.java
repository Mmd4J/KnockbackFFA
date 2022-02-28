package me.gameisntover.kbffa.knockbackffa;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.*;
import me.gameisntover.kbffa.knockbackffa.Listeners.ArenaSettings;
import me.gameisntover.kbffa.knockbackffa.Listeners.DeathListener;
import me.gameisntover.kbffa.knockbackffa.Listeners.NoHunger;
import me.gameisntover.kbffa.knockbackffa.Listeners.guiStuff;
import me.gameisntover.kbffa.knockbackffa.Placeholders.Expansion;
import me.gameisntover.kbffa.knockbackffa.arenas.ArenaCommands;
import me.gameisntover.kbffa.knockbackffa.arenas.GameRules;
import me.gameisntover.kbffa.knockbackffa.arenas.WandListener;
import me.gameisntover.kbffa.knockbackffa.commands.Commands;
import me.gameisntover.kbffa.knockbackffa.commands.CommandsTabCompleter;
import me.gameisntover.kbffa.knockbackffa.messages.ChatFormats;
import me.gameisntover.kbffa.knockbackffa.messages.JoinLeaveListeners;
import me.gameisntover.kbffa.knockbackffa.scoreboard.MainScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.List;

public final class KnockbackFFA extends JavaPlugin implements Listener
{
    public static KnockbackFFA INSTANCE;
    int ArenaID = 0;

    public static KnockbackFFA getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        loadCommands();
        loadTasks();
        loadConfig();
        loadListeners();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p)) {
                if (p.getInventory().contains(Material.BOW) && !p.getInventory().contains(Material.ARROW)) {
                    KnockbackFFAKit kitManager = new KnockbackFFAKit();
                    kitManager.kbbowArrow(p, 1);
                }
            }
        }
        if (KnockbackFFAAPI.isLegacyVersion()) {
            getLogger().info("[KnockbackFFA] : Legacy version detected i dont reommend you to use this version");
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new Expansion(this).register();
        } else {
            getLogger().warning("Could not find placeholder API. This plugin is needed!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    private void loadConfig() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        CosmeticConfiguration.setup();
        MessageConfiguration.setup();
        SoundConfiguration.setup();
        ArenaConfiguration.setup();
        ScoreboardConfiguration.setup();
        ItemConfiguration.setup();
        saveDefaultConfig();

    }

    private void loadTasks() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run() {
                if (KnockbackFFAArena.arenaisReady(1)) {
                    ArenaID++;
                    if (KnockbackFFAArena.arenaisReady(ArenaID) && ArenaID != KnockbackFFAArena.getEnabledArenaint()) {
                        ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                        ArenaConfiguration.save();
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                KnockbackFFAAPI.playSound(p, "arenachange", 1, 1);
                                p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                            }
                        }
                    } else if (!KnockbackFFAArena.arenaisReady(ArenaID)) {
                        if (KnockbackFFAArena.arenaisReady(ArenaID + 1) && ArenaID + 1 != KnockbackFFAArena.getEnabledArenaint()) {
                            ArenaID = ArenaID + 1;
                            ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                            ArenaConfiguration.save();
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                    KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                    KnockbackFFAAPI.playSound(p, "arenachange", 1, 1);
                                    p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                                }
                            }
                        } else {
                            ArenaID = 0;
                        }
                    }
                    if (KnockbackFFAArena.arenaisReady(ArenaID) && ArenaID != KnockbackFFAArena.getEnabledArenaint()) {
                        ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                        ArenaConfiguration.save();
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                KnockbackFFAAPI.playSound(p, "arenachange", 1, 1);
                                p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                            }
                        }
                    } else if (!KnockbackFFAArena.arenaisReady(ArenaID)) {
                        if (KnockbackFFAArena.arenaisReady(ArenaID + 1) && ArenaID + 1 != KnockbackFFAArena.getEnabledArenaint()) {
                            ArenaID = ArenaID + 1;
                            ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                            ArenaConfiguration.save();
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                    KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                    KnockbackFFAAPI.playSound(p, "arenachange", 1, 1);
                                    p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                                }
                            }
                        } else {
                            ArenaID = 0;
                        }
                    }
                }
            }
        }, 0, getConfig().getInt("ArenaChangeTimer") * 20);
        new BukkitRunnable()
        {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                        World world = p.getWorld();
                        List<Entity> entList = world.getEntities();

                        for (Entity current : entList) {
                            if (current instanceof Item) {
                                if (KnockbackFFAAPI.isLegacyVersion()) {
                                    if (((Item) current).getItemStack().getType().getId() == MaterialLegacy.GOLD_PLATE.getId() || ((Item) current).getItemStack().getType().getId() == MaterialLegacy.ARROW.getId() || ((Item) current).getItemStack().getType().getId() == MaterialLegacy.STICK.getId()) {
                                        current.remove();
                                    }
                                } else {
                                    if (((Item) current).getItemStack().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE || ((Item) current).getItemStack().getType() == Material.getMaterial(MaterialLegacy.GOLD_PLATE.name())) {
                                        current.remove();
                                    }
                                    if (((Item) current).getItemStack().getType() == Material.TERRACOTTA) {
                                        current.remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 5);

        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        if (getConfig().getBoolean("ClearItems.enabled")) {
            scheduler1.scheduleSyncRepeatingTask(this, new Runnable()
            {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(MessageConfiguration.get().getString("ItemRemoved").replace("&", "§"));
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                            World world = p.getWorld();
                            List<Entity> entList = world.getEntities();

                            for (Entity current : entList) {
                                if (current instanceof Item) {
                                    current.remove();
                                    KnockbackFFAAPI.playSound(p, "itemremoved", 1, 1);
                                }
                            }
                        }
                    }
                }
            }, getConfig().getInt("ClearItems.delay"), getConfig().getInt("ClearItems.period") * 20);
        }
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new NoHunger(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new ChatFormats(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new WandListener(), this);
        getServer().getPluginManager().registerEvents(new GameRules(), this);
        getServer().getPluginManager().registerEvents(new MainScoreboard(), this);
        getServer().getPluginManager().registerEvents(new guiStuff(),this);
        getServer().getPluginManager().registerEvents(new KnockbackFFAKit(), this);
        getServer().getPluginManager().registerEvents(new ArenaSettings(), this);
    }

    private void loadCommands() {
        getCommand("join").setExecutor(new Commands());
        getCommand("leave").setExecutor(new Commands());
        getCommand("setmainLobby").setExecutor(new Commands());
        getCommand("wand").setExecutor(new ArenaCommands());
        getCommand("reload").setExecutor(new Commands());
        getCommand("setsafezone").setExecutor(new ArenaCommands());
        getCommand("gotoworld").setExecutor(new ArenaCommands());
        getCommand("createarena").setExecutor(new ArenaCommands());
        getCommand("createworld").setExecutor(new Commands());
        getCommand("gotoworld").setTabCompleter(new CommandsTabCompleter());
        getCommand("editarena").setExecutor(new ArenaCommands());
        getCommand("setvoid").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData.create(player);
        if (PlayerData.get().getString("deaths") == null) {
            PlayerData.load(player);
            PlayerData.get().set("deaths", 0);
            PlayerData.get().set("kills", 0);
            PlayerData.save();
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[KnockbackFFA]") && event.getLine(1).equalsIgnoreCase("Join")) {
            event.setLine(0, ChatColor.YELLOW + "[A]KnockbackFFA");
            event.setLine(1, ChatColor.GREEN + "Join");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            if (e.getBlockPlaced().getType() == Material.WHITE_WOOL) {
                Block block = e.getBlockPlaced();
                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run() {
                        switch (block.getType()) {
                            case WHITE_WOOL:
                                block.setType(Material.YELLOW_WOOL);
                                break;
                            case YELLOW_WOOL:
                                block.setType(Material.ORANGE_WOOL);
                                break;
                            case ORANGE_WOOL:
                                block.setType(Material.RED_WOOL);
                                break;
                            case RED_WOOL:
                                block.setType(Material.AIR);
                                cancel();
                                break;
                        }
                    }
                };
                runnable.runTaskTimer(this, 10L, 20L);
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                int i = scheduler.scheduleSyncDelayedTask(this, new Runnable()
                {
                    @Override
                    public void run() {
                        KnockbackFFAKit kitManager = new KnockbackFFAKit();
                        kitManager.BuildingBlock(player, 1);
                    }
                }, 1);
            }
            if (e.getBlockPlaced().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                Block block = e.getBlockPlaced();
                block.getDrops().clear();
                BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
                blockTimer.scheduleSyncDelayedTask(this, new Runnable()
                {
                    @Override
                    public void run() {
                        e.getBlock().setType(Material.AIR);
                    }
                }, 100);
            }
        }
    }

    @EventHandler
    public void onPressureButton(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            if (e.getAction().equals(Action.PHYSICAL)) {
                if (e.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                    Block block = e.getClickedBlock();
                    block.getDrops().clear();
                    player.setVelocity(player.getLocation().getDirection().setY(3));
                    KnockbackFFAAPI.playSound(player, "jumpplate", 1, 1);
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getState() instanceof Sign || e.getClickedBlock().getState() instanceof WallSign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + "[A]KnockbackFFA")) {
                    if (sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Join")) {
                        if (KnockbackFFAAPI.isInGame(player.getPlayer())) {
                            player.sendMessage(ChatColor.RED + "You are already in the game!");
                        } else {
                            player.chat("/join");
                        }
                    }
                }
            }
        }
    }

}