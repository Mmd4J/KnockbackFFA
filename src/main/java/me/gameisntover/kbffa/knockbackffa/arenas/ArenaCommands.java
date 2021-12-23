package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class ArenaCommands implements CommandExecutor {
    Integer sz = 1;
    Integer arenaID;
    public static Map <UUID,Integer> arenaidMap = new HashMap<>();
    @Override
    public boolean onCommand( CommandSender sender,  Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (KnockbackFFA.getInstance().getCommand("createarena").getName().equalsIgnoreCase(command.getName())){
                if (args.length == 0){
                    p.sendMessage(ChatColor.RED + "You must specify a name for the arena!");
                }
                else if (args.length == 1){
                    if (WandListener.pos1m.get(p) == null&&WandListener.pos2m.get(p) == null){
                        p.sendMessage(ChatColor.RED + "You must set the first and second positions of the arena!");
                    } else if (WandListener.pos1m.get(p) != null&&WandListener.pos2m.get(p) != null){
                        Location loc1 = WandListener.pos1m.get(p);
                        Location loc2 = WandListener.pos2m.get(p);
                        BoundingBox box = new BoundingBox(loc1.getX(),loc1.getY(),loc1.getZ(),loc2.getX(),loc2.getY(),loc2.getZ());
                        p.getWorld().getWorldBorder().setCenter(box.getCenterX(),box.getCenterZ());
                        p.getWorld().getWorldBorder().setSize(box.getMaxX()-box.getMinX());
                        ArenaData.create(args[0]);
                        ArenaData.get().addDefault("block-break", false);
                        ArenaData.get().addDefault("item-drop", true);
                        ArenaData.get().addDefault("world-border",false);
                        ArenaData.get().set("arena.pos1.x", loc1.getX());
                        ArenaData.get().set("arena.pos1.y", loc1.getY());
                        ArenaData.get().set("arena.pos1.z", loc1.getZ());
                        ArenaData.get().set("arena.pos2.x", loc2.getX());
                        ArenaData.get().set("arena.pos2.y", loc2.getY());
                        ArenaData.get().set("arena.pos2.z", loc2.getZ());
                        String world = p.getWorld().getName();
                        ArenaData.get().set("arena.world", world);
                        ArenaData.save();
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (arenaID== null){
                                    arenaID=1;
                                }
                            if (ArenaConfiguration.get().getString("arena"+arenaID+ ".name") == null){
                                String world = p.getWorld().getName();
                                sender.sendMessage(ChatColor.GREEN + "Arena"+arenaID+"has been created!");
                                ArenaConfiguration.get().set("arena"+arenaID+".x", ((Player) sender).getLocation().getX());
                                ArenaConfiguration.get().set("arena"+arenaID+".y", ((Player) sender).getLocation().getY());
                                ArenaConfiguration.get().set("arena"+arenaID+".z", ((Player) sender).getLocation().getZ());
                                ArenaConfiguration.get().set("arena"+arenaID+".world", world);
                                String arenaname=args[0];
                                ArenaConfiguration.get().set("arena"+arenaID+".name",arenaname);
                                ArenaConfiguration.save();
                                cancel();
                                arenaID = 1;
                            } else if (!KnockbackFFAArena.isEnabled("arena"+ arenaID)){
                                arenaID++;
                            }
                            }
                        }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
                    }
                }
            }
            if (KnockbackFFA.getInstance().getCommand("editarena").getName().equalsIgnoreCase(command.getName())){
                if (args.length==1) {
                            if (ArenaConfiguration.get().getString("arena" + args[0] + ".name") == null) {
                                p.sendMessage(ChatColor.RED + "That arena ID does not exist!");
                            }
                    else if (ArenaConfiguration.get().getString("arena"+args[0]+".name") != null){
                        p.sendMessage(ChatColor.GREEN + "You are now editing arena " + args[0]);
                        Inventory arenaGUI = Bukkit.createInventory(null, 54,  "Arena Editor");
                        ItemStack blockBreak = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                        ItemMeta blockBreakMeta = blockBreak.getItemMeta();
                        blockBreakMeta.setDisplayName(ChatColor.GRAY + "Block Break");
                        ItemStack itemDrop = new ItemStack(Material.DIAMOND, 1);
                        ItemMeta itemDropMeta = itemDrop.getItemMeta();
                        blockBreakMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        itemDropMeta.setDisplayName(ChatColor.GRAY + "Item Drop");
                        ItemStack setspawn = new ItemStack(Material.NETHER_STAR, 1);
                        ItemMeta setspawnMeta = setspawn.getItemMeta();
                        setspawnMeta.setDisplayName(ChatColor.GRAY + "Set Spawn");
                        ItemStack setpos = new ItemStack(Material.REDSTONE_BLOCK, 1);
                        ItemMeta setposMeta = setpos.getItemMeta();
                        setposMeta.setDisplayName(ChatColor.GRAY + "Set Arena Positions");
                        ItemStack worldBorder = new ItemStack(Material.BARRIER, 1);
                        ItemMeta worldBorderMeta = worldBorder.getItemMeta();
                        worldBorderMeta.setDisplayName(ChatColor.GRAY + "World Border");
                                List blockBreaklore = new ArrayList<String>();
                                ArenaData.load(args[0]);
                                List itemDropLore = new ArrayList<String>();
                                List setspawnLore = new ArrayList<String>();
                                List setposLore = new ArrayList<String>();
                                List worldBorderLore = new ArrayList<String>();
                                        blockBreaklore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                                        blockBreaklore.add(ChatColor.GREEN + "Currently Block Breaking is "+ ArenaData.get().getBoolean("block-break"));
                                        itemDropLore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                                        itemDropLore.add(ChatColor.GREEN +"Currently Item Dropping is "+ArenaData.get().getBoolean("item-drop"));
                                        setspawnLore.add(ChatColor.GRAY + "Set the spawnpoint for the arena so players will spawn there");
                                        worldBorderLore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                                        worldBorderLore.add(ChatColor.GREEN + "Currently the world border is "+ArenaData.get().getBoolean("world-border"));
                                        itemDropMeta.setLore(itemDropLore);
                                        itemDrop.setItemMeta(itemDropMeta);
                                        blockBreakMeta.setLore(blockBreaklore);
                                        blockBreak.setItemMeta(blockBreakMeta);
                                        setspawnMeta.setLore(setspawnLore);
                                        setspawn.setItemMeta(setspawnMeta);
                                        setposMeta.setLore(setposLore);
                                        setpos.setItemMeta(setposMeta);
                                        worldBorderMeta.setLore(worldBorderLore);
                                        worldBorder.setItemMeta(worldBorderMeta);
                        arenaGUI.setItem(10, blockBreak);
                                arenaGUI.setItem(11, itemDrop);
                                arenaGUI.setItem(12, setspawn);
                                arenaGUI.setItem(13, setpos);
                                arenaGUI.setItem(14, worldBorder);
                                p.openInventory(arenaGUI);
                                Integer arena = Integer.parseInt(args[0]);
                        arenaidMap.put(p.getUniqueId(),arena);
                    }
                }
            }
        if (KnockbackFFA.getInstance().getCommand("wand").getName().equalsIgnoreCase(command.getName())) {
            ItemStack wand = new ItemStack(Material.BLAZE_ROD);
            ItemMeta wandmeta = wand.getItemMeta();
            wandmeta.setDisplayName(ChatColor.DARK_PURPLE + "Safezone Wand");
            wandmeta.addEnchant(Enchantment.MENDING, 1, true);
            wandmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            wand.setItemMeta(wandmeta);
            p.getInventory().addItem(wand);
        }if (KnockbackFFA.getInstance().getCommand("setsafezone").getName().equalsIgnoreCase(command.getName())) {
                    if (WandListener.pos2m.get(p) != null && WandListener.pos1m.get(p) != null) {
                        new BukkitRunnable(){
                        @Override
                        public void run() {
                        if (ArenaConfiguration.get().getString("Safezones." + sz + ".Safezone.world")== null) {
                            String world = p.getWorld().getName();
                            Location loc1 = WandListener.pos1m.get(p);
                            Location loc2 = WandListener.pos2m.get(p);
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.world", world);
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.pos1.x", loc1.getX());
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.pos1.y", loc1.getY());
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.pos1.z", loc1.getZ());
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.pos2.x", loc2.getX());
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.pos2.y", loc2.getY());
                            ArenaConfiguration.get().set("Safezones." + sz + ".Safezone.pos2.z", loc2.getZ());
                            ArenaConfiguration.save();
                            p.sendMessage(ChatColor.GREEN + "Safezone "+ sz +" has been saved in the arena config file!");
                            sz=1;
                            cancel();
                        } else if(ArenaConfiguration.get().getString("Safezones." + sz + ".Safezone.world")!= null) {
                            sz++;
                        }
                        }
                        }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
                    }
                    }

        if (command.getName().equalsIgnoreCase("gotoworld")) {
            if (args.length >0) {
                World world = Bukkit.getWorld(args[0]);
                if (world != null) {
                    p.teleport(world.getSpawnLocation());
                } else if (world == null){
                    p.sendMessage(ChatColor.RED + "World does not exist!");
                } else if(p.getWorld()==world){
                    p.sendMessage(ChatColor.RED + "You are already in this world!");
                }
            }
        }
    } return false;
  }
}