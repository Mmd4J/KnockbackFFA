package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpecialItemsCommand extends SubCommand {
    public SpecialItemsCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getSubName() {
        return "specialitems";
    }

    @Override
    public @NotNull String getSubDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5a basic gui for giving some special items good for creating kits.");
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/specialitems";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Inventory specialItems = Bukkit.createInventory(null, 9, "Special Items");
        KnockbackFFAKit kits = new KnockbackFFAKit();
        specialItems.addItem(kits.kbStick());
        specialItems.addItem(kits.kbBow());
        specialItems.addItem(kits.kbbowArrow());
        specialItems.addItem(kits.JumpPlate());
        specialItems.addItem(kits.EnderPearl());
        specialItems.addItem(kits.BuildingBlock());
        knocker.getPlayer().openInventory(specialItems);
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
