package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpCommand extends KnockCommand {
    public HelpCommand(@NotNull String name) {
        super(name);
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
    public void perform(Knocker knocker, String[] args) {
        knocker.sendMessage("&8==================================");
        knocker.sendMessage("&3[Advanced] KnockbackFFA help");
        for (KnockCommand cmd : KnockbackFFA.getINSTANCE().getCommandManager().getSubCommands())
            knocker.sendMessage("&b" + cmd.getSyntax() + " : &f" + cmd.getKnockDescription());
        knocker.sendMessage("&3[Advanced] KnockbackFFA version : " + KnockbackFFA.getINSTANCE().getDescription().getVersion());
        knocker.sendMessage("&8==================================");
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
