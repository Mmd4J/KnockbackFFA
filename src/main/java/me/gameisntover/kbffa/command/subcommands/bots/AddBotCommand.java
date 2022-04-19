package me.gameisntover.kbffa.command.subcommands.bots;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.bots.BotA;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddBotCommand extends KnockCommand {

    public AddBotCommand() {
        super("addbot");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Toggles bots";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/addbot <botname>";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        if (args.length > 0) {
            if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena() == null)
                knocker.sendMessage("&cThere is no arena for bot to join there! please create arena before using");
            else {
                if (KnockbackFFA.getInstance().getBotManager().getBotHandler().containsKey(args[0]))
                    knocker.sendMessage("This bot already exists!");
                else {
                    Bot bot = new BotA(args[0], KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getSpawnLocation());
                    KnockbackFFA.getInstance().getBotManager().getBotHandler().put(args[0], bot);
                }
            }
        } else knocker.sendMessage("&cWrong usage!" + getSyntax());

    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
