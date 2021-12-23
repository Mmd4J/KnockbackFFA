package me.gameisntover.kbffa.knockbackffa.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAArena;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlaySoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlayerData;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAKit;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners  implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String joinText = MessageConfiguration.get().getString("joinmessage").replace("&", "§");
        joinText = PlaceholderAPI.setPlaceholders(e.getPlayer(), joinText);

        player.playSound(player.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("join")), 1, 1);
        e.setJoinMessage(joinText);
        if (KnockbackFFAAPI.BungeeMode()) {
            KnockbackFFAArena.teleportPlayertoArena(player);
            if (!KnockbackFFAArena.arenaisReady(1) && !KnockbackFFAArena.arenaisReady(2) && !KnockbackFFAArena.arenaisReady(3) && !KnockbackFFAArena.arenaisReady(4)) {
                if (player.isOp()) {
                    player.sendMessage("§cSpawn is not set! Please set it in the config or /setspawn!");
                } else if (player.isOp() == false) {
                    player.sendMessage("§cAsk an admin to create the spawnpoint so i can get you there!");
                }
            }
        } else if (KnockbackFFAAPI.BungeeMode() == false) {
            if (!KnockbackFFAAPI.isInGame(player)) {
                KnockbackFFAArena.leaveArena(player);
            }
        }
    }
    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        String leaveText = MessageConfiguration.get().getString("leavemessage").replace("&", "§");
        leaveText = PlaceholderAPI.setPlaceholders(e.getPlayer(), leaveText);
        e.setQuitMessage(leaveText);
        Player player = e.getPlayer();
        if (!KnockbackFFAAPI.BungeeMode()) {
            PlayerData.load(player);
            PlayerData.get().set("In-Game",false);
            PlayerData.save();
        }
    }
}
