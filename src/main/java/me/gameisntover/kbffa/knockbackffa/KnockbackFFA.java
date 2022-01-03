package me.gameisntover.kbffa.knockbackffa;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.*;
import me.gameisntover.kbffa.knockbackffa.Listeners.ArenaSettings;
import me.gameisntover.kbffa.knockbackffa.arenas.ArenaCommands;
import me.gameisntover.kbffa.knockbackffa.arenas.GameRules;
import me.gameisntover.kbffa.knockbackffa.arenas.WandListener;
import me.gameisntover.kbffa.knockbackffa.commands.Commands;
import me.gameisntover.kbffa.knockbackffa.commands.CommandsTabCompleter;
import me.gameisntover.kbffa.knockbackffa.messages.ChatFormats;
import me.gameisntover.kbffa.knockbackffa.messages.JoinLeaveListeners;
import me.gameisntover.kbffa.knockbackffa.Listeners.DeathListener;
import me.gameisntover.kbffa.knockbackffa.Listeners.NoHunger;
import me.gameisntover.kbffa.knockbackffa.Placeholders.Expansion;
import me.gameisntover.kbffa.knockbackffa.scoreboard.MainScoreboard;
import org.bukkit.*;
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


import java.util.List;

public final class KnockbackFFA extends JavaPlugin implements Listener {
    int ArenaID = 0;
    public static KnockbackFFA INSTANCE;

    @Override
    public void onEnable() {
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
        INSTANCE = this;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (KnockbackFFAAPI.BungeeMode()|| KnockbackFFAAPI.isInGame(p)){
                if (p.getInventory().contains(Material.BOW)&&!p.getInventory().contains(Material.ARROW)){
                    KnockbackFFAKit.kbbowArrow(p,1);
                }
            }
        }
        PlaySoundConfiguration.setup();
        PlaySoundConfiguration.get().addDefault("arenachange", "BLOCK_NOTE_BLOCK_HAT");
        PlaySoundConfiguration.get().addDefault("itemremoved", "BLOCK_NOTE_BLOCK_BASS");
        PlaySoundConfiguration.get().addDefault("jumpplate", "ENTITY_BAT_TAKEOFF");
        PlaySoundConfiguration.get().addDefault("join", "ENTITY_PLAYER_LEVELUP");
        PlaySoundConfiguration.get().addDefault("5kills", "ENTITY_WOLF_HOWL");
        PlaySoundConfiguration.get().addDefault("10kills", "ENTITY_LIGHTNING_BOLT_THUNDER");
        PlaySoundConfiguration.get().addDefault("15kills", "UI_TOAST_CHALLENGE_COMPLETE");
        PlaySoundConfiguration.get().addDefault("+15kills", "ENTITY_LIGHTNING_BOLT_THUNDER");
        PlaySoundConfiguration.get().options().copyDefaults(true);
        PlaySoundConfiguration.save();
        MessageConfiguration.setup();
        MessageConfiguration.get().options().copyDefaults(true);
        MessageConfiguration.save();
        ArenaConfiguration.setup();
        ScoreboardConfiguration.setup();
        saveDefaultConfig();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (KnockbackFFAArena.arenaisReady(1)) {
                    ArenaID++;
                    if (KnockbackFFAArena.arenaisReady(ArenaID)&& ArenaID != KnockbackFFAArena.getEnabledArenaint()) {
                        ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                        ArenaConfiguration.save();
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                p.playSound(p.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
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
                                    p.playSound(p.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                                    p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                                }
                            }
                        }
                        else{
                            ArenaID = 0;
                        }
                    }
                    if (KnockbackFFAArena.arenaisReady(ArenaID)&& ArenaID != KnockbackFFAArena.getEnabledArenaint()) {
                        ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                        ArenaConfiguration.save();
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                p.playSound(p.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                                p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                            }
                        }
                    }else if (!KnockbackFFAArena.arenaisReady(ArenaID)) {
                        if (KnockbackFFAArena.arenaisReady(ArenaID + 1) && ArenaID + 1 != KnockbackFFAArena.getEnabledArenaint()) {
                            ArenaID = ArenaID + 1;
                            ArenaConfiguration.get().set("EnabledArena", "arena" + ArenaID);
                            ArenaConfiguration.save();
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                                    KnockbackFFAArena.teleportPlayertoArena(p, ArenaID);
                                    p.playSound(p.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                                    p.sendMessage(MessageConfiguration.get().getString("arenachangemsg").replace("%arena%", ArenaConfiguration.get().getString("arena" + ArenaID + ".name")).replace("&", "§"));
                                }
                            }
                        }
                        else{
                            ArenaID = 0;
                        }
                    }
                }
            }
        }, 0, getConfig().getInt("ArenaChangeTimer") * 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(p.getPlayer())) {
                        World world = p.getWorld();
                        List<Entity> entList = world.getEntities();

                        for (Entity current : entList) {
                            if (current instanceof Item) {

                                if (((Item) current).getItemStack().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE||((Item)current).getItemStack().getType()==Material.LEGACY_GOLD_PLATE) {
                                    current.remove();
                                }
                                if (((Item) current).getItemStack().getType() == Material.WHITE_WOOL || ((Item) current).getItemStack().getType() == Material.ORANGE_WOOL || ((Item) current).getItemStack().getType() == Material.RED_WOOL || ((Item) current).getItemStack().getType() == Material.YELLOW_WOOL) {
                             current.remove();
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 5);

        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        int o = scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {
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
                                p.playSound(p.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("itemremoved")), 1, 1);
                            }
                        }
                    }
                }
            }
        }, getConfig().getInt("ClearItems.delay"), getConfig().getInt("ClearItems.period") * 20);
        loadListeners();
        ScoreboardConfiguration.setup();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new Expansion(this).register();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required for better configurable!");
            Bukkit.getPluginManager().disablePlugin(this);

        }
    }
    public void loadListeners(){
        getServer().getPluginManager().registerEvents(new NoHunger(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new ChatFormats(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new WandListener(), this);
        getServer().getPluginManager().registerEvents(new GameRules(), this);
        getServer().getPluginManager().registerEvents(new MainScoreboard(), this);
        getServer().getPluginManager().registerEvents(new KnockbackFFAKit(), this);
        getServer().getPluginManager().registerEvents(new ArenaSettings(), this);
    }
    public static KnockbackFFA getInstance() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData.create(player);
        if (PlayerData.get().getLocation("deaths")==null){
            PlayerData.load(player);
            PlayerData.get().set("deaths", 0);
            PlayerData.get().set("kills", 0);
            PlayerData.save();
        }
        }
    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[KnockbackFFA]")&&event.getLine(1).equalsIgnoreCase("Join")) {
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
                BukkitRunnable runnable = new BukkitRunnable() {
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
                int i = scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        KnockbackFFAKit.BuildingBlock(player, 1);
                    }
                }, 1);
            }
            if (e.getBlockPlaced().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                Block block = e.getBlockPlaced();
                block.getDrops().clear();
                BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
                blockTimer.scheduleSyncDelayedTask(this, new Runnable() {
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
                    player.playSound(player.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("jumpplate")), 1, 1);
                }
            }
        }if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock().getState() instanceof Sign||e.getClickedBlock().getState() instanceof WallSign) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW+"[A]KnockbackFFA")) {
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