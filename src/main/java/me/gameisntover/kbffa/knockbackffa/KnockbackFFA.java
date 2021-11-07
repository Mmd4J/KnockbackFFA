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
import me.gameisntover.kbffa.knockbackffa.otherlisteners.NoHunger;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.NoItemDrop;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.RespawnListener;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.deathlistener;
import me.gameisntover.kbffa.knockbackffa.scoreboard.mainscoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class KnockbackFFA extends JavaPlugin implements Listener {
    Integer Checkarenaid=0;
    public static KnockbackFFA INSTANCE;
    @Override
    public void onEnable() {
        getCommand("wand").setExecutor(new ArenaCommands());
        getCommand("setspawn").setExecutor(new NotCommandslist());
        getCommand("reload").setExecutor(new NotCommandslist());
        getCommand("setsafezone").setExecutor(new ArenaCommands());
        INSTANCE = this;
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                for (int arena=0;arena<4;arena++){

                }
                }
            }, 100);

            getConfig().addDefault("enabledarena","world");
        PlaySoundConfiguration.setup();
        PlaySoundConfiguration.get().addDefault("join", "ENTITY_PLAYER_LEVELUP");
        PlaySoundConfiguration.get().addDefault("5kills", "ENTITY_WOLF_HOWL");
        PlaySoundConfiguration.get().addDefault("10kills", "ENTITY_LIGHTNING_BOLT_THUNDER");
        PlaySoundConfiguration.get().addDefault("15kills", "UI_TOAST_CHALLENGE_COMPLETE");
        PlaySoundConfiguration.get().addDefault("+15kills", "ENTITY_LIGHTNING_BOLT_THUNDER");
        PlaySoundConfiguration.get().options().copyDefaults(true);
        PlaySoundConfiguration.save();
        saveDefaultConfig();
        MessageConfiguration.setup();
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
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new NoHunger(), this);
        getServer().getPluginManager().registerEvents(new NoItemDrop(),this);
        getServer().getPluginManager().registerEvents(new mainscoreboard(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new ChatFormats(), this);
        getServer().getPluginManager().registerEvents(new deathlistener(), this);
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
}
