package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.util.CommonUtils;
import me.gameisntover.kbffa.util.Items;
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
        KnockbackFFA.getInstance().getDatabaseManager().loadPlayer(player.getUniqueId());

        if (KnockbackFFA.getInstance().getConfig().getBoolean("join-sound")){
            player.playSound(player.getLocation(), Sounds.PLAYER_JOIN.toSound(), 1, 1);
        }

        if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena() == null){
            ArenaManager.setInGame(player.getUniqueId(), false);
        } else {
            if (KnockbackFFA.getInstance().BungeeMode()) {
                KnockbackFFA.getInstance().getArenaManager().teleportPlayerToArena(player);
                player.getInventory().clear();
                CommonUtils.giveLobbyItems(player);
            } else {
                if (ArenaManager.isInGame(player.getUniqueId())) return;
                KnockbackFFA.getInstance().getArenaManager().teleportToMainLobby(player);
            }
        }

    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ReworkedKnocker.unload(player.getUniqueId());
    }
}
