package me.gameisntover.kbffa.command.subcommands;

import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.bots.Bot;
import me.gameisntover.kbffa.bots.BotA;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class botsjoin extends KnockCommand {

    public botsjoin(@NotNull String name) {
        super(name);
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
        return "/joinbots";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Bot bot = new BotA(knocker.getName(), knocker.getPlayer().getLocation());
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
