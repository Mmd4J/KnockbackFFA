package me.gameisntover.kbffa.message;

import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!KnockbackFFAAPI.BungeeMode()) KnockbackFFAAPI.setInGamePlayer(player, false);
        if (KnockbackFFA.getInstance().getConfig().getBoolean("joinsound"))
            PlayerUtil.playSound(player, "join", 1, 1);

        if (Arena.getEnabledArena() == null) KnockbackFFAAPI.setInGamePlayer(player, false);
        else {
            if (KnockbackFFAAPI.BungeeMode()) {
                Arena.teleportPlayerToArena(player);
                KnockbackFFAKit kitManager = new KnockbackFFAKit();
                player.getInventory().clear();
                kitManager.lobbyItems(player);
                KnockbackFFAAPI.setInGamePlayer(player, KnockbackFFAAPI.BungeeMode());
            } else {
                if (!KnockbackFFAAPI.isInGame(player)) {
                    Arena.teleportToMainLobby(player);
                    KnockbackFFAAPI.setInGamePlayer(player, KnockbackFFAAPI.BungeeMode());
                }
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
            KnockbackFFAAPI.setInGamePlayer(player, KnockbackFFAAPI.BungeeMode());

    }
}
