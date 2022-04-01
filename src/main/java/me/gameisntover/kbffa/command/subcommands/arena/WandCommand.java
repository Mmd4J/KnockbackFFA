package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class WandCommand extends SubCommand {
    public WandCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getSubName() {
        return "wand";
    }

    @Override
    public @NotNull String getSubDescription() {
        return ChatColor.AQUA +"gives player position selector wand";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/wand";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        ItemStack wand = new me.gameisntover.kbffa.util.ItemBuilder(Material.BLAZE_ROD, 1, ChatColor.DARK_PURPLE + "PositionSelector Wand", Arrays.asList("Use this wand to select positions!",
                "LEFTCLICK = First Position", "RIGHTCLICK = Second position")).create(Enchantment.MENDING, 1, ItemFlag.HIDE_ENCHANTS);
        knocker.getPlayer().getInventory().addItem(wand);
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
