package me.gameisntover.kbffa.command;

import lombok.Getter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class KnockCommand extends Command {


    protected KnockCommand(@NotNull String name) {
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
