package me.gameisntover.kbffa.knockbackffa;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ScoreboardConfiguration;
import me.gameisntover.kbffa.knockbackffa.scoreboard.SideBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Repeater
{
    private static final List<BukkitTask> tasks = new ArrayList<>();    

    public static void start() {
        tasks.add(new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<String> scoreboardLines = ScoreboardConfiguration.get().getStringList("lines");
                    SideBar sidebar = new SideBar(ScoreboardConfiguration.get().getString("Title").replace("&", "§"), "mainScoreboard");

                    for (String string : scoreboardLines) {
                        string = PlaceholderAPI.setPlaceholders(player, string);
                        sidebar.add(string.replaceAll("&", "§"));
                    }
                    sidebar.apply(player);
                }
            }
        }.runTaskTimer(KnockbackFFA.getInstance(), 20, 20));
    }

    public static void stop() {
        for (BukkitTask task : tasks) {
            task.cancel();
        }
    }
}
