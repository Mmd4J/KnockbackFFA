package me.gameisntover.kbffa.command.subcommands.kit;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelKitCommand extends KnockCommand {
    public DelKitCommand() {
        super("delkit");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Deletes the specified kit";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/delkit <kitname>";
    }


    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Usage: /delkit <kitname>");
        } else {
            Kit kit = KnockbackFFA.getInstance().getKitManager().load(args[0]);
            kit.getFile().delete();
            p.sendMessage(ChatColor.GREEN + "I've deleted the kit " + args[0] + "!");
        }
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
