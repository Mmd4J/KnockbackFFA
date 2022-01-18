package me.gameisntover.kbffa.knockbackffa.scoreboard;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.API.KnockbackFFAAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ScoreboardConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;
import java.util.List;


public class MainScoreboard implements Listener {
        @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (KnockbackFFAAPI.BungeeMode() || KnockbackFFAAPI.isInGame(player.getPlayer())) {
            if (ScoreboardConfiguration.get().getBoolean("enabled")) {
                toggleScoreboard(player, true);
            }
        } else {
            toggleScoreboard(player, false);
        }
    }
    public static void toggleScoreboard(Player player, boolean toggle) {
        if (toggle) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            int i =   scheduler.scheduleSyncRepeatingTask(KnockbackFFA.getInstance(), new Runnable() {
                @Override
                public void run() {
                    List<String> scoreboardLines = ScoreboardConfiguration.get().getStringList("lines");
                    SideBar sidebar = new SideBar(ScoreboardConfiguration.get().getString("Title").replace("&", "§"), "mainScoreboard");
                    if(player.getScoreboard() != null) {
                        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    }
                    for (String string : scoreboardLines) {
                        string = PlaceholderAPI.setPlaceholders(player, string);
                        sidebar.add(string.replaceAll("&", "§"));
                    }
                    sidebar.apply(player);

                        }
            }, 0, 20);
        }
    }
    }
