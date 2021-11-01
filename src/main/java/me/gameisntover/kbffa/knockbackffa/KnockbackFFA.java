package me.gameisntover.kbffa.knockbackffa;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
public final class KnockbackFFA extends JavaPlugin  {
    public static KnockbackFFA INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE= this;

        getCommand("setspawn").setExecutor(new NotCommandslist());
        getCommand("kits").setExecutor(new NotCommandslist());
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(),this);
        
        getServer().getPluginManager().registerEvents(new deathlistener() ,this);
        getServer().getPluginManager().registerEvents(new GUIClickevent(),this);

    }
    public static KnockbackFFA getInstance() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
