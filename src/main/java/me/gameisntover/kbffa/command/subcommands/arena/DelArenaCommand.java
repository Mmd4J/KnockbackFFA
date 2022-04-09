package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DelArenaCommand extends KnockCommand {
    public DelArenaCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull String getSubName() {
        return "delarena";
    }

    @Override
    public @NotNull String getSubDescription() {
        return "Removes an specified arena";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/delarena <arenaname>";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
       Arena arena = KnockbackFFA.getINSTANCE().getArenaManager().load(args[0]);
    if (arena.isReady()) arena.removeArena();
    else knocker.sendMessage("&cThe arena does not exists!");
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return Arrays.stream(KnockbackFFA.getINSTANCE().getArenaManager().getFolder().list()).map(s -> s.replace(".yml","")).collect(Collectors.toList());
    }
}
