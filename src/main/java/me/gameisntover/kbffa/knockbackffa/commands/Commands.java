package me.gameisntover.kbffa.knockbackffa.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.*;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.arenas.VoidChunkGenerator;
import me.gameisntover.kbffa.knockbackffa.arenas.WandListener;
import me.gameisntover.kbffa.knockbackffa.scoreboard.MainScoreboard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (KnockbackFFA.getInstance().getCommand("createkit").getName().equalsIgnoreCase(command.getName())) {
            if (p.hasPermission("kbffa.command.createkit")) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Usage: /createkit <kitname>");
                } if (args.length == 1) {
                    Kits kit = new Kits(args[0]);
                    kit.get().set("KitContents", Arrays.stream(p.getInventory().getContents()).filter(item -> item != null));
                    kit.get().set("Price",100);
                    kit.get().set("KitName",args[0]);
                    ItemConfiguration.get().set("CosmeticMenu."+args[0]+".name",args[0]);
                    if (p.getInventory().getItemInMainHand()!=null){
                    ItemConfiguration.get().set("CosmeticMenu."+args[0]+".material",p.getInventory().getItemInMainHand().getType().toString());
                    kit.get().set("KitIcon",p.getInventory().getItemInMainHand().getType().toString());
                    }else {
                        kit.get().set("KitIcon","BARRIER");
                        ItemConfiguration.get().set("CosmeticMenu."+args[0]+".material","BARRIER");
                    }
                    List<String> lore = new ArrayList<>();
                    lore.add("this is a kit");
                    List<String> defaultKitLore = new ArrayList<>();
                    defaultKitLore.add(ChatColor.GRAY + "Another cool kit!");
                    defaultKitLore.add(ChatColor.GRAY + "Must be configured in plugins/KnockbackFFA/kits !");
                    kit.get().set("KitDescription",defaultKitLore);
                    kit.save();
                    ItemConfiguration.get().addDefault("CosmeticMenu."+args[0]+".",null);
                    ItemConfiguration.get().set("CosmeticMenu."+args[0]+".lore",lore);
                    ItemConfiguration.get().set("CosmeticMenu."+args[0]+".price",100);
                    ItemConfiguration.get().set("CosmeticMenu."+args[0]+".type","KIT");
                    ItemConfiguration.save();
                    List<String> registeredC = CosmeticConfiguration.get().getStringList("registered-cosmetics");
                    if (!registeredC.contains(args[0])){
                        registeredC.add(args[0]);
                    }
                    CosmeticConfiguration.get().set("registered-cosmetics",registeredC);
                    CosmeticConfiguration.save();
                    p.sendMessage(ChatColor.GREEN + "I've created the kit " + args[0]+ "! now you need to configure it in the plugins plugins/KnockbackFFA/kits!");
                }
            }else {
                p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            }
        }
        if (KnockbackFFA.getInstance().getCommand("delkit").getName().equalsIgnoreCase(command.getName())) {
            if (p.hasPermission("kbffa.command.delkit")) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Usage: /delkit <kitname>");
                }else if (args.length == 1) {
                    Kits kit = new Kits(args[0]);
                    kit.getfile().delete();
                    ItemConfiguration.get().set("CosmeticMenu."+args[0],null);
                    ItemConfiguration.save();
                    CosmeticConfiguration.get().set("registered-cosmetics",CosmeticConfiguration.get().getStringList("registered-cosmetics").stream().filter(s -> !s.equalsIgnoreCase(args[0])).collect(Collectors.toList()));
                    CosmeticConfiguration.save();
                    p.sendMessage(ChatColor.GREEN + "I've deleted the kit " + args[0]+ "!");
                }
            }
        }
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
                    KnockbackFFAAPI.setInGamePlayer(p,true);

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
                KnockbackFFAAPI.setInGamePlayer(p,false);
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
            SoundConfiguration.reload();
            ScoreboardConfiguration.reload();
            sender.sendMessage(ChatColor.AQUA + "Configs are reloaded!");
        }
        if (KnockbackFFA.getInstance().getCommand("setvoid").getName().equalsIgnoreCase(command.getName())) {
            if (WandListener.pos1m.get(p) != null && WandListener.pos2m.get(p) != null) {
                Location pos1 = WandListener.pos1m.get(p);
                Location pos2 = WandListener.pos2m.get(p);
                Integer vd = 1;
                while (ArenaConfiguration.get().getString("voids." + vd) != null) {
                    vd++;
                }
                if (ArenaConfiguration.get().getString("voids." + vd) == null) {
                    ArenaConfiguration.get().set("voids." + vd + ".pos1", pos1);
                    ArenaConfiguration.get().set("voids." + vd + ".pos2", pos2);
                    ArenaConfiguration.get().set("voids." + vd + ".damage", 8);
                    ArenaConfiguration.save();
                    sender.sendMessage(ChatColor.GREEN + "Void " + vd + " has been set and now players will get damage if they go there");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You have to set two positions first!");
            }
        }
        return false;
    }
}

