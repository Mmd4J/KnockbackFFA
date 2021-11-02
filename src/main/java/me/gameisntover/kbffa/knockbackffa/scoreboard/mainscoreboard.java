package me.gameisntover.kbffa.knockbackffa.scoreboard;

import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

public class mainscoreboard implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("test", "dummy", KnockbackFFA.getInstance().getConfig().getString("scoreboard.title").replace("&", "§"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
}