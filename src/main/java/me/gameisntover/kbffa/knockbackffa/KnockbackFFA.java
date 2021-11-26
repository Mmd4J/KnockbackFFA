package me.gameisntover.kbffa.knockbackffa;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlaySoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.arenas.GameRules;
import me.gameisntover.kbffa.knockbackffa.arenas.WandListener;
import me.gameisntover.kbffa.knockbackffa.arenas.ArenaCommands;
import me.gameisntover.kbffa.knockbackffa.commands.NotCommandslist;
import me.gameisntover.kbffa.knockbackffa.messages.ChatFormats;
import me.gameisntover.kbffa.knockbackffa.messages.JoinLeaveListeners;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.*;
import me.gameisntover.kbffa.knockbackffa.placeholderapisupport.Expansion;
import me.gameisntover.kbffa.knockbackffa.scoreboard.mainscoreboard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class KnockbackFFA extends JavaPlugin implements Listener {
    int ArenaID = 0;
    public static KnockbackFFA INSTANCE;
    Map<Block, Integer> blocksMap=new HashMap<>();
    @Override
    public void onEnable() {
        getCommand("wand").setExecutor(new ArenaCommands());
        getCommand("setspawn").setExecutor(new NotCommandslist());
        getCommand("reload").setExecutor(new NotCommandslist());
        getCommand("setsafezone").setExecutor(new ArenaCommands());
        INSTANCE = this;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        int i = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                ArenaID++;
                if (ArenaID == 1) {
                    if(ArenaConfiguration.get().getString("arena1.world")!=null) {
                        ArenaConfiguration.get().set("EnabledArena", "arena1");
                        ArenaConfiguration.save();
                        double x = ArenaConfiguration.get().getDouble("arena1.x");
                        double y = ArenaConfiguration.get().getDouble("arena1.y");
                        double z = ArenaConfiguration.get().getDouble("arena1.z");
                        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena1.world"));
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            p.teleport(new Location(world, x, y, z));
                            p.playSound(p.getLocation(),Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                        }
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Changing arena to arena 1...");
                    }else {
                            ArenaID ++;
                    }
                    } else if (ArenaID == 2) {
                    if(ArenaConfiguration.get().getString("arena2.world")!=null) {
                        ArenaConfiguration.get().set("EnabledArena", "arena2");
                        ArenaConfiguration.save();
                        double x = ArenaConfiguration.get().getDouble("arena2.x");
                    double y = ArenaConfiguration.get().getDouble("arena2.y");
                    double z = ArenaConfiguration.get().getDouble("arena2.z");
                    World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena2.world"));
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.teleport(new Location(world, x, y, z));
                        p.playSound(p.getLocation(),Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                    }
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Changing arena to arena 2...");}
                    else {
                        ArenaID ++;
                    }
                } else if (ArenaID == 3) {
                    if(ArenaConfiguration.get().getString("arena3.world")!=null) {
                        ArenaConfiguration.get().set("EnabledArena", "arena3");
                        ArenaConfiguration.save();
                        double x = ArenaConfiguration.get().getDouble("arena3.x");
                    double y = ArenaConfiguration.get().getDouble("arena3.y");
                    double z = ArenaConfiguration.get().getDouble("arena3.z");
                    World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena3.world"));

                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.teleport(new Location(world, x, y, z));
                        p.playSound(p.getLocation(),Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                    }
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Changing arena to arena 3...");}
                    else {
                        ArenaID++;
                    }
                } else if (ArenaID == 4) {
                    if(ArenaConfiguration.get().getString("arena4.world")!=null) {
                        ArenaConfiguration.get().set("EnabledArena", "arena4");
                        ArenaConfiguration.save();
                        double x = ArenaConfiguration.get().getDouble("arena4.x");
                    double y = ArenaConfiguration.get().getDouble("arena4.y");
                    double z = ArenaConfiguration.get().getDouble("arena4.z");
                    World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena4.world"));
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.teleport(new Location(world, x, y, z));
                        p.playSound(p.getLocation(),Sound.valueOf(PlaySoundConfiguration.get().getString("arenachange")), 1, 1);
                    }
                    }
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Changing arena to arena 4...");
                    ArenaID = 0;}
                    else {
                        ArenaID =0;
                    }
                }
        }, 0, getConfig().getInt("ArenaChangeTimer")*20);
        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        int o= scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                World world = getServer().getWorld("world");//get the world
                List<Entity> entList = world.getEntities();//get all entities in the world

                for(Entity current : entList) {
                    if (current instanceof Item) {
                        current.remove();
                        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                            p.playSound(p.getLocation(),Sound.valueOf(PlaySoundConfiguration.get().getString("itemremoved")), 1, 1);}
                        Bukkit.broadcastMessage(MessageConfiguration.get().getString("ItemRemoved").replace("&", "§"));
                    }
                }
            }
                },getConfig().getInt("ClearItems.delay"),getConfig().getInt("ClearItems.period")*20);

        PlaySoundConfiguration.setup();
        PlaySoundConfiguration.get().addDefault("arenachange","BLOCK_NOTE_BLOCK_HAT");
        PlaySoundConfiguration.get().addDefault("itemremoved", "BLOCK_NOTE_BLOCK_BASS");
        PlaySoundConfiguration.get().addDefault("jumpplate", "ENTITY_BAT_TAKEOFF");
        PlaySoundConfiguration.get().addDefault("join", "ENTITY_PLAYER_LEVELUP");
        PlaySoundConfiguration.get().addDefault("5kills", "ENTITY_WOLF_HOWL");
        PlaySoundConfiguration.get().addDefault("10kills", "ENTITY_LIGHTNING_BOLT_THUNDER");
        PlaySoundConfiguration.get().addDefault("15kills", "UI_TOAST_CHALLENGE_COMPLETE");
        PlaySoundConfiguration.get().addDefault("+15kills", "ENTITY_LIGHTNING_BOLT_THUNDER");
        PlaySoundConfiguration.get().options().copyDefaults(true);
        PlaySoundConfiguration.save();
        saveDefaultConfig();
        MessageConfiguration.setup();
        MessageConfiguration.get().addDefault("ItemRemoved", "&aEvery items that were on ground were removed!");
        MessageConfiguration.get().addDefault("deathmessage",  "&c %player_name%  &a was killed by &c %killer%");
        MessageConfiguration.get().addDefault("chatformat","&7[&8%player%&7]&f : %message%");
        MessageConfiguration.get().addDefault("leavemessage","&f -=&7[&4-&7]&f=- &7Hope to see you again &4%player_name% &7:(!");
        MessageConfiguration.get().addDefault("kill","&7You Killed &c %player%");
        MessageConfiguration.get().addDefault("joinmessage","&f -=&7[&4+&7]&f=- &7hey &2%player_name% &7welcome to knockbackFFA");
        MessageConfiguration.get().options().copyDefaults(true);
        MessageConfiguration.save();

        ArenaConfiguration.setup();
        ArenaConfiguration.get().addDefault("arena1","arena1 ");
        ArenaConfiguration.get().addDefault("arena2","arena2 ");
        ArenaConfiguration.get().addDefault("arena3","arena3 ");
        ArenaConfiguration.get().addDefault("arena4","arena4 ");
        ArenaConfiguration.get().addDefault("arena1.world","world");
        ArenaConfiguration.get().addDefault("arena2.world","world");
        ArenaConfiguration.get().addDefault("arena3.world","world");
        ArenaConfiguration.get().addDefault("arena4.world","world");
        ArenaConfiguration.get().options().copyDefaults();
        ArenaConfiguration.save();
        getServer().getPluginManager().registerEvents(new NoHunger(), this);
        getServer().getPluginManager().registerEvents(new NoItemDrop(),this);
        getServer().getPluginManager().registerEvents(new mainscoreboard(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new ChatFormats(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new WandListener(), this);
        getServer().getPluginManager().registerEvents(new GameRules(), this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required for better configurable!");
            Bukkit.getPluginManager().disablePlugin(this);

        }
    }
    public static KnockbackFFA getInstance() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (e.getBlockPlaced().getType() == Material.WHITE_TERRACOTTA) {
            Block block = e.getBlockPlaced();
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    switch (block.getType()) {
                        case WHITE_TERRACOTTA:
                            block.setType(Material.YELLOW_TERRACOTTA);
                            break;
                        case YELLOW_TERRACOTTA:
                            block.setType(Material.ORANGE_TERRACOTTA);
                            break;
                        case ORANGE_TERRACOTTA:
                            block.setType(Material.RED_TERRACOTTA);
                            break;
                        case RED_TERRACOTTA:
                            block.setType(Material.AIR);
                            cancel();
                            break;
                    }
                }
            };
            runnable.runTaskTimer(this, 10L, 10L);
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            int i = scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    KnockbackFFAKit.BuildingBlock(player, 1);
                }
        },1);
        }
        if (e.getBlockPlaced().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {

            BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
            int o = blockTimer.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    e.getBlock().setType(Material.AIR);
                    KnockbackFFAKit.JumpPlate(player,1);
                }
            }, 100);
        }
    }
    @EventHandler
    public void onPressureButton(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL)){
            if (e.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)){
                player.setVelocity(player.getLocation().getDirection().setY(3));
                player.playSound(player.getLocation(),Sound.valueOf(PlaySoundConfiguration.get().getString("jumpplate")), 1, 1);
            }
        }
        }
    }

