package me.gameisntover.kbffa.command.subcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.Knocker;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinCommand extends SubCommand {

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return ChatColor.translateAlternateColorCodes('&',"&6Players can use this command to join the game");
    }

    @Override
    public String getSyntax() {
        return "/join";
    }
    @Override
    public String getPermission(){
        return "knockbackffa.command.join";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (KnockbackFFA.getINSTANCE().BungeeMode() || knocker.isInGame()) p.sendMessage(Message.ALREADY_INGAME.toString());
        else {
            if (KnockbackFFA.getINSTANCE().getTempArenaManager().getEnabledArena() == null)
                knocker.getPlayer().sendMessage(Message.NO_READY_ARENA.toString());
            else {
                String joinText = Message.ARENA_JOIN.toString();
                joinText = PlaceholderAPI.setPlaceholders(p, joinText);
                p.sendMessage(joinText);
                knocker.setInventory(p.getInventory());
                p.getInventory().clear();
                p.setFoodLevel(20);
                KnockbackFFAKit kit = new KnockbackFFAKit();
                kit.lobbyItems(p);
                knocker.showScoreBoard();
                knocker.setInGame(true);
            }
            KnockbackFFA.getINSTANCE().getTempArenaManager().teleportPlayerToArena(p);
        }
    }

    @Override
    public List<String> tabComplete(Knocker knocker, String[] args) {
        return null;
    }
}
