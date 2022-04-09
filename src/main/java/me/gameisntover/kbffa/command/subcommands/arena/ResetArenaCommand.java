package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.api.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

public class ResetArenaCommand extends KnockCommand {
    private String name;
    public ResetArenaCommand(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        perform(KnockbackFFA.getINSTANCE().getKnocker((Player) sender),args);
        return false;
    }

    @Override
    public @NotNull String getKnockDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5Resets arena blocks to the saved blocks from arena data files");
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
            File file = new File(KnockbackFFA.getINSTANCE().getArenaManager().getFolder() + File.separator + args[0] + ".yml");
            if (file.exists()) {
                Arena arena = KnockbackFFA.getINSTANCE().getArenaManager().load(args[0]);
                arena.resetArena();
                p.sendMessage(ChatColor.GREEN + "Arena has been reset!");
            } else p.sendMessage(ChatColor.RED + "Arena does not exist");
        } else p.sendMessage(ChatColor.RED + "Wrong usage "+ getSyntax());
    }
    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return Arrays.asList(Arrays.stream(KnockbackFFA.getINSTANCE().getArenaManager().getFolder().list()).map(s ->  s.replace(".yml", "")).toArray(String[]::new));
    }
}
