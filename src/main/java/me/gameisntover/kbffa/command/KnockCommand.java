package me.gameisntover.kbffa.command;

import lombok.Getter;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class KnockCommand extends Command {
    public String getKnockName;

    protected KnockCommand(@NotNull String name) {
        super(name);
        getKnockName = name;
    }

    public abstract @NotNull String getKnockDescription();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender.hasPermission("knockbackffa.command." + getKnockName)) {
            perform(ReworkedKnocker.get(((Player) sender).getUniqueId()), args);
        } else {
            sender.sendMessage(getPermissionMessage());
        }
        return false;
    }

    public abstract PermissionDefault getPermissionDefault();

    public abstract String getSyntax();

    public abstract void perform(ReworkedKnocker knocker, String[] args);

    public abstract List<String> performTab(ReworkedKnocker knocker, String[] args);

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return performTab(ReworkedKnocker.get(((Player) sender).getUniqueId()), args);
    }
}
