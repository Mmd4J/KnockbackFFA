package me.gameisntover.kbffa.knockbackffa.commands;

import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.arenas.WandListener;
import org.bukkit.*;
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
import java.util.stream.Collectors;

public class ArenaCommands implements CommandExecutor
{
    public static Map<UUID, String> arenaNameMap = new HashMap<>();

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
                        ArenaData.get().set("block-break", false);
                        ArenaData.get().set("item-drop", true);
                        ArenaData.get().set("world-border", false);
                        ArenaData.get().set("arena", ((Player) sender).getLocation());
                        ArenaData.get().set("arena.pos1", loc1);
                        ArenaData.get().set("arena.pos2", loc2);
                        ArenaData.get().set("block-break", false);
                        ArenaData.get().set("item-drop", false);
                        ArenaData.get().set("world-border", false);
                        ArenaData.save();
                        if (ArenaData.getfolder().list().length == 1) {
                            ArenaConfiguration.get().set("EnabledArena", args[0]);
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
                        InventoryGUI arenaGUI = new InventoryGUI(Bukkit.createInventory(null, 54, "Arena Editor"));
                        ItemButton blockBreak = ItemButton.create(new ItemBuilder(Material.DIAMOND_PICKAXE).setName(ChatColor.GRAY+ "Block Break"),e -> {
                            ArenaData.load(arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                                ArenaData.get().set("block-break", !ArenaData.get().getBoolean("block-break"));
                                ArenaData.save();
                                List<String> lore = new ArrayList<>();
                                ItemMeta im = e.getCurrentItem().getItemMeta();
                                lore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                                lore.add(ChatColor.GREEN + "Currently Block Breaking is " + ArenaData.get().getBoolean("block-break"));
                                im.setLore(lore);
                                e.getCurrentItem().setItemMeta(im);
                        });
                        ItemMeta blockBreakMeta = blockBreak.getItem().getItemMeta();
                        ItemButton itemDrop = ItemButton.create(new ItemBuilder(Material.DIAMOND).setName(ChatColor.GRAY+ "Item Drop"),e -> {
                            ArenaData.load(arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                                ArenaData.get().set("item-drop", !ArenaData.get().getBoolean("item-drop"));
                                ArenaData.save();
                                List<String> lore = new ArrayList<>();
                                ItemMeta im = e.getCurrentItem().getItemMeta();
                                lore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                                lore.add(ChatColor.GREEN + "Currently Item Dropping is " + ArenaData.get().getBoolean("item-drop"));
                                im.setLore(lore);
                                e.getCurrentItem().setItemMeta(im);
                                });
                        ItemMeta itemDropMeta = itemDrop.getItem().getItemMeta();
                        blockBreakMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        ItemButton setspawn = ItemButton.create(new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.GRAY+ "Set Spawn"),e -> {
                            ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                            Player player = (Player) e.getWhoClicked();
                            ArenaData.get().set("arena", player.getLocation());
                            ArenaConfiguration.save();
                            player.sendMessage(ChatColor.GREEN + "Arena Spawn Location Set!");
                        });
                        ItemMeta setspawnMeta = setspawn.getItem().getItemMeta();
                        ItemButton setpos = ItemButton.create(new ItemBuilder(Material.REDSTONE_BLOCK).setName(ChatColor.GRAY+ "Set Position"),e -> {
                            ArenaData.load(ArenaCommands.arenaNameMap.get(e.getWhoClicked().getUniqueId()));
                            if (WandListener.pos1m.get(e.getWhoClicked()) != null && WandListener.pos2m.get(e.getWhoClicked()) != null) {
                                Location loc1 = WandListener.pos1m.get(e.getWhoClicked());
                                Location loc2 = WandListener.pos2m.get(e.getWhoClicked());
                                BoundingBox box = new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
                                e.getWhoClicked().getWorld().getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                                e.getWhoClicked().getWorld().getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                                ArenaData.get().set("arena.pos1.x", loc1.getX());
                                ArenaData.get().set("arena.pos1.y", loc1.getY());
                                ArenaData.get().set("arena.pos1.z", loc1.getZ());
                                ArenaData.get().set("arena.pos2.x", loc2.getX());
                                ArenaData.get().set("arena.pos2.y", loc2.getY());
                                ArenaData.get().set("arena.pos2.z", loc2.getZ());
                                String world = e.getWhoClicked().getWorld().getName();
                                ArenaData.get().set("arena.world", world);
                                ArenaData.save();
                                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Arena Positions Set!");
                            }});
                        ItemMeta setposMeta = setpos.getItem().getItemMeta();
                        ItemButton worldBorder = ItemButton.create(new ItemBuilder(Material.BARRIER).setName(ChatColor.GRAY + "World Border"),e -> {
                            ArenaData.get().set("world-border", !ArenaData.get().getBoolean("world-border"));
                            ArenaData.save();
                            boolean worldBorderBool = ArenaData.get().getBoolean("world-border");
                            if (worldBorderBool){
                                Double x1 = ArenaData.get().getDouble("arena.pos1.x");
                                Double y1 = ArenaData.get().getDouble("arena.pos1.y");
                                Double z1 = ArenaData.get().getDouble("arena.pos1.z");
                                Double x2 = ArenaData.get().getDouble("arena.pos2.x");
                                Double y2 = ArenaData.get().getDouble("arena.pos2.y");
                                Double z2 = ArenaData.get().getDouble("arena.pos2.z");
                                BoundingBox box = new BoundingBox(x1, y1, z1, x2, y2, z2);
                                Bukkit.getWorld(ArenaData.get().getString("arena.world")).getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                                Bukkit.getWorld(ArenaData.get().getString("arena.world")).getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                            }else {
                                WorldBorder worldBorderr = Bukkit.getWorld(ArenaData.get().getString("arena.world")).getWorldBorder();
                                worldBorderr.reset();
                            }
                            List<String> lore = new ArrayList<>();
                            ItemMeta im = e.getCurrentItem().getItemMeta();
                            lore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                            lore.add(ChatColor.GREEN + "Currently the world border is " + ArenaData.get().getBoolean("world-border"));
                            im.setLore(lore);
                            e.getCurrentItem().setItemMeta(im);
                                });
                        ItemMeta worldBorderMeta = worldBorder.getItem().getItemMeta();
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
                        arenaGUI.open(p);
                        arenaNameMap.put(p.getUniqueId(), arenaName);
                    }
                }
                else {
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
                      }else{
                        String szstring = safezones.get(safezones.size()-1);
                         sz = Integer.parseInt(szstring);
                        sz++;}
                                String world = p.getWorld().getName();
                                Location loc1 = WandListener.pos1m.get(p);
                                Location loc2 = WandListener.pos2m.get(p);
                                ArenaConfiguration.get().set("Safezones." + sz + ".world", world);
                                ArenaConfiguration.get().set("Safezones." + sz + ".pos1", loc1);
                                ArenaConfiguration.get().set("Safezones." + sz + ".pos2", loc2);
                                safezones.add(sz+"");
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