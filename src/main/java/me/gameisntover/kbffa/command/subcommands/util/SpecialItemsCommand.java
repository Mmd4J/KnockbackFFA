package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class SpecialItemsCommand extends SubCommand {
    @Override
    public String getName() {
        return "specialitems";
    }

    @Override
    public String getDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5a basic gui for giving some special items good for creating kits.");
    }

    @Override
    public String getSyntax() {
        return "/specialitems";
    }

    @Override
    public String getPermission() {
        return "knockbackffa.command.specialitems";
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
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return null;
    }
}
