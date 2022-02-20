package me.gameisntover.kbffa.knockbackffa.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners implements Listener
{
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFA.getInstance().getConfig().getBoolean("joinmessage")) {
            String joinText = MessageConfiguration.get().getString("joinmessage").replace("&", "§");
            joinText = PlaceholderAPI.setPlaceholders(e.getPlayer(), joinText);
            e.setJoinMessage(joinText);
        }
        if (KnockbackFFA.getInstance().getConfig().getBoolean("joinsound")) {
            KnockbackFFAAPI.playSound(player, "join", 1, 1);
        }
        if (KnockbackFFAArena.isEnabled(KnockbackFFAArena.getEnabledArena()) == false) {
            if (player.isOp()) {
                player.sendMessage("§cSpawn is not set! If you have an ready arena set the spawnpoint for it.");
            } else if (player.isOp() == false) {
                player.sendMessage(MessageConfiguration.get().getString("nospawnpoint").replace("&", "§"));
            }
        } else {
            if (KnockbackFFAAPI.BungeeMode()) {
                KnockbackFFAArena.teleportPlayertoArena(player);
            } else if (KnockbackFFAAPI.BungeeMode() == false) {
                if (!KnockbackFFAAPI.isInGame(player)) {
                    KnockbackFFAArena.leaveArena(player);
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
        if (!KnockbackFFAAPI.BungeeMode()) {
            PlayerData.load(player);
            PlayerData.get().set("In-Game", false);
            PlayerData.save();
        }
    }
}
