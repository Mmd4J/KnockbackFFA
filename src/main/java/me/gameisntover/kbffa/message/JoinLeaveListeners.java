package me.gameisntover.kbffa.message;

import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.api.KnockbackFFAKit;
import me.gameisntover.kbffa.arena.Arena;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.customconfig.PlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners implements Listener {
    
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerData.create(player);
        if (PlayerData.get().getString("deaths") != null) return;
        PlayerData.load(player);
        PlayerData.get().set("deaths", 0);
        PlayerData.get().set("kills", 0);
        PlayerData.get().set("owned-kits", PlayerData.get().getStringList("owned-kits").add("Default"));
        PlayerData.get().set("selected-kit", "Default");
        PlayerData.save();
        if (!KnockbackFFAAPI.BungeeMode()) KnockbackFFAAPI.setInGamePlayer(player, false);
        if (KnockbackFFA.getInstance().getConfig().getBoolean("joinsound"))
            player.playSound(player.getLocation(), Sound.valueOf(Sounds.PLAYER_JOIN.toString()), 1, 1);
        if (ArenaManager.getEnabledArena() == null) KnockbackFFAAPI.setInGamePlayer(player, false);
        else {
            if (KnockbackFFAAPI.BungeeMode()) {
                ArenaManager.teleportPlayerToArena(player);
                KnockbackFFAKit kitManager = new KnockbackFFAKit();
                player.getInventory().clear();
                kitManager.lobbyItems(player);
                KnockbackFFAAPI.setInGamePlayer(player, KnockbackFFAAPI.BungeeMode());
            } else {
                if (!KnockbackFFAAPI.isInGame(player)) {
                    ArenaManager.teleportToMainLobby(player);
                    KnockbackFFAAPI.setInGamePlayer(player, KnockbackFFAAPI.BungeeMode());
                }
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
            KnockbackFFAAPI.setInGamePlayer(player, false);

    }
}
