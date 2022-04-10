package me.gameisntover.kbffa.command.subcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoinCommand extends KnockCommand {
    public JoinCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getKnockDescription() {
        return ChatColor.translateAlternateColorCodes('&', "&6Players can use this command to join the game");
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }

    @Override
    public String getSyntax() {
        return "/join";
    }

    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (KnockbackFFA.getINSTANCE().BungeeMode() && knocker.isInGame())
            p.sendMessage(Message.ALREADY_INGAME.toString());
        else {
            if (KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena() == null)
                knocker.getPlayer().sendMessage(Message.NO_READY_ARENA.toString());
            else {
                String joinText = Message.ARENA_JOIN.toString();
                joinText = PlaceholderAPI.setPlaceholders(p, joinText);
                p.sendMessage(joinText);
                knocker.setInventory(p.getInventory());
                p.getInventory().clear();
                p.setFoodLevel(20);
                knocker.giveLobbyItems();
                knocker.showScoreBoard();
                knocker.setInGame(true);
            }
            KnockbackFFA.getINSTANCE().getArenaManager().teleportPlayerToArena(p);
        }
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return null;
    }
}
