package me.gameisntover.kbffa.command;

import me.gameisntover.kbffa.api.event.ArenaCreateEvent;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.Cuboid;
import me.gameisntover.kbffa.arena.TempArenaManager;
import me.gameisntover.kbffa.customconfig.ArenaConfiguration;
import me.gameisntover.kbffa.listeners.WandListener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaCommands implements CommandExecutor {
    private final TempArenaManager tempArenaManager = new TempArenaManager();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
            Player p = (Player) sender;
            switch (command.getName()) {
                case "createarena":
                    switch (args.length) {
                        case 0:
                            p.sendMessage(ChatColor.RED + "You must specify a name for the arena!");
                        case 1:
                            if (WandListener.pos1m.get(p) == null && WandListener.pos2m.get(p) == null)
                                p.sendMessage(ChatColor.RED + "You must set the first and second positions of the arena!");
                            else if (WandListener.pos1m.get(p) != null && WandListener.pos2m.get(p) != null) {
                                Location loc1 = WandListener.pos1m.get(p);
                                Location loc2 = WandListener.pos2m.get(p);
                                Arena arena = tempArenaManager.create(args[0], loc1, loc2, p.getLocation());
                                List<String> blocks = new ArrayList<>();
                                Cuboid region = new Cuboid(loc1, loc2);
                                for (Block block : region.getBlocks()) blocks.add(block.getType().name());
                                arena.getConfig().set("blocks", blocks);
                                arena.save();
                                if (tempArenaManager.getfolder().list().length == 1)
                                    tempArenaManager.setEnabledArena(args[0]);
                                ArenaCreateEvent event = new ArenaCreateEvent(p, arena);
                                Bukkit.getPluginManager().callEvent(event);
                                sender.sendMessage(ChatColor.GREEN + "Arena " + args[0] + " has been created!");
                            }
                    }
                    break;
                case "editarena":
                    if (args.length != 1) {
                        p.sendMessage(ChatColor.RED + "Command Arguements missing or is invalid /editarena arenaname");
                        return false;
                    }
                    List<String> arenaList = Arrays.asList(tempArenaManager.getfolder().list());
                    if (!arenaList.contains(args[0] + ".yml")) {
                        p.sendMessage(ChatColor.RED + "That arena name does not exist!");
                        return false;
                    }
                    p.sendMessage(ChatColor.GREEN + "You are now editing " + args[0]);
                    Arena arena = tempArenaManager.load(args[0]);
                    InventoryGUI arenaGUI = new InventoryGUI(Bukkit.createInventory(null, 54, "Arena Editor"));
                    ItemButton blockBreak = ItemButton.create(new ItemBuilder(Material.DIAMOND_PICKAXE).setName(ChatColor.GRAY + "Block Break"), e -> {
                        arena.getConfig().set("block-break", !arena.getConfig().getBoolean("block-break"));
                        arena.save();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                        lore.add(ChatColor.GREEN + "Currently Block Breaking is " + arena.getConfig().getBoolean("block-break"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    });
                    ItemMeta blockBreakMeta = blockBreak.getItem().getItemMeta();
                    ItemButton itemDrop = ItemButton.create(new ItemBuilder(Material.DIAMOND).setName(ChatColor.GRAY + "Item Drop"), e -> {
                        arena.getConfig().set("item-drop", !arena.getConfig().getBoolean("item-drop"));
                        arena.save();
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                        lore.add(ChatColor.GREEN + "Currently Item Dropping is " + arena.getConfig().getBoolean("item-drop"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    });
                    ItemMeta itemDropMeta = itemDrop.getItem().getItemMeta();
                    blockBreakMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    ItemButton setspawn = ItemButton.create(new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.GRAY + "Set Spawn"), e -> {
                        Player player = (Player) e.getWhoClicked();
                        arena.getConfig().set("arena.spawn", player.getLocation());
                        arena.save();
                        player.sendMessage(ChatColor.GREEN + "Arena Spawn Location Set!");
                    });
                    ItemButton autoReset = ItemButton.create(new ItemBuilder(Material.DISPENSER).setName(ChatColor.GRAY + "Auto Reset"), e -> {
                        arena.getConfig().set("auto-reset", !arena.getConfig().getBoolean("auto-reset"));
                        arena.save();
                        p.sendMessage("Auto Reset is now set to" + arena.getConfig().getBoolean("auto-reset"));
                        if (arena.getConfig().getString("blocks") != null) return;
                        Location loc1 = arena.getConfig().getLocation("arena.pos1");
                        Location loc2 = arena.getConfig().getLocation("arena.pos2");
                        List<String> blocks = new ArrayList<>();
                        Cuboid region = new Cuboid(loc1, loc2);
                        for (Block block : region.getBlocks()) {
                            blocks.add(block.getType().name());
                        }
                        arena.getConfig().set("blocks", blocks);
                        arena.save();
                    });
                    ItemMeta autoResetMeta = autoReset.getItem().getItemMeta();
                    List<String> autoResetLore = new ArrayList<>();
                    autoResetLore.add(ChatColor.GRAY + "Toggle whether or not the arena will reset blocks placed or broke automatically");
                    autoResetLore.add(ChatColor.GREEN + "Currently Auto Reset is " + arena.getConfig().getBoolean("auto-reset"));
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
                            arena.getConfig().set("arena.pos1", loc1);
                            arena.getConfig().set("arena.pos2", loc2);
                            String world = e.getWhoClicked().getWorld().getName();
                            arena.getConfig().set("arena.spawn.world", world);
                            List<String> blocks = new ArrayList<>();
                            Cuboid region = new Cuboid(loc1, loc2);
                            for (Block block : region.getBlocks()) {
                                blocks.add(block.getType().name());
                            }
                            arena.getConfig().set("blocks", blocks);
                            arena.save();
                            e.getWhoClicked().sendMessage(ChatColor.GREEN + "Arena Positions Set!");
                        }
                    });
                    ItemMeta setposMeta = setpos.getItem().getItemMeta();
                    ItemButton worldBorder = ItemButton.create(new ItemBuilder(Material.BARRIER).setName(ChatColor.GRAY + "World Border"), e -> {
                        arena.getConfig().set("world-border", !arena.getConfig().getBoolean("world-border"));
                        arena.save();
                        Location spawnLoc = arena.getConfig().getLocation("arena.spawn");
                        boolean worldBorderBool = arena.getConfig().getBoolean("world-border");
                        if (worldBorderBool) {
                            Location loc1 = arena.getConfig().getLocation("arena.pos1");
                            Location loc2 = arena.getConfig().getLocation("arena.pos2");
                            BoundingBox box = new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
                            spawnLoc.getWorld().getWorldBorder().setCenter(box.getCenterX(), box.getCenterZ());
                            spawnLoc.getWorld().getWorldBorder().setSize(box.getMaxX() - box.getMinX());
                        } else {
                            WorldBorder worldBorderr = spawnLoc.getWorld().getWorldBorder();
                            worldBorderr.reset();
                        }
                        List<String> lore = new ArrayList<>();
                        ItemMeta im = e.getCurrentItem().getItemMeta();
                        lore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                        lore.add(ChatColor.GREEN + "Currently the world border is " + arena.getConfig().getBoolean("world-border"));
                        im.setLore(lore);
                        e.getCurrentItem().setItemMeta(im);
                    });
                    ItemMeta worldBorderMeta = worldBorder.getItem().getItemMeta();
                    List<String> blockBreaklore = new ArrayList<>();
                    List<String> itemDropLore = new ArrayList<>();
                    List<String> setspawnLore = new ArrayList<>();
                    List<String> setposLore = new ArrayList<>();
                    List<String> worldBorderLore = new ArrayList<>();
                    blockBreaklore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
                    blockBreaklore.add(ChatColor.GREEN + "Currently breaking block is " + arena.getConfig().getBoolean("block-break"));
                    itemDropLore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
                    itemDropLore.add(ChatColor.GREEN + "Currently Item Dropping is " + arena.getConfig().getBoolean("item-drop"));
                    setspawnLore.add(ChatColor.GRAY + "Set the spawnpoint for the arena so players will spawn there");
                    worldBorderLore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
                    worldBorderLore.add(ChatColor.GREEN + "Currently the world border is " + arena.getConfig().getBoolean("world-border"));
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
                    break;
                case "wand":
                    ItemStack wand = new me.gameisntover.kbffa.util.ItemBuilder(Material.BLAZE_ROD,1,ChatColor.DARK_PURPLE + "PositionSelector Wand",Arrays.asList("Use this wand to select positions!",
                            "LEFTCLICK = First Position", "RIGHTCLICK = Second position")).create(Enchantment.MENDING,1,ItemFlag.HIDE_ENCHANTS);
                    p.getInventory().addItem(wand);
                break;
                case "setsafezone":
                    if (WandListener.pos2m.get(p) != null && WandListener.pos1m.get(p) == null) return false;
                        List<String> safezones = ArenaConfiguration.get().getStringList("registered-safezones");
                        int sz;
                        if (safezones.size() == 0) sz = 1;
                         else {
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
                break;

                case "gotoworld":
                    if (args.length < 0) return false;
                        World world2 = Bukkit.getWorld(args[0]);
                        if (world2 != null) p.teleport(world2.getSpawnLocation());
                         else if (world2 == null) p.sendMessage(ChatColor.RED + "World does not exist!");
                         else if (p.getWorld() == world2) p.sendMessage(ChatColor.RED + "You are already in this world!");

                    break;
            }
        return false;
    }
}
