package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpecialItemsCommand extends KnockCommand {
    public SpecialItemsCommand() {
        super("specialitems");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "basic gui for giving some special items good for creating kits.";
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
        specialItems.addItem(Items.KNOCKBACK_STICK.getItem());
        specialItems.addItem(Items.BOW.getItem());
        specialItems.addItem(Items.ARROW.getItem());
        specialItems.addItem(Items.JUMP_PLATE.getItem());
        specialItems.addItem(Items.ENDER_PEARL.getItem());
        specialItems.addItem(Items.BUILDING_BLOCK.getItem());
        knocker.getPlayer().openInventory(specialItems);
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
