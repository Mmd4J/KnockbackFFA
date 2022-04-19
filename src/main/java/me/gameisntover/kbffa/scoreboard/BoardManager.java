package me.gameisntover.kbffa.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BoardManager {

    private final Map<UUID, FastBoard> playerBoards = new HashMap<>();

    public BoardManager(){
        updateScoreboards();
    }

    public void register(Player player){
        FastBoard board = new FastBoard(player);
        String title = KnockbackFFA.getInstance().getKnockScoreboard().getConfig.getString("Title");
        List<String> lines = KnockbackFFA.getInstance().getKnockScoreboard()
                .getConfig.getStringList("lines");
        for(int i = 0; i <= lines.size(); i++){
            lines.set(i, translate(PlaceholderAPI.setPlaceholders(player, lines.get(i))));
        }
        board.updateTitle(title);
        board.updateLines(lines);
        playerBoards.put(player.getUniqueId(), board);
    }

    public void unregister(Player player){
        playerBoards.get(player.getUniqueId()).delete();
        playerBoards.remove(player.getUniqueId());
    }

    private void updateScoreboards(){
        List<String> lines = KnockbackFFA.getInstance()
                .getKnockScoreboard().getConfig.getStringList("lines");
        Bukkit.getScheduler().runTaskTimerAsynchronously(KnockbackFFA.getInstance(), () ->
                playerBoards.values().forEach(board -> {
            final List<String> playerLines = new ArrayList<>();
            for(int i = 0; i <= lines.size(); i++){
                playerLines.set(i, translate(PlaceholderAPI.setPlaceholders(board.getPlayer(), lines.get(i))));
            }
            board.updateLines(playerLines);
        } ), 0, 10);
    }

    private String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
