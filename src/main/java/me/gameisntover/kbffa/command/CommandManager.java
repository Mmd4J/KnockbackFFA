package me.gameisntover.kbffa.command;

import lombok.Getter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.subcommands.arena.*;
import me.gameisntover.kbffa.command.subcommands.bots.AddBotCommand;
import me.gameisntover.kbffa.command.subcommands.bots.CreateAIPath;
import me.gameisntover.kbffa.command.subcommands.bots.RemoveBotCommand;
import me.gameisntover.kbffa.command.subcommands.game.JoinCommand;
import me.gameisntover.kbffa.command.subcommands.game.LeaveCommand;
import me.gameisntover.kbffa.command.subcommands.kit.CreateKitCommand;
import me.gameisntover.kbffa.command.subcommands.kit.DelKitCommand;
import me.gameisntover.kbffa.command.subcommands.util.HelpCommand;
import me.gameisntover.kbffa.command.subcommands.util.ReloadCommand;
import me.gameisntover.kbffa.command.subcommands.util.SpecialItemsCommand;
import me.gameisntover.kbffa.command.subcommands.worlds.CreateWorldCommand;
import me.gameisntover.kbffa.command.subcommands.worlds.GotoWorldCommand;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;


@Getter
public class CommandManager {
    private final List<KnockCommand> subCommands;

    public CommandManager() {
        subCommands = Arrays.asList(new JoinCommand(), new LeaveCommand(), new SetMainLobbyCommand()
                , new ResetArenaCommand(), new CreateWorldCommand(), new GotoWorldCommand()
                , new SpecialItemsCommand(), new ReloadCommand(), new DelKitCommand(), new SetVoidCommand()
                , new CreateKitCommand(), new WandCommand(), new EditArenaCommand()
                , new CreateArenaCommand(), new DelArenaCommand(), new HelpCommand(), new AddBotCommand()
                , new RemoveBotCommand(), new CreateAIPath(), new SetSafeZoneCommand());
        for (KnockCommand sb : subCommands) {
            sb.setName(sb.getKnockDescription());
            sb.setName(sb.getKnockName);
            Permission permission = new Permission("knockbackffa.command." + sb.getKnockName,
                    sb.getKnockDescription(), sb.getPermissionDefault());
            Bukkit.getPluginManager().addPermission(permission);
            sb.setPermission(permission.getName());
            sb.setPermissionMessage(Message.NO_PERMISSION.toString());
            sb.setLabel(sb.getKnockName);
            KnockbackFFA.getInstance().getCommandMap().register("knockbackffa", sb);
        }
    }

}
