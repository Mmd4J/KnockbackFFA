package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReloadCommand extends KnockCommand {
    public ReloadCommand() {
        super("reload");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "reloads the kbffa configuration files";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/reload";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        KnockbackFFA.getInstance().reloadConfig();
        KnockbackFFA.getInstance().loadMessages();
        KnockbackFFA.getInstance().loadSounds();
        KnockbackFFA.getInstance().getZoneConfiguration().reload();
        KnockbackFFA.getInstance().getKnockScoreboard().reload();
        KnockbackFFA.getInstance().getItems().reload();
        KnockbackFFA.getInstance().getCosmeticConfiguration().reload();
        knocker.getPlayer().sendMessage(ChatColor.AQUA + "Configs are reloaded!");
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }

}
