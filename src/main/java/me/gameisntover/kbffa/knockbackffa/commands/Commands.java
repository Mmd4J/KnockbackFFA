package me.gameisntover.kbffa.knockbackffa.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.*;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.arenas.VoidChunkGenerator;
import me.gameisntover.kbffa.knockbackffa.scoreboard.MainScoreboard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
            if (KnockbackFFA.getInstance().getCommand("join").getName().equalsIgnoreCase(command.getName())) {
                if (!KnockbackFFAAPI.BungeeMode() && !KnockbackFFAAPI.isInGame(p)) {
                    if (KnockbackFFAArena.arenaisReady(1)) {
                        KnockbackFFAArena.teleportPlayertoArena(p.getPlayer());
                        String joinText = MessageConfiguration.get().getString("join-arena").replace("&", "§");
                        joinText = PlaceholderAPI.setPlaceholders(p, joinText);
                        sender.sendMessage(joinText);
                        if (KnockbackFFA.getInstance().getConfig().getBoolean("save-inventory-on-join")) {
                            PlayerData.load(p.getPlayer());
                            PlayerData.get().set("inventory", p.getPlayer().getInventory().getContents());
                            PlayerData.get().set("armor", p.getPlayer().getInventory().getArmorContents());
                            PlayerData.save();
                            p.getPlayer().getInventory().clear();
                        }
                        MainScoreboard.toggleScoreboard(p, true);
                        PlayerData.load(p.getPlayer());
                        PlayerData.get().set("In-Game", true);
                        PlayerData.save();
                    } else {
                        sender.sendMessage(MessageConfiguration.get().getString("no-arena-ready").replace("&", "§"));
                    }
                }
            }
            if (KnockbackFFA.getInstance().getCommand("leave").getName().equalsIgnoreCase(command.getName())) {
                if (!KnockbackFFAAPI.BungeeMode() && KnockbackFFAAPI.isInGame(p.getPlayer())) {
                    String leaveText = MessageConfiguration.get().getString("leave-arena").replace("&", "§");
                    leaveText = PlaceholderAPI.setPlaceholders(p, leaveText);
                    sender.sendMessage(leaveText);
                            KnockbackFFAArena.leaveArena(p.getPlayer());
                    if (KnockbackFFA.getInstance().getConfig().getBoolean("save-inventory-on-join")) {
                        PlayerData.load(p.getPlayer());
                        List<ItemStack> items = (List<ItemStack>) PlayerData.get().get("inventory");
                        items.stream().filter(item -> item != null);
                        List<ItemStack> armor = (List<ItemStack>) PlayerData.get().get("armor");
                        armor.stream().filter(item -> item != null);
                        p.getInventory().setContents(items.toArray(new ItemStack[items.size()]));
                        p.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
                    }
                    MainScoreboard.toggleScoreboard(p, false);
                    PlayerData.load(p.getPlayer());
                    PlayerData.get().set("In-Game", false);
                    PlayerData.save();
                } else {
                    p.sendMessage(MessageConfiguration.get().getString("cannotuseleave").replace("&", "§"));
                }
            }
            if (KnockbackFFA.getInstance().getCommand("setmainlobby").getName().equalsIgnoreCase(command.getName())) {
                sender.sendMessage(ChatColor.GREEN + "Main lobby spawn has been set");
                Location loc = p.getLocation();
                ArenaConfiguration.get().set("mainlobby.x", loc.getX());
                ArenaConfiguration.get().set("mainlobby.y", loc.getY());
                ArenaConfiguration.get().set("mainlobby.z", loc.getZ());
                String world = loc.getWorld().getName();
                ArenaConfiguration.get().set("mainlobby.world", world);
                ArenaConfiguration.save();
            }
            if (KnockbackFFA.getInstance().getCommand("createworld").getName().equalsIgnoreCase(command.getName())) {
                if (args.length > 0) {
                   WorldCreator wc = new WorldCreator(args[0]);
                   wc.generateStructures(false);
                   wc.generator(new VoidChunkGenerator());
                   wc.createWorld();
                   World world = Bukkit.getWorld(args[0]);
                   world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                   world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

                   Block stone = world.getSpawnLocation().getBlock();
                   if (stone.getType() == Material.AIR) {
                       stone.setType(Material.STONE);
                   }
                    sender.sendMessage(ChatColor.GREEN + "World " + args[0] + " has been loaded");
                }
            }
            if (KnockbackFFA.getInstance().getCommand("reload").getName().equalsIgnoreCase(command.getName())) {
                KnockbackFFA.getInstance().reloadConfig();
                MessageConfiguration.reload();
                ArenaConfiguration.reload();
                PlaySoundConfiguration.reload();
                ScoreboardConfiguration.reload();
                sender.sendMessage(ChatColor.AQUA + "Configs are reloaded!");
            }
            return false;
        }
    }

