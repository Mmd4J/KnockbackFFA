package me.gameisntover.kbffa.knockbackffa.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ScoreboardConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class MainScoreboard implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            if (ScoreboardConfiguration.get().getBoolean("enabled") == true) {
                toggleScoreboard(player, true);
            }
        } else {
            toggleScoreboard(player, false);
        }
    }

    public static void toggleScoreboard(Player player, boolean toggle) {
        FastBoard board = new FastBoard((player));
        if (toggle) {
            if (ScoreboardConfiguration.get().getBoolean("enabled") == true) {
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                board.size();
                scheduler.scheduleSyncRepeatingTask(KnockbackFFA.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        String title = ScoreboardConfiguration.get().getString("Title");
                        PlaceholderAPI.setPlaceholders(player, title);
                        board.updateTitle(title.replace("&", "§"));
                        board.updateLines(PlaceholderAPI.setPlaceholders(player, ScoreboardConfiguration.get().getStringList("lines".replace("&", "§"))));
                    }
                }, 0L, 20L);

            }
        } else if (!toggle){
            board.size();
            board.updateTitle("");
            board.updateLines(Arrays.asList(""));
            board.removeLine(0);
        }
    }
}