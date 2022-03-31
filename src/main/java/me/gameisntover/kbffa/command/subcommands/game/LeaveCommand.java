package me.gameisntover.kbffa.command.subcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand extends SubCommand {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&5leaves the game if the player is already in game");
    }

    @Override
    public String getSyntax() {
        return "/leave";
    }

    @Override
    public String getPermission() {
        return "knockbackffa.command.leave";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (!KnockbackFFA.getINSTANCE().BungeeMode() && knocker.isInGame()) {
            String leaveText = Message.ARENA_LEAVE.toString();
            leaveText = PlaceholderAPI.setPlaceholders(p, leaveText);
            p.sendMessage(leaveText);
            KnockbackFFA.getINSTANCE().getTempArenaManager().teleportToMainLobby(p);
            p.getInventory().clear();
            if (KnockbackFFA.getINSTANCE().getConfig().getBoolean("save-inventory-on-join")) p.getInventory().equals(knocker.getInventory());
            knocker.hideScoreBoard();
            knocker.setInGame(false);
        } else p.sendMessage(Message.CAN_NOT_LEAVE.toString());

    }

    @Override
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return null;
    }
}
