package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.*;
import org.bukkit.ChatColor;

import java.util.List;

public class ReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "reloads the kbffa configuration files";
    }

    @Override
    public String getSyntax() {
        return "/reload";
    }

    @Override
    public String getPermission() {
        return "knockbackffa.command.reload";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        KnockbackFFA.getINSTANCE().reloadConfig();
        KnockbackFFA.getINSTANCE().loadMessages();
        KnockbackFFA.getINSTANCE().loadSounds();
        ArenaConfiguration.reload();
        ScoreboardConfiguration.reload();
        ItemConfiguration.reload();
        CosmeticConfiguration.reload();
        knocker.getPlayer().sendMessage(ChatColor.AQUA + "Configs are reloaded!");
    }

    @Override
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return null;
    }
}
