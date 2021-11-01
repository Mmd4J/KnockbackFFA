package me.gameisntover.kbffa.knockbackffa.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public class JoinLeaveListeners  implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        ScoreboardManager board  = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = board.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Title", "dummy", KnockbackFFA.getInstance().getConfig().getString("scoreboard.title").replace("&", "§"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        /*Score score = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line1").replace("&", "§"));
        Score score2 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line2").replace("&", "§"));
        Score score3 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line3").replace("&", "§"));
        Score score4 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line4").replace("&", "§"));
        Score score5 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line5").replace("&", "§"));
        Score score6 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line6").replace("&", "§"));
        Score score7 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line7").replace("&", "§"));
        Score score8 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line8").replace("&", "§"));
        Score score9 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line9").replace("&", "§"));
        Score score10 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line10").replace("&", "§"));
        Score score11 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line11").replace("&", "§"));
        Score score12 = objective.getScore(KnockbackFFA.getInstance().getConfig().getString("scoreboard.line12").replace("&", "§"));
        score.setScore(1);
        score2.setScore(2);
        score3.setScore(3);
        score4.setScore(4);
        score5.setScore(5);
        score6.setScore(6);
        score7.setScore(7);
        //score8.setScore(8);
        //score9.setScore(9);
        //score10.setScore(10);
        //score11.setScore(11);
        //score12.setScore(12);*/

                String joinText = KnockbackFFA.getInstance().getConfig().getString("joinmessage").replace("&", "§");
        joinText = PlaceholderAPI.setPlaceholders(e.getPlayer(), joinText);
        e.setJoinMessage(joinText);
        Player player = e.getPlayer();
        player.setScoreboard(scoreboard);
        Double x = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.x");
        Double y = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.y");
        Double z = KnockbackFFA.getInstance().getConfig().getDouble("spawnpoint.z");
        World world = KnockbackFFA.getInstance().getServer().getWorld(KnockbackFFA.getInstance().getConfig().getString("spawnpoint.world"));
        player.teleport(new org.bukkit.Location(world, x, y, z));
        if (KnockbackFFA.getInstance().getConfig().getString("spawn") == null) {
        } else {


        }
        player.getWorld().setSpawnLocation(x.intValue(), y.intValue(), z.intValue());
    }
    @EventHandler
    public void playerLeave(PlayerQuitEvent e){
        String leaveText = KnockbackFFA.getInstance().getConfig().getString("leavemessage").replace("&", "§");
        leaveText = PlaceholderAPI.setPlaceholders(e.getPlayer(), leaveText);
        e.setQuitMessage(leaveText);
        Player player = e.getPlayer();
    }
}
