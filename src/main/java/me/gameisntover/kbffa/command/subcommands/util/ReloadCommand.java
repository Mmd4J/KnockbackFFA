package me.gameisntover.kbffa.command.subcommands.util;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.command.KnockCommand;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReloadCommand extends KnockCommand {
    public ReloadCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getKnockDescription() {
        return ChatColor.AQUA + "reloads the kbffa configuration files";
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
    public void perform(Knocker knocker, String[] args) {
        KnockbackFFA.getINSTANCE().reloadConfig();
        KnockbackFFA.getINSTANCE().loadMessages();
        KnockbackFFA.getINSTANCE().loadSounds();
        KnockbackFFA.getINSTANCE().getZoneConfiguration().reload();
        KnockbackFFA.getINSTANCE().getKnockScoreboard().reload();
        KnockbackFFA.getINSTANCE().getItems().reload();
        KnockbackFFA.getINSTANCE().getCosmeticConfiguration().reload();
        knocker.getPlayer().sendMessage(ChatColor.AQUA + "Configs are reloaded!");
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }

}
