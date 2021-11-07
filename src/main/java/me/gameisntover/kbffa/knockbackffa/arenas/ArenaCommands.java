package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class ArenaCommands implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender,  Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
        if (KnockbackFFA.getInstance().getCommand("wand").getName().equalsIgnoreCase(command.getName())) {
            ItemStack wand = new ItemStack(Material.BLAZE_ROD);
            ItemMeta wandmeta = wand.getItemMeta();
            wandmeta.setDisplayName(ChatColor.DARK_PURPLE + "Safezone Wand");
            wandmeta.addEnchant(Enchantment.MENDING, 1, true);
            wandmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            wand.setItemMeta(wandmeta);
            p.getInventory().addItem(wand);
        }if (KnockbackFFA.getInstance().getCommand("setsafezone").getName().equalsIgnoreCase(command.getName())) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("1")) {
                        p.sendMessage(ChatColor.GREEN + "Safezone for arena 1 has been saved in the arena config file!");
                        World world = p.getWorld();
                        ArenaConfiguration.get().set("arena1.safezone1.x", ArenaConfiguration.get().getDouble("temp-pos1.x"));
                        ArenaConfiguration.get().set("arena1.safezone1.y", ArenaConfiguration.get().getDouble("temp-pos1.y"));
                        ArenaConfiguration.get().set("arena1.safezone1.z", ArenaConfiguration.get().getDouble("temp-pos1.z"));
                        ArenaConfiguration.get().set("arena1.safezone2.x", ArenaConfiguration.get().getDouble("temp-pos2.x"));
                        ArenaConfiguration.get().set("arena1.safezone2.y", ArenaConfiguration.get().getDouble("temp-pos2.y"));
                        ArenaConfiguration.get().set("arena1.safezone2.z", ArenaConfiguration.get().getDouble("temp-pos2.z"));
                        ArenaConfiguration.get().set("arena1.world", p.getWorld().getName());
                        ArenaConfiguration.save();
                    }
                    if (args[0].equalsIgnoreCase("2")) {
                        p.sendMessage(ChatColor.GREEN + "Safezone for arena 2 has been saved in the arena config file!");
                        World world = p.getWorld();
                        ArenaConfiguration.get().set("arena2.safezone1.x", ArenaConfiguration.get().getDouble("temp-pos1.x"));
                        ArenaConfiguration.get().set("arena2.safezone1.y", ArenaConfiguration.get().getDouble("temp-pos1.y"));
                        ArenaConfiguration.get().set("arena2.safezone1.z", ArenaConfiguration.get().getDouble("temp-pos1.z"));
                        ArenaConfiguration.get().set("arena2.safezone2.x", ArenaConfiguration.get().getDouble("temp-pos2.x"));
                        ArenaConfiguration.get().set("arena2.safezone2.y", ArenaConfiguration.get().getDouble("temp-pos2.y"));
                        ArenaConfiguration.get().set("arena2.safezone2.z", ArenaConfiguration.get().getDouble("temp-pos2.z"));
                        ArenaConfiguration.get().set("arena2.world", p.getWorld().toString());
                        ArenaConfiguration.save();
                    }
                    if (args[0].equalsIgnoreCase("3")) {
                        p.sendMessage(ChatColor.GREEN + "Safezone for arena 3 has been saved in the arena config file!");
                        World world = p.getWorld();
                        ArenaConfiguration.get().set("arena3.safezone1.x", ArenaConfiguration.get().getDouble("temp-pos1.x"));
                        ArenaConfiguration.get().set("arena3.safezone1.y", ArenaConfiguration.get().getDouble("temp-pos1.y"));
                        ArenaConfiguration.get().set("arena3.safezone1.z", ArenaConfiguration.get().getDouble("temp-pos1.z"));
                        ArenaConfiguration.get().set("arena3.safezone2.x", ArenaConfiguration.get().getDouble("temp-pos2.x"));
                        ArenaConfiguration.get().set("arena3.safezone2.y", ArenaConfiguration.get().getDouble("temp-pos2.y"));
                        ArenaConfiguration.get().set("arena3.safezone2.z", ArenaConfiguration.get().getDouble("temp-pos2.z"));
                        ArenaConfiguration.get().set("arena3.world", p.getWorld().toString());
                        ArenaConfiguration.save();
                    }
                    if (args[0].equalsIgnoreCase("4")) {
                        p.sendMessage(ChatColor.GREEN + "Safezone for arena 4 has been saved in the arena config file!");
                        World world = p.getWorld();
                        ArenaConfiguration.get().set("arena4.safezone1.x", ArenaConfiguration.get().getDouble("temp-pos1.x"));
                        ArenaConfiguration.get().set("arena4.safezone1.y", ArenaConfiguration.get().getDouble("temp-pos1.y"));
                        ArenaConfiguration.get().set("arena4.safezone1.z", ArenaConfiguration.get().getDouble("temp-pos1.z"));
                        ArenaConfiguration.get().set("arena4.safezone2.x", ArenaConfiguration.get().getDouble("temp-pos2.x"));
                        ArenaConfiguration.get().set("arena4.safezone2.y", ArenaConfiguration.get().getDouble("temp-pos2.y"));
                        ArenaConfiguration.get().set("arena4.safezone2.z", ArenaConfiguration.get().getDouble("temp-pos2.z"));
                        ArenaConfiguration.get().set("arena4.world", p.getWorld().toString());
                        ArenaConfiguration.save();
                    }
                }
            }
    } return false;
  }
}