package me.gameisntover.kbffa.command.subcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LeaveCommand extends KnockCommand {

    public LeaveCommand() {
        super("leave");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "leaves the game if the player is already in game";
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
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (!ArenaManager.isInGame(p.getUniqueId())) {
            String leaveText = Message.ARENA_LEAVE.toString();
            leaveText = PlaceholderAPI.setPlaceholders(p, leaveText);
            p.sendMessage(leaveText);
            KnockbackFFA.getInstance().getArenaManager().teleportToMainLobby(p);
            p.getInventory().clear();
            /* will be back l8r
             if (KnockbackFFA.getInstance().getConfig().getBoolean("save-inventory-on-join")){
                //TODO: recach invs p.getInventory().setContents(knocker.getInventory().getContents());
            }
            */
            knocker.setScoreboardEnabled(false);
            ArenaManager.setInGame(p.getUniqueId(), false);
        } else {
            p.sendMessage(Message.CAN_NOT_LEAVE.toString());
        }
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
