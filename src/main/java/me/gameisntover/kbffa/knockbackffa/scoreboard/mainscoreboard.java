package me.gameisntover.kbffa.knockbackffa.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
public class mainscoreboard implements Listener {


            @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

                Player player = e.getPlayer();
                FastBoard board = new FastBoard((player));
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                board.size();
                scheduler.scheduleSyncRepeatingTask(KnockbackFFA.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        board.updateLines(PlaceholderAPI.setPlaceholders(player, KnockbackFFA.getInstance().getConfig().getStringList("scoreboard.lines".replace("&", "§"))));
                        board.updateTitle(KnockbackFFA.getInstance().getConfig().getString("scoreboard.title").replace("&", "§"));
                    }
                }, 0L, 20L);

    }


}
