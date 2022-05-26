package me.gameisntover.kbffa.command.subcommands.bots;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CreateAIPath extends KnockCommand {
    public CreateAIPath() {
        super("createaipath");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Creates path blocks where the bot should go when it is lost";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/createpath <arenaname>";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        if (args.length > 0) {
            Arena arena = KnockbackFFA.getInstance().getArenaManager().load(args[0]);
            if (arena == null) knocker.getPlayer().sendMessage("&cThe specified arena could not be found!");
            else {
                arena.createAIPath(knocker.getPlayer().getLocation());
                knocker.getPlayer().sendMessage("&aSuccessfully created ai path for the " + arena.getName());
            }
        } else knocker.getPlayer().sendMessage("&cWrong usage : " + getSyntax());
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return Arrays.asList(Arrays.stream(KnockbackFFA.getInstance().getArenaManager().getFolder().list()).map(s -> s.replace(".yml", "")).toArray(String[]::new));
    }
}
