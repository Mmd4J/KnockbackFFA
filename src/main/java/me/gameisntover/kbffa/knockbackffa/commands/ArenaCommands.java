package me.gameisntover.kbffa.knockbackffa.commands;

import me.gameisntover.kbffa.knockbackffa.API.ArenaCreateEvent;
import me.gameisntover.kbffa.knockbackffa.Arena.Arena;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.Arena.Cuboid;
import me.gameisntover.kbffa.knockbackffa.Arena.WandListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.*;

public class ArenaCommands implements CommandExecutor {

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
                       Arena arena =  Arena.create(args[0]);
                        arena.get().set("block-break", false);
                        arena.get().set("item-drop", true);
                        arena.get().set("world-border", false);
                        arena.get().set("arena.pos1", loc1);
                        arena.get().set("arena.pos2", loc2);
                        arena.get().set("block-break", false);
                        arena.get().set("item-drop", false);
                        arena.get().set("world-border", false);
                        arena.get().set("auto-reset",false);
                        arena.get().set("arena.spawn",p.getLocation());
                        List<String> blocks = new ArrayList<>();
                        List<String> locations = new ArrayList<>();
                        Cuboid region = new Cuboid(loc1, loc2);
                        for (Block block : region.getBlocks()) {
                            blocks.add(block.getType().name());
                            locations.add(block.getLocation().toString());
                        }
                        arena.get().set("blocks", blocks);
                        arena.get().set("locations", locations);
                        arena.save();
                            if (arena.getfolder().list().length == 1) {
                                ArenaConfiguration.get().set("EnabledArena", args[0]);
                                ArenaConfiguration.save();
                            }
                        ArenaCreateEvent event = new ArenaCreateEvent(p,arena);
                            Bukkit.getPluginManager().callEvent(event);
                            sender.sendMessage(ChatColor.GREEN + "Arena " + args[0] + " has been created!");
                        }
                    }
                }
                if (KnockbackFFA.getInstance().getCommand("editarena").getName().equalsIgnoreCase(command.getName())) {
                    if (args.length == 1) {
                        List<String> arenaList = Arrays.asList(Arena.getfolder().list());
                        if (!arenaList.contains(args[0] + ".yml")) {
                            p.sendMessage(ChatColor.RED + "That arena name does not exist!");
                        } else if (arenaList.contains(args[0] + ".yml")) {
                            p.sendMessage(ChatColor.GREEN + "You are now editing " + args[0]);
                          Arena arena =  Arena.load(args[0]);
                            InventoryGUI arenaGUI = new InventoryGUI(Bukkit.createInventory(null, 54, "Arena Editor"));
                            ItemButton blockBreak = ItemButton.create(new ItemBuilder(Material.DIAMOND_PICKAXE).setName(ChatColor.GRAY + "Block Break"), e -> {
                                arena.get().set("block-break", !arena.get().getBoolean("block-break"));
                                arena.save();
                                List<String> lore = new ArrayList<>();
                                ItemMeta im = e.getCurrentItem().getItemMeta();
                                lore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                                lore.add(ChatColor.GREEN + "Currently Block Breaking is " + arena.get().getBoolean("block-break"));
                                im.setLore(lore);
                                e.getCurrentItem().setItemMeta(im);
                            });
                            ItemMeta blockBreakMeta = blockBreak.getItem().getItemMeta();
                            ItemButton itemDrop = ItemButton.create(new ItemBuilder(Material.DIAMOND).setName(ChatColor.GRAY + "Item Drop"), e -> {
                                arena.get().set("item-drop", !arena.get().getBoolean("item-drop"));
                                arena.save();
                                List<String> lore = new ArrayList<>();
                                ItemMeta im = e.getCurrentItem().getItemMeta();
                                lore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                                lore.add(ChatColor.GREEN + "Currently Item Dropping is " + arena.get().getBoolean("item-drop"));
                                im.setLore(lore);
                                e.getCurrentItem().setItemMeta(im);
                            });
                            ItemMeta itemDropMeta = itemDrop.getItem().getItemMeta();
                            blockBreakMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                            ItemButton setspawn = ItemButton.create(new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.GRAY + "Set Spawn"), e -> {
                                Player player = (Player) e.getWhoClicked();
                                arena.get().set("arena.spawn", player.getLocation());
                                arena.save();
                                player.sendMessage(ChatColor.GREEN + "Arena Spawn Location Set!");
                            });
                            ItemButton autoReset = ItemButton.create(new ItemBuilder(Material.DISPENSER).setName(ChatColor.GRAY + "Auto Reset"), e -> {
                                arena.get().set("auto-reset", !arena.get().getBoolean("auto-reset"));
                                if (arena.get().getString("blocks")==null) {
                                    Location loc1 = arena.get().getLocation("arena.pos1");
                                    Location loc2 = arena.get().getLocation("arena.pos2");
                                    List<String> blocks = new ArrayList<>();
                                    List<String> locations = new ArrayList<>();
                                    Cuboid region = new Cuboid(loc1, loc2);
                                    for (Block block : region.getBlocks()) {
                                        blocks.add(block.getType().name());
                                        locations.add(block.getLocation().toString());
                                    }
                                    arena.get().set("blocks", blocks);
                                    arena.get().set("locations", locations);
                                    arena.save();
                                }
                                arena.save();
                                p.sendMessage("Auto Reset is now set to" + arena.get().getBoolean("auto-reset"));
                                    });
                            ItemMeta autoResetMeta = autoReset.getItem().getItemMeta();
                            List<String> autoResetLore = new ArrayList<>();
                            autoResetLore.add(ChatColor.GRAY + "Toggle whether or not the arena will reset blocks placed or broke automatically");
                            autoResetLore.add(ChatColor.GREEN + "Currently Auto Reset is " + arena.get().getBoolean("auto-reset"));
                            autoResetMeta.setLore(autoResetLore);
                            autoReset.getItem().setItemMeta(autoResetMeta);
                            ItemMeta setspawnMeta = setspawn.getItem().getItemMeta();
                            ItemButton setpos = ItemButton.create(new ItemBuilder(Material.REDSTONE_BLOCK).setName(ChatColor.GRAY + "Set Position"), e -> {
                                if (WandListener.pos1m.get(e.getWhoClicked()) != null && WandListener.pos2m.get(e.getWhoClicked()) != null) {
                                    Location loc1 = WandListener.pos1m.get(e.getWhoClicked());
                                    Location loc2 = WandListener.pos2m.get(e.getWhoClicked());
                                    BoundingBox box = new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
                                    e.getWhoClicked().getWorld().getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                                    e.getWhoClicked().getWorld().getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                                    arena.get().set("arena.pos1", loc1);
                                    arena.get().set("arena.pos2", loc2);
                                    String world = e.getWhoClicked().getWorld().getName();
                                    arena.get().set("arena.spawn.world", world);
                                    List<String> blocks = new ArrayList<>();
                                    List<String> locations = new ArrayList<>();
                                    Cuboid region = new Cuboid(loc1, loc2);
                                    for (Block block : region.getBlocks()) {
                                        blocks.add(block.getType().name());
                                        locations.add(block.getLocation().toString());
                                    }
                                    arena.get().set("blocks", blocks);
                                    arena.get().set("locations", locations);
                                    arena.save();
                                    e.getWhoClicked().sendMessage(ChatColor.GREEN + "Arena Positions Set!");
                                }
                            });
                            ItemMeta setposMeta = setpos.getItem().getItemMeta();
                            ItemButton worldBorder = ItemButton.create(new ItemBuilder(Material.BARRIER).setName(ChatColor.GRAY + "World Border"), e -> {
                                arena.get().set("world-border", !arena.get().getBoolean("world-border"));
                                arena.save();
                                Location spawnLoc = arena.get().getLocation("arena.spawn");
                                boolean worldBorderBool = arena.get().getBoolean("world-border");
                                if (worldBorderBool) {
                                    Location loc1 = arena.get().getLocation("arena.pos1");
                                    Location loc2 = arena.get().getLocation("arena.pos2");
                                    BoundingBox box = new BoundingBox(loc1.getX(), loc1.getY(),loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
                                    spawnLoc.getWorld().getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                                    spawnLoc.getWorld().getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                                } else {
                                    WorldBorder worldBorderr = spawnLoc.getWorld().getWorldBorder();
                                    worldBorderr.reset();
                                }
                                List<String> lore = new ArrayList<>();
                                ItemMeta im = e.getCurrentItem().getItemMeta();
                                lore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                                lore.add(ChatColor.GREEN + "Currently the world border is " + arena.get().getBoolean("world-border"));
                                im.setLore(lore);
                                e.getCurrentItem().setItemMeta(im);
                            });
                            ItemMeta worldBorderMeta = worldBorder.getItem().getItemMeta();
                            List<String> blockBreaklore = new ArrayList<>();
                            List<String> itemDropLore = new ArrayList<>();
                            List<String> setspawnLore = new ArrayList<>();
                            List<String> setposLore = new ArrayList<>();
                            List<String> worldBorderLore = new ArrayList<>();
                            String arenaName = args[0];
                            blockBreaklore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                            blockBreaklore.add(ChatColor.GREEN + "Currently breaking block is " + arena.get().getBoolean("block-break"));
                            itemDropLore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                            itemDropLore.add(ChatColor.GREEN + "Currently Item Dropping is " + arena.get().getBoolean("item-drop"));
                            setspawnLore.add(ChatColor.GRAY + "Set the spawnpoint for the arena so players will spawn there");
                            worldBorderLore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                            worldBorderLore.add(ChatColor.GREEN + "Currently the world border is " + arena.get().getBoolean("world-border"));
                            itemDropMeta.setLore(itemDropLore);
                            itemDrop.getItem().setItemMeta(itemDropMeta);
                            blockBreakMeta.setLore(blockBreaklore);
                            blockBreak.getItem().setItemMeta(blockBreakMeta);
                            setspawnMeta.setLore(setspawnLore);
                            setspawn.getItem().setItemMeta(setspawnMeta);
                            setposMeta.setLore(setposLore);
                            setpos.getItem().setItemMeta(setposMeta);
                            worldBorderMeta.setLore(worldBorderLore);
                            worldBorder.getItem().setItemMeta(worldBorderMeta);
                            arenaGUI.addButton(10, blockBreak);
                            arenaGUI.addButton(11, itemDrop);
                            arenaGUI.addButton(12, setspawn);
                            arenaGUI.addButton(13, setpos);
                            arenaGUI.addButton(14, worldBorder);
                            arenaGUI.addButton(15, autoReset);
                            arenaGUI.open(p);
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Command Arguements missing or is invalid /editarena arenaname");
                    }
                }
                if (KnockbackFFA.getInstance().getCommand("wand").getName().equalsIgnoreCase(command.getName())) {
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
                        List<String> safezones = ArenaConfiguration.get().getStringList("registered-safezones");
                        int sz;
                        if (safezones.size() == 0) {
                            sz = 1;
                        } else {
                            String szstring = safezones.get(safezones.size() - 1);
                            sz = Integer.parseInt(szstring);
                            sz++;
                        }
                        String world = p.getWorld().getName();
                        Location loc1 = WandListener.pos1m.get(p);
                        Location loc2 = WandListener.pos2m.get(p);
                        ArenaConfiguration.get().set("Safezones." + sz + ".world", world);
                        ArenaConfiguration.get().set("Safezones." + sz + ".pos1", loc1);
                        ArenaConfiguration.get().set("Safezones." + sz + ".pos2", loc2);
                        safezones.add(sz + "");
                        ArenaConfiguration.get().set("registered-safezones", safezones);
                        ArenaConfiguration.save();
                        p.sendMessage(ChatColor.GREEN + "Safezone " + sz + " has been saved in the arena config file!");
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