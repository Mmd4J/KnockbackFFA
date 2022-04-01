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
import me.gameisntover.kbffa.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;


@Getter
public class CommandManager  {
    private List<SubCommand> subCommands;
    public CommandManager() {
        subCommands = Arrays.asList(new JoinCommand("join"),new LeaveCommand("leave"),new SetMainLobbyCommand("setmainlobby"), new ResetArenaCommand("resetarena")
                ,new CreateWorldCommand("createworld"), new GotoWorldCommand("gotoworld"),new SetVoidCommand("setvoid")
                ,new SpecialItemsCommand("specialitems"),new ReloadCommand("reload"),new DelKitCommand("delkit"),
                new CreateKitCommand("createkit"), new SetSafeZoneCommand("setsafezone"),new WandCommand("wand"),new EditArenaCommand("editarena"),new CreateArenaCommand("createarena"));
        for (SubCommand sb : subCommands){
            sb.setName(sb.getSubDescription());
            sb.setName(sb.getSubName());
            Permission permission = new Permission("knockbackffa.command."+sb.getSubName(),sb.getSubDescription(),sb.getPermissionDefault());
            Bukkit.getPluginManager().addPermission(permission);
            sb.setPermission(permission.getName());
            sb.setPermissionMessage(Message.NO_PERMISSION.toString());
            sb.setLabel(sb.getSubName());
            KnockbackFFA.getINSTANCE().getCommandMap().register("knockbackffa",sb);
        }
    }

}
