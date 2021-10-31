package me.gameisntover.knockbackffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class KnockbackFFA extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("kits");
        getServer().getPluginManager().registerEvents(new DeathListener() ,this);
        getServer().getPluginManager().registerEvents(new GUIClickevent(),this);
        this.saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            Inventory kits = Bukkit.createInventory(player,9,"Kits");
            player.openInventory(kits);

            ItemStack normal= new ItemStack(Material.STICK,1);
            ItemMeta meta = normal.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA+  "Normal Class");
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.KNOCKBACK,6, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            normal.setItemMeta(meta);
            kits.addItem(normal);
        }
        if (sender instanceof Player){
            if (getCommand("setspawn").getName().equalsIgnoreCase(command.getName())){
                getConfig().addDefaults(getConfig().getDefaults());
                getConfig().addDefault("spawn.world",((Player) sender).getLocation().getWorld().getName());
                getConfig().addDefault("spawn.x",((Player) sender).getLocation().getX());
                getConfig().addDefault("spawn.y",((Player) sender).getLocation().getY());
                getConfig().addDefault("spawn.z",((Player) sender).getLocation().getZ());
                sender.sendMessage(ChatColor.AQUA+"Spawn point successfully set on " + getConfig().getString("spawn.x") + " , " +getConfig().getString("spawn.y")+ " , "+getConfig().getString("spawn.z"));
            }
        }

        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    void playerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.teleport(getConfig().getLocation("spawn"));
        e.setJoinMessage(getConfig().getString("joinmessage").replace("%player%" , player.getDisplayName()).replace("&", "§"));
    }
}
