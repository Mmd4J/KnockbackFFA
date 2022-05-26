package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.arena.regions.Cuboid;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.gui.Button;
import me.gameisntover.kbffa.gui.GUI;
import me.gameisntover.kbffa.listeners.WandListener;
import me.gameisntover.kbffa.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditArenaCommand extends KnockCommand {
    public EditArenaCommand() {
        super("editarena");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        perform(ReworkedKnocker.get(((Player)sender).getUniqueId()), args);
        return false;
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "shows the sender a gui to edit arena details.";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/editarena <arenaname>";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Command Arguements missing or is invalid /editarena arenaname");
            return;
        }
        List<String> arenaList = Arrays.asList(KnockbackFFA.getInstance().getArenaManager().getFolder().list());
        if (!arenaList.contains(args[0] + ".yml")) {
            p.sendMessage(ChatColor.RED + "That arena name does not exist!");
            return;
        }
        p.sendMessage(ChatColor.GREEN + "You are now editing " + args[0]);
        Arena arena = KnockbackFFA.getInstance().getArenaManager().load(args[0]);
        GUI arenaGUI = new GUI("Arena Editor", (short) 5);
        List<String> blockbreaklore = new ArrayList<>();
        blockbreaklore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
        blockbreaklore.add(ChatColor.GREEN + "Currently Block Breaking is " + arena.getConfig().getBoolean("block-break"));
        Button blockBreak = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.DIAMOND_PICKAXE, 1, ChatColor.GRAY + "Block Break", blockbreaklore).create(ItemFlag.HIDE_ATTRIBUTES), e -> {
            arena.getConfig().set("block-break", !arena.getConfig().getBoolean("block-break"));
            arena.save();
            ItemMeta im = e.getCurrentItem().getItemMeta();
            blockbreaklore.clear();
            blockbreaklore.add(ChatColor.GRAY + "Toggle whether or not players can break blocks");
            blockbreaklore.add(ChatColor.GREEN + "Currently Block Breaking is " + arena.getConfig().getBoolean("block-break"));
            im.setLore(blockbreaklore);
            e.getCurrentItem().setItemMeta(im);
        });
        List<String> itemDropLore = new ArrayList<>();
        itemDropLore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
        itemDropLore.add(ChatColor.GREEN + "Currently Item Dropping is " + arena.getConfig().getBoolean("item-drop"));
        Button itemDrop = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.DIAMOND, 1, ChatColor.GRAY + "Item Drop", itemDropLore).create(), e -> {
            arena.getConfig().set("item-drop", !arena.getConfig().getBoolean("item-drop"));
            arena.save();
            ItemMeta im = e.getCurrentItem().getItemMeta();
            itemDropLore.clear();
            itemDropLore.add(ChatColor.GRAY + "Toggle whether or not players can drop items");
            itemDropLore.add(ChatColor.GREEN + "Currently Item Dropping is " + arena.getConfig().getBoolean("item-drop"));
            im.setLore(itemDropLore);
            e.getCurrentItem().setItemMeta(im);
        });
        Button setSpawn = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.NETHER_STAR, 1, ChatColor.GRAY + "Set Spawn", Arrays.asList("sets the spawn point of players")).create(), e -> {
            Player player = (Player) e.getWhoClicked();
            arena.getConfig().set("arena.spawn", player.getLocation());
            arena.save();
            player.sendMessage(ChatColor.GREEN + "Arena Spawn Location Set!");
        });
        List<String> autoResetLore = new ArrayList<>();
        autoResetLore.add(ChatColor.GRAY + "Toggle whether or not the arena will reset blocks placed or broke automatically");
        autoResetLore.add(ChatColor.GREEN + "Currently Auto Reset is " + arena.getConfig().getBoolean("auto-reset"));
        Button autoReset = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.DISPENSER, 1, ChatColor.GRAY + "Auto Reset", autoResetLore).create(), e -> {
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
            autoResetLore.clear();
            autoResetLore.add(ChatColor.GRAY + "Toggle whether or not the arena will reset blocks placed or broke automatically");
            autoResetLore.add(ChatColor.GREEN + "Currently Auto Reset is " + arena.getConfig().getBoolean("auto-reset"));
            ItemStack item = e.getCurrentItem();
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            meta.setLore(autoResetLore);
            item.setItemMeta(meta);
        });
        Button setpos = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.REDSTONE_BLOCK, 1, ChatColor.GRAY + "Set Position", Arrays.asList(ChatColor.GRAY + "set the new arena positions from here!")).create(), e -> {
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
        List<String> worldBorderlore = new ArrayList<>();
        worldBorderlore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
        worldBorderlore.add(ChatColor.GREEN + "Currently the world border is " + arena.getConfig().getBoolean("world-border"));
        Button worldBorder = KnockbackFFA.getInstance().getButtonManager().create(new ItemBuilder(Material.BARRIER, 1, ChatColor.GRAY + "World Border", worldBorderlore).create(), e -> {
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
            worldBorderlore.clear();
            ItemMeta im = e.getCurrentItem().getItemMeta();
            worldBorderlore.add(ChatColor.GRAY + "Toggle whether or not the world border is enabled.");
            worldBorderlore.add(ChatColor.GREEN + "Currently the world border is " + arena.getConfig().getBoolean("world-border"));
            im.setLore(worldBorderlore);
            e.getCurrentItem().setItemMeta(im);
        });
        arenaGUI.add(blockBreak, 10);
        arenaGUI.add(itemDrop, 11);
        arenaGUI.add(setSpawn, 12);
        arenaGUI.add(setpos, 13);
        arenaGUI.add(worldBorder, 14);
        arenaGUI.add(autoReset, 15);
        knocker.getPlayer().openInventory(arenaGUI.getInventory());
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return Arrays.asList(Arrays.stream(KnockbackFFA.getInstance().getArenaManager().getFolder().list()).map(s -> s.replace(".yml", "")).toArray(String[]::new));
    }
}
