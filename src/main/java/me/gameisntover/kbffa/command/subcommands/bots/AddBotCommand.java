package me.gameisntover.kbffa.command.subcommands.bots;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.bots.BotA;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.entity.Player;
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
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player player = knocker.getPlayer();
        if (args.length > 0) {
            if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena() == null)
                player.sendMessage("&cThere is no arena for bot to join there! please create arena before using");
            else {
                if (KnockbackFFA.getInstance().getBotManager().getBotHandler().containsKey(args[0]))
                    player.sendMessage("This bot already exists!");
                else {
                    Bot bot = new BotA(args[0], KnockbackFFA.getInstance().getArenaManager().getEnabledArena().getSpawnLocation());
                    KnockbackFFA.getInstance().getBotManager().getBotHandler().put(args[0], bot);
                }
            }
        } else player.sendMessage("&cWrong usage!" + getSyntax());

    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
