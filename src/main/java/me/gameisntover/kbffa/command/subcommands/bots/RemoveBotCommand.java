package me.gameisntover.kbffa.command.subcommands.bots;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RemoveBotCommand extends KnockCommand {
    public RemoveBotCommand() {
        super("removebot");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Removes the specified bot";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/removebot <botname>";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        if (args.length > 0) {
            if (KnockbackFFA.getInstance().getBotManager().getBotHandler().containsKey(args[0])) {
                Bot bot = KnockbackFFA.getInstance().getBotManager().getBotHandler().get(args[0]);
                bot.remove();
                KnockbackFFA.getInstance().getBotManager().getBotHandler().remove(args[0]);
                knocker.getPlayer().sendMessage("&aSuccessfully removed the bot from the game");
            }
        } else knocker.getPlayer().sendMessage("&cWrong usage :" + getSyntax());
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return new ArrayList<>(KnockbackFFA.getInstance().getBotManager().getBotHandler().keySet());
    }
}
