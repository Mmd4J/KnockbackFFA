package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.util.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        if (knocker.getConfig().getString("deaths") != null) return;
        knocker.getConfig().set("deaths", 0);
        knocker.getConfig().set("kills", 0);
        knocker.getConfig().set("owned-kits", knocker.getConfig().getStringList("owned-kits").add("Default"));
        knocker.getConfig().set("selected-kit", "Default");
        knocker.saveConfig();
        if (!KnockbackFFA.getINSTANCE().BungeeMode()) knocker.setInGame(false);
        if (KnockbackFFA.getINSTANCE().getConfig().getBoolean("joinsound"))
            player.playSound(player.getLocation(), Sounds.PLAYER_JOIN.toSound(), 1, 1);
        if (KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena() == null) knocker.setInGame(false);
        else {
            if (KnockbackFFA.getINSTANCE().BungeeMode()) {
                KnockbackFFA.getINSTANCE().getArenaManager().teleportPlayerToArena(player);
                player.getInventory().clear();
                knocker.giveLobbyItems();
                knocker.setInGame(KnockbackFFA.getINSTANCE().BungeeMode());
            } else {
                if (!knocker.isInGame()) {
                    KnockbackFFA.getINSTANCE().getArenaManager().teleportToMainLobby(player);
                    knocker.setInGame(KnockbackFFA.getINSTANCE().BungeeMode());
                }
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = KnockbackFFA.getINSTANCE().getKnocker(player);
        knocker.setInGame(false);
    }
}
