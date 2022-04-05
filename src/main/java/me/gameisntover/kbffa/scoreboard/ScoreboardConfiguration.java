package me.gameisntover.kbffa.scoreboard;

import me.gameisntover.kbffa.api.KnockConfiguration;

public class ScoreboardConfiguration extends KnockConfiguration {
    @Override
    public String getName() {
        return "scoreboard";
    }

    @Override
    public String getResourceName() {
        return "scoreboard.yml";
    }

}
