package me.gameisntover.kbffa.knockbackffa.arenas;

import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.MaterialLegacy;
import me.gameisntover.kbffa.knockbackffa.arena.Arena;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ArenaCommands implements CommandExecutor
{
    public static Map<UUID, String> arenaNameMap = new HashMap<>();
    int sz = 1;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (KnockbackFFA.getInstance().getCommand("createarena").getName().equalsIgnoreCase(command.getName())) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "You must specify a name for the arena!");
                } else if (args.length == 1) {
                    if (WandListener.pos1m.get(p) == null && WandListener.pos2m.get(p) == null) {
                        p.sendMessage(ChatColor.RED + "You must set the first and second positions of the arena!");
                    } else if (WandListener.pos1m.get(p) != null && WandListener.pos2m.get(p) != null) {
                        Location loc1 = WandListener.pos1m.get(p);
                        Location loc2 = WandListener.pos2m.get(p);
                        ArenaData.create(args[0]);
                        ArenaData.get().addDefault("block-break", false);
                        ArenaData.get().addDefault("item-drop", true);
                        ArenaData.get().addDefault("world-border", false);
                        ArenaData.get().set("arena.pos1.x", loc1.getX());
                        ArenaData.get().set("arena.pos1.y", loc1.getY());
                        ArenaData.get().set("arena.pos1.z", loc1.getZ());
                        ArenaData.get().set("arena.x", ((Player) sender).getLocation().getX());
                        ArenaData.get().set("arena.y", ((Player) sender).getLocation().getY());
                        ArenaData.get().set("arena.z",  ((Player) sender).getLocation().getZ());
                        ArenaData.get().set("arena.pos2.x", loc2.getX());
                        ArenaData.get().set("arena.pos2.y", loc2.getY());
                        ArenaData.get().set("arena.pos2.z", loc2.getZ());
                        ArenaData.get().set("block-break", false);
                        ArenaData.get().set("item-drop", false);
                        ArenaData.get().set("world-border", false);
                        String world = p.getWorld().getName();
                        ArenaData.get().set("arena.world", world);
                        ArenaData.save();
                        if (ArenaData.getfolder().list().length == 1) {
                            ArenaConfiguration.get().set("EnabledArena", "arena1");
                            ArenaConfiguration.save();
                        }
                            sender.sendMessage(ChatColor.GREEN + "Arena " + args [0] + " has been created!");


                    }
                }
            }
            if (KnockbackFFA.getInstance().getCommand("editarena").getName().equalsIgnoreCase(command.getName())) {
                if (args.length == 1) {
                    List<String> arenaList = Arrays.asList(ArenaData.getfolder().list());
                    if (!arenaList.contains(args[0] + ".yml")) {
                        p.sendMessage(ChatColor.RED + "That arena name does not exist!");
                    } else if (arenaList.contains(args[0] + ".yml")) {
                        p.sendMessage(ChatColor.GREEN + "You are now editing " + args[0]);
                        Inventory arenaGUI = Bukkit.createInventory(null, 54, "Arena Editor");
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
                        List<String> blockBreaklore = new ArrayList<>();
                        List<String>  itemDropLore = new ArrayList<>();
                        List<String>  setspawnLore = new ArrayList<>();
                        List<String>  setposLore = new ArrayList<>();
                        List<String>  worldBorderLore = new ArrayList<>();
                        String arenaName = args[0];
                        ArenaData.load(arenaName);
                        blockBreaklore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                        blockBreaklore.add(ChatColor.GREEN + "Currently breaking block is " + ArenaData.get().getBoolean("block-break"));
                        itemDropLore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                        itemDropLore.add(ChatColor.GREEN + "Currently Item Dropping is " + ArenaData.get().getBoolean("item-drop"));
                        setspawnLore.add(ChatColor.GRAY + "Set the spawnpoint for the arena so players will spawn there");
                        worldBorderLore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                        worldBorderLore.add(ChatColor.GREEN + "Currently the world border is " + ArenaData.get().getBoolean("world-border"));
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
                        arenaNameMap.put(p.getUniqueId(), arenaName);
                    }
                }
            }
            if (KnockbackFFA.getInstance().getCommand("wand").getName().equalsIgnoreCase(command.getName())) {
                if (KnockbackFFAAPI.isLegacyVersion()) {
                    ItemStack wand = new ItemStack(Material.getMaterial(MaterialLegacy.BLAZE_ROD.name()));
                    ItemMeta wandmeta = wand.getItemMeta();
                    wandmeta.setDisplayName("PositionSelector Wand");
                    wandmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
                    wandmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    wand.setItemMeta(wandmeta);
                    p.getInventory().addItem(wand);

                }
                ItemStack wand = new ItemStack(Material.BLAZE_ROD);
                ItemMeta wandmeta = wand.getItemMeta();
                wandmeta.setDisplayName(ChatColor.DARK_PURPLE + "PositionSelector Wand");
                wandmeta.addEnchant(Enchantment.MENDING, 1, true);
                wandmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                wand.setItemMeta(wandmeta);
                p.getInventory().addItem(wand);
            }
            if (KnockbackFFA.getInstance().getCommand("setsafezone").getName().equalsIgnoreCase(command.getName())) {
                if (WandListener.pos2m.get(p) != null && WandListener.pos1m.get(p) != null) {
                    new BukkitRunnable()
                    {
                        @Override
                        public void run() {
                            if (ArenaConfiguration.get().getString("Safezones." + sz + ".Safezone.world") == null) {
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
                                p.sendMessage(ChatColor.GREEN + "Safezone " + sz + " has been saved in the arena config file!");
                                sz = 1;
                                cancel();
                            } else if (ArenaConfiguration.get().getString("Safezones." + sz + ".Safezone.world") != null) {
                                sz++;
                            }
                        }
                    }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
                }
            }

            if (command.getName().equalsIgnoreCase("gotoworld")) {
                if (args.length > 0) {
                    World world = Bukkit.getWorld(args[0]);
                    if (world != null) {
                        p.teleport(world.getSpawnLocation());
                    } else if (world == null) {
                        p.sendMessage(ChatColor.RED + "World does not exist!");
                    } else if (p.getWorld() == world) {
                        p.sendMessage(ChatColor.RED + "You are already in this world!");
                    }
                }
            }
        }
        return false;
    }
}