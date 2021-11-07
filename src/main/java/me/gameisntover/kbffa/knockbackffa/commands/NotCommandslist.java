package me.gameisntover.kbffa.knockbackffa.commands;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlaySoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class NotCommandslist  implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (sender instanceof Player) {

            if (sender instanceof Player) {
                if (KnockbackFFA.getInstance().getCommand("setspawn").getName().equalsIgnoreCase(command.getName())) {
                    if (args.length > 0) {
                        if (args[0].equalsIgnoreCase("lobby1")) {
                            sender.sendMessage(ChatColor.GREEN+"Set lobby1 spawn!");
                            ArenaConfiguration.get().set("arena1.x", ((Player) sender).getLocation().getX());
                            ArenaConfiguration.get().set("arena1.y", ((Player) sender).getLocation().getY());
                            ArenaConfiguration.get().set("arena1.z", ((Player) sender).getLocation().getZ());
                            ArenaConfiguration.get().set("arena1.world", p.getLocation().getWorld().getName());
                            ArenaConfiguration.save();
                        }
                        if (args[0].equalsIgnoreCase("lobby2")) {
                            sender.sendMessage(ChatColor.GREEN+"Set lobby2 spawn!");
                            ArenaConfiguration.get().set("arena2.x", ((Player) sender).getLocation().getX());
                            ArenaConfiguration.get().set("arena2.y", ((Player) sender).getLocation().getY());
                            ArenaConfiguration.get().set("arena2.z", ((Player) sender).getLocation().getZ());
                            ArenaConfiguration.get().set("arena2.world", p.getLocation().getWorld().getName());
                            ArenaConfiguration.save();
                        }
                        if (args[0].equalsIgnoreCase("lobby3")) {
                            sender.sendMessage(ChatColor.GREEN+"Set lobby3 spawn!");
                            ArenaConfiguration.get().set("arena3.x", ((Player) sender).getLocation().getX());
                            ArenaConfiguration.get().set("arena3.y", ((Player) sender).getLocation().getY());
                            ArenaConfiguration.get().set("arena3.z", ((Player) sender).getLocation().getZ());
                            ArenaConfiguration.get().set("arena3.world", p.getLocation().getWorld().getName());
                            ArenaConfiguration.save();
                        }
                        if (args[0].equalsIgnoreCase("lobby4")) {
                            sender.sendMessage(ChatColor.GREEN+"Set lobby4 spawn!");
                            ArenaConfiguration.get().set("arena4.x", ((Player) sender).getLocation().getX());
                            ArenaConfiguration.get().set("arena4.y", ((Player) sender).getLocation().getY());
                            ArenaConfiguration.get().set("arena4.z", ((Player) sender).getLocation().getZ());
                            ArenaConfiguration.get().set("arena4.world", p.getLocation().getWorld().getName());
                            ArenaConfiguration.save();
                        }
                    }
                } else {
                    sender.sendMessage("sorry but you need to be a player to do that!");
                }
                if (KnockbackFFA.getInstance().getCommand("reload").getName().equalsIgnoreCase(command.getName())) {
                    KnockbackFFA.getInstance().reloadConfig();
                    MessageConfiguration.reload();
                    ArenaConfiguration.reload();
                    PlaySoundConfiguration.reload();
                    sender.sendMessage(ChatColor.AQUA + "Configs are reloaded!");
                }
            }

        }   return false;
    }
}