package me.gameisntover.kbffa.message;

import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.customconfig.MessageConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatFormats implements Listener {
    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        e.getFormat();
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player)) e.setFormat(MessageConfiguration.get().getString("chatformat").replace("&", "§").replace("%player%", player.getName().replace("&", "§")).replace("%message%", e.getMessage().replace("&", "§")));

    }
}

