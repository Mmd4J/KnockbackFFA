package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ResetArenaCommand extends SubCommand {
    @Override
    public String getName() {
        return "resetarena";
    }

    @Override
    public String getDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5Resets arena blocks to the saved blocks from arena data files");
    }

    @Override
    public String getSyntax() {
        return "/resetarena <arenaname>";
    }

    @Override
    public String getPermission() {
        return "knockbackffa.command.resetarena";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length > 0) {
            File file = new File(KnockbackFFA.getINSTANCE().getTempArenaManager().getfolder() + File.separator + args[0] + ".yml");
            if (file.exists()) {
                Arena arena = KnockbackFFA.getINSTANCE().getTempArenaManager().load(args[0]);
                arena.resetArena();
                p.sendMessage(ChatColor.GREEN + "Arena has been reset!");
            } else p.sendMessage(ChatColor.RED + "Arena does not exist");
        } else p.sendMessage(ChatColor.RED + "Wrong usage "+ getSyntax());
    }
    @Override
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return Arrays.asList(Arrays.stream(KnockbackFFA.getINSTANCE().getTempArenaManager().getfolder().list()).map(s ->  s.replace(".yml", "")).toArray(String[]::new));
    }
}
