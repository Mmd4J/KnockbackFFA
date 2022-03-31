package me.gameisntover.kbffa.command;

import lombok.Getter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.subcommands.arena.*;
import me.gameisntover.kbffa.command.subcommands.game.JoinCommand;
import me.gameisntover.kbffa.command.subcommands.game.LeaveCommand;
import me.gameisntover.kbffa.command.subcommands.kit.CreateKitCommand;
import me.gameisntover.kbffa.command.subcommands.kit.DelKitCommand;
import me.gameisntover.kbffa.command.subcommands.util.ReloadCommand;
import me.gameisntover.kbffa.command.subcommands.util.SpecialItemsCommand;
import me.gameisntover.kbffa.command.subcommands.worlds.CreateWorldCommand;
import me.gameisntover.kbffa.command.subcommands.worlds.GotoWorldCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Getter
public class CommandManager implements CommandExecutor,TabCompleter {
    private List<SubCommand> subCommands;

    public CommandManager() {
        subCommands = Arrays.asList(new JoinCommand(),new LeaveCommand(),new SetMainLobbyCommand(), new ResetArenaCommand(),new CreateWorldCommand(),
                new GotoWorldCommand(),new SetVoidCommand(),new SpecialItemsCommand(),new ReloadCommand(),new DelKitCommand(),
            new CreateKitCommand(), new SetSafeZoneCommand(),new WandCommand(),new EditArenaCommand(),new CreateArenaCommand());
        for (SubCommand sb : subCommands) KnockbackFFA.getINSTANCE().getCommand(sb.getName()).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(p);
        for (SubCommand sub : getSubCommands())
            if (command.getName().equalsIgnoreCase(sub.getName())) {
                if (sender.hasPermission(sub.getPermission()))
                    sub.perform(knocker, args);
                 else sender.sendMessage(Message.NO_PERMISSION.toString());
            }
                return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player p = (Player) sender;
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(p);
        for (SubCommand sub : getSubCommands())
            if (command.getName().equalsIgnoreCase(sub.getName()))
                if (sender.hasPermission(sub.getPermission()))
                  return sub.tabComplete(knocker, args);

        return null;
    }
}
