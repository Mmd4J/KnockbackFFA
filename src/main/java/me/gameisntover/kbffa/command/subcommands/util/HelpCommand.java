package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpCommand extends KnockCommand {
    public HelpCommand() {
        super("kbffahelp");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "shows a list of commands that the knockbackffa plugin have";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/help";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player player = knocker.getPlayer();
        player.sendMessage("&8==================================");
        player.sendMessage("&3[Advanced] KnockbackFFA help");
        for (KnockCommand cmd : KnockbackFFA.getInstance().getCommandManager().getSubCommands())
            player.sendMessage("&b" + cmd.getSyntax() + " : &f" + cmd.getKnockDescription());
        player.sendMessage("&3[Advanced] KnockbackFFA version : " + KnockbackFFA.getInstance().getDescription().getVersion());
        player.sendMessage("&8==================================");
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
