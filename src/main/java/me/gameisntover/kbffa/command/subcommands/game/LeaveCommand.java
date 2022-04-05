package me.gameisntover.kbffa.command.subcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LeaveCommand extends SubCommand {
    private String name;
    public LeaveCommand(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public @NotNull String getSubName() {
        return name;
    }

    @Override
    public @NotNull String getSubDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5leaves the game if the player is already in game");
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }

    @Override
    public String getSyntax() {
        return "/leave";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (!KnockbackFFA.getINSTANCE().BungeeMode() && knocker.isInGame()) {
            String leaveText = Message.ARENA_LEAVE.toString();
            leaveText = PlaceholderAPI.setPlaceholders(p, leaveText);
            p.sendMessage(leaveText);
            KnockbackFFA.getINSTANCE().getArenaManager().teleportToMainLobby(p);
            p.getInventory().clear();
            if (KnockbackFFA.getINSTANCE().getConfig().getBoolean("save-inventory-on-join")) p.getInventory().equals(knocker.getInventory());
            knocker.hideScoreBoard();
            knocker.setInGame(false);
        } else p.sendMessage(Message.CAN_NOT_LEAVE.toString());

    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
