package me.gameisntover.kbffa.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.api.KnockbackFFAAPI;
import me.gameisntover.kbffa.customconfigs.ScoreboardConfiguration;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;


public class MainScoreboard implements Listener {
    public static void toggleScoreboard(Player player, boolean toggle) {
        if (!toggle) return;
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.scheduleSyncRepeatingTask(KnockbackFFA.getInstance(), () -> {
                List<String> scoreboardLines = ScoreboardConfiguration.get().getStringList("lines");
                SideBar sidebar = new SideBar(ScoreboardConfiguration.get().getString("Title").replace("&", "§"), "mainScoreboard");
                if (player.getScoreboard() != null) player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                for (String string : scoreboardLines) {
                    string = PlaceholderAPI.setPlaceholders(player, string);
                    sidebar.add(string.replaceAll("&", "§"));
                }
                sidebar.apply(player);

            }, 0, 20);
        }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer()))
            toggleScoreboard(player,ScoreboardConfiguration.get().getBoolean("enabled") );

    }
}
