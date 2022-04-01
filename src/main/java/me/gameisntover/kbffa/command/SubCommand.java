package me.gameisntover.kbffa.command;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class SubCommand extends Command {
    public SubCommand(String name) {
        super(name);
    }

    public abstract @NotNull String getSubName();

    public abstract @NotNull String getSubDescription();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        perform(KnockbackFFA.getINSTANCE().getKnocker((Player) sender),args);
        return false;
    }
    public abstract PermissionDefault getPermissionDefault();

    public abstract String getSyntax();

    public abstract void perform(Knocker knocker , String[] args);

    public abstract List<String> performTab(Knocker knocker, String[] args);

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return performTab(KnockbackFFA.getINSTANCE().getKnocker((Player) sender),args);
    }
}
