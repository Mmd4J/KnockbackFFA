package me.gameisntover.kbffa.knockbackffa.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class SideBar
{
    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();

    private final String name;
    private final String identifier;

    private final Scoreboard board;
    private final Objective obj;
    private final List<Score> scores;

    public SideBar(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.board = manager.getNewScoreboard();
        this.obj = board.registerNewObjective(identifier, "");
        this.scores = new ArrayList<>();
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(name);
    }

    public void add(String s) {
        Score score = obj.getScore(s);
        scores.add(0, score);
    }

    public void apply(Player player) {
        for (int i = 0; i < scores.size(); i++)
            scores.get(i).setScore(i);
        player.setScoreboard(board);
    }

}