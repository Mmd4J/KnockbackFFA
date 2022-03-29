package me.gameisntover.kbffa.listeners;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {
    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFA.getInstance().getApi().BungeeMode() || KnockbackFFA.getInstance().getApi().isInGame(player))
            e.setFormat(Message.CHATFORMAT.toString().replace("%player%", player.getName()).replace("%message%", e.getMessage()));
    }
}

