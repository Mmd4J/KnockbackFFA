package me.gameisntover.kbffa.command.subcommands.bots;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RemoveBotCommand extends KnockCommand {
    public RemoveBotCommand(@NotNull String name) {
        super(name);
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
    public void perform(Knocker knocker, String[] args) {
        if (args.length > 0) {
            if (KnockbackFFA.getINSTANCE().getBotManager().getBotHandler().containsKey(args[0])) {
                Bot bot = KnockbackFFA.getINSTANCE().getBotManager().getBotHandler().get(args[0]);
                bot.remove();
                KnockbackFFA.getINSTANCE().getBotManager().getBotHandler().remove(args[0]);
                knocker.sendMessage("&aSuccessfully removed the bot from the game");
            }
        } else knocker.sendMessage("&cWrong usage :" + getSyntax());
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return new ArrayList<>(KnockbackFFA.getINSTANCE().getBotManager().getBotHandler().keySet());
    }
}
