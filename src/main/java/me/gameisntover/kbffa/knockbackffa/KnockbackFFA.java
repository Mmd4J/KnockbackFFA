package me.gameisntover.kbffa.knockbackffa;
import me.gameisntover.kbffa.knockbackffa.commands.NotCommandslist;
import me.gameisntover.kbffa.knockbackffa.kits.kbffakits;
import me.gameisntover.kbffa.knockbackffa.messages.ChatFormats;
import me.gameisntover.kbffa.knockbackffa.messages.JoinLeaveListeners;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.NoHunger;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.NoItemDrop;
import me.gameisntover.kbffa.knockbackffa.otherlisteners.deathlistener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class KnockbackFFA extends JavaPlugin implements Listener {
    public static KnockbackFFA INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        getCommand("setspawn").setExecutor(new NotCommandslist());
        getCommand("kits").setExecutor(new NotCommandslist());
        getCommand("reload").setExecutor(new NotCommandslist());
        getServer().getPluginManager().registerEvents(new NoHunger(), this);
        getServer().getPluginManager().registerEvents(new NoItemDrop(),this);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new ChatFormats(), this);
        getServer().getPluginManager().registerEvents(new deathlistener(), this);
        getServer().getPluginManager().registerEvents(new kbffakits(), this);
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
