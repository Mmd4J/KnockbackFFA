package me.gameisntover.kbffa.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.Arena.Arena;
import me.gameisntover.kbffa.customconfigs.MessageConfiguration;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.scoreboard.MainScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!KnockbackFFAAPI.BungeeMode()) {
            KnockbackFFAAPI.setInGamePlayer(player, false);
        }
        if (KnockbackFFA.getInstance().getConfig().getBoolean("joinmessage")) {
            String joinText = MessageConfiguration.get().getString("joinmessage").replace("&", "§");
            joinText = PlaceholderAPI.setPlaceholders(e.getPlayer(), joinText);
            e.setJoinMessage(joinText);
        }
        if (KnockbackFFA.getInstance().getConfig().getBoolean("joinsound")) {
            KnockbackFFAAPI.playSound(player, "join", 1, 1);
        }
        if (Arena.getEnabledArena() == null) {
            KnockbackFFAAPI.setInGamePlayer(player, false);
        } else {
            if (KnockbackFFAAPI.BungeeMode()) {
                Arena.teleportPlayerToArena(player);
                KnockbackFFAKit kitManager = new KnockbackFFAKit();
                player.getInventory().clear();
                kitManager.lobbyItems(player);
                KnockbackFFAAPI.setInGamePlayer(player, true);

            } else if (!KnockbackFFAAPI.BungeeMode()) {
                if (!KnockbackFFAAPI.isInGame(player)) {
                    Arena.leaveArena(player);
                    KnockbackFFAAPI.setInGamePlayer(player, false);
                }
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        if (KnockbackFFA.getInstance().getConfig().getBoolean("leavemessage")) {
            String leaveText = MessageConfiguration.get().getString("leavemessage").replace("&", "§");
            leaveText = PlaceholderAPI.setPlaceholders(e.getPlayer(), leaveText);
            e.setQuitMessage(leaveText);
        }
        Player player = e.getPlayer();
        MainScoreboard.toggleScoreboard(player, false);
        if (!KnockbackFFAAPI.BungeeMode()) {
            KnockbackFFAAPI.setInGamePlayer(player, false);
        }
    }
}
