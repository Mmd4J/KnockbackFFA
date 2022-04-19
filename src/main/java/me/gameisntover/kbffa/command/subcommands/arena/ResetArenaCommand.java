package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ResetArenaCommand extends KnockCommand {

    public ResetArenaCommand() {
        super("resetarena");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        perform(KnockbackFFA.getInstance().getKnocker((Player) sender), args);
        return false;
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Resets arena blocks to the saved blocks from arena data files";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/resetarena <arenaname>";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length > 0) {
            File file = new File(KnockbackFFA.getInstance().getArenaManager().getFolder() + File.separator + args[0] + ".yml");
            if (file.exists()) {
                Arena arena = KnockbackFFA.getInstance().getArenaManager().load(args[0]);
                arena.resetArena();
                p.sendMessage(ChatColor.GREEN + "Arena has been reset!");
            } else p.sendMessage(ChatColor.RED + "Arena does not exist");
        } else p.sendMessage(ChatColor.RED + "Wrong usage " + getSyntax());
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return Arrays.asList(Arrays.stream(KnockbackFFA.getInstance().getArenaManager().getFolder().list()).map(s -> s.replace(".yml", "")).toArray(String[]::new));
    }
}
