package me.gameisntover.kbffa.knockbackffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class NotCommandslist  implements CommandExecutor {
    public static NotCommandslist inostance;
    public static NotCommandslist getInstance() {
        return inostance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (KnockbackFFA.getInstance().getCommand("setspawn").getName().equalsIgnoreCase(command.getName())) {
                KnockbackFFA.getInstance().getConfig();
                KnockbackFFA.getInstance().getConfig();
                KnockbackFFA.getInstance().getConfig().set("spawnpoint.x",((Player) sender).getLocation().getX());
                KnockbackFFA.getInstance().getConfig().set("spawnpoint.y",((Player) sender).getLocation().getY());
                KnockbackFFA.getInstance().getConfig().set("spawnpoint.z",((Player) sender).getLocation().getZ());
                KnockbackFFA.getInstance().getConfig().addDefault("spawnpoint.world",((Player) sender).getLocation().getWorld());
                KnockbackFFA.getInstance().saveConfig();
                 sender.sendMessage(ChatColor.AQUA + "Spawn point successfully set on " + ((Player) sender).getLocation().getX() + " , " + ((Player) sender).getLocation().getY() + " , " + ((Player) sender).getLocation().getZ());
            }
        }
        else {
            sender.sendMessage("sorry but you need to be a player to do that!");
        }
        if (sender instanceof Player) {
            if (KnockbackFFA.getInstance().getCommand("kits").getName().equalsIgnoreCase(command.getName())) {
                Player player = (Player) sender;
                Inventory kits = Bukkit.createInventory(player, 9, "Kits");
                player.openInventory(kits);
                ItemStack normal = new ItemStack(Material.STICK, 1);
                ItemMeta meta = normal.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "Normal Class");
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.KNOCKBACK, 6, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                normal.setItemMeta(meta);
                kits.addItem(normal);
            }
        }
        return true;
    }
}
