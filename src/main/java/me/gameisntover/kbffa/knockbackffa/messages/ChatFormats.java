package me.gameisntover.kbffa.knockbackffa.messages;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;



public class ChatFormats implements Listener {
    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        e.getFormat();
        Player player = e.getPlayer();
        e.setFormat(KnockbackFFA.getInstance().getConfig().getString("chatformat").replace("%player%" , player.getDisplayName()).replace("&", "§").replace("%message%", e.getMessage()));
    }
}

